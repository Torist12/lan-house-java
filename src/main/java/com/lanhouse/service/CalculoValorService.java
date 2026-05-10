package com.lanhouse.service;

import com.lanhouse.model.Locacao;
import com.lanhouse.model.Computador;

public class CalculoValorService {
    
    /**
     * Calcula o valor total de uma locação baseado no tempo e tier do computador
     * 
     * @param locacao A locação finalizada com tempo registrado
     * @param computador O computador usado na locação
     * @return Valor total a pagar
     */
    public double calcularValor(Locacao locacao, Computador computador) {
        if (locacao.getFim() == null) {
            throw new IllegalArgumentException("Locação não foi finalizada ainda");
        }
        
        double tempoHoras = locacao.getTempoHoras();
        double precoHora = computador.getPrecoHora();
        
        // Aplicar taxa adicional se for tier GAMER
        double taxa = getTaxaTier(computador.getTier());
        
        return Math.round((tempoHoras * precoHora * taxa) * 100.0) / 100.0;
    }
    
    /**
     * Calcula o tempo estimado de uma sessão e retorna o valor a pagar
     * 
     * @param tempoMinutos Tempo total da sessão em minutos
     * @param precoHora Preço por hora do computador
     * @param tier Tier do computador
     * @return Valor a pagar
     */
    public double calcularValorEstimado(long tempoMinutos, double precoHora, String tier) {
        double tempoHoras = tempoMinutos / 60.0;
        double taxa = getTaxaTier(tier);
        
        return Math.round((tempoHoras * precoHora * taxa) * 100.0) / 100.0;
    }
    
    /**
     * Retorna a taxa multiplicadora baseada no tier do computador
     * 
     * @param tier BASICO, INTERMEDIARIO ou GAMER
     * @return Taxa multiplicadora (1.0, 1.2 ou 1.5)
     */
    private double getTaxaTier(String tier) {
        return switch (tier) {
            case "BASICO" -> 1.0;
            case "INTERMEDIARIO" -> 1.2;
            case "GAMER" -> 1.5;
            default -> 1.0;
        };
    }
    
    /**
     * Retorna uma descrição do preço para o usuário
     */
    public String descricaoPreco(Computador computador) {
        return String.format("Computador %d (%s): R$ %.2f/hora",
            computador.getNumero(),
            computador.getTier(),
            computador.getPrecoHora());
    }
}
