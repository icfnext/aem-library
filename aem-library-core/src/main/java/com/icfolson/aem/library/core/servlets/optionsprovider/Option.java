package com.icfolson.aem.library.core.servlets.optionsprovider;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Text/value pair for displaying in a selection dialog widget, used in conjunction with the
 * <code>AbstractOptionsProviderServlet</code>.
 */
public final class Option {

    public static final Option EMPTY = new Option("", "");

    public static final Comparator<Option> ALPHA = Comparator.comparing(Option:: getText);

    public static final Comparator<Option> ALPHA_IGNORE_CASE = (option1, option2) -> option1.getText()
        .compareToIgnoreCase(option2.getText());

    /**
     * @param map map where key=[option value] and value=[option text]
     * @return list of options for map
     */
    public static List<Option> fromMap(final Map<String, String> map) {
        return map.entrySet()
            .stream()
            .map(entry -> new Option(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    private final String value;

    private final String text;

    public Option(final String value, final String text) {
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
