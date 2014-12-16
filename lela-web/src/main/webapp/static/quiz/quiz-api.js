/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

/****************************************************
 *   PUBLIC API
 ****************************************************/

// dummy debug console for ie7/8
if (!window.console) console = {log: function() {}};

var Lela = Lela || {};
Lela.USER_ALREADY_EXISTS_ERROR = "User already exists";
Lela.QUIZ_DONE_COOKIE = '_lelaqzdn'
Lela._loadTemplates = function(options) {

    // If a theme stylesheet is defined, load it now
    if (options && options.theme) {
        // Load the base quiz stylesheets
        Lela.loadCss(Lela.baseUrl + '/static/quiz/themes/' + options.theme + '.css');
    }

    // Load the quiz templates
    var xhr = Lela.$.ajax(Lela.baseUrl + '/static/quiz/quiz-templates.html',
        Lela.request({
            type: 'get',
            dataType: 'html',
            success: function(html) {

                Ember.Logger.log("Retrieved templates");

                Lela.$('BODY').append(html);

                // Re-bootstrap Ember handlebars search
                Ember.Handlebars.bootstrap( Ember.$(document) );
                Lela._internalCreateQuiz(options);
            },
            error: function() {
                Ember.Logger.log("Template error");
                alert('Could not load quiz templates');
            }
        }));
    };

/*
    Arguments
    * baseUrl - Lela.com url hosting quiz
    * Quiz Options

    Quiz Options
    * affiliateId
    * applicationId
    * targetSelector
    * quizCreated: callback function - arguments: quiz
    * quizCompleted: callback function - arguments: quiz
    * stepFinished: callback function - arguments: quiz, step rlnm
    * registrationCompleted: callback function - arguments: user
    * saveQuizCompleted: callback function
 */
Lela._internalCreateQuiz = function(options) {

    console.log("Creating quiz");
    //Lela.blockUI(options.targetSelector);

    // Create the ember application and when ready query the quiz json
    window.App = Ember.Application.create({
        requiresRegistration: false,
        rootElement: options.targetSelector,
        transitionSpeed: 500,

        user: Ember.Object.create({
            email: null,
            firstName: null,
            lastName: null,
            gender: null,
            isCurrentUser: false,
            isValidEmail: function() {
                return Lela.validateEmail(this.get('email'));
            }.property('email')
        }),

        ready: function() {

            App.quizOptions = options;
            if (options.user) {
                App.get('user').set('email', options.user.email);
                App.get('user').set('firstName', options.user.firstName);
                App.get('user').set('lastName', options.user.lastName);
            }

            // If there is a user present with email, first and last name it is assumed they will be
            // registered regardless of whether or not there is a registration step
            if(App.getPath('user.email') && App.getPath('user.firstName') && App.getPath('user.lastName')) {
                App.set('requiresRegistration', true);
            }

            Lela.defineClasses();
            Lela.defineTransitions();
            Lela.defineViews();
            Lela.defineControllers();
            Lela.defineRouter();

            var url = Lela.baseUrl + '/api/quiz/' + options.applicationId + '?affiliateId=' + options.affiliateId;
            if (Lela.lelacd) {
                url += '&_lelacd=' + Lela.lelacd;
            }

            var xhr = Lela.$.ajax(url,
                Lela.request({
                    type: 'get',
                    dataType: 'json',
                    success: function(json) {

                        // Set LelaCd cookie from json response
                        var dt=new Date();
                        dt.setDate(dt.getDate() + 365);
                        var c= json.srcd + '; domain=.' + window.location.hostname + '; path=/; expires=' + dt.toUTCString();
                        document.cookie='_lelacd=' + c;

                        // If a default theme is defined and nothing was configured... load it now
                        // If a theme stylesheet is defined, load it now
                        if (json.thm && (!options || !options.theme)) {
                            // Load the base quiz stylesheets
                            Lela.loadCss(Lela.baseUrl + '/static/quiz/themes/' + json.thm + '.css');
                        }

                        // Set up google analytics
                        if (json.analyticsEnabled) {
                            if (console) console.log("Quiz analytics enabled");
                            _gaq.push([Lela.googleTracker + '_setAccount', 'UA-26275943-1']);
                            //_gaq.push([Lela.googleTracker + '_trackPageview']);
                        } else {
                            if (console) console.log("Quiz analytics disabled");
                        }

                        // Build the quiz structure from the json
                        App._quiz = Lela._buildQuiz(json);
                        App.initialize();

                        App.get('router').get('applicationController').set('content', App._quiz);

                        // unblock the UI
                        Lela.unblockUI(options.targetSelector);

                        // If returning, explicity go to the returned state
                        if (Lela.getCookie(Lela.QUIZ_DONE_COOKIE) === 'true' && App._quiz.get('rtrncntnt')) {
                            App.get('router').transitionTo('returned');
                        }

                        // Inform the caller that the quiz is ready
                        Lela.trackEvent('quiz start', App._quiz.get('rlnm'));
                        if (options.quizCreated) {
                            options.quizCreated({  });
                        }
                    },
                    error: function() {
                        alert('Could not load quiz');
                        Lela.unblockUI(options.targetSelector);
                    }
                })
            );
        }
    });
};

