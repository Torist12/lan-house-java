package com.lanhouse.dao.memory;

import com.lanhouse.model.Computador;
import java.util.*;

public class ComputadorDAOMemoria {
    private Map<Integer, Computador> computadores = new HashMap<>();
    private int idCounter = 1;

    public ComputadorDAOMemoria() {
        // Inicializar com 5 computadores padrão
        salvarPadrao(1, "BASICO", 10.00);
        salvarPadrao(2, "BASICO", 10.00);
        salvarPadrao(3, "INTERMEDIARIO", 15.00);
        salvarPadrao(4, "INTERMEDIARIO", 15.00);
        salvarPadrao(5, "GAMER", 25.00);
    }

    private void salvarPadrao(int numero, String tier, double precoHora) {
        Computador pc = new Computador(idCounter++, numero, "livre", tier, precoHora);
        computadores.put(pc.getId(), pc);
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
            if ("livre".equals(pc.getStatus())) {
                livres.add(pc);
            }
        }
        return livres;
    }

    public boolean atualizarStatus(int id, String novoStatus) {
        Computador pc = computadores.get(id);
        if (pc != null) {
            pc.setStatus(novoStatus);
            return true;
        }
        return false;
    }
}
