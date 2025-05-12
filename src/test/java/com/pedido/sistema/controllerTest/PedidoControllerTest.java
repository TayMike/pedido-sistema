package com.pedido.sistema.controllerTest;

import com.pedido.sistema.controller.PedidoController;
import com.pedido.sistema.entities.Pedido;
import com.pedido.sistema.services.PedidoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PedidoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    private UUID id = UUID.randomUUID();
    private Pedido pedido;
    private AutoCloseable openMocks;
    private String json = """
        {
            "id": "%s",
            "sku": ["ONE0001"],
            "cpf": "11111111111",
            "cartao": "1111111111111111",
            "quantidade": [10]
        }
        """.formatted(id.toString());

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pedidoController).build();
        pedido = Pedido.builder()
                .id(id)
                .sku(List.of("ONE0001"))
                .cpf("11111111111")
                .cartao("1111111111111111")
                .quantidade(List.of(10L))
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testEncontrarPedido_Success() throws Exception {
        // Arrange
        when(pedidoService.encontrarPedido(pedido.getId())).thenReturn(Optional.of(pedido));

        // Act & Assert
        mockMvc.perform(get("/pedidos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku[0]").value("ONE0001"));
        verify(pedidoService, times(1)).encontrarPedido(id);
    }

    @Test
    void testEncontrarPedido_NotFound() throws Exception {
        // Arrange
        when(pedidoService.encontrarPedido(pedido.getId())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/pedidos/{id}", id))
                .andExpect(status().isNotFound());
        verify(pedidoService, times(1)).encontrarPedido(id);
    }

    @Test
    void testEncontrarTodos_Success() throws Exception {
        // Arrange
        when(pedidoService.encontrarPedidos()).thenReturn(List.of(pedido));

        // Act & Assert
        mockMvc.perform(get("/pedidos/todos", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku[0]").value("ONE0001"));
        verify(pedidoService, times(1)).encontrarPedidos();
    }

    @Test
    void testEncontrarTodos_NotFound() throws Exception {
        // Arrange
        when(pedidoService.encontrarPedidos()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/pedidos/todos", id))
                .andExpect(status().isNotFound());
        verify(pedidoService, times(1)).encontrarPedidos();
    }

    @Test
    void testCadastrarPedido_Success() throws Exception {
        // Arrange
        when(pedidoService.cadastrarPedido(any(Pedido.class))).thenReturn(pedido);

        // Act & Assert
        mockMvc.perform(post("/pedidos/cadastrar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().json(json));
        verify(pedidoService, times(1)).cadastrarPedido(any(Pedido.class));
    }

    @Test
    void testCadastrarPedido_BadRequest() throws Exception {
        // Arrange
        when(pedidoService.cadastrarPedido(any(Pedido.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

        // Act & Assert
        mockMvc.perform(post("/pedidos/cadastrar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(pedidoService, times(1)).cadastrarPedido(any(Pedido.class));
    }

    @Test
    void testAtualizarPedido_Success() throws Exception {
        // Arrange
        when(pedidoService.encontrarPedido(id)).thenReturn(Optional.of(pedido));
        when(pedidoService.alterarPedido(any(Pedido.class))).thenReturn(pedido);

        // Act & Assert
        mockMvc.perform(put("/pedidos/alterar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
        verify(pedidoService, times(1)).alterarPedido(any(Pedido.class));
    }

    @Test
    void testAtualizarPedido_BadRequest() throws Exception {
        // Arrange
        when(pedidoService.encontrarPedido(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/pedidos/alterar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(pedidoService, times(0)).alterarPedido(any(Pedido.class));
    }

    @Test
    void testDeletarPedido_Success() throws Exception {
        // Arrange
        when(pedidoService.encontrarPedido(pedido.getId())).thenReturn(Optional.of(pedido));

        // Act & Assert
        mockMvc.perform(delete("/pedidos/{id}", id))
                .andExpect(status().isNoContent());
        verify(pedidoService, times(1)).deletarPedido(id);
    }

    @Test
    void testDeletarPedido_BadRequest() throws Exception {
        // Arrange
        when(pedidoService.encontrarPedido(pedido.getId())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/pedidos/{id}", id))
                .andExpect(status().isBadRequest());
        verify(pedidoService, times(0)).deletarPedido(id);
    }

}
