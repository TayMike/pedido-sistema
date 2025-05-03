package com.pedido.sistema.controller;

import com.pedido.sistema.services.PedidoService;
import com.pedido.sistema.entities.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    PedidoService pedidoService;

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> encontrarPedido(@PathVariable UUID id) {
        Optional<Pedido> pedido = pedidoService.encontrarPedido(id);
        return pedido.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Pedido>> encontrarPedidos() {
        List<Pedido> pedidos = pedidoService.encontrarPedidos();
        if (pedidos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Pedido> cadastrarPedido(@RequestBody Pedido pedido) {
        try {
            Pedido pedidoCadastrado = pedidoService.cadastrarPedido(pedido);
            return ResponseEntity.ok(pedidoCadastrado);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<Pedido> alterarPedido(@RequestBody Pedido pedido) {
        try {
            Pedido pedidoAlterado = pedidoService.alterarPedido(pedido);
            return ResponseEntity.ok(pedidoAlterado);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/deletar/{cpf}")
    public ResponseEntity<Pedido> deletarPedido(@PathVariable UUID pedido) {
        try {
            pedidoService.deletarPedido(pedido);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