/****************************************************
 *   DEFINE STATE MANAGEMENT
 ****************************************************/
Lela.defineRouter = function() {

    App.RootState = Ember.Route.extend({

        // Events
        // Next
        next: function(router, event) {
            if (router.getPath('quizStepController.isNextActive')) {

                // Notify that step is completed
                Lela.trackEvent('step complete', router.getPath('quizStepController.rlnm'));
                Lela.trackStep(router.getPath('quizStepController.rlnm'));
                if (App.quizOptions.stepCompleted) {
                    App.quizOptions.stepCompleted({ stepId: router.getPath('quizStepController.rlnm') });
                }

                // Transition to next state
                if (router.currentState.get('next')) {
                    var nextState = router.currentState.get('next');
                    router.transitionTo(nextState);
                } else if (App.get('requiresRegistration')) {
                    router.transitionTo('register');
                } else {
                    router.transitionTo('saveQuiz');
                }
            }
        },

        // This is similar to the Next action but it handles an explicit registration page
        registrationSubmit: function(router, event) {
            if (router.getPath('quizStepController.isNextActive')) {

                Lela.blockUI(App.quizOptions.targetSelector);

                // Notify that step is completed
                Lela.trackEvent('step complete', router.getPath('quizStepController.rlnm'));
                Lela.trackStep(router.getPath('quizStepController.rlnm'));
                if (App.quizOptions.stepCompleted) {
                    App.quizOptions.stepCompleted({ stepId: router.getPath('quizStepController.rlnm') });
                }

                // Register the user
                App.set('requiresRegistration', false);
                Lela.register(
                        App.get('user'),
                        function(data) {
                            Lela.unblockUI(App.quizOptions.targetSelector);
                            App.registeredUser = data;

                            // Transition to next state
                            if (router.currentState.get('next')) {
                                var nextState = router.currentState.get('next');
                                router.transitionTo(nextState);
                            } else {
                                router.transitionTo('saveQuiz');
                            }
                        },
                        function(error) {
                            Lela.unblockUI(App.quizOptions.targetSelector);
                            App.get('router').get('quizErrorController').set('message', error);
                            router.transitionTo('error');
                        }
                );
            }
        },

        // This state will be hit if there was no explicit registration step
        // but user data was pre-filled
        register: Ember.State.extend({
            enter: function(router) {
                Lela.blockUI(App.quizOptions.targetSelector);

                // Register the user
                Lela.register(App.get('user'),
                    function(data) {
                        Lela.unblockUI(App.quizOptions.targetSelector);
                        App.registeredUser = data;
                        router.transitionTo('saveQuiz');
                    },
                    function(error) {
                        Lela.unblockUI(App.quizOptions.targetSelector);
                        App.get('router').get('quizErrorController').set('message', error);
                        router.transitionTo('error');
                    }
                );
            }
        }),

        saveQuiz: Ember.State.extend({
            enter: function(router) {
                Lela.blockUI(App.quizOptions.targetSelector);

                Lela.saveQuiz(App.registeredUser, function(data) {
                            Lela.unblockUI(App.quizOptions.targetSelector);
                    router.transitionTo('quizCompleted');
                },
                function(error) {
                    Lela.unblockUI(App.quizOptions.targetSelector);
                    App.get('router').get('quizErrorController').set('message', error);
                    router.transitionTo('error');
                });
            }
        }),

        quizCompleted: Ember.Route.extend({
            route: '/completed',
            enter: function(router) {
                Lela.trackEvent('quiz complete', App._quiz.get('rlnm'));

                // Set a quiz completed cookie
                Lela.setCookie(Lela.QUIZ_DONE_COOKIE, 'true', 365);

                if (App.quizOptions.quizCompleted) {
                    App.quizOptions.quizCompleted({});
                }

                if (App._quiz.get('cmpltrl')) {
                    window.location = App._quiz.get('cmpltrl');
                } else if (App._quiz.get('cmpltcntnt')) {
                    router.get('applicationController').set('templateName', 'application-splash');
                }
            },
            connectOutlets: function (router, context) {
                if (!App._quiz.get('cmpltrl') && App._quiz.get('cmpltcntnt')) {
                    router.get('quizStaticContentController').set('returned', false);
                    router.get('applicationController').connectOutlet('quizStaticContent', App._quiz.get('cmpltcntnt'));
                }
            }
        }),

        returned: Ember.Route.extend({
            route: '/returned',
            enter: function(router) {
                Lela.trackEvent('quiz return visit', App._quiz.get('rlnm'));

                // Set a quiz completed cookie
                Lela.setCookie(Lela.QUIZ_DONE_COOKIE, 'true', 365);

                if (App._quiz.get('rtrncntnt')) {
                    router.get('applicationController').set('templateName', 'application-splash');
                } else {
                    router.get('applicationController').set('templateName', 'application');
                    router.transitionTo('');
                }
            },
            connectOutlets: function (router, context) {
                if (App._quiz.get('rtrncntnt')) {
                    router.get('quizStaticContentController').set('returned', true);
                    router.get('applicationController').connectOutlet('quizStaticContent', App._quiz.get('rtrncntnt'));
                }
            }
        }),

        error: Ember.Route.extend({
            route: '/error',
            connectOutlets: function(router, context) {
                router.get('applicationController').connectOutlet('quizError');
            }
        })

        // Note: Route Quiz Steps are dynamically created in _buildSteps(...)
    })

    App.Router = Ember.Router.extend({
        enableLogging: true,
        location: 'hash',
        root: App.RootState
    });
};

