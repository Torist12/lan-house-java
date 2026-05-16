package com.lanhouse.repository.memory;

import com.lanhouse.model.Computador;
import com.lanhouse.model.StatusComputador;
import com.lanhouse.model.TierComputador;
import com.lanhouse.repository.IComputadorRepositorio;
import java.util.*;

public class ComputadorDAOMemoria implements IComputadorRepositorio {
    private Map<Integer, Computador> computadores = new HashMap<>();
    private int idCounter = 1;

    public ComputadorDAOMemoria() {
        salvarPadrao(1, TierComputador.BASICO, 10.00);
        salvarPadrao(2, TierComputador.BASICO, 10.00);
        salvarPadrao(3, TierComputador.INTERMEDIARIO, 15.00);
        salvarPadrao(4, TierComputador.INTERMEDIARIO, 15.00);
        salvarPadrao(5, TierComputador.GAMER, 25.00);
    }

    private void salvarPadrao(int numero, TierComputador tier, double precoHora) {
        Computador pc = new Computador(idCounter++, numero, StatusComputador.LIVRE, tier, precoHora);
        computadores.put(pc.getId(), pc);
    }

    public int salvar(Computador computador) {
        Computador novo = new Computador(idCounter++, computador.getNumero(), computador.getStatus(), computador.getTier(), computador.getPrecoHora());
        computadores.put(novo.getId(), novo);
        return novo.getId();
    }

    public List<Computador> listarTodos() {
        return new ArrayList<>(computadores.values());
    }

    public Computador buscarPorId(int id) {
        return computadores.get(id);
    }

    public Computador buscarPorNumero(int numero) {
        for (Computador pc : computadores.values()) {
            if (pc.getNumero() == numero) {
                return pc;
            }
        }
        return null;
    }

    public List<Computador> listarLivres() {
        List<Computador> livres = new ArrayList<>();
        for (Computador pc : computadores.values()) {
            if (pc.getStatus() == StatusComputador.LIVRE) {
                livres.add(pc);
            }
        }
        return livres;
    }

    public boolean atualizar(Computador computador) {
        if (computadores.containsKey(computador.getId())) {
            computadores.put(computador.getId(), computador);
            return true;
        }
        return false;
    }

    public boolean atualizarStatus(int id, StatusComputador novoStatus) {
        Computador pc = computadores.get(id);
        if (pc != null) {
            pc.setStatus(novoStatus);
            return true;
        }
        return false;
    }

    public boolean deletar(int id) {
        return computadores.remove(id) != null;
    }
}
