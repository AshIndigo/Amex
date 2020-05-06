package com.ashindigo.amex.widgets;

import com.ashindigo.amex.modules.AmexModule;
import spinnery.widget.WItem;
import spinnery.widget.WTooltipItem;

public class WTooltipModule extends WItem {

    private AmexModule module;

    @SuppressWarnings("UnusedReturnValue")
    public WTooltipModule setModule(AmexModule module) {
        this.module = module;
        return this;
    }

    public AmexModule getModule() {
        return module;
    }

    @Override
    public boolean isFocusedMouseListener() {
        return true;
    }
}
