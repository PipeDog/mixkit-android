package com.pipedog.mixkit.websdk.interfaces.widget;

import android.view.ViewGroup;

import com.pipedog.mixkit.websdk.constants.WebStyle;

public interface IWidget {

    int getWidgetWidth();
    int getWidgetHeight();

    default void setTheme(@WebStyle.WebTheme int theme) {

    }

}
