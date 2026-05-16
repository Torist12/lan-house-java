package com.lanhouse.repository.memory;

import com.lanhouse.model.Locacao;
import com.lanhouse.model.StatusLocacao;
import com.lanhouse.repository.ILocacaoRepositorio;
import java.time.LocalDateTime;
import java.util.*;

public class LocacaoDAOMemoria implements ILocacaoRepositorio {
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
            locacao.setStatus(StatusLocacao.FINALIZADA);
            return true;
        }
        return false;
    }

    public List<Locacao> listarAtivas() {
        List<Locacao> ativas = new ArrayList<>();
        for (Locacao loc : locacoes.values()) {
            if (loc.getStatus() == StatusLocacao.ATIVA) {
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
