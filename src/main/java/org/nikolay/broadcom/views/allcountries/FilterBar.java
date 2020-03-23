package org.nikolay.broadcom.views.allcountries;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import org.nikolay.broadcom.services.CountriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@CssImport("./styles/filter-bar.css")
class FilterBar extends Div {

    private TextField searchField = new TextField();
    private ComboBox<String> regions = new ComboBox<>();
    private FilterByNameListener filterByNameListener;
    private FilterByRegionListener filterByRegionListener;

    @Autowired
    FilterBar(@Qualifier("countriesService")CountriesService countriesService) {
        // TODO refactor in the way to use filter beans
        // FilterBar should autowire all implementations of Filter interface, render filters and process their listeners

        searchField.setClearButtonVisible(true);
        searchField.setPlaceholder("Search for a country...");
        searchField.addValueChangeListener(e -> {
            if(filterByNameListener != null) {
                filterByNameListener.onFilter(e.getValue());
            }
        });

        regions.setAllowCustomValue(false);
        regions.setClearButtonVisible(true);
        regions.setPlaceholder("Filter by Region");
        regions.setItems(countriesService.getRegions());
        regions.addValueChangeListener(e -> {
            if(filterByRegionListener != null) {
                filterByRegionListener.onFilter(e.getValue());
            }
        });

        add(searchField, regions);
    }

    void setFilterByNameListener(@NotNull FilterByNameListener listener) {
        this.filterByNameListener = Objects.requireNonNull(listener, "Filter listener must not be null");
    }

    void setFilterByRegionListener(@NotNull FilterByRegionListener listener) {
        this.filterByRegionListener = Objects.requireNonNull(listener, "Filter listener must not be null");
    }

    interface FilterByNameListener {
        void onFilter(@Nullable String name);
    }

    interface FilterByRegionListener {
        void onFilter(@NotEmpty String region);
    }
}
