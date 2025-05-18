package com.pagamento.sistema.controllerTest;

import com.pagamento.sistema.controller.PagamentoController;
import com.pagamento.sistema.entities.Pagamento;
import com.pagamento.sistema.services.PagamentoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PagamentoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PagamentoService pagamentoService;

    @InjectMocks
    private PagamentoController pagamentoController;

    private String cpf = "11111111111";
    private Pagamento pagamento;
    private AutoCloseable openMocks;
    private String json = """
            {
                "cpf": "11111111111",
                "numeroCartao": "1111111111111111",
                "codigoVerificador": "111",
                "dataValidadeCartao": "2000-11-01"
            }
            """;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pagamentoController).build();
        pagamento = Pagamento.builder()
                .cpf(cpf)
                .numeroCartao("1111111111111111")
                .codigoVerificador("111")
                .dataValidadeCartao(LocalDate.of(2000, 11, 1))
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testEncontrarPagamento_Success() throws Exception {
        // Arrange
        when(pagamentoService.encontrarPagamento(pagamento.getCpf())).thenReturn(Optional.of(pagamento));

        // Act & Assert
        mockMvc.perform(get("/pagamentos/{cpf}", cpf))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("11111111111"));
        verify(pagamentoService, times(1)).encontrarPagamento(cpf);
    }

    @Test
    void testEncontrarPagamento_NotFound() throws Exception {
        // Arrange
        when(pagamentoService.encontrarPagamento(pagamento.getCpf())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/pagamentos/{cpf}", cpf))
                .andExpect(status().isNotFound());
        verify(pagamentoService, times(1)).encontrarPagamento(cpf);
    }

    @Test
    void testEncontrarTodos_Success() throws Exception {
        // Arrange
        when(pagamentoService.encontrarPagamentos()).thenReturn(List.of(pagamento));

        // Act & Assert
        mockMvc.perform(get("/pagamentos/todos", cpf))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cpf").value("11111111111"));
        verify(pagamentoService, times(1)).encontrarPagamentos();
    }

    @Test
    void testEncontrarTodos_NotFound() throws Exception {
        // Arrange
        when(pagamentoService.encontrarPagamentos()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/pagamentos/todos", cpf))
                .andExpect(status().isNotFound());
        verify(pagamentoService, times(1)).encontrarPagamentos();
    }

    @Test
    void testCadastrarPagamento_Success() throws Exception {
        // Arrange
        when(pagamentoService.cadastrarPagamento(any(Pagamento.class))).thenReturn(pagamento);

        // Act & Assert
        mockMvc.perform(post("/pagamentos/cadastrar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().json(json));
        verify(pagamentoService, times(1)).cadastrarPagamento(any(Pagamento.class));
    }

    @Test
    void testCadastrarPagamento_BadRequest() throws Exception {
        // Arrange
        when(pagamentoService.cadastrarPagamento(any(Pagamento.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

        // Act & Assert
        mockMvc.perform(post("/pagamentos/cadastrar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(pagamentoService, times(1)).cadastrarPagamento(any(Pagamento.class));
    }

    @Test
    void testAtualizarPagamento_Success() throws Exception {
        // Arrange
        when(pagamentoService.encontrarPagamento(cpf)).thenReturn(Optional.of(pagamento));
        when(pagamentoService.alterarPagamento(any(Pagamento.class))).thenReturn(pagamento);

        // Act & Assert
        mockMvc.perform(put("/pagamentos/alterar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
        verify(pagamentoService, times(1)).alterarPagamento(any(Pagamento.class));
    }

    @Test
    void testAtualizarPagamento_BadRequest() throws Exception {
        // Arrange
        when(pagamentoService.encontrarPagamento(cpf)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/pagamentos/alterar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(pagamentoService, times(0)).alterarPagamento(any(Pagamento.class));
    }

    @Test
    void testDeletarPagamento_Success() throws Exception {
        // Arrange
        when(pagamentoService.encontrarPagamento(pagamento.getCpf())).thenReturn(Optional.of(pagamento));

        // Act & Assert
        mockMvc.perform(delete("/pagamentos/{cpf}", cpf))
                .andExpect(status().isNoContent());
        verify(pagamentoService, times(1)).deletarPagamento(cpf);
    }

    @Test
    void testDeletarPagamento_BadRequest() throws Exception {
        // Arrange
        when(pagamentoService.encontrarPagamento(pagamento.getCpf())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/pagamentos/{cpf}", cpf))
                .andExpect(status().isBadRequest());
        verify(pagamentoService, times(0)).deletarPagamento(cpf);
    }

}