/****************************************************
 *   DEFINE VIEWS
 ****************************************************/
Lela.defineViews = function() {
    App.ApplicationView = Ember.View.extend({
        templateName: function() {
            return App.getPath('router.applicationController.templateName');
        }.property('App.router.applicationController.templateName'),

        _templateNameChanged: function() {
            this.rerender();
        }.observes('templateName'),

        changeStep: function(event) {
            var state = 'step-' + event.context.get('step').rlnm;
            App.get('router').transitionTo(state);
        },
        saveQuiz: function(event) {
            this.get('controller').saveQuiz();
        },
        registerUser: function(event) {
            this.get('controller').registerUser();
        }
    });

    App.QuizStaticContentView = Ember.View.extend({
        templateName: 'quiz-static-content'
    });

    App.QuizErrorView = Ember.View.extend({
        templateName: 'error'
    });

    App.QuizStepView = Ember.View.extend(
        App.FadeTransition,
        {
            templateName: function() {
                var type = this.getPath('controller.tp');
                if (type) {
                    return 'quiz-step-' + type;
                } else {
                    return 'quiz-step';
                }
            }.property(),

            ntrs: function () {
                return this.getPath('controller.ntrs');
            }.property(),

            didInsertElement: function(event) {
                var type = this.getPath('controller.tp');
                if(type == "REGISTRATION") {
                    App.set('requiresRegistration', true);
                    Lela.$('input:first').focus();
                }

                // Callback for step started
                Lela.trackEvent('step start', this.getPath('controller.rlnm'));
                if (App.quizOptions.stepStarted) {
                    App.quizOptions.stepStarted({ stepId: this.getPath('controller.rlnm')});
                }
            }
        }
    );

    App.QuizStepEntryView = Ember.View.extend({
        templateName: function() {
            var type = this.getPath('content.question.tp');
            if (type) {
                return 'question-' + type;
            } else {
                return 'question';
            }
        }.property(),

        didInsertElement: function(event) {
            this.$().find('.question-row').mouseenter(function() {
                Lela.$(this).addClass('selected');
            }).mouseleave(function() {
                Lela.$(this).removeClass('selected');
            });
        }
    });

    App.QuizAnswerView = Ember.View.extend({
        templateName: 'answer'
    });

    App.QuizAnswerSliderView = Ember.View.extend({
        templateName: 'answer-SLIDER',
        click: function(event) {
            // Ensure that no other answers of the current question are selected
            var parent = this.get('parentView');
            parent.getPath('content.question.nswrs').forEach(function(answer) {
                answer.set('isSelected', false);
            });

            // Set the current answer as selected
            this.get('content').set('isSelected', true);

            return false;
        }
    });

    App.QuizAnswerImageView = Ember.View.extend({
        templateName: 'answer-IMAGE',
        click: function(event) {
            // Ensure that no other answers of the current question are selected
            var parent = this.get('parentView');
            parent.getPath('content.question.nswrs').forEach(function(answer) {
                answer.set('isSelected', false);
            });

            // Set the current answer as selected
            this.get('content').set('isSelected', true);

            return false;
        },

        didInsertElement: function(event) {
            Lela.$('.quiz-images a').each(function(index) {
                Lela.$(this).addClass('img-'+index);
            });
        }
    });

    App.QuizAnswerGenderView = Ember.View.extend({
        templateName: 'answer-GENDER',
        click: function(event) {
            // Ensure that no other answers of the current question are selected
            var parent = this.get('parentView');
            parent.getPath('content.question.nswrs').forEach(function(answer) {
                answer.set('isSelected', false);
            });

            // Set the current answer as selected
            this.get('content').set('isSelected', true);
            App.setPath('user.gender', this.getPath('content.nm'));

            return false;
        }
    });

    App.QuizStepSplashView = Ember.View.extend({
        templateName: 'content-SPLASH'
    });
};

