/*
 ~ Copyright (c) 2012. Lela.com.
 */

function Filter(_filterContainer, _dataUrl, _dataType) {

    this.filterContainer = _filterContainer; // DOM container for filters

    this.formType = 'POST'; // Form type (default POST)
    this.dataUrl = _dataUrl; // Data url for AJAX form send
    this.dataType = _dataType; // Data type (json, html)

    this.filterQuery = {}; // Current filter query
    this.json; // The filter query in JSON sent to the server

    this.changedCallback; // Method to call when a value has been changed (any value)
    this.jsonCallback; // Method to call when constructing the json query for extra processing, returns json
    this.beforeSendCallback; // Method to call upon before sending the filters
    this.errorCallback; // Method to call upon when error has occurred when sending the filters
    this.successCallback; // Method to call upon successful call for the filters

    this.range_min; // Minimum value for price slider (absolute minimum)
    this.range_max; // Maximum value for price slider (absolute maximum)
    this.price_min; // Minimum value for price slider (current value)
    this.price_max; // Maximum value for price slider (current value)

    var self = this; // maintain a reference to this Filter object across jQuery closures

    /*
     ~ Initialize the filter
     ~ Before calling this you want to define the jsonCallback, beforeSendCallback and successCallback methods for this instance
     */

    this.init = function() {

        // Set price range slider values and initialize the slider plugin if price slider is present

        if($(this.filterContainer +' .amount_min').length > 0) {

            this.price_min = Number($(this.filterContainer +' .amount_min').val().replace('$',''));
            this.price_max = Number($(this.filterContainer +' .amount_max').val().replace('$',''));
            this.range_min = Number($(this.filterContainer +' .range_min').val());
            this.range_max = Number($(this.filterContainer +' .range_max').val());

            var slider = $(this.filterContainer +' .slider-range').slider({
                range: true,
                min: self.range_min,
                max: self.range_max,
                step: 1,
                values: [ self.price_min, self.price_max ],
                slide: function( event, ui ) {
                    $(self.filterContainer +' .amount_min').val('$' + ui.values[ 0 ]);
                    $(self.filterContainer +' .amount_max').val('$' + ui.values[ 1 ]);
                },
                stop: function(event, ui) {
                    trackEvent('user', 'price range', 'high', Number(ui.values[ 1 ]));
                    trackEvent('user', 'price range', 'low', Number(ui.values[ 0 ]));

                    // Call the changed callback
                    if(self.changedCallback) {
                        self.changedCallback([$(self.filterContainer +' .amount_min').val(), $(self.filterContainer +' .amount_max').val()]); // pass the values for this price range
                    }

                    self.submit();
                }
            });

            // Update slider when field is updated by typing in the value, min value
            $(this.filterContainer +' .amount_min').data('timeout', null).keyup(function() {
                var value = Number($(self.filterContainer +' .amount_min').val().replace(/[^0-9]+/g,''));
                clearTimeout($(this).data('timeout'));
                $(this).data('timeout', setTimeout(function() {

                    if(value < Number($(self.filterContainer +' .range_min').val())) {
                        value = $(self.filterContainer +' .range_min').val();
                    } else if (value >= Number($(self.filterContainer +' .amount_max').val().replace('$',''))) {
                        value = Number($(self.filterContainer +' .amount_max').val().replace('$','')) - 10;
                    }

                    slider.slider( 'values', 0, value );
                    $(self.filterContainer +' .amount_min').val('$'+String(value));

                    // Call the changed callback
                    if(self.changedCallback) {
                        self.changedCallback([$(self.filterContainer +' .amount_min').val(), $(self.filterContainer +' .amount_max').val()]); // pass the values for this price range
                    }

                    self.submit();

                }, 1000));
            });

            // Same for max
            $(this.filterContainer +' .amount_max').data('timeout', null).keyup(function() {
                var value = Number($(self.filterContainer +' .amount_max').val().replace(/[^0-9]+/g,''));
                clearTimeout($(this).data('timeout'));
                $(this).data('timeout', setTimeout(function() {

                    if(value > Number($(self.filterContainer +' .range_max').val())) {
                        value = $(this.filterContainer +' .range_max').val();
                    } else if (value <= Number($(self.filterContainer +' .amount_min').val().replace('$',''))) {
                        value = Number($(self.filterContainer +' .amount_min').val().replace('$','')) + 10;
                    }

                    slider.slider( 'values', 1, value );
                    $(self.filterContainer +' .amount_max').val('$'+String(value));

                    // Call the changed callback
                    if(self.changedCallback) {
                        self.changedCallback([$(self.filterContainer +' .amount_min').val(), $(self.filterContainer +' .amount_max').val()]); // pass the values for this price range
                    }

                    self.submit();

                }, 1000));
            }); // END Update slider when field is updated

            $(this.filterContainer +' .amount_min').val( "$" + $(this.filterContainer +' .slider-range').slider( "values", 0 ));
            $(this.filterContainer +' .amount_max').val( "$" + $(this.filterContainer +' .slider-range').slider( "values", 1 ));

        }

        // Normal options
        $(this.filterContainer +' .lela-filter .filter-option').click(function(e) {

            e.preventDefault();

            // stop execution if this filter is disabled
            if($(this).parent().hasClass('filter-disabled')) {
                return true;
            }
            else {
                var activeCheckbox = $(this).next('.filter-hidden-input'); // find the real checkbox for this item

                if($(this).hasClass('filter-option-selected')) {
                    $(this).removeClass('filter-option-selected');
                    activeCheckbox.removeAttr('checked');
                }
                else {
                    $(this).addClass('filter-option-selected');
                    activeCheckbox.attr('checked', 'checked');
                }

                if(!activeCheckbox.attr('checked')) {
                    trackEvent('user', 'filters', activeCheckbox.attr('name'), 1);
                }

                self.submit();
            }

            // Call the changed callback
            if(self.changedCallback) {
                self.changedCallback(this);
            }

        });


    }

    /*
     ~ Construct the filter object and submit it as JSON
     */

    this.submit = function() {

        this.filterQuery.filters = {};

        // Construct the filter object
        $(this.filterContainer +' .lela-filter').each(function() {
            var key = $(this).attr('key');
            self.filterQuery.filters[key] = [];
            self.filterQuery.filters[key] = {};

            // Exception for price range filter, filter key needs high and low value
            if($(this).hasClass('price-range')) {
                self.filterQuery.filters[key].high = $(this).find('.amount_max').val().replace('$','');
                self.filterQuery.filters[key].low = $(this).find('.amount_min').val().replace('$','');
            } else {
                $(this).find('.filter-option-container').each(function() {
                    if ($(this).find('input:checked').val() !== undefined) {
                        var tKey = $(this).attr('option').split(/\s+/)[0];
                        $(this).find('input').val()
                        self.filterQuery.filters[key][tKey] = $(this).find('input').val();
                    }
                });
            }
        });

        // Call jsonCallback, if present, to add / filter the filterQuery object
        if(this.jsonCallback) {
            this.json = this.jsonCallback(this.filterQuery); // it's assumed that the callback returns json so we don't have to call JSON.stringify
        }
        else {
            this.json = JSON.stringify(this.filterQuery);
        }

        $.ajax({
            cache: false,
            url: self.dataUrl,
            type: self.formType,
            contentType: 'application/json; charset=utf-8',
            data : self.json,
            dataType : self.dataType,
            beforeSend: function( xhr ) {
                if(self.beforeSendCallback) {
                    self.beforeSendCallback(xhr);
                }
            },
            error: function( jqXHR, textStatus, errorThrown ) {
                if(self.errorCallback) {
                    self.errorCallback(jqXHR, textStatus, errorThrown);
                }
            },
            success: function( data ) {
                if(self.successCallback) {
                    self.successCallback(data);
                }
            }
        });

    }

}