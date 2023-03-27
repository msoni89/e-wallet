package org.project.services;

import lombok.AllArgsConstructor;
import org.project.dtos.CurrencyDTO;
import org.project.models.Currency;
import org.project.repositories.CurrencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


public interface CurrencyService {
    List<CurrencyDTO> findAll();
}
