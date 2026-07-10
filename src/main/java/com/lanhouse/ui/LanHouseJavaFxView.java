package com.lanhouse.ui;

import com.lanhouse.model.Cliente;
import com.lanhouse.model.Computador;
import com.lanhouse.model.Locacao;
import com.lanhouse.service.AppService;
import com.lanhouse.service.AuthService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class LanHouseJavaFxView {
    private final Stage stage;
    private final AppService service;
    private final AuthService authService;
    private final BorderPane root = new BorderPane();
    private final VBox contentArea = new VBox(16);
    private final Label statusLabel = new Label("Bem-vindo ao sistema");

    public LanHouseJavaFxView(Stage stage, AppService service, AuthService authService) {
        this.stage = stage;
        this.service = service;
        this.authService = authService;
        montarTelaLogin();
    }

    private void montarTelaLogin() {
        VBox loginBox = new VBox(12);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(24));
        loginBox.setStyle("-fx-background-color: #0f172a; -fx-background-radius: 16px;");

        Label title = new Label("LAN HOUSE SYSTEM");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label("Acesse com o usuário administrador");
        subtitle.setStyle("-fx-text-fill: #cbd5e1;");

        TextField usuarioField = new TextField();
        usuarioField.setPromptText("Usuário");

        PasswordField senhaField = new PasswordField();
        senhaField.setPromptText("Senha");

        Label mensagem = new Label();
        mensagem.setStyle("-fx-text-fill: #fda4af;");

        Button entrarButton = new Button("Entrar");
        entrarButton.setStyle("-fx-background-color: #38bdf8; -fx-text-fill: white; -fx-font-weight: bold;");
        entrarButton.setOnAction(e -> {
            if (authService.autenticar(usuarioField.getText(), senhaField.getText())) {
                montarTelaPrincipal();
            } else {
                mensagem.setText("Usuário ou senha inválidos. Use adm / adm.");
            }
        });

        loginBox.getChildren().addAll(title, subtitle, usuarioField, senhaField, entrarButton, mensagem);

        Scene scene = new Scene(loginBox, 420, 320);
        stage.setTitle("Lan House - Login");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void montarTelaPrincipal() {
        root.setStyle("-fx-background-color: #f8fafc;");

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(16));
        topBar.setSpacing(12);
        topBar.setStyle("-fx-background-color: #0f172a;");

        Label title = new Label("Painel da Lan House");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        statusLabel.setStyle("-fx-text-fill: #e2e8f0;");
        HBox.setHgrow(statusLabel, javafx.scene.layout.Priority.ALWAYS);

        Button logoutButton = new Button("Sair");
        logoutButton.setOnAction(e -> montarTelaLogin());
        logoutButton.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white;");

        topBar.getChildren().addAll(title, statusLabel, logoutButton);
        root.setTop(topBar);

        VBox menu = new VBox(10);
        menu.setPadding(new Insets(16));
        menu.setPrefWidth(180);
        menu.setStyle("-fx-background-color: #111827;");

        Button clientesButton = criarBotaoMenu("Clientes", () -> carregarPainelClientes());
        Button computadoresButton = criarBotaoMenu("Computadores", () -> carregarPainelComputadores());
        Button locacoesButton = criarBotaoMenu("Locações", () -> carregarPainelLocacoes());
        Button relatoriosButton = criarBotaoMenu("Relatórios", () -> carregarPainelRelatorios());

        menu.getChildren().addAll(clientesButton, computadoresButton, locacoesButton, relatoriosButton);
        root.setLeft(menu);

        contentArea.setPadding(new Insets(16));
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 1100, 740);
        stage.setTitle("Lan House System");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        carregarPainelClientes();
    }

    private Button criarBotaoMenu(String texto, Runnable acao) {
        Button button = new Button(texto);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;");
        button.setOnAction(e -> acao.run());
        return button;
    }

    private void carregarPainelClientes() {
        statusLabel.setText("Gerenciando clientes");
        contentArea.getChildren().setAll(criarPainelClientes());
    }

    private void carregarPainelComputadores() {
        statusLabel.setText("Consultando computadores");
        contentArea.getChildren().setAll(criarPainelComputadores());
    }

    private void carregarPainelLocacoes() {
        statusLabel.setText("Controle de locações");
        contentArea.getChildren().setAll(criarPainelLocacoes());
    }

    private void carregarPainelRelatorios() {
        statusLabel.setText("Relatórios do dia");
        contentArea.getChildren().setAll(criarPainelRelatorios());
    }

    private Node criarPainelClientes() {
        VBox painel = new VBox(12);

        Label titulo = new Label("Clientes");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);

        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome");
        TextField documentoField = new TextField();
        documentoField.setPromptText("CPF/CNPJ");
        TextField telefoneField = new TextField();
        telefoneField.setPromptText("Telefone");

        Button salvarButton = new Button("Salvar cliente");
        salvarButton.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white;");
        salvarButton.setOnAction(e -> {
            String nome = nomeField.getText().trim();
            String documento = documentoField.getText().trim();
            String telefone = telefoneField.getText().trim();
            if (nome.isEmpty() || documento.isEmpty()) {
                mostrarAlerta("Preencha nome e documento.");
                return;
            }
            int id = service.criarCliente(nome, documento, telefone);
            if (id > 0) {
                limparCampos(nomeField, documentoField, telefoneField);
                carregarPainelClientes();
                mostrarMensagem("Cliente cadastrado com sucesso.");
            } else {
                mostrarAlerta("Não foi possível salvar o cliente.");
            }
        });

        form.add(new Label("Nome:"), 0, 0);
        form.add(nomeField, 1, 0);
        form.add(new Label("Documento:"), 0, 1);
        form.add(documentoField, 1, 1);
        form.add(new Label("Telefone:"), 0, 2);
        form.add(telefoneField, 1, 2);
        form.add(salvarButton, 1, 3);

        TableView<Cliente> table = new TableView<>();
        table.setPrefHeight(280);

        TableColumn<Cliente, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().getId()).asObject());

        TableColumn<Cliente, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nome"));

        TableColumn<Cliente, String> documentoCol = new TableColumn<>("Documento");
        documentoCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("documento"));

        TableColumn<Cliente, String> telefoneCol = new TableColumn<>("Telefone");
        telefoneCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("telefone"));

        table.getColumns().setAll(idCol, nomeCol, documentoCol, telefoneCol);
        table.setItems(obterClientes());

        HBox botoes = new HBox(10);
        Button atualizarButton = new Button("Atualizar selecionado");
        atualizarButton.setOnAction(e -> {
            Cliente selecionado = table.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                mostrarAlerta("Selecione um cliente.");
                return;
            }
            boolean ok = service.atualizarCliente(selecionado.getId(), nomeField.getText().trim().isEmpty() ? selecionado.getNome() : nomeField.getText().trim(), telefoneField.getText().trim().isEmpty() ? selecionado.getTelefone() : telefoneField.getText().trim());
            if (ok) {
                limparCampos(nomeField, documentoField, telefoneField);
                carregarPainelClientes();
                mostrarMensagem("Cliente atualizado com sucesso.");
            } else {
                mostrarAlerta("Não foi possível atualizar o cliente.");
            }
        });

        Button excluirButton = new Button("Excluir selecionado");
        excluirButton.setOnAction(e -> {
            Cliente selecionado = table.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                mostrarAlerta("Selecione um cliente.");
                return;
            }
            boolean ok = service.deletarCliente(selecionado.getId());
            if (ok) {
                carregarPainelClientes();
                mostrarMensagem("Cliente removido com sucesso.");
            } else {
                mostrarAlerta("Não foi possível remover o cliente.");
            }
        });

        botoes.getChildren().addAll(atualizarButton, excluirButton);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nomeField.setText(newSelection.getNome());
                documentoField.setText(newSelection.getDocumento());
                telefoneField.setText(newSelection.getTelefone());
            }
        });

        painel.getChildren().addAll(titulo, form, botoes, table);
        return painel;
    }

    private Node criarPainelComputadores() {
        VBox painel = new VBox(12);
        Label titulo = new Label("Computadores");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<Computador> table = new TableView<>();
        table.setPrefHeight(320);

        TableColumn<Computador, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().getId()).asObject());
        TableColumn<Computador, Integer> numeroCol = new TableColumn<>("Número");
        numeroCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().getNumero()).asObject());
        TableColumn<Computador, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("status"));
        TableColumn<Computador, String> tierCol = new TableColumn<>("Tier");
        tierCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("tier"));
        TableColumn<Computador, Double> precoCol = new TableColumn<>("Preço/h");
        precoCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleDoubleProperty(cd.getValue().getPrecoHora()).asObject());

        table.getColumns().setAll(idCol, numeroCol, statusCol, tierCol, precoCol);
        table.setItems(obterComputadores());

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        TextField computadorIdField = new TextField();
        computadorIdField.setPromptText("ID do computador");
        ComboBox<String> statusBox = new ComboBox<>(FXCollections.observableArrayList("livre", "ocupado"));
        statusBox.setValue("livre");
        Button atualizarStatusButton = new Button("Atualizar status");
        atualizarStatusButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(computadorIdField.getText().trim());
                boolean ok = service.atualizarStatusComputador(id, statusBox.getValue());
                if (ok) {
                    computadorIdField.clear();
                    carregarPainelComputadores();
                    mostrarMensagem("Status atualizado com sucesso.");
                } else {
                    mostrarAlerta("Não foi possível atualizar o status.");
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Informe um ID válido.");
            }
        });

        form.add(new Label("ID:"), 0, 0);
        form.add(computadorIdField, 1, 0);
        form.add(new Label("Status:"), 0, 1);
        form.add(statusBox, 1, 1);
        form.add(atualizarStatusButton, 1, 2);

        painel.getChildren().addAll(titulo, form, table);
        return painel;
    }

    private Node criarPainelLocacoes() {
        VBox painel = new VBox(12);
        Label titulo = new Label("Locações");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        TextField clienteIdField = new TextField();
        clienteIdField.setPromptText("ID do cliente");
        TextField computadorIdField = new TextField();
        computadorIdField.setPromptText("ID do computador");
        Button iniciarButton = new Button("Iniciar locação");
        iniciarButton.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white;");
        iniciarButton.setOnAction(e -> {
            try {
                int clienteId = Integer.parseInt(clienteIdField.getText().trim());
                int computadorId = Integer.parseInt(computadorIdField.getText().trim());
                int locacaoId = service.iniciarLocacao(clienteId, computadorId);
                if (locacaoId > 0) {
                    clienteIdField.clear();
                    computadorIdField.clear();
                    carregarPainelLocacoes();
                    mostrarMensagem("Locação iniciada com sucesso. ID: " + locacaoId);
                } else {
                    mostrarAlerta("Não foi possível iniciar a locação.");
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Informe IDs válidos.");
            } catch (IllegalArgumentException ex) {
                mostrarAlerta(ex.getMessage());
            }
        });

        form.add(new Label("Cliente ID:"), 0, 0);
        form.add(clienteIdField, 1, 0);
        form.add(new Label("Computador ID:"), 0, 1);
        form.add(computadorIdField, 1, 1);
        form.add(iniciarButton, 1, 2);

        TableView<Locacao> table = new TableView<>();
        table.setPrefHeight(260);
        TableColumn<Locacao, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().getId()).asObject());
        TableColumn<Locacao, Integer> clienteCol = new TableColumn<>("Cliente");
        clienteCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().getClienteId()).asObject());
        TableColumn<Locacao, Integer> pcCol = new TableColumn<>("PC");
        pcCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().getComputadorId()).asObject());
        TableColumn<Locacao, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("status"));
        table.getColumns().setAll(idCol, clienteCol, pcCol, statusCol);
        table.setItems(obterLocacoesAtivas());

        Button finalizarButton = new Button("Finalizar locação selecionada");
        finalizarButton.setOnAction(e -> {
            Locacao selecionada = table.getSelectionModel().getSelectedItem();
            if (selecionada == null) {
                mostrarAlerta("Selecione uma locação ativa.");
                return;
            }
            try {
                double valor = service.finalizarLocacao(selecionada.getId());
                carregarPainelLocacoes();
                mostrarMensagem("Locação finalizada com sucesso. Valor: R$ " + String.format("%.2f", valor));
            } catch (IllegalArgumentException ex) {
                mostrarAlerta(ex.getMessage());
            }
        });

        painel.getChildren().addAll(titulo, form, finalizarButton, table);
        return painel;
    }

    private Node criarPainelRelatorios() {
        VBox painel = new VBox(12);
        Label titulo = new Label("Relatórios");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox cards = new HBox(12);
        cards.getChildren().addAll(
                criarCard("Clientes", String.valueOf(service.listarClientes().size())),
                criarCard("Computadores", String.valueOf(service.listarComputadores().size())),
                criarCard("Locações ativas", String.valueOf(service.listarLocacoesAtivas().size()))
        );

        TableView<Locacao> table = new TableView<>();
        table.setPrefHeight(320);
        TableColumn<Locacao, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().getId()).asObject());
        TableColumn<Locacao, Integer> clienteCol = new TableColumn<>("Cliente");
        clienteCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().getClienteId()).asObject());
        TableColumn<Locacao, Integer> pcCol = new TableColumn<>("PC");
        pcCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleIntegerProperty(cd.getValue().getComputadorId()).asObject());
        TableColumn<Locacao, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("status"));
        TableColumn<Locacao, Double> valorCol = new TableColumn<>("Valor");
        valorCol.setCellValueFactory(cd -> new javafx.beans.property.SimpleDoubleProperty(cd.getValue().getValorTotal()).asObject());
        table.getColumns().setAll(idCol, clienteCol, pcCol, statusCol, valorCol);
        table.setItems(obterTodasLocacoes());

        painel.getChildren().addAll(titulo, cards, table);
        return painel;
    }

    private Node criarCard(String titulo, String valor) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(16));
        card.setPrefWidth(180);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);");
        Label tituloLabel = new Label(titulo);
        tituloLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b;");
        Label valorLabel = new Label(valor);
        valorLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        card.getChildren().addAll(tituloLabel, valorLabel);
        return card;
    }

    private ObservableList<Cliente> obterClientes() {
        return FXCollections.observableArrayList(service.listarClientes());
    }

    private ObservableList<Computador> obterComputadores() {
        return FXCollections.observableArrayList(service.listarComputadores());
    }

    private ObservableList<Locacao> obterLocacoesAtivas() {
        return FXCollections.observableArrayList(service.listarLocacoesAtivas());
    }

    private ObservableList<Locacao> obterTodasLocacoes() {
        return FXCollections.observableArrayList(service.listarTodasLocacoes());
    }

    private void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void limparCampos(TextField... campos) {
        for (TextField campo : campos) {
            campo.clear();
        }
    }
}
