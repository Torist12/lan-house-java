package com.lanhouse.dao.memory;

import com.lanhouse.model.Locacao;
import java.time.LocalDateTime;
import java.util.*;

public class LocacaoDAOMemoria {
    private Map<Integer, Locacao> locacoes = new HashMap<>();
    private int idCounter = 1;

    public int iniciarLocacao(Locacao locacao) {
        locacao = new Locacao(idCounter, locacao.getClienteId(), locacao.getComputadorId(), 
                            locacao.getInicio(), locacao.getFim(), locacao.getValorTotal(), locacao.getStatus());
        locacoes.put(idCounter, locacao);
        return idCounter++;
    }

    public boolean finalizarLocacao(int id, LocalDateTime fim, double valorTotal) {
        Locacao locacao = locacoes.get(id);
        if (locacao != null) {
            locacao.setFim(fim);
            locacao.setValorTotal(valorTotal);
            locacao.setStatus("finalizada");
            return true;
        }
        return false;
    }

    public List<Locacao> listarAtivas() {
        List<Locacao> ativas = new ArrayList<>();
        for (Locacao loc : locacoes.values()) {
            if ("ativa".equals(loc.getStatus())) {
                ativas.add(loc);
            }
        }
        return ativas;
    }

    public List<Locacao> listarPorCliente(int clienteId) {
        List<Locacao> result = new ArrayList<>();
        for (Locacao loc : locacoes.values()) {
            if (loc.getClienteId() == clienteId) {
                result.add(loc);
            }
        }
        return result;
    }

    public List<Locacao> listarTodas() {
        return new ArrayList<>(locacoes.values());
    }

    public Locacao buscarPorId(int id) {
        return locacoes.get(id);
    }
}
