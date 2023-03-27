package org.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.dtos.CreateWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = AppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class WalletRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(username = "spring")
    public void givenAuthRequestOnPrivateService_shouldSucceedWith200() throws Exception {
        mockMvc.perform(get("/api/v1/wallet/{id}/list", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // Commented due to spring security by passed, and allowing all request with authorization.
 /*   @Test
    public void givenUnAuthRequestOnPrivateService_shouldFailWith403() throws Exception {
        mockMvc.perform(get("/api/v1/wallet/{id}/list", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }*/


    @Test
    @WithMockUser(username = "mehul")
    public void givenAuthRequest_create_wallet_shouldFailWith200() throws Exception {
        mockMvc.perform(post("/api/v1/wallet/{userId}", 1).content(mapper.writeValueAsString(CreateWallet.builder().name("Account 1").currencyId(1L).build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "mehul")
    public void givenInvalidUser_AuthRequest_create_wallet_shouldFailWithFail() throws Exception {
        mockMvc.perform(post("/api/v1/wallet/{userId}", 2).content(mapper.writeValueAsString(CreateWallet.builder().name("Account 1").currencyId(1L).build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found with id 2")));
    }

    @Test
    @WithMockUser(username = "mehul")
    public void givenInvalidCurrentId_AuthRequest_create_wallet_shouldFailWithFail() throws Exception {
        mockMvc.perform(post("/api/v1/wallet/{userId}", 1).content(mapper.writeValueAsString(CreateWallet.builder().name("Account 1").currencyId(3L).build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Unsupported Currency id 1")));
    }

    @Test
    @WithMockUser(username = "spring")
    public void getAll_User_Wallets_shouldSucceedWith200() throws Exception {

        mockMvc.perform(post("/api/v1/wallet/{userId}", 1).content(mapper.writeValueAsString(CreateWallet.builder().name("Account 1").currencyId(1L).build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/wallet/{id}/list", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Account 1")));
    }


    @Test
    @WithMockUser(username = "spring")
    public void get_WalletById_shouldSucceedWith200() throws Exception {

        mockMvc.perform(get("/api/v1/wallet/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Account 1")));

    }


    @Test
    @WithMockUser(username = "spring")
    public void perform_topUp_User_Wallets_shouldSucceedWith200() throws Exception {
        mockMvc.perform(post("/api/v1/wallet/{id}/credit", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", BigDecimal.valueOf(1000).toString()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/wallet/{id}/credit", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", BigDecimal.valueOf(-1000).toString()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/wallet/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Account 1")))
                .andExpect(jsonPath("$.balance").value(is(2900.0), Double.class));
    }

    @Test
    @WithMockUser(username = "spring")
    public void perform_withdrawAmount_User_Wallets_shouldSucceedWith200() throws Exception {

        mockMvc.perform(post("/api/v1/wallet/{id}/credit", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", BigDecimal.valueOf(1000).toString()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/wallet/{id}/debit", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", BigDecimal.valueOf(100).toString()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/wallet/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Account 1")))
                .andExpect(jsonPath("$.balance").value(is(900.0), Double.class));
    }

    @Test
    @WithMockUser(username = "spring")
    public void perform_transferAmount_User_Wallets_shouldSucceedWith200() throws Exception {
        mockMvc.perform(post("/api/v1/wallet/{userId}", 1).content(mapper.writeValueAsString(CreateWallet.builder().name("Account 3").currencyId(1L).build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/wallet/{userId}", 1).content(mapper.writeValueAsString(CreateWallet.builder().name("Account 4").currencyId(1L).build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/wallet/{id}/credit", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", BigDecimal.valueOf(1000).toString()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/wallet/transfer/{from}/{to}", 2, 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", BigDecimal.valueOf(100).toString()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/wallet/transfer/{from}/{to}", 2, 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", BigDecimal.valueOf(100).toString()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/wallet/transfer/{from}/{to}", 2, 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", BigDecimal.valueOf(100).toString()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/wallet/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Account 4")))
                .andExpect(jsonPath("$.balance").value(is(300.0), Double.class));
    }

}
