package org.nikolay.broadcom.views;

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
        Button back = new Button("Back");
        back.addClickListener(e -> getUI().orElseThrow(() -> new RuntimeException("Can't get UI")).navigate(AllCountriesView.class));

        add(back);

        try {
            CountryInfo info = countriesService.getCountryInfo(parameter);

            PlasticImage flag = new PlasticImage();
            flag.setSrc(info.getFlag());
            flag.setLazyLoad(true);
            flag.setClassName("country-flag");

            Div infoPanel = new Div();

            Label name = new Label(info.getName());
            Label nativeName = new Label("Native name: " + info.getNativeName());
            Label population = new Label("Population: " + info.getPopulation());
            Label region = new Label("Region: " + info.getRegion());
            Label subRegion = new Label("Sub Region: " + info.getSubRegion());
            Label capital = new Label("Capital: " + info.getCapital());
            Label topLevelDomain = new Label("Top Level Domain: " + String.join(", ", info.getTopLevelDomain()));
            Label currencies = new Label("Currencies: " + String.join(", ", info.getCurrencies().stream().map(Currency::getName).collect(Collectors.toList())));
            Label languages = new Label("Languages: " + String.join(", ", info.getLanguages().stream().map(Language::getName).collect(Collectors.toList())));

            infoPanel.add(name, nativeName, population, region, subRegion, capital, topLevelDomain, currencies, languages);

            if(info.getBorders().isEmpty() == false) {
                Div borders = new Div();
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

            add(flag, infoPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
