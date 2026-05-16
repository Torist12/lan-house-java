package com.lanhouse.ui;

import com.lanhouse.model.*;
import java.util.List;

/**
 * Interface que define o contrato para implementações de View.
 * Qualquer implementação (CLI, Gráfica, Web) deve implementar esses métodos.
 */
public interface IView {
    
    // ===== MÉTODOS DE EXIBIÇÃO =====
    
    void exibirMenuPrincipal();
    
    void exibirMensagem(String mensagem);
    
    void exibirErro(String mensagem);
    
    void limparTela();
    
    // ===== ENTRADA DE DADOS =====
    
    int lerOpcaoMenu();
    
    int lerInteiro(String prompt);
    
    String lerTexto(String prompt);
    
    double lerDouble(String prompt);
    
    // ===== EXIBIÇÃO DE LISTAS =====
    
    void exibirComputadores(List<Computador> computadores);
    
    void exibirClientes(List<Cliente> clientes);
    
    void exibirLocacoes(List<Locacao> locacoes);
    
    void exibirComputador(Computador computador);
    
    void exibirCliente(Cliente cliente);
    
    void exibirLocacao(Locacao locacao);
    
    // ===== CASOS DE USO (MENUS ESPECÍFICOS) =====
    
    void exibirMenuClientes();
    
    void exibirMenuComputadores();
    
    void exibirMenuLocacoes();
    
    void exibirMenuRelatorios();
    
    // ===== CONFIRMAÇÕES =====
    
    boolean confirmar(String mensagem);

    // ===== CICLO DE VIDA =====
    void encerrar();
}
