package com.nozagleh.captainmexico;

import android.text.Html;

/**
 * Created by arnarfreyr on 17.9.2017.
 */

public class UrlBuilder {

    private String url = "http://" + ToolBox.SERVER_ROOT + "/";

    private int fieldCount = 0;

    public UrlBuilder() {

    }

    public void addPath(String endpoint) {
        url += endpoint;
    }

    public void addField(String field, String value) {
        if (fieldCount >= 1)
            url += "&";
        else
            url += "?";

        url += field + "=" + value;

        fieldCount++;
    }

    public String getUrl() {
        return url;
    }
}
