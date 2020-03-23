package org.nikolay.broadcom.views.allcountries;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import org.jsoup.helper.Validate;
import org.nikolay.broadcom.components.PlasticImage;
import org.nikolay.broadcom.model.Country;

import javax.validation.constraints.NotNull;

@CssImport("./styles/country-tile.css")
class CountryTile extends Div {

    CountryTile(@NotNull Country country) {
        Validate.notNull(country);

        setClassName("country-tile");

        PlasticImage flag = new PlasticImage();
        flag.setSrc(country.getFlag());
        flag.setLazyLoad(true);
        flag.setClassName("country-tile-flag");

        Div infoContent = new Div();
        infoContent.setClassName("country-tile-info-content");

        Label name = new Label();
        name.setText(country.getName());
        name.setClassName("country-tile-name");

        Html population = new Html(String.format("<div><strong>Population</strong>: %s</div>", country.getPopulation()));
        Html region = new Html(String.format("<div><strong>Region</strong>: %s</div>", country.getRegion()));
        Html capital = new Html(String.format("<div><strong>Capital</strong>: %s</div>", country.getCapital()));

        infoContent.add(name, population, region, capital);
        add(flag, infoContent);
    }
}
