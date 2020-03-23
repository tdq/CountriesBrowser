package org.nikolay.broadcom.views.mainlayout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;

@CssImport("./styles/top-bar.css")
class TopBar extends Div {

    TopBar() {
        Div logo = new Div();
        logo.addClassName("top-bar-logo");

        add(logo, new Label("Where is the world?"), new Button("Dark Mode"));
    }
}
