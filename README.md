# Lan House Java

Sistema de gestão para lan house desenvolvido em Java com JavaFX e SQLite.

## Versão atual
- Versão: 1.7
- Status: estável para uso local

## O que mudou na versão 1.7
- Adicionado suporte à geração de instaladores nativos com `jpackage`.
- Inclusão do script `package.sh` para automatizar a criação de imagem de aplicativo Linux e pacote `.deb`.
- Atualização da documentação para explicar o processo de criação do instalador.
- O `package.sh` agora gerou o arquivo `dist/linux/lanhousesystem_1.0.0_amd64.deb` a partir do build atual.

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

## Criar instalador
### Como funciona
O instalador é criado pelo `jpackage`, que empacota o `jar` e um runtime Java mínimo em uma imagem de aplicativo nativa.
O script `package.sh` automatiza o processo no Linux:
- roda `mvn clean package` para gerar o `jar` final em `target/`
- executa `jpackage` para criar a imagem do app
- executa `jpackage --type deb` quando o sistema possui suporte a pacote Debian
- o resultado atual é o pacote `dist/linux/lanhousesystem_1.0.0_amd64.deb`

### Linux
1. Certifique-se de ter JDK 17+ com `jpackage` instalado.
2. Execute:
```bash
./package.sh
```
3. A imagem do aplicativo e, quando disponível, o pacote `.deb` serão gerados em `dist/linux`.

### Windows
No Windows com JDK, rode um comando similar ao seguinte:
```bat
jpackage --input target --name lanhousesystem --main-jar lanhousesystem-1.0.0.jar --main-class com.lanhouse.Main --type msi --app-version 1.0.0 --dest dist\windows
```

> Observação: `jpackage` não costuma suportar build cruzado. Para gerar o instalador Windows, rode o comando em uma máquina Windows com JDK instalado.

## Observações
- A aplicação utiliza SQLite para armazenamento local.
- Os dados são salvos no ambiente local da máquina.
- Para uso em produção, recomenda-se revisar segurança, backup e permissões de acesso.
