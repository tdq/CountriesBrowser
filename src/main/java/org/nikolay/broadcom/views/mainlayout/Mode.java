package org.nikolay.broadcom.views.mainlayout;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

enum Mode {
    DARK("Dark mode"), LIGHT("Light mode");

    private String modeName;

    Mode(@NotEmpty String modeName) {
        this.modeName = modeName;
    }

    @NotNull
    String getModeName() {
        return modeName;
    }

    static Mode getNextMode(Mode mode) {
        if(Mode.DARK == mode) {
            return Mode.LIGHT;
        } else {
            return Mode.DARK;
        }
    }
}
