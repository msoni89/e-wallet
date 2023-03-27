package org.project.services.impl;

import lombok.AllArgsConstructor;
import org.project.dtos.CurrencyDTO;
import org.project.repositories.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CurrencyService implements org.project.services.CurrencyService {
    private final CurrencyRepository currencyService;

    @Override
    public List<CurrencyDTO> findAll() {
        return currencyService.findAll().stream().map(currency ->
                CurrencyDTO.builder()
                        .code(currency.getCode())
                        .name(currency.getName())
                        .id(currency.getId())
                        .symbol(currency.getSymbol())
                        .build())
                .collect(Collectors.toList());
    }
}
