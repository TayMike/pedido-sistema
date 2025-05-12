package com.pedido.sistema.controller;

import com.pedido.sistema.services.PedidoService;
import com.pedido.sistema.entities.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(pedidoCadastrado.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(pedidoCadastrado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<Pedido> alterarPedido(@RequestBody Pedido pedido) {
        Optional<Pedido> pedidoVerificado = pedidoService.encontrarPedido(pedido.getId());
        if (pedidoVerificado.isPresent()) {
            Pedido pedidoAlterado = pedidoService.alterarPedido(pedido);
            return ResponseEntity.ok(pedidoAlterado);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pedido> deletarPedido(@PathVariable UUID id) {
        Optional<Pedido> pedido = pedidoService.encontrarPedido(id);
        if (pedido.isPresent()) {
            pedidoService.deletarPedido(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
