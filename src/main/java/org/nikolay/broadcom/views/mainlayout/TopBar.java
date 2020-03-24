package org.nikolay.broadcom.views.mainlayout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@CssImport("./styles/top-bar.css")
class TopBar extends Div {

    private Mode currentMode = Mode.DARK;

    TopBar(@NotNull ModeSwitchListener listener) {
        Objects.requireNonNull(listener);

        setClassName("top-bar");

        Div logo = new Div();
        logo.addClassName("top-bar-logo");

        Div panel = new Div();
        panel.setClassName("header-panel");

        Label title = new Label("Where is the world?");

        Button modeSwitcher = new Button(currentMode.getModeName());
        modeSwitcher.addClickListener(e -> {
            currentMode = Mode.getNextMode(currentMode);
            modeSwitcher.setText(currentMode.getModeName());

            listener.onModeChange(currentMode);
        });

        panel.add(title, modeSwitcher);
        add(logo, panel);
    }

    interface ModeSwitchListener {
        void onModeChange(Mode mode);
    }
}
