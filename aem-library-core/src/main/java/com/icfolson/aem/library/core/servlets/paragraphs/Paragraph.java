package com.icfolson.aem.library.core.servlets.paragraphs;

public final class Paragraph {

    private final String path;

    private final String html;

    public Paragraph(final String path, final String html) {
        this.path = path;
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public String getPath() {
        return path;
    }
}
