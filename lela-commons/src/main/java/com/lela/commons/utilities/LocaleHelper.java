/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.utilities;

import com.lela.domain.dto.DisplayLocale;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 9/2/11
 * Time: 12:06 PM
 * Responsibility:
 */
public class LocaleHelper {
    private Map<String, String> countries = new HashMap<String, String>();
    private Map<String, String> languages = new HashMap<String, String>();
    private List<DisplayLocale> uniqueLanguages = null;
    private List<DisplayLocale> uniqueCountries = null;
    private Locale locale = null;

    public LocaleHelper() {
        processLocales();
    }

    public LocaleHelper(Locale displayLocale) {
        this.locale = displayLocale;
        processLocales();
    }

    public static void main(String[] args) {
        LocaleHelper lh = new LocaleHelper();
        lh.printUniqueLocales();
    }

    public List<DisplayLocale> getUniqueLanguages() {
        if (uniqueLanguages == null) {
            uniqueLanguages = new ArrayList<DisplayLocale>();

            for (String key : languages.keySet()) {
                uniqueLanguages.add(new DisplayLocale(key, languages.get(key)));
            }

            Collections.sort(uniqueLanguages);
        }

        return uniqueLanguages;
    }

    public List<DisplayLocale> getUniqueCountries() {
        if (uniqueCountries == null) {
            uniqueCountries = new ArrayList<DisplayLocale>();

            for (String key : countries.keySet()) {
                uniqueCountries.add(new DisplayLocale(key, countries.get(key)));
            }

            Collections.sort(uniqueCountries);
        }

        return uniqueCountries;
    }

    private void processLocales() {
        Locale[] locales = Locale.getAvailableLocales();

        for (Locale l : locales) {
            if (StringUtils.isNotBlank(l.getCountry()) && !countries.containsKey(l.getCountry())) {
                if (locale != null) {
                    countries.put(l.getCountry(), l.getDisplayCountry(locale));
                } else {
                    countries.put(l.getCountry(), l.getDisplayCountry(l));
                }
            }
        }

        for (Locale l : locales) {
            if (!languages.containsKey(l.getLanguage())) {
                if (locale != null) {
                    languages.put(l.getLanguage(), l.getDisplayLanguage(locale));
                } else {
                    languages.put(l.getLanguage(), l.getDisplayLanguage(l));
                }
            }
        }
    }

    public void printUniqueLocales() {
        System.out.println("# countries");
        for (String country : countries.keySet()) {
            System.out.println(country + "=" + countries.get(country));
        }

        System.out.println("# languages");
        for (String language : languages.keySet()) {
            System.out.println(language + "=" + languages.get(language));
        }
    }
}