/****************************************************
 *   EMBER CONTROLLERS
 ****************************************************/
Lela.defineControllers = function() {
    App.ApplicationController = Ember.ObjectController.extend({
        templateName: 'application',
        saveQuiz: function() {
            // Save quiz goes here
            alert("Saving quiz");
        },
        registerUser: function() {
            // Register user goes here
            alert("Registering user");
        },
        tabSteps: function() {

            var result = [];
            var tabStep = null;
            var i = 1;

            this.get('content').get('stps').forEach(function(step) {
                if (step.get('tp') != "SPLASH") {
                    tabStep = TabStep.create({
                        step: step,
                        ky: i
                    });

                    result.push(tabStep);
                    i++;
                }
            });

            return result;

        }.property('content.stps')
    });

    App.QuizStaticContentController = Ember.ObjectController.extend({
        bdy: null,
        returned: false,
        _contentChanged: function(event) {
            var controller = this;
            var url = Lela.baseUrl + '/api/quiz/content/' + this.get('content') + '?_lelacd=' + Lela.lelacd + '&applicationId=' + App.quizOptions.applicationId + '&affiliateId=' + App.quizOptions.affiliateId + '&returned=' + this.get('returned');
            var xhr = Lela.$.ajax(url,
                Lela.request({
                    type: 'GET',
                    dataType: 'json',
                    success: function(json) {
                        controller.set('bdy', new Handlebars.SafeString(json.bdy));
                    },
                    error: function(error) {
                        alert('Error: ' + error.statusText);
                    }
                })
                );
        }.observes('content')
    });

    App.QuizErrorController = Ember.ObjectController.extend({
        message: null
    });

    App.QuizStepController = Ember.ObjectController.extend({
        buttonText: function() {
            return this.getPath('content.isLastStep') ? "FINISH" : "NEXT STEP";
        }.property('content.isLastStep'),

        isNextActive: function() {
            var result = true;

            var content = this.get('content');
            if (content) {
                if (this.get('tp') == 'QUESTION') {
                    this.get('ntrs').forEach(function(entry) {
                        if (!entry.get('isAnswered')) {
                            result = false;
                        }
                    });
                } else if (this.get('tp') == 'REGISTRATION') {
                    result = App.get('user').get('isValidEmail');
                }
            }

            Ember.Logger.log('Step is complete? ' + result);
            return result;
        }.property('content.ntrs.@each.isAnswered', 'App.user.email').cacheable(false)
    });

},

/****************************************************
 *   EMBER QUIZ CLASSES
 ****************************************************/

