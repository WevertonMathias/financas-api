# 💰 Finanças API - Sistema de Gestão Financeira Pessoal

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![JWT](https://img.shields.io/badge/Auth-JWT-blueviolet.svg)]()
[![JUnit5](https://img.shields.io/badge/JUnit-5-25A162.svg)](https://junit.org/junit5/)
[![Mockito](https://img.shields.io/badge/Mockito-5.x-yellow.svg)](https://site.mockito.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

API RESTful para controle e gerenciamento financeiro pessoal desenvolvida em **Java** e **Spring Boot**. O projeto conta com arquitetura em camadas, padrão DTO (Request/Response), autenticação JWT, tratamento de exceções customizado, relatórios financeiros, documentação interativa via Swagger, testes unitários com JUnit 5 e Mockito, e front-end próprio consumindo a API em produção.

**🔗 Aplicação em produção:** [financas-api-mpp3.onrender.com](https://financas-api-mpp3.onrender.com)

---

## 🎯 Objetivo do Projeto

Aplicação construída para solucionar o gerenciamento de finanças pessoais (receitas, despesas e relatórios) com foco em **boas práticas de desenvolvimento backend**, **código limpo** e **segurança das informações**.

---

## 🛠️ Tecnologias e Ferramentas Utilizadas

- **Linguagem:** Java 17
- **Framework Principal:** Spring Boot 3.x
- **Persistência de Dados:** Spring Data JPA + MySQL
- **Segurança:** Spring Security + JWT, BCrypt Password Encoder
- **Documentação da API:** Springdoc OpenAPI (Swagger UI)
- **Testes Automatizados:** JUnit 5 + Mockito
- **Front-end:** HTML5, CSS3 e JavaScript puro, consumindo a API via `fetch`
- **Infraestrutura:** Docker (MySQL local), MySQL gerenciado na nuvem (Aiven)
- **Deploy:** Render
- **Gerenciador de Dependências:** Apache Maven
- **IDE:** IntelliJ IDEA

---

## 🧩 Arquitetura do Sistema

O projeto segue a **Arquitetura em Camadas** (*Layered Architecture*), promovendo alta coesão e baixo acoplamento:

```text
com.weverton.financas_api
├── config       --> Configurações do Spring (Security, CORS)
├── controller   --> Endpoints REST (Usuario, Movimentacao, Categoria, Relatorio)
├── dto          --> Objetos de transferência de dados (Request/Response)
├── exception    --> Exceções customizadas e tratamento centralizado
├── model        --> Entidades do banco de dados (JPA Entities)
├── repository   --> Camada de acesso aos dados (Spring Data JPA)
├── security     --> Filtro de autenticação JWT
└── service      --> Regras de negócio, validações, relatórios e geração/validação de tokens

frontend/
└── financas-app.html  --> Interface web standalone, consumindo a API
```

### Padrão DTO (Request/Response)

Toda comunicação de entrada e saída da API utiliza objetos de transferência de dados específicos, evitando expor as entidades do banco diretamente. Isso garante que dados sensíveis (como senhas criptografadas) nunca sejam retornados nas respostas.

Exemplos de DTOs implementados:
- `UsuarioRequestDTO` / `UsuarioResponseDTO`
- `LoginRequestDTO` / `LoginResponseDTO`
- `AtualizarPerfilRequestDTO`
- `MovimentacaoRequestDTO` / `MovimentacaoResponseDTO`
- `RelatorioSaldoDTO` / `RelatorioPorCategoriaDTO`

### Autenticação com JWT

A API utiliza autenticação stateless via JSON Web Token. Após o login, o cliente recebe um token que deve ser enviado no cabeçalho `Authorization` em todas as requisições a rotas protegidas:

```
Authorization: Bearer <token>
```

- Rotas públicas: `POST /usuarios` (cadastro) e `POST /usuarios/login`
- Todas as demais rotas exigem token válido
- O usuário autenticado é extraído automaticamente do token (via `@AuthenticationPrincipal`), eliminando a necessidade de enviar `idUsuario` manualmente — e impedindo que um usuário acesse ou modifique dados de outro
- Chave secreta e tempo de expiração gerenciados via variáveis de ambiente (`JWT_SECRET`)

### Tratamento de Exceções Customizado

Exceções específicas mapeadas para o status HTTP correto por um handler centralizado (`@RestControllerAdvice`):

| Exceção | Status HTTP | Uso |
|---|---|---|
| `RecursoNaoEncontradoException` | 404 Not Found | Busca de um recurso (usuário, categoria, movimentação) inexistente |
| `RecursoJaExisteException` | 409 Conflict | Tentativa de criar um recurso duplicado (ex: e-mail já cadastrado) |
| `DadosInvalidosException` | 422 Unprocessable Entity | Violação de regra de negócio (ex: valor negativo, senha incorreta) |
| `AcessoNegadoException` | 403 Forbidden | Tentativa de acessar/modificar recurso de outro usuário |

---

## 📦 Modelo de Dados (Entidades)

- **Usuario** — id, nome, email, senha (criptografada com BCrypt)
- **Categoria** — id, nome, tipo (RECEITA ou DESPESA)
- **TipoMovimentacao** (Enum) — RECEITA, DESPESA
- **Movimentacao** — id, descricao, valor, data, categoria (`@ManyToOne`), usuario (`@ManyToOne`)

---

## ⚙️ Camadas de Serviço

### `UsuarioService`
Cadastro, login (com geração de token JWT), busca por ID, atualização de perfil e alteração de senha.

### `MovimentacaoService`
Criação, listagem (geral, por período, por tipo), atualização e exclusão — com validação de propriedade do recurso.

### `RelatorioService`
- **Saldo do período** — total de receitas, total de despesas e saldo (diferença entre os dois)
- **Total por categoria** — agrupamento de valores por categoria, útil para identificar onde há maior concentração de gastos

---

## 🌐 Endpoints da API

### Usuários (`/usuarios`)
| Verbo | Rota | Descrição |
|---|---|---|
| POST | `/usuarios` | Cadastrar novo usuário |
| POST | `/usuarios/login` | Autenticar usuário e gerar token JWT |
| GET | `/usuarios/{id}` | Buscar usuário por ID (apenas o próprio) |
| PUT | `/usuarios/{id}` | Atualizar perfil (nome/e-mail) |
| PUT | `/usuarios/{id}/senha` | Alterar senha |

### Movimentações (`/movimentacoes`)
| Verbo | Rota | Descrição |
|---|---|---|
| POST | `/movimentacoes` | Criar movimentação |
| GET | `/movimentacoes` | Listar movimentações do usuário autenticado |
| GET | `/movimentacoes/periodo?dataInicio=&dataFim=` | Listar por período |
| GET | `/movimentacoes/tipo?tipo=` | Listar por tipo (RECEITA/DESPESA) |
| PUT | `/movimentacoes/{id}` | Atualizar movimentação |
| DELETE | `/movimentacoes/{id}` | Excluir movimentação |

### Categorias (`/categorias`)
| Verbo | Rota | Descrição |
|---|---|---|
| POST | `/categorias` | Criar categoria |
| GET | `/categorias` | Listar todas as categorias |
| PUT | `/categorias/{id}` | Atualizar categoria |
| DELETE | `/categorias/{id}` | Excluir categoria |

### Relatórios (`/relatorios`)
| Verbo | Rota | Descrição |
|---|---|---|
| GET | `/relatorios/saldo?dataInicio=&dataFim=` | Saldo do período (receitas, despesas e diferença) |
| GET | `/relatorios/categoria?tipo=&dataInicio=&dataFim=` | Total agrupado por categoria |

---

## 🧪 Cobertura de Testes Unitários

Testes unitários com JUnit 5 e Mockito, aplicando o padrão AAA (Arrange, Act, Assert):

- [x] **UsuarioService**: cadastro, login, busca por ID, atualização de perfil e alteração de senha — cenários de sucesso e de erro.
- [x] **MovimentacaoService**: criação, listagem (geral, por período, por tipo), atualização e exclusão — incluindo validação de propriedade do recurso.
- [x] **RelatorioService**: cálculo de saldo e total por categoria.
- [x] **TokenService**: geração e validação de tokens JWT.
- [x] **Mocks de Repositório**: isolamento total da camada de persistência com `Mockito.when()` e `Mockito.verify()`.

---

## 🚀 Como Executar o Projeto

### 1. Em produção (mais rápido)

A API já está em execução na nuvem (Render + MySQL gerenciado via Aiven). Basta abrir o arquivo `frontend/financas-app.html` em qualquer navegador para interagir com o sistema em tempo real, sem precisar instalar nada.

### 2. Localmente

**Pré-requisitos:** Java 17+, Maven, Docker (para o banco MySQL local)

```bash
# Clone o repositório
git clone https://github.com/WevertonMathias/financas-api.git
cd financas-api

# Suba o banco de dados MySQL via Docker
docker-compose up -d

# Configure as variáveis de ambiente necessárias:
# DB_USERNAME, DB_PASSWORD, JWT_SECRET
# (via IDE em Run > Edit Configurations > Environment Variables,
#  ou exportando no terminal antes de rodar)

# Execute os testes unitários
mvn test

# Inicie a aplicação
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

### 📄 Documentação Interativa (Swagger)

Com a aplicação rodando, acesse:
```
http://localhost:8080/swagger-ui.html
```
Todos os endpoints, DTOs de entrada/saída e códigos de resposta estão documentados e podem ser testados diretamente pela interface.

---

## 📌 Status do Projeto & Roadmap

- [x] CRUD completo de Usuários e Movimentações
- [x] Criptografia de senhas com BCrypt
- [x] Autenticação e autorização via Spring Security + JWT
- [x] Tratamento de exceções customizado (`@RestControllerAdvice`)
- [x] Relatórios financeiros (saldo do período, total por categoria)
- [x] Documentação interativa via Swagger/OpenAPI
- [x] Front-end web (HTML5, CSS3, JS) consumindo a API
- [x] Banco de dados gerenciado na nuvem (Aiven)
- [x] Deploy da API em produção (Render)

> **Nota sobre integração com IA:** avaliada durante o desenvolvimento, mas descartada por questões de privacidade — como o projeto lida com dados financeiros pessoais, optou-se por não enviar essas informações a APIs de IA de terceiros. Os relatórios de análise financeira já são calculados diretamente pela aplicação, sem depender de serviços externos.

---

## 👤 Desenvolvedor

**Weverton Mathias Rocha**
Desenvolvedor Backend Java | Estudante de Análise e Desenvolvimento de Sistemas (Unifacvest)
📍 Olímpia, SP - Brasil