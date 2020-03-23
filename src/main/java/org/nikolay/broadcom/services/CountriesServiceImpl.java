package org.nikolay.broadcom.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.nikolay.broadcom.model.Country;
import org.nikolay.broadcom.model.CountryInfo;
import org.nikolay.broadcom.model.NoSuchCountryException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

class CountriesServiceImpl implements CountriesService {

    // TODO use some cache mechanism with expiration time
    private final Cache<Map<String, String>> countriesCodesMap;
    private final Cache<List<String>> regions;
    private final Cache<List<Country>> countriesCache;

    private final ObjectMapper objectMapper = new ObjectMapper();

    CountriesServiceImpl() {
        countriesCache = new Cache<>(this::loadCountries);

        countriesCodesMap = new Cache<>(() -> Collections.unmodifiableMap(
                countriesCache.getValue().stream()
                        .collect(Collectors.toMap(Country::getAlpha3Code, Country::getName))));

        regions = new Cache<>(() -> Collections.unmodifiableList(
                countriesCache.getValue().stream()
                        .map(Country::getRegion)
                        .distinct()
                        .filter(name -> !name.isEmpty())
                        .sorted()
                        .collect(Collectors.toList())));
    }

    @Override
    public @NotNull List<Country> getCountries() throws IOException {
        return countriesCache.getValue();
    }

    @Override
    public List<Country> getCountriesByname(@NotEmpty String name) throws IOException {
        if(StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name can't be null or empty");
        }

        HttpGet get = new HttpGet("https://restcountries.eu/rest/v2/name/" + URLEncoder.encode(name, "UTF-8") + "?fields=flag;name;capital;population;region;alpha3Code;");

        try (
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                CloseableHttpResponse httpResponse = httpClient.execute(get);
                Reader responseReader = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8)) {

            return Collections.unmodifiableList(objectMapper.readValue(responseReader, new CountriesList()));
        }
    }

    @Override
    public List<Country> getCountriesByRegion(@NotEmpty String region) throws IOException {
        if(StringUtils.isBlank(region)) {
            throw new IllegalArgumentException("Region can't be empty");
        }

        HttpGet get = new HttpGet("https://restcountries.eu/rest/v2/region/" + URLEncoder.encode(region, "UTF-8") + "?fields=flag;name;capital;population;region;alpha3Code;");

        try (
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                CloseableHttpResponse httpResponse = httpClient.execute(get);
                Reader responseReader = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8)) {

            return Collections.unmodifiableList(objectMapper.readValue(responseReader, new CountriesList()));
        }
    }

    @Override
    public @NotNull List<String> getRegions() {
        return regions.getValue();
    }

    @Override
    public Optional<String> getCountryName(@NotEmpty String code) {
        if(StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("Country code can't be empty");
        }

        return Optional.ofNullable(countriesCodesMap.getValue().get(code));
    }

    @Override
    public @NotNull CountryInfo getCountryInfo(@NotEmpty String code) throws NoSuchCountryException, IOException {
        if(StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("Code can't be null or empty");
        }

        HttpGet get = new HttpGet("https://restcountries.eu/rest/v2/alpha/" + URLEncoder.encode(code, "UTF-8") + "?fields=flag;name;capital;population;region;alpha3Code;nativeName;subregion;topLevelDomain;currencies;languages;borders;");

        try (
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                CloseableHttpResponse httpResponse = httpClient.execute(get);
                Reader responseReader = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8)) {

            if(httpResponse.getStatusLine().getStatusCode() == 404) {
                throw new NoSuchCountryException(code);
            }

            return objectMapper.readValue(responseReader, CountryInfo.class);
        }
    }

    private List<Country> loadCountries() {
        HttpGet get = new HttpGet("https://restcountries.eu/rest/v2/all?fields=flag;name;capital;population;region;alpha3Code;");

        try {
            try (
                    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                    CloseableHttpResponse httpResponse = httpClient.execute(get);
                    Reader responseReader = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8)) {

                return Collections.unmodifiableList(objectMapper.readValue(responseReader, new CountriesList()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class CountriesList extends TypeReference<List<Country>> { }
}