Lela.defineClasses = function() {
    Quiz = Ember.Object.extend({
        nm: null,
        rlnm: null,
        stps: null,
        analyticsEnabled: false,
        kmEnabled: false,
        kmKey: null,
        srcd: null,
        cmpltrl: null,
        cmpltcntnt: null,
        rtrncntnt: null,
        setUnknownProperty: function(key, value) {
            return false;
        }
    });

    QuizStep = Ember.Object.extend({
        rdr: null,
        rlnm: null,
        nm: null,
        d: null,
        tp: null,
        ntrs: null,

        // App property to be moved to a controller
        isCurrentStep: function() {
            var current = App.get('router').get('quizStepController').content;
            var same = this == current;

            return same;
        }.property('App.router.quizStepController.content'),

        isPastStep1: function() {
            var current = App.get('router').get('quizStepController').content;
            if(this.get('rdr') + 1 < current.get('rdr')) {
                return true;
            } else {
                return false;
            }
        }.property('App.router.quizStepController.content'),

        isPastStep2: function() {
            var current = App.get('router').get('quizStepController').content;
            if(this.get('rdr') + 1 == current.get('rdr')) {
                return true;
            } else {
                return false;
            }
        }.property('App.router.quizStepController.content'),

        isLastStep: false,

        setUnknownProperty: function(key, value) {
            return false;
        }
    });

    TabStep = Ember.Object.extend({
        step: null,
        ky: null,
        setUnknownProperty: function(key, value) {
            return false;
        }
    });

    QuizStepEntry = Ember.Object.extend({
        rdr: null,
        rlnm: null,
        question: null,
        staticContent: null,
        d: null,

        // App function to be moved to a Controller
        isAnswered: function() {
            var result = false;

            if (this.getPath('question.nswrs')) {
                this.getPath('question.nswrs').forEach(function(answer) {
                    if (answer.get('isSelected')) {
                        result = true;
                    }
                });
            }

            Ember.Logger.log('Question is answered? ' + result);
            return result;
        }.property('question.nswrs.@each.isSelected').cacheable(false),

        setUnknownProperty: function(key, value) {
            return false;
        }
    });

    Question = Ember.Object.extend({
        grp: null,
        nm: null,
        rlnm: null,
        tp: null,
        nswrs: null,
        setUnknownProperty: function(key, value) {
            return false;
        }
    });

    Answer = Ember.Object.extend({
        ky: null,
        mg: null,
        nm: null,
        d: null,

        // App property to be moved to a Controller
        image: null,
        isSelected: false,

        setUnknownProperty: function(key, value) {
            return false;
        }
    });

    StaticContent = Ember.Object.extend({
        rlnm: null,
        bdy: null,
        nm: null,
        setUnknownProperty: function(key, value) {
            return false;
        }
    });
};

/****************************************************
 *   BUILD QUIZ FROM JSON
 ****************************************************/

Lela._buildQuiz = function(json) {
    var quiz = Quiz.create({
        nm: json.nm,
        rlnm: json.rlnm,
        stps: Lela._buildSteps(json.stps),
        analyticsEnabled: json.analyticsEnabled,
        kmEnabled: json.kmEnabled,
        kmKey: json.kmKey,
        srcd: json.srcd,
        cmpltrl: json.cmpltrl,
        cmpltcntnt: json.cmpltcntnt,
        rtrncntnt: json.rtrncntnt
    });

    return quiz;
};

Lela._buildQuizStaticContent = function(content) {
    var result = null;
    if (content) {
        result = StaticContent.create({
            rlnm: content.rlnm,
            nm: content.nm,
            bdy: new Handlebars.SafeString(content.bdy)
        });
    }

    return result;
}

Lela._buildSteps = function(list) {
    var result = [];
    var step = null;

    // Build the DTO objects
    list.forEach(function(each) {
        step = QuizStep.create({
            rdr: each.rdr,
            rlnm: each.rlnm,
            nm: each.nm,
            d: each.d,
            tp: each.tp,
            ntrs: Lela._buildEntries(each.ntrs)
        });

        // Omit the registration step if all registration information is already preset
        if(step.tp == "REGISTRATION" && App.getPath('user.email') && App.getPath('user.firstName') && App.getPath('user.lastName')) {
            App.set('requiresRegistration', true);
            return;
        }

        result.push(step);
    });

    if (step) {
        step.set('isLastStep', true);
    }

    // Build the router steps in reverse
    var nextState = null;
    var i = result.length;
    while (i--) {
        // Add a Router entry
        step = result[i];
        var stateName = "step-" + step.get('rlnm');
        var route = '/';
        if (i > 0) {
            route += stateName;
        }

        var additional = {};
        additional[stateName] = Ember.Route.extend({
            route: route,
            step: step,
            connectOutlets: function (router, context) {
                router.get('applicationController').connectOutlet('quizStep', this.step);
            },
            next: nextState
        });

        App.RootState.reopen(additional);

        nextState = stateName;
    }

    return result;
};

