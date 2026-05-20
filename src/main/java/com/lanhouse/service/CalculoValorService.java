package com.lanhouse.service;

import com.lanhouse.model.Locacao;
import com.lanhouse.model.Computador;
import com.lanhouse.model.TierComputador;
/**
 * Service responsible for calculating rental values based on computer tier and duration.
 */

public class CalculoValorService {
    
    /**
     * Default constructor for CalculoValorService.
     */
    public CalculoValorService() {
    }
    /**
     * Calculates the total value of a rental based on the time and computer tier.
     *
     * @param locacao The finalized rental with recorded time.
     * @param computador The computer used in the rental.
     * @return Total value to pay
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
     * Calculates the estimated value of a session.
     *
     * @param tempoMinutos Total session time in minutes.
     * @param precoHora Hourly price of the computer.
     * @param tier Computer tier.
     * @return Value to pay.
     */
    public double calcularValorEstimado(long tempoMinutos, double precoHora, TierComputador tier) {
        double tempoHoras = tempoMinutos / 60.0;
        double taxa = getTaxaTier(tier);
        
        return Math.round((tempoHoras * precoHora * taxa) * 100.0) / 100.0;
    }
    
    /**
     * Returns the multiplier rate based on the computer's tier.
     *
     * @param tier BASIC, INTERMEDIATE, or GAMER.
     * @return Multiplier rate (1.0, 1.2, or 1.5).
     */
    private double getTaxaTier(TierComputador tier) {
        return switch (tier) {
            case BASICO -> 1.0;
            case INTERMEDIARIO -> 1.2;
            case GAMER -> 1.5;
        };
    }
    
    /**
     * Returns a price description for the user.
     *
     * @param computador The computer for which to generate the description.
     * @return A formatted string with number, tier, and price.
     */
    public String descricaoPreco(Computador computador) {
        return String.format("Computador %d (%s): R$ %.2f/hora",
            computador.getNumero(),
            computador.getTier(),
            computador.getPrecoHora());
    }
}
