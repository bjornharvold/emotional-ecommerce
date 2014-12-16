/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
"use strict";

var Lela = Lela || {};

/* App Module */
(function(global) {

var USER_ALREADY_EXISTS_ERROR = 'User already exists',
    QUIZ_DONE_COOKIE = '_lelaqzdn';

var quizApp = angular.module('com.lela.quiz', ['com.lela.util', 'com.lela.quiz.controllers', 'com.lela.quiz.directives'])
.constant('widgetType', 'quiz')

.config(['$routeProvider', function($routeProvider) {
   $routeProvider.otherwise({ templateUrl: Lela.baseUrl + '/static/api/quiz/partials/quiz-container.html?_lelacd=' + Lela.lelacd + '&_rnd=' + Math.floor(Math.random()*10000000000), controller: 'Lela.QuizController' });
}])
.run(['$rootElement', 'appOptions', function($rootElement, appOptions) {
    // Make sure the root element has the ng-view attribute set
    var element = angular.element($rootElement);
    if (!element.attr("ng-view")) {
        element.attr("ng-view", "");
    }

    // Grab configuration options from the element
    var eventRegex = /^on[A-Z].*$/;
    var dataAttrs = element.data();
    for (var key in dataAttrs) {
        if (!key.match(eventRegex)) {
            appOptions[key] = dataAttrs[key];
            console.log("App option: " + key + '=' + dataAttrs[key]);
        } else {
            var fn = global[dataAttrs[key]];
            if (fn) {
                appOptions[key] = fn;
                console.log("App callback found: " + dataAttrs[key]);
            }
        }
    }

    // If a theme stylesheet is defined, load it now
    if (appOptions && appOptions.theme) {
        // Load the base quiz stylesheets
        Lela.loadCss(Lela.baseUrl + '/static/api/quiz/themes/' + appOptions.theme + '.css');
    }

}]);

/* Controllers Module */
angular.module('com.lela.quiz.controllers', ['com.lela.util'])
.controller('Lela.QuizController', ['$scope', '$rootScope', '$timeout', 'baseUrl', 'appOptions', 'lelaConfig', 'lelaQuizService', 'lelaHttp', 'lelaUI', 'lelaQuizCookie', 'lelaAnalytics', function($scope, $rootScope, $timeout, baseUrl, appOptions, lelaConfig, lelaQuizService, lelaHttp, lelaUI, lelaQuizCookie, lelaAnalytics) {

    $rootScope.showQuiz = false;
    $timeout(function() {
        if (!$rootScope.showQuiz) {
            lelaUI.blockRoot();
            $rootScope.$watch('showQuiz', function(newValue, oldValue) {
                if (newValue) {
                    lelaUI.unblockRoot();
                }
            });
        }
    }, 500);

    // Set up user object
    $rootScope.requiresRegistration = false;
    $rootScope.user = {};
    if (lelaConfig.hasUser()) {
        var user = lelaConfig.getUser();
        $rootScope.user.lelacd = user.lelacd;
        $rootScope.user.firstName = user.firstName;
        $rootScope.user.lastName = user.lastName;
        $rootScope.user.email = user.email;
        $rootScope.user.gender = user.gender;

        $rootScope.requiresRegistration = user.email ? true : false;
    }

    // If all user information present, requires registration = true
    // $rootScope.requiresRegistration = true;

    console.log('quiz.controller');
    lelaAnalytics.trackEvent('app started', appOptions.applicationId);

    if (!appOptions) {
        alert("Initialization appOptions were not defined");
        return;
    }

    var url = baseUrl + '/api/quiz/' + appOptions.applicationId + '?affiliateId=' + appOptions.affiliateId + '&_lelacd=' + Lela.lelacd + '&_rnd=' + appOptions.random;

    console.log("Calling server for Quiz JSON");
    var xhr = Lela.$.ajax(url,
            lelaHttp.formatRequest({
                type: 'get',
                dataType: 'json',
                success: function(data) {
                    console.log("Retrieved Quiz JSON");
                    $rootScope.data = data;

                    // Process Quiz Data
                    processQuizData(appOptions, $rootScope, $scope, data);

                    // Force the template to update with the data
                    $rootScope.$apply();

                    // Set up google analytics
                    appOptions.analyticsEnabled = data.analyticsEnabled;
                    lelaAnalytics.initGA();
                    lelaAnalytics.trackEvent('data loaded', appOptions.applicationId);

                    // Callback
                    if (appOptions.onQuizCreated) {
                        appOptions.onQuizCreated(quizApp);
                    }

                    // If returning, explicity go to the returned state
                    if (lelaQuizCookie.hasQuizCompleteCookie() && data.rtrncntnt) {
                        $rootScope.$broadcast('showStaticContent', data.rtrncntnt, true);
                    }
                },
                error: function() {
                    console.log("Error retrieving quiz JSON");
                    lelaAnalytics.trackEvent('data error', appOptions.applicationId);
                    alert('Could not load quiz definition');
                }
            })
    );

    $scope.baseUrl = baseUrl;

    $scope.tabSteps = function() {
        var result = [  ];
        if ($scope.data && $scope.data.stps) {
            $scope.data.stps.forEach(function(step) {
                if (step.tp != "SPLASH") {
                    result.push(step);
                }
            });
        }

        return result;
    };

    $scope.stepClass = function(step) {
        var classes = [];

        if ($rootScope.currentStep) {
            if ($rootScope.currentStep == step) {
                classes.push('selected');
            }

            if (step.rdr + 1 < $rootScope.currentStep.rdr) {
                classes.push('past1');
            }

            if (step.rdr + 1 == $rootScope.currentStep.rdr) {
                classes.push('past2');
            }

            if (step.isLastStep) {
                classes.push('last');
            }
        }

        return classes;
    };

    function processQuizData(appOptions, $rootScope, $scope, data) {
        if (data) {
            appOptions.quizId = data.rlnm;

            if (data.stps && data.stps.length > 0) {
                data.stps[data.stps.length-1].isLastStep = true;
            }
        }
    }

    $scope.which = function () {
        return "Lela.QuizController";
    }

    //
    // STATE MANAGEMENT
    //

    // Manage save quiz event
    $rootScope.saveQuiz = function($event) {
        if ($event) {
            $event.preventDefault();
            $event.stopPropagation();
        }

        if ($rootScope.isNextActive) {
            lelaUI.blockRoot();

            lelaQuizService.saveQuiz($rootScope.registeredUser,
                function(data) {
                    lelaUI.unblockRoot();
                    $rootScope.quizCompleted($event);
                },
                function(error) {
                    lelaUI.unblockRoot();
                    $rootScope.error($event, error);
                }
            );
        }

        return false;
    };

    $rootScope.quizCompleted = function($event) {
        lelaAnalytics.trackEvent('quiz complete', appOptions.applicationId);

        // Set a quiz completed cookie
        lelaQuizCookie.setQuizCompleteCookie();
        if (appOptions.onQuizCompleted) {
            appOptions.onQuizCompleted(quizApp);
        }

        if ($rootScope.data.cmpltrl) {
            window.location = $rootScope.data.cmpltrl;
        } else if ($rootScope.data.cmpltcntnt) {
            $rootScope.$broadcast('showStaticContent', $rootScope.data.cmpltcntnt, false);
        }
    };

    $rootScope.error = function($event, errorMessage) {
        if ($event) {
            $event.preventDefault();
            $event.stopPropagation();
        }

        $rootScope.$broadcast('error', errorMessage);
    };
}])

.controller('Lela.ContainerController', ['$element', '$scope', '$rootScope', '$compile', 'baseUrl', 'appOptions', 'lelaHttp', function($element, $scope, $rootScope, $compile, baseUrl, appOptions, lelaHttp) {
    function errorHandler($event, errorMessage) {
        $event.preventDefault();

        $scope.errorMessage = errorMessage;
        $element.html('<h2><span>Oooops... Something wrong happened!</span></h2><div id="quiz-error-message">{{errorMessage}}</div>');
        $compile($element.contents())($scope);
    };
    $scope.$on('error', errorHandler);

    $scope.$on('showStaticContent', function($event, contentId, returned) {
        var xhr = Lela.$.ajax(baseUrl + '/api/quiz/content/' + contentId + '?_lelacd=' + Lela.lelacd,
            lelaHttp.formatRequest({
                type: 'POST',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify({
                    "rlnm": contentId,
                    "returned": returned ? true : false,
                    "applicationId": appOptions.applicationId,
                    "affiliateId": appOptions.affiliateId
                }),
                success: function(json) {
                    $rootScope.showQuiz = true;
                    $element.html(json.bdy);
                },
                error: function(error) {
                    errorHandler($event, error);
                }
            })
        );
    });
}])

.controller('Lela.QuestionStepController', ['$scope', '$rootScope', 'baseUrl', 'appOptions', function($scope, $rootScope, baseUrl, appOptions) {
    $rootScope.isNextActive = false;
    $scope.$on('answer', function(event) {
        var stepAnswered = true;
        $scope.step.ntrs.forEach(function(entry) {
            var entryAnswered = false;
            if (entry.question && entry.question.nswrs) {
                entry.question.nswrs.forEach(function(answer) {
                    if (answer.isSelected) {
                        entryAnswered = true;
                    }
                });
            }

            if (!entryAnswered) {
                stepAnswered = false;
            }
        });

        $rootScope.isNextActive = stepAnswered;
    });
}])

.controller('Lela.QuestionController', ['$scope', '$rootScope', 'baseUrl', 'appOptions', function($scope, $rootScope, baseUrl, appOptions) {
    $scope.clicked = function($event, answer) {
        $event.preventDefault();
        $event.stopPropagation();

        // Ensure that no other answers of the current question are selected
        $scope.question.nswrs.forEach(function(each) {
            each.isSelected = false;
        });

        // Set the current answer as selected
        answer.isSelected = true;
        //$scope.$apply();

        // Notify the parent step that something was answered
        $scope.$emit('answer');

        return false;
    };
}])

.controller('Lela.GenderQuestionController', ['$scope', '$rootScope', 'baseUrl', 'appOptions', function($scope, $rootScope, baseUrl, appOptions) {
    $scope.clicked = function($event, answer) {
        $event.preventDefault();
        $event.stopPropagation();

        // Ensure that no other answers of the current question are selected
        $scope.question.nswrs.forEach(function(each) {
            each.isSelected = false;
        });

        // Set the current answer as selected
        answer.isSelected = true;
        $rootScope.user.gender = answer.nm;

        // Notify the parent step that something was answered
        $scope.$emit('answer');

        return false;
    };
}])

.controller('Lela.RegistrationQuestionController', ['$scope', '$rootScope', 'lelaQuizService', 'lelaUI', 'baseUrl', 'appOptions', function($scope, $rootScope, lelaQuizService, lelaUI, baseUrl, appOptions) {
    $scope.checkValid = function() {
        if ($scope.registrationForm.$valid) {
            $rootScope.isNextActive = true;
        } else {
            $rootScope.isNextActive = false;
        }
    }

    $rootScope.registrationSubmit = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        if ($rootScope.isNextActive) {
            lelaUI.blockRoot();

            $rootScope.requiresRegistration = false;
            lelaQuizService.registerUser($rootScope.user,
                function(data) {
                    lelaUI.unblockRoot();
                    $rootScope.registeredUser = data;

                    // Transition to next state
                    if ($rootScope.hasNextStep()) {
                        $rootScope.next($event);
                    } else {
                        $rootScope.saveQuiz($event);
                    }
                },
                function(error) {
                    lelaUI.unblockRoot();
                    $rootScope.error($event, error);
                }
            );
        }

        return false;
    };
}]);

/* Directives Module */
angular.module('com.lela.quiz.directives', ['com.lela.util', 'com.lela.quiz.util'])
.directive('lelaStepNavigator', ['$http', '$templateCache', '$rootScope', '$compile', 'baseUrl', 'appOptions', 'lelaQuizAnalytics', function ($http, $templateCache, $rootScope, $compile, baseUrl, appOptions, lelaQuizAnalytics) {
    var def = {
        restrict: 'A',
        scope: true,
        controller: ['$scope', '$element', '$attrs', '$transclude', function($scope, $element, $attrs, $transclude) {
            var steps = null;
            var index = 0;
            var childScope;

            $scope.$watch($attrs.lelaStepNavigator, function(newValue, oldValue) {
                steps = $scope.$eval($attrs.lelaStepNavigator);
                index = 0;
                if (steps && steps.length > 0) {
                    loadStep();
                }
            });

            $rootScope.hasNextStep = function() {
                return steps && (index < steps.length-1);
            };

            $rootScope.isNextActive = false;
            $rootScope.setNextActive = function(value) {
                $rootScope.isNextActive = value;
            };

            $rootScope.next = function($event) {
                if ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                }

                if ($rootScope.isNextActive && steps && steps.length > 0) {
                    // Transition to next state
                    if ($rootScope.hasNextStep()) {
                        var priorIndex = index;
                        index = index + 1 >= steps.length ? index : index + 1;

                        if (priorIndex !== index) {
                            lelaQuizAnalytics.trackStep(steps[priorIndex].rlnm);
                            loadStep();

                            // Callback
                            if (appOptions.onStepCompleted) {
                                appOptions.onStepCompleted(quizApp, steps[priorIndex].rlnm);
                            }
                        }
                    } else if ($rootScope.requiresRegistration) {
                        $rootScope.registrationSubmit($event);
                    } else {
                        $rootScope.saveQuiz($event);
                    }

                }
            };

            function loadStep() {
                if (steps && steps[index]) {
                    $rootScope.isNextActive = false;
                    $rootScope.currentStep = steps[index];
                    var url = stepTemplate(steps[index]);
                    $http.get(url, {cache: $templateCache}).success(function(response) {
                        console.log('Loaded step template: ' + url);
                        if (childScope) childScope.$destroy();
                        childScope = $scope.$new();

                        childScope.step = steps[index];
                        childScope.isLastStep = index + 1 >= steps.length ? true : false;
                        childScope.buttonText = index + 1 >= steps.length ? "FINISH" : "NEXT STEP";

                        $element.html(response);
                        $compile($element.contents())(childScope);
                        $rootScope.showQuiz = true;
                    }).error(function() {
                        console.log("Could not load step template: " + url);
                    });
                }
            }

            function stepTemplate(step) {
                var url = baseUrl + '/static/api/quiz/partials/quiz-step-' + step.tp.toLowerCase() + '.html?_lelacd=' + Lela.lelacd + '&_rnd=' + appOptions.random;
                return url;
            }
        }]
    };

    return def;
}])
.directive('lelaSplashEntry', ['$rootScope', '$compile', 'appOptions', function ($rootScope, $compile, appOptions) {
    var def = {
        restrict: 'A',
        compile: function(element, attrs) {
            return {
                pre: function(scope, element, attrs, controller) {
                    var linkFn = $compile(scope.entry.staticContent.bdy);
                    element.replaceWith(linkFn(scope));
                }
            }
        }
    };

    return def;
}])
.directive('lelaQuestion', ['$http', '$templateCache', '$rootScope', '$compile', 'baseUrl', 'appOptions', function ($http, $templateCache, $rootScope, $compile, baseUrl, appOptions) {
    var def = {
        restrict: 'A',
        scope: true,
        compile: function(element, attrs) {
            return {
                pre: function(scope, element, attrs, controller) {
                    var question = scope.$eval(attrs.lelaQuestion);
                    if (question) {
                        scope.question = question;

                        var url = questionTemplate(question);
                        $http.get(url, {cache: $templateCache}).success(function(response) {
                            console.log('Loaded question template: ' + url);
                            element.html(response);
                            $compile(element.contents())(scope);
                        }).error(function() {
                            console.log("Could not load question template: " + url);
                        });
                    }
                }
            }
        }
    };

    return def;

    function questionTemplate(question) {
        var url = baseUrl + '/static/api/quiz/partials/question-' + question.tp.toLowerCase() + '.html?_lelacd=' + Lela.lelacd + '&_rnd=' + appOptions.random;
        return url;
    }
}]);

