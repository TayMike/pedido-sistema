package com.pedido.sistema.services;

import com.pedido.sistema.entities.Pedido;
import com.pedido.sistema.repositories.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    public Optional<Pedido> encontrarPedido(UUID pedido) {
        return pedidoRepository.findById(pedido);
    }

    public List<Pedido> encontrarPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido cadastrarPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public Pedido alterarPedido(Pedido pedidoNovo) {
        if(pedidoRepository.existsById(pedidoNovo.getId())) {
            Pedido pedidoVelho = pedidoRepository.getReferenceById(pedidoNovo.getId());
            pedidoVelho.setCartao(pedidoNovo.getCartao());
            pedidoVelho.setSku(pedidoNovo.getSku());
            pedidoVelho.setQuantidade(pedidoNovo.getQuantidade());
            return pedidoRepository.save(pedidoVelho);
        }
        return null;
    }

    public void deletarPedido(UUID pedido) {
        pedidoRepository.deleteById(pedido);
    }

}
