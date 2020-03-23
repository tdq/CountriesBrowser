package org.nikolay.broadcom.views.mainlayout;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.RouterLayout;

@CssImport("./styles/main-layout.css")
public class MainLayout extends Div implements RouterLayout {

    public MainLayout() {
        TopBar topBar = new TopBar();

        add(topBar);
    }
}
