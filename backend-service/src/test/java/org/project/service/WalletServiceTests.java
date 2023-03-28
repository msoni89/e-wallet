package org.project.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.project.dtos.CreateWallet;
import org.project.dtos.CurrencyDTO;
import org.project.dtos.wallet.WalletDTO;
import org.project.models.Currency;
import org.project.models.User;
import org.project.models.Wallet;
import org.project.repositories.ICurrencyRepository;
import org.project.repositories.IUserRepository;
import org.project.repositories.IWalletRepository;
import org.project.services.impl.WalletService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTests {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IWalletRepository walletRepository;

    @Mock
    private ICurrencyRepository currencyRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void testCreateWallet() {
        User user = User.builder().lastname("test")
                .createdAt(Timestamp.from(Instant.now()))
                .enabled(true)
                .firstname("test")
                .email("mson@gmail.com")
                .build();

        when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(User.builder().lastname("test")
                        .createdAt(Timestamp.from(Instant.now()))
                        .enabled(true)
                        .firstname("test")
                        .email("mson@gmail.com")
                        .build()));

        CurrencyDTO currencyDTO = CurrencyDTO.builder().id(1L).code("INR").name("Indian Rupee").symbol("Ru").build();

        Currency currency = Currency.builder().id(1L).code("INR").name("Indian Rupee").symbol("Ru").build();

        when(currencyRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(currency));

        when(walletRepository.save(Mockito.any())).thenReturn(Wallet.builder().user(user).enabled(true).name("Account 1").currency(currency).build());

        WalletDTO wallet = walletService.create(1L, CreateWallet.builder().currencyId(currencyDTO.getId()).name("Account1").build());

        assertEquals("Account 1", wallet.getName());
    }


}
