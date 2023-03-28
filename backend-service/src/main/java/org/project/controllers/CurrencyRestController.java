package org.project.controllers;

import lombok.AllArgsConstructor;
import org.project.dtos.CurrencyDTO;
import org.project.services.ICurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currency")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CurrencyRestController {

    private final ICurrencyService currencyService;

    @GetMapping
    private ResponseEntity<List<CurrencyDTO>> findAll() {
        return ResponseEntity.ok().body(currencyService.findAll());
    }

}
