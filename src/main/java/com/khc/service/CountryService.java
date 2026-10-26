package com.khc.service;

import com.khc.dto.CountrySaveReq;
import com.khc.model.Country;
import com.khc.repository.CountryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryRepo countryRepo;

    public String countrySave(CountrySaveReq countrySaveReq) {

        if (countrySaveReq.getCountryName() == null) return "country name is mandatory";
        Country country = new Country();
        country.setCountryName(countrySaveReq.getCountryName());
        country.setIsActive(countrySaveReq.getIsActive());
        country.setCreatedDate(LocalDateTime.now());
        country.setModifyDate(LocalDateTime.now());
        country.setCreatedId(countrySaveReq.getCreatedId());
        country.setModifyId(countrySaveReq.getModifyId());
        countryRepo.save(country);
        return "Country is saved successfully";
    }

    public List<Country> findAll() {
        return countryRepo.findAll();
    }
}
