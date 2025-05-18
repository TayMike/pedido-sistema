package com.pagamento.sistema.servicesTest;

import com.pagamento.sistema.entities.Pagamento;
import com.pagamento.sistema.repositories.PagamentoRepository;
import com.pagamento.sistema.services.PagamentoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    private String cpf = "11111111111";
    private AutoCloseable openMocks;
    private Pagamento pagamento;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

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
    void testEncontrarPagamento() {
        // Arrange
        when(pagamentoRepository.findById(pagamento.getCpf())).thenReturn(Optional.of(pagamento));

        // Act
        Optional<Pagamento> resultado = pagamentoService.encontrarPagamento(pagamento.getCpf());

        // Assert
        assertNotNull(resultado);
        assertEquals(pagamento.getCpf(), resultado.get().getCpf());
        verify(pagamentoRepository, times(1)).findById(pagamento.getCpf());
    }

    @Test
    void testEncontrarTodosPagamento() {
        // Arrange
        when(pagamentoRepository.findAll()).thenReturn(List.of(pagamento));

        // Act
        List<Pagamento> resultado = pagamentoService.encontrarPagamentos();

        // Assert
        assertNotNull(resultado);
        assertEquals(pagamento.getCpf(), resultado.getFirst().getCpf());
        verify(pagamentoRepository, times(1)).findAll();
    }

    @Test
    void testCadastrarPagamento() {
        // Arrange
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        // Act
        Pagamento resultado = pagamentoService.cadastrarPagamento(pagamento);

        // Assert
        assertNotNull(resultado);
        assertEquals(pagamento.getCpf(), resultado.getCpf());
        verify(pagamentoRepository, times(1)).save(pagamento);
    }

    @Test
    void testAtualizarPagamento_Success() {
        // Arrange
        when(pagamentoRepository.existsById(pagamento.getCpf())).thenReturn(true);
        when(pagamentoRepository.getReferenceById(pagamento.getCpf())).thenReturn(pagamento);
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        // Act
        Pagamento resultado = pagamentoService.alterarPagamento(pagamento);

        // Assert
        assertNotNull(resultado);
        verify(pagamentoRepository, times(1)).save(pagamento);
    }

    @Test
    void testAtualizarPagamento_Null() {
        // Arrange
        when(pagamentoRepository.existsById(pagamento.getCpf())).thenReturn(false);

        // Act
        Pagamento resultado = pagamentoService.alterarPagamento(pagamento);

        // Assert
        assertNull(resultado);
        verify(pagamentoRepository, times(0)).save(pagamento);
    }

    @Test
    void testDeletarPagamento() {
        // Arrange & Act
        pagamentoService.deletarPagamento(pagamento.getCpf());

        // Assert
        verify(pagamentoRepository, times(1)).deleteById(pagamento.getCpf());
    }

}