Lela._buildEntries = function(list) {
    if(list) {
        var result = [];
        list.forEach(function(each) {
            result.push(QuizStepEntry.create({
                rdr: each.rdr,
                rlnm: each.rlnm,
                d: each.d,
                question: Lela._buildQuestion(each.question),
                staticContent: Lela._buildStaticContent(each.staticContent)
            }));
        });
    }

    return result;
};

Lela._buildQuestion = function(each) {
    if (each != null) {
        var result = Question.create({
            grp: each.grp,
            nm: each.nm,
            rlnm: each.rlnm,
            tp: each.tp,
            nswrs: Lela._buildAnswers(each.nswrs)
        });
    }

    return result;
};

Lela._buildAnswers = function(list) {
    var result = [];
    list.forEach(function(each) {
        var answer = Answer.create({
            ky: each.ky,
            mg: each.mg,
            nm: each.nm,
            d: each.d
        });
        result.push(answer);

        // Preload the image
        if (each.mg) {
            answer.set('image', Lela.$('<img/>')[0].src=each.mg);
        }
    });

    return result;
};

Lela._buildStaticContent = function(each) {

    var result = null;

    if (each != null) {
        result = StaticContent.create({
            rlnm: each.rlnm,
            nm: each.nm,
            bdy: new Handlebars.SafeString(each.bdy)
        });
    }

    return result;
};

/****************************************************
 *   SERVER CALLS
 ****************************************************/
Lela.register = function(user, callback, errorCallback) {
    var data = {
        "email": user.get('email'),
        "firstName": user.get('firstName'),
        "lastName": user.get('lastName'),
        "optin":true,
        "applicationId": App.quizOptions.applicationId,
        "affiliateId": App.quizOptions.affiliateId
    };

    var json = JSON.stringify(data);

    // Call the registration API
    Ember.Logger.log("Register User");
    var xhr = Lela.$.ajax(Lela.baseUrl + '/api/user?_lelacd=' + Lela.lelacd,
        Lela.request({
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: json,
            success: function(data) {
                if (data.userAlreadyExists) {
                    App.get('user').set('isCurrentUser', true);
                }

                Lela.callRegistrationCompleted(user, data, false);
                callback(data);
            },
            error: function(response) {
                if (response.responseText) {
                    var data = Lela.$.parseJSON(response.responseText);
                    if (data && data.message && data.message == Lela.USER_ALREADY_EXISTS_ERROR) {
                        App.get('user').set('isCurrentUser', true);
                        Lela.callRegistrationCompleted(user, data, true);
                        callback(data);
                        return;
                    }
                }

                Lela.trackEvent('quiz error', 'registration');
                errorCallback('User registration failed: ' + data.message);
            }
        })
    );
};

Lela.callRegistrationCompleted = function(user, data, alreadyRegistered) {
    if (App.quizOptions.registrationCompleted) {
        var callbackData = {
            email: data.email,
            firstName: user.get('firstName'),
            lastName: user.get('lastName'),
            alreadyRegistered: alreadyRegistered
        };

        // Check for a Gender question
        if (user.get('gender')) {
            callbackData.gender = user.get('gender');
        }

        App.quizOptions.registrationCompleted(callbackData);
    }
};

