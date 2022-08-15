package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Country;

import java.util.List;

public interface CountryService {

    List<Country> getCountries();

    void addCountry(Country country);

    List<Country> getCountries(String game);

}
