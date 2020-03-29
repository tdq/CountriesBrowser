package org.nikolay.broadcom.views.mainlayout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.RouterLayout;

@CssImport("./styles/main-layout.css")
public class MainLayout extends Div implements RouterLayout {

    public MainLayout() {
        setClassName("main-layout");

        TopBar topBar = new TopBar(this::modeSwitch);

        add(topBar);
    }

    private void modeSwitch(Mode mode) {
        UI.getCurrent().getElement().setAttribute("mode", mode.name());
    }
}
