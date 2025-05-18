package com.pagamento.sistema.controller;

import com.pagamento.sistema.entities.Pagamento;
import com.pagamento.sistema.services.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    PagamentoService pagamentoService;

    @GetMapping("/{cpf}")
    public ResponseEntity<Pagamento> encontrarPagamento(@PathVariable String cpf) {
        Optional<Pagamento> pagamento = pagamentoService.encontrarPagamento(cpf);
        if (pagamento.isPresent()) {
            return ResponseEntity.ok(pagamento.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Pagamento>> encontrarPagamentos() {
        List<Pagamento> Pagamentos = pagamentoService.encontrarPagamentos();
        if (Pagamentos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(Pagamentos);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Pagamento> cadastrarPagamento(@RequestBody Pagamento pagamento) {
        try {
            Pagamento pagamentoCadastrado = pagamentoService.cadastrarPagamento(pagamento);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{cpf}")
                    .buildAndExpand(pagamentoCadastrado.getCpf())
                    .toUri();
            return ResponseEntity.created(uri).body(pagamentoCadastrado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<Pagamento> alterarPagamento(@RequestBody Pagamento pagamento) {
        Optional<Pagamento> pagamentoVerificado = pagamentoService.encontrarPagamento(pagamento.getCpf());
        if (pagamentoVerificado.isPresent()) {
            Pagamento pagamentoAlterado = pagamentoService.alterarPagamento(pagamento);
            return ResponseEntity.ok(pagamentoAlterado);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Pagamento> deletarPagamento(@PathVariable String cpf) {
        Optional<Pagamento> pagamento = pagamentoService.encontrarPagamento(cpf);
        if (pagamento.isPresent()) {
            pagamentoService.deletarPagamento(cpf);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
