package com.pagamento.sistema.services;

import com.pagamento.sistema.entities.Pagamento;
import com.pagamento.sistema.repositories.PagamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PagamentoService {

    @Autowired
    PagamentoRepository pagamentoRepository;

    public Optional<Pagamento> encontrarPagamento(String cpf) {
        return pagamentoRepository.findById(cpf);
    }

    public List<Pagamento> encontrarPagamentos() {
        return pagamentoRepository.findAll();
    }

    public Pagamento cadastrarPagamento(Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento alterarPagamento(Pagamento pagamentoNovo) {
        if(pagamentoRepository.existsById(pagamentoNovo.getCpf())) {
            Pagamento pagamentoVelho = pagamentoRepository.getReferenceById(pagamentoNovo.getCpf());
            pagamentoVelho.setNumeroCartao(pagamentoNovo.getNumeroCartao());
            pagamentoVelho.setCodigoVerificador(pagamentoNovo.getCodigoVerificador());
            pagamentoVelho.setDataValidadeCartao(pagamentoNovo.getDataValidadeCartao());
            return pagamentoRepository.save(pagamentoVelho);
        }
        return null;
    }

    public void deletarPagamento(String cpf) {
        pagamentoRepository.deleteById(cpf);
    }

}