angular.module('com.lela.quiz.util', ['com.lela.util'])
.factory('lelaQuizService', ['$rootScope', 'lelaHttp', 'lelaAnalytics', 'baseUrl', 'appOptions', function($rootScope, lelaHttp, lelaAnalytics, baseUrl, appOptions) {
    function registerUser(user, callback, errorCallback) {
        var data = {
            "email": user.email,
            "firstName": user.firstName,
            "lastName": user.lastName,
            "optin": true,
            "applicationId": appOptions.applicationId,
            "affiliateId": appOptions.affiliateId
        };

        var json = JSON.stringify(data);

        // Call the registration API
        var xhr = Lela.$.ajax(baseUrl + '/api/user?_lelacd=' + Lela.lelacd,
                lelaHttp.formatRequest({
                    type: 'POST',
                    contentType: 'application/json',
                    dataType: 'json',
                    data: json,
                    success: function(data) {
                        callRegistrationCompleted(user, data, false);
                        callback(data);
                    },
                    error: function(response) {
                        if (response.responseText) {
                            var data = Lela.$.parseJSON(response.responseText);
                            if (data && data.message && data.message == USER_ALREADY_EXISTS_ERROR) {
                                $rootScope.user.isCurrentUser = true;
                                callRegistrationCompleted(user, data, true);
                                callback(data);
                                return;
                            }
                        }

                        lelaAnalytics.trackEvent('quiz error', 'registration');
                        errorCallback('User registration failed: ' + data.message);
                    }
                })
        );
    };

    function callRegistrationCompleted(user, data, alreadyRegistered) {
        if (appOptions.onUserRegistered) {
            var callbackData = {
                lelacd: data.srcd,
                email: data.email,
                firstName: $rootScope.user.firstName,
                lastName: $rootScope.user.lastName,
                alreadyRegistered: alreadyRegistered
            };

            // Check for a Gender question
            if ($rootScope.user.gender) {
                callbackData.gender = $rootScope.user.gender;
            }

            appOptions.onUserRegistered(quizApp, callbackData);
        }
    };

    function saveQuiz(registeredUser, callback, errorCallback) {
        var data = {
            "applicationId": appOptions.applicationId,
            "affiliateId": appOptions.affiliateId,
            "quizUrlName": appOptions.quizId,
            "list": []
        };

        if (registeredUser) {
            data.email = registeredUser.email;
        }

        if ($rootScope.data.stps) {
            $rootScope.data.stps.forEach(function(step) {
                if (step.ntrs) {
                    step.ntrs.forEach(function(entry) {
                        if (entry.question && entry.question.nswrs) {
                            entry.question.nswrs.forEach(function(answer) {
                                if (answer.isSelected) {
                                    data.list.push({
                                        questionUrlName: entry.question.rlnm,
                                        answerKey: answer.ky
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
        console.log("Saving Quiz");
        var xhr = Lela.$.ajax(baseUrl + '/api/quiz?_lelacd=' + Lela.lelacd,
                lelaHttp.formatRequest({
                    type: 'POST',
                    contentType: 'application/json;charset=UTF-8',
                    dataType: 'json',
                    data: json,
                    success: function(data) {
                        if (appOptions.onQuizSaved) {
                            appOptions.onQuizSaved({ email: data.email });
                        }

                        callback(data);
                    },
                    error: function(response) {
                        lelaAnalytics.trackEvent('quiz error', 'save');

                        var data = Lela.$.parseJSON(response.responseText);
                        var message = data['message'] != undefined ? data['message'] : data;
                        errorCallback('Save quiz failed: ' + message);
                    }
                })
        );
    };

    var service = {
        registerUser: registerUser,
        saveQuiz: saveQuiz
    };

    return service;
}])

.factory('lelaQuizCookie', ['$rootElement', 'baseUrl', 'lelaCookie', function($rootElement, baseUrl, lelaCookie){
    var QUIZ_DONE_COOKIE = '_lelaqzdn';

    function hasQuizCompleteCookie() {
        return lelaCookie.getCookie(QUIZ_DONE_COOKIE) === 'true';
    }

    function setQuizCompleteCookie() {
        // Set a quiz completed cookie
        lelaCookie.setCookie(QUIZ_DONE_COOKIE, 'true', 365);
    }

    var service = {
        hasQuizCompleteCookie: hasQuizCompleteCookie,
        setQuizCompleteCookie: setQuizCompleteCookie
    };

    return service;
}])

.factory('lelaQuizAnalytics', ['$rootScope', 'widgetType', 'baseUrl', 'appOptions', function($rootScope, widgetType, baseUrl, appOptions) {
    var service = {
        trackStep: function(stepId) {
            var url = baseUrl + '/api/quiz/metrics/step.gif?';
            url += '_lelacd=' + Lela.lelacd;
            url += '&affiliateId=' + appOptions.affiliateId;
            url += '&applicationId=' + appOptions.applicationId;
            url += '&quizId=' + appOptions.quizId;
            url += '&stepId=' + stepId;
            url += '&_t=' + new Date().getTime();

            var img = new Image();
            img.src = url;
            img.height = 1;
            img.width = 1;
            document.body.appendChild(img);
        }
    };

    return service;
}]);


})(this);