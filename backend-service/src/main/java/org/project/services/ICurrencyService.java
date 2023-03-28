package org.project.services;

import org.project.dtos.CurrencyDTO;

import java.util.List;


public interface ICurrencyService {
    List<CurrencyDTO> findAll();
}
