package com.lanhouse.repository;

import com.lanhouse.model.Computador;
import com.lanhouse.model.StatusComputador;
import java.util.List;

public interface IComputadorRepositorio {
    int salvar(Computador computador);
    List<Computador> listarTodos();
    Computador buscarPorId(int id);
    Computador buscarPorNumero(int numero);
    List<Computador> listarLivres();
    boolean atualizar(Computador computador);
    boolean atualizarStatus(int id, StatusComputador novoStatus);
    boolean deletar(int id);
}
