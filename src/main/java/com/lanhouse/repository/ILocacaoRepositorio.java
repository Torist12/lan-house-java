package com.lanhouse.repository;

import com.lanhouse.model.Locacao;
import java.time.LocalDateTime;
import java.util.List;

public interface ILocacaoRepositorio {
    int iniciarLocacao(Locacao locacao);
    boolean finalizarLocacao(int id, LocalDateTime fim, double valorTotal);
    List<Locacao> listarAtivas();
    List<Locacao> listarPorCliente(int clienteId);
    List<Locacao> listarTodas();
    Locacao buscarPorId(int id);
}
