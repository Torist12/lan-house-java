# Lan House Java

Sistema de gestão para lan house desenvolvido em Java com JavaFX e SQLite.

## Versão atual
- Versão: 1.6
- Status: estável para uso local

## O que mudou na versão 1.6
- Correção no cadastro de funcionários, incluindo salvamento mais confiável.
- Correção no login: agora o usuário consegue entrar novamente mesmo após errar a senha uma vez, desde que a senha correta seja informada.
- Melhorias na autenticação para aceitar credenciais com tratamento mais robusto.
- O administrador pode visualizar e editar as senhas dos funcionários.
- A área de funcionários ficou restrita apenas ao administrador.
- Foi adicionado um seletor para definir se um usuário é "Funcionário" ou "Administrador".

## Funcionalidades principais
- Cadastro e gerenciamento de clientes
- Cadastro e gerenciamento de computadores
- Controle de locações
- Gestão de funcionários
- Acesso com autenticação por usuário e senha
- Painel administrativo para usuários com permissão de administrador

## Requisitos
- Java 17 ou superior
- Maven
- Sistema operacional Linux, Windows ou macOS

## Como executar
### 1. Clone o projeto
```bash
git clone https://github.com/Torist12/lan-house-java.git
cd lan-house-java
```

### 2. Instale as dependências e execute os testes
```bash
mvn test
```

### 3. Inicie a aplicação
```bash
./run.sh
```

Ou, se preferir:
```bash
mvn javafx:run
```

## Como usar
1. Abra a aplicação.
2. Faça login com uma conta existente ou com a conta administrativa.
3. No painel, utilize as opções para gerenciar clientes, computadores e locações.
4. Para gerenciar funcionários, entre com uma conta de administrador.
5. No formulário de funcionários, informe o usuário, a senha e selecione o tipo de acesso.

## Observações
- A aplicação utiliza SQLite para armazenamento local.
- Os dados são salvos no ambiente local da máquina.
- Para uso em produção, recomenda-se revisar segurança, backup e permissões de acesso.
