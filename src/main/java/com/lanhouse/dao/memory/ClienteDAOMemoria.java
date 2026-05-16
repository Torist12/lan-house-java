package com.lanhouse.dao.memory;

import com.lanhouse.model.Cliente;
import java.util.*;

public class ClienteDAOMemoria {
    private Map<Integer, Cliente> clientes = new HashMap<>();
    private int idCounter = 1;

    public int salvar(Cliente cliente) {
        cliente = new Cliente(idCounter, cliente.getNome(), cliente.getDocumento(), cliente.getTelefone());
        clientes.put(idCounter, cliente);
        return idCounter++;
    }

    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes.values());
    }

    public Cliente buscarPorId(int id) {
        return clientes.get(id);
    }

    public Cliente buscarPorDocumento(String documento) {
        for (Cliente c : clientes.values()) {
            if (c.getDocumento().equals(documento)) {
                return c;
            }
        }
        return null;
    }

    public boolean atualizar(Cliente cliente) {
        if (clientes.containsKey(cliente.getId())) {
            clientes.put(cliente.getId(), cliente);
            return true;
        }
        return false;
    }

    public boolean deletar(int id) {
        return clientes.remove(id) != null;
    }
}