Lela.saveQuiz = function(registeredUser, callback, errorCallback) {
    var data = {
        "applicationId": App.quizOptions.applicationId,
        "affiliateId": App.quizOptions.affiliateId,
        "quizUrlName": App._quiz.get('rlnm'),
        "list": []
    };

    if (registeredUser) {
        data.email = registeredUser.email;
    }

    if (App._quiz.get('stps')) {
        App._quiz.get('stps').forEach(function(step) {
            if (step.get('ntrs')) {
                step.get('ntrs').forEach(function(entry) {
                    if (entry.getPath('question.nswrs')) {
                        entry.getPath('question.nswrs').forEach(function(answer) {
                            if (answer.get('isSelected')) {
                                data.list.push({
                                    questionUrlName: entry.getPath('question.rlnm'),
                                    answerKey: answer.get('ky')
                                });
                            }
                        })
                    }
                });
            }
        });
    }

    var json = JSON.stringify(data);

    // Call the save quiz API
    Ember.Logger.log("Saving Quiz");
    var xhr = Lela.$.ajax(Lela.baseUrl + '/api/quiz?_lelacd=' + Lela.lelacd,
        Lela.request({
            type: 'POST',
            contentType: 'application/json;charset=UTF-8',
            dataType: 'json',
            data: json,
            success: function(data) {
                if (App.quizOptions.saveQuizCompleted) {
                    App.quizOptions.saveQuizCompleted({ email: data.email });
                }

                callback(data);
            },
            error: function(response) {
                Lela.trackEvent('quiz error', 'save');

                var data = Lela.$.parseJSON(response.responseText);
                var message = data['message'] != undefined ? data['message'] : data;
                errorCallback('User registration failed: ' + message);
            }
        })
    );
};

/****************************************************
 *   TRANSITIONS
 ****************************************************/
Lela.defineTransitions = function() {
    App.FadeTransition = Ember.Mixin.create({
        didInsertElement: function() {
            //Ember.Logger.log('didInsertElement', this.$());
            return this.$().hide().fadeIn(App.transitionSpeed);
        },
        transitionIn: function() {
            //Ember.Logger.log('transitionIn', this.$());
            return this.$().hide().fadeIn(App.transitionSpeed);
        },
        transitionOut: function(transition) {
            //Ember.Logger.log('transitionOut', this.$());
            return this.$().fadeOut(App.transitionSpeed, function() {
                transition.resume();
            });
        }
    });

    App.SlideTransition = Ember.Mixin.create({
        didInsertElement: function() {
            Ember.Logger.log('didInsertElement', this.$());
            return this.$().hide().slideDown(App.transitionSpeed);
        },
        transitionIn: function() {
            Ember.Logger.log('transitionIn', this.$());
            return this.$().hide().slideDown(App.transitionSpeed);
        },
        transitionOut: function() {
            Ember.Logger.log('transitionOut', this.$());
            return this.$().slideUp(App.transitionSpeed);
        }
    });

};

/****************************************************
 *   Validation
 ****************************************************/
Lela.validateEmail = function(elementValue) {
    var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailPattern.test(elementValue);
};

/****************************************************
 *   UTILITIES
 ****************************************************/
Lela.request = function(options) {
    if (Lela.crossDomain) {
        console.log("Sending cross domain Ajax request");
        options.crossDomain = true;
        options.xhrFields = { withCredentials: true };
    }

    return options;
};

Lela.blockUI = function(id) {
    Lela.$.blockUI({
        message:  '<img src="' + Lela.baseUrl + '/static/images/icons/lela_loader.gif" alt="loading"/>',
        css: {
        border: 'none',
        padding: '15px',
        backgroundColor: 'none',
        opacity: 1
        },
        overlayCSS:  {
            backgroundColor: '#fff',
            opacity:         0.8
        }
    });
};

Lela.unblockUI = function(id) {
    Lela.$.unblockUI();
};

/****************************************************
 *   GOOGLE ANALYTICS
 ****************************************************/
Lela.trackEvent = function(action, label) {
    if(App._quiz.get('analyticsEnabled') == true) {
        var cat = 'quiz-' + App.quizOptions.affiliateId;
        var value = +1;

        if (console) console.log('Analytics: ' + cat + ' - ' + action + ' - ' + label + ' - ' + value);
        _gaq.push([Lela.googleTracker + '_trackEvent', cat, action, label, value]);
    }
};

/****************************************************
 *   ANALYTICS
 ****************************************************/

Lela.trackStep = function(stepId) {
    var url = Lela.baseUrl + '/api/quiz/metrics/step.gif?';
    url += '_lelacd=' + Lela.lelacd;
    url += '&affiliateId=' + App.quizOptions.affiliateId;
    url += '&applicationId=' + App.quizOptions.applicationId;
    url += '&quizId=' + App._quiz.get('rlnm');
    url += '&stepId=' + stepId;
    url += '&_t=' + new Date().getTime();

    var img = new Image();
    img.src = url;
    img.height = 1;
    img.width = 1;
    document.body.appendChild(img);
}
