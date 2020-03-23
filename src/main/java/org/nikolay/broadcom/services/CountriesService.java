package org.nikolay.broadcom.services;

import org.nikolay.broadcom.model.Country;
import org.nikolay.broadcom.model.CountryInfo;
import org.nikolay.broadcom.model.NoSuchCountryException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Countries service provides information about every country in the world
 */
public interface CountriesService {

    /**
     * Provides information about all countries
     * @return set of countries with basic information
     */
    @NotNull
    List<Country> getCountries() throws IOException;

    /**
     * Provides information about countries with corresponding name
     * @param name
     * @return
     * @throws IOException
     */
    List<Country> getCountriesByname(@NotEmpty String name) throws IOException;

    /**
     * Provides information about countries from provided region
     * @param region
     * @return
     * @throws IOException
     */
    List<Country> getCountriesByRegion(@NotEmpty String region) throws IOException;

    /**
     * Provides all world regions
     * @return set of regions names
     */
    @NotNull
    List<String> getRegions();

    /**
     * Converts country alpha3 code to country name;
     * @param code country code in alpha3 format
     * @return country name if the code is known
     */
    Optional<String> getCountryName(@NotEmpty String code);

    /**
     * Provides information about requested country
     *
     * @param code requested country alpha3 code
     * @return full information about requested country
     * @throws NoSuchCountryException throws in case if country is not exist
     */
    @NotNull
    CountryInfo getCountryInfo(@NotEmpty String code) throws NoSuchCountryException, IOException;
}
