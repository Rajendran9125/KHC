package com.khc.controller;

import com.khc.dto.CountrySaveReq;
import com.khc.model.Country;
import com.khc.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @PostMapping("/save")
    public ResponseEntity<String> countrySave(@RequestBody CountrySaveReq countrySaveReq){
    String mas = countryService.countrySave(countrySaveReq);
    return new ResponseEntity<>(mas, HttpStatus.OK);
    }
    @GetMapping("/findAll")
    public List<Country> findAll(){
        return countryService.findAll();


    }

}
