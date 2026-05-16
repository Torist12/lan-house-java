package com.lanhouse.repository;

import java.util.List;
import com.lanhouse.model.*;

public interface IClienteRepositorio {
    int salvar(Cliente cliente);
    List<Cliente> listarTodos();
    Cliente buscarPorId(int id);
    Cliente buscarPorDocumento(String documento);
    boolean atualizar(Cliente cliente);
    boolean deletar(int id);
}
