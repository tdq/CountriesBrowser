package org.nikolay.broadcom.views;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.nikolay.broadcom.components.PlasticImage;
import org.nikolay.broadcom.model.CountryInfo;
import org.nikolay.broadcom.model.Currency;
import org.nikolay.broadcom.model.Language;
import org.nikolay.broadcom.services.CountriesService;
import org.nikolay.broadcom.views.allcountries.AllCountriesView;
import org.nikolay.broadcom.views.mainlayout.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.util.stream.Collectors;

@Route(value = "country", layout = MainLayout.class)
@CssImport("./styles/country-view.css")
public class CountryView extends Div implements HasUrlParameter<String> {

    private final transient CountriesService countriesService;

    @Autowired
    public CountryView(@Qualifier("countriesService") CountriesService countriesService) {
        this.countriesService = countriesService;

        setClassName("country-view");
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String parameter) {

        removeAll();

        Div topPanel = new Div();
        topPanel.setClassName("country-view-top-panel");

        Button back = new Button("Back");
        back.addClickListener(e -> getUI().orElseThrow(() -> new RuntimeException("Can't get UI")).navigate(AllCountriesView.class));

        topPanel.add(back);
        add(topPanel);

        try {
            CountryInfo info = countriesService.getCountryInfo(parameter);

            Div content = new Div();
            content.setClassName("country-view-content");

            PlasticImage flag = new PlasticImage();
            flag.setSrc(info.getFlag());
            flag.setLazyLoad(true);
            flag.setClassName("country-flag");

            Div infoPanel = new Div();
            infoPanel.setClassName("country-view-info-panel");

            Label name = new Label(info.getName());
            name.setClassName("country-view-name");

            Div countryStat = new Div();
            countryStat.setClassName("country-stat");

            Html nativeName = new Html(String.format("<div><strong>Native name</strong>: %s</div>", info.getNativeName()));
            Html population = new Html(String.format("<div><strong>Population</strong>: %s</div>", info.getPopulation()));
            Html region = new Html(String.format("<div><strong>Region</strong>: %s</div>", info.getRegion()));
            Html subRegion = new Html(String.format("<div><strong>Sub Region</strong>: %s</div>", info.getSubRegion()));
            Html capital = new Html(String.format("<div><strong>Capital</strong>: %s</div>", info.getCapital()));
            Html topLevelDomain = new Html(String.format("<div><strong>Top Level Domain</strong>: %s</div>", String.join(", ", info.getTopLevelDomain())));
            Html currencies = new Html(String.format("<div><strong>Currencies</strong>: %s</div>", String.join(", ", info.getCurrencies().stream().map(Currency::getName).collect(Collectors.toList()))));
            Html languages = new Html(String.format("<div><strong>Languages</strong>: %s</div>", String.join(", ", info.getLanguages().stream().map(Language::getName).collect(Collectors.toList()))));

            countryStat.add(nativeName, population, region, subRegion, capital, topLevelDomain, currencies, languages);
            infoPanel.add(name, countryStat);

            if(info.getBorders().isEmpty() == false) {
                Div borders = new Div();
                borders.setClassName("borders");

                Label borderCountries = new Label("Border Countries:");
                borders.add(borderCountries);

                info.getBorders().forEach(code -> {
                    countriesService.getCountryName(code).ifPresent(countryName -> {
                        Button codeButton = new Button(countryName);
                        codeButton.addClickListener(e -> getUI().orElseThrow(() -> new RuntimeException("Can't get UI")).navigate(CountryView.class, code));

                        borders.add(codeButton);
                    });
                });

                infoPanel.add(borders);
            }

            content.add(flag, infoPanel);
            add(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
