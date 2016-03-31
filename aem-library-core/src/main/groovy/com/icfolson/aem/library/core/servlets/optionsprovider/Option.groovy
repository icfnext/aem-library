package com.icfolson.aem.library.core.servlets.optionsprovider

import groovy.transform.Immutable

/**
 * Text/value pair for displaying in a selection dialog widget, used in conjunction with the
 * <code>AbstractOptionsProviderServlet</code>.
 */
@Immutable
class Option {

    public static final Comparator<Option> ALPHA = new Comparator<Option>() {
        @Override
        int compare(Option option1, Option option2) {
            option1.text.compareTo(option2.text)
        }
    }

    public static final Comparator<Option> ALPHA_IGNORE_CASE = new Comparator<Option>() {
        @Override
        int compare(Option option1, Option option2) {
            option1.text.compareToIgnoreCase(option2.text)
        }
    }

    String value

    String text

    /**
     * @param map map where key=[option value] and value=[option text]
     * @list of options created from map
     */
    public static List<Option> fromMap(Map<String, String> map) {
        map.collect { value, text -> new Option(value, text) } as List
    }
}
