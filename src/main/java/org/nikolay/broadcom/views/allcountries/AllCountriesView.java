package org.nikolay.broadcom.views.allcountries;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.apache.commons.lang3.StringUtils;
import org.nikolay.broadcom.model.Country;
import org.nikolay.broadcom.services.CountriesService;
import org.nikolay.broadcom.views.CountryView;
import org.nikolay.broadcom.views.mainlayout.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.util.List;

@CssImport("./styles/all-countries-view.css")
@Route(value = "countries", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class AllCountriesView extends Div {

    private final transient Div countriesContainer = new Div();

    @Autowired
    public AllCountriesView(
            @Qualifier("countriesService") CountriesService countriesService,
            FilterBar filterBar) {

        setClassName("all-countries-view");
        countriesContainer.setClassName("countries-container");


        // TODO Refactor in the way to use FilterAction interface and invoke action by filter bar
        // This view will only register filter actions observer to retrieve list of countries and render it
        try {
            renderTiles(countriesService.getCountries());
        } catch (IOException e) {
            e.printStackTrace();
        }

        filterBar.setFilterByNameListener(name -> {
            try {
                renderTiles(StringUtils.isBlank(name) ? countriesService.getCountries() : countriesService.getCountriesByname(name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        filterBar.setFilterByRegionListener(region -> {
            try {
                renderTiles(StringUtils.isBlank(region) ? countriesService.getCountries() : countriesService.getCountriesByRegion(region));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        add(filterBar, countriesContainer);
    }

    private void renderTiles(List<Country> countries) {
        countriesContainer.removeAll();

        countries.forEach(country -> {
            CountryTile countryTile = new CountryTile(country);
            countriesContainer.add(countryTile);

            countryTile.addClickListener(e -> getUI().orElseThrow(() -> new RuntimeException("Can't get UI")).navigate(CountryView.class, country.getAlpha3Code()));
        });
    }
}
