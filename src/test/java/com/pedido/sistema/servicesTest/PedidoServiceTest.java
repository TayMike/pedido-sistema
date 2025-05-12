package com.pedido.sistema.servicesTest;

import com.pedido.sistema.entities.Pedido;
import com.pedido.sistema.repositories.PedidoRepository;
import com.pedido.sistema.services.PedidoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private AutoCloseable openMocks;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        pedido = Pedido.builder()
                .id(UUID.randomUUID())
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
    void testEncontrarPedido() {
        // Arrange
        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.of(pedido));

        // Act
        Optional<Pedido> resultado = pedidoService.encontrarPedido(pedido.getId());

        // Assert
        assertNotNull(resultado);
        assertEquals(pedido.getId(), resultado.get().getId());
        verify(pedidoRepository, times(1)).findById(pedido.getId());
    }

    @Test
    void testEncontrarTodosPedido() {
        // Arrange
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        // Act
        List<Pedido> resultado = pedidoService.encontrarPedidos();

        // Assert
        assertNotNull(resultado);
        assertEquals(pedido.getId(), resultado.getFirst().getId());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void testCadastrarPedido() {
        // Arrange
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Act
        Pedido resultado = pedidoService.cadastrarPedido(pedido);

        // Assert
        assertNotNull(resultado);
        assertEquals(pedido.getId(), resultado.getId());
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    void testAtualizarPedido_Success() {
        // Arrange
        when(pedidoRepository.existsById(pedido.getId())).thenReturn(true);
        when(pedidoRepository.getReferenceById(pedido.getId())).thenReturn(pedido);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Act
        Pedido resultado = pedidoService.alterarPedido(pedido);

        // Assert
        assertNotNull(resultado);
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    void testAtualizarPedido_Null() {
        // Arrange
        when(pedidoRepository.existsById(pedido.getId())).thenReturn(false);

        // Act
        Pedido resultado = pedidoService.alterarPedido(pedido);

        // Assert
        assertNull(resultado);
        verify(pedidoRepository, times(0)).save(pedido);
    }

    @Test
    void testDeletarPedido() {
        // Arrange & Act
        pedidoService.deletarPedido(pedido.getId());

        // Assert
        verify(pedidoRepository, times(1)).deleteById(pedido.getId());
    }

}
