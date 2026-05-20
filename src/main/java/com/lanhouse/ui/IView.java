package com.lanhouse.ui;

import com.lanhouse.model.*;
import java.util.List;

/**
 * Interface that defines the contract for View implementations.
 * Any implementation (CLI, Graphical, Web) must implement these methods.
 */
public interface IView {
    
    // ===== DISPLAY METHODS =====
    
    /**
     * Displays the main system menu.
     */
    void exibirMenuPrincipal();
    
    /**
     * Displays an informative or success message.
     * @param mensagem The message text.
     */
    void exibirMensagem(String mensagem);
    
    /**
     * Displays an error message.
     * @param mensagem The error description.
     */
    void exibirErro(String mensagem);
    
    /**
     * Clears the current view/screen.
     */
    void limparTela();
    
    // ===== DATA INPUT =====
    
    /**
     * Reads a numeric option for a menu.
     * @return the selected option.
     */
    int lerOpcaoMenu();
    
    /**
     * Prompts for and reads an integer.
     */
    int lerInteiro(String prompt);
    
    /**
     * Prompts for and reads a string.
     */
    String lerTexto(String prompt);
    
    /**
     * Prompts for and reads a decimal number.
     */
    double lerDouble(String prompt);
    
    // ===== LIST DISPLAY =====
    
    /**
     * Renders a list of computers.
     */
    void exibirComputadores(List<Computador> computadores);
    
    /**
     * Renders a list of clients.
     */
    void exibirClientes(List<Cliente> clientes);
    
    /**
     * Renders a list of rentals.
     */
    void exibirLocacoes(List<Locacao> locacoes);
    
    /**
     * Renders details for a single computer.
     */
    void exibirComputador(Computador computador);
    
    /**
     * Renders details for a single client.
     */
    void exibirCliente(Cliente cliente);
    
    /**
     * Renders details for a single rental.
     */
    void exibirLocacao(Locacao locacao);
    
    // ===== USE CASES (SPECIFIC MENUS) =====
    
    /**
     * Displays the client management submenu.
     */
    void exibirMenuClientes();
    
    /**
     * Displays the computer management submenu.
     */
    void exibirMenuComputadores();
    
    /**
     * Displays the rental management submenu.
     */
    void exibirMenuLocacoes();
    
    /**
     * Displays the reports submenu.
     */
    void exibirMenuRelatorios();
    
    // ===== CONFIRMATIONS =====
    
    /**
     * Asks for a binary confirmation from the user.
     * @param mensagem The question.
     * @return true if confirmed.
     */
    boolean confirmar(String mensagem);

    // ===== LIFE CYCLE =====
    /**
     * Performs cleanup and closes the view resources.
     */
    void encerrar();
}
