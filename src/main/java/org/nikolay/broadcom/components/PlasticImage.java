package org.nikolay.broadcom.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;

/**
 * Integration of plastic-image web component {@url https://www.webcomponents.org/element/plastic-image}
 */
@Tag("plastic-image")
@NpmPackage(value = "plastic-image", version = "3.0.0")
@JsModule("plastic-image/plastic-image.js")
public class PlasticImage extends Component implements HasStyle {

    public PlasticImage() {
        getElement().setAttribute("sizing", "contain");
    }

    /**
     * Set source of image
     * @param src
     */
    public void setSrc(@NotEmpty String src) {
        if(StringUtils.isBlank(src)) {
            throw new IllegalArgumentException("Src can't be empty");
        }

        getElement().setAttribute("srcset", src);
    }

    /**
     * Enable or disable lazy loading of image
     * @param lazy
     */
    public void setLazyLoad(boolean lazy) {
        if(lazy) {
            getElement().setAttribute("lazy-load", "");
        } else {
            getElement().removeAttribute("lazy-load");
        }
    }
}
