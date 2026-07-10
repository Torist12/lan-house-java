# Documentação de criação e evolução do Lan House Java

Este documento registra a evolução do projeto desde a sua implementação inicial até a versão atual 1.6, com foco no contexto de criação, nas principais mudanças e no funcionamento geral do sistema.

## 1. Visão geral do projeto

O Lan House Java é um sistema desktop para gestão de uma lan house, com funcionalidades para:

- cadastro de clientes;
- cadastro de computadores;
- controle de locações;
- gestão de funcionários;
- autenticação de usuários;
- painel administrativo para usuários com permissão de administrador.

A aplicação foi desenvolvida em Java, utilizando JavaFX para a interface gráfica e SQLite para o armazenamento local dos dados.

## 2. Estrutura inicial do projeto

No início, o sistema foi organizado em camadas simples para facilitar o desenvolvimento e a manutenção:

- Model: representa os dados principais, como clientes, computadores, funcionários e locações.
- DAO: responsável pela comunicação com o banco de dados SQLite.
- Service: concentra a lógica de negócio.
- UI: contém a interface gráfica com JavaFX.
- Main: ponto de entrada da aplicação.

Essa estrutura permitiu evoluir o sistema de forma mais organizada, sem precisar reestruturar tudo a cada nova funcionalidade.

## 3. Marcos principais da criação

### 3.1 Criação da base do sistema

A primeira versão do projeto foi estruturada com:

- interface inicial em JavaFX;
- conexão com SQLite;
- cadastro básico de entidades;
- fluxo de autenticação simples.

O objetivo inicial era oferecer um sistema funcional para controlar os principais processos de uma lan house.

### 3.2 Implementação da autenticação

Uma etapa importante foi o desenvolvimento do módulo de autenticação, permitindo que o usuário faça login com usuário e senha. Com isso, o sistema passou a ter um controle básico de acesso.

### 3.3 Implementação do cadastro e gerenciamento de dados

Foram adicionadas as operações básicas de CRUD para:

- clientes;
- computadores;
- locações;
- funcionários.

A ideia era permitir que o administrador ou o operador do sistema tivesse controle completo sobre as operações do dia a dia.

### 3.4 Evolução para um sistema mais seguro e controlado

Com o avanço do projeto, foram feitas melhorias importantes para tornar o uso mais confiável:

- correção de problemas no salvamento de funcionários;
- correção de falhas no login;
- melhoria no tratamento de senhas;
- restrição de acesso a áreas sensíveis para usuários sem permissão.

## 4. Histórico de versões e mudanças

A seguir está um resumo do que foi adicionado e melhorado em cada versão do projeto.

### Versão 1.0
- Criação da estrutura inicial do sistema.
- Implementação da interface gráfica com JavaFX.
- Conexão com banco de dados SQLite.
- Cadastro básico de clientes, computadores, locações e funcionários.
- Implementação inicial do fluxo de login.

### Versão 1.1
- Melhoria na organização do código em camadas.
- Refinamento da estrutura de serviços e DAO.
- Ajustes na interface para facilitar o uso.

### Versão 1.2
- Melhorias no cadastro e edição de dados.
- Organização mais clara dos módulos principais.
- Ajustes iniciais no controle de operações do sistema.

### Versão 1.3
- Implementação de melhorias na autenticação.
- Ajustes no fluxo de acesso do usuário.
- Maior estabilidade no uso da aplicação.

### Versão 1.4
- Melhoria no gerenciamento de funcionários.
- Ajustes para tornar o cadastro mais confiável.
- Melhor tratamento de senhas e credenciais.

### Versão 1.5
- Correções em problemas de login e acesso.
- Melhorias no comportamento após erro de senha.
- Aumento da robustez no fluxo de autenticação.

### Versão 1.6
- Correção no cadastro de funcionários.
- Correção no login para permitir nova tentativa com a senha correta.
- Melhoria no tratamento de autenticação.
- O administrador pode visualizar e editar as senhas dos funcionários.
- A área de funcionários passou a ficar restrita apenas ao administrador.
- Adição de um seletor de tipo de acesso: Funcionário ou Administrador.

## 5. Principais mudanças da versão 1.6

A versão 1.6 representa uma evolução importante no sistema, com foco em estabilidade, segurança e organização de permissões.

### 4.1 Correções no cadastro e login

Foram corrigidos problemas relacionados a:

- cadastro de funcionários;
- entrada no sistema após erro de senha;
- tratamento de credenciais inválidas e válidas.

Isso deixou o fluxo de autenticação mais previsível e confiável.

### 4.2 Melhorias no controle de permissões

A partir dessa evolução, o sistema passou a diferenciar melhor os tipos de usuário:

- usuário comum;
- administrador.

O administrador passou a ter acesso a funções exclusivas, como a gestão de funcionários.

### 4.3 Restrição da área de funcionários

A área de gerenciamento de funcionários foi limitada apenas ao administrador. Isso evita que usuários comuns tenham acesso indevido às informações e configurações sensíveis.

### 4.4 Visualização e edição de senhas

O administrador agora consegue visualizar e editar as senhas dos funcionários, o que facilita a administração do sistema.

### 4.5 Seletor de tipo de acesso

Foi adicionado um seletor para definir se um usuário será cadastrado como:

- Funcionário;
- Administrador.

Essa mudança torna o controle de papéis mais claro e alinhado com as necessidades do sistema.

## 5. Arquitetura atual

A estrutura atual do projeto segue um modelo simples e organizado:

- Main: inicializa a aplicação.
- Controller: controla a navegação e o fluxo entre telas.
- Service: implementa regras de negócio.
- DAO: realiza operações no banco SQLite.
- Model: representa as entidades do sistema.
- UI: concentra a interface JavaFX.

Essa abordagem permitiu evoluir o projeto com menos impacto em diferentes módulos.

## 6. Tecnologias utilizadas

As tecnologias principais do projeto são:

- Java 17+
- JavaFX para interface gráfica
- SQLite para armazenamento de dados
- Maven para build e dependências
- JUnit para testes automatizados

## 7. Como o projeto funciona

O fluxo principal da aplicação é:

1. O usuário inicia o sistema.
2. Faz login com usuário e senha.
3. O sistema valida as credenciais.
4. O usuário acessa os módulos disponíveis conforme seu perfil.
5. As operações de cadastro e gestão são salvas localmente no banco SQLite.

## 8. Como executar o projeto

### Requisitos

- Java 17 ou superior
- Maven instalado

### Passos

```bash
git clone https://github.com/Torist12/lan-house-java.git
cd lan-house-java
mvn test
./run.sh
```

Ou, caso prefira:

```bash
mvn javafx:run
```

## 9. Como usar o sistema

1. Abra a aplicação.
2. Faça login com uma conta válida.
3. Utilize os menus para gerenciar clientes, computadores e locações.
4. Para gerenciar funcionários, use uma conta de administrador.
5. No cadastro de funcionário, informe o nome de usuário, a senha e defina o tipo de acesso.

## 10. Considerações finais

O projeto começou como uma aplicação simples para gestão de uma lan house e evoluiu para um sistema mais completo, com foco em:

- usabilidade;
- estabilidade;
- controle de acesso;
- organização da interface;
- confiabilidade nas operações principais.

A versão 1.6 representa um marco importante nessa evolução, consolidando funcionalidades essenciais e corrigindo pontos críticos do fluxo inicial.
