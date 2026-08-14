# 💰 Finanças API - Sistema de Gestão Financeira Pessoal

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![JUnit5](https://img.shields.io/badge/JUnit-5-25A162.svg)](https://junit.org/junit5/)
[![Mockito](https://img.shields.io/badge/Mockito-5.x-yellow.svg)](https://site.mockito.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

API RESTful para controle e gerenciamento financeiro pessoal desenvolvida em **Java** e **Spring Boot**. O projeto conta com arquitetura em camadas, padrão DTO (Request/Response), tratamento de exceções customizado, validações de regras de negócio, criptografia de senhas e **cobertura de testes unitários** com **JUnit 5** e **Mockito**.

---

## 🎯 Objetivo do Projeto

Esta aplicação foi construída para solucionar o gerenciamento de finanças pessoais (receitas, despesas e relatórios) com foco em **boas práticas de desenvolvimento backend**, **código limpo** e **segurança das informações**.

---

## 🛠️ Tecnologias e Ferramentas Utilizadas

- **Linguagem:** Java 17
- **Framework Principal:** Spring Boot 3.x
- **Persistência de Dados:** Spring Data JPA + MySQL (Docker)
- **Segurança:** BCrypt Password Encoder
- **Testes Automatizados:** JUnit 5 + Mockito
- **Gerenciador de Dependências:** Apache Maven
- **IDE:** IntelliJ IDEA
- **Testes de API:** Postman

---

## 🧩 Arquitetura do Sistema

O projeto segue a **Arquitetura em Camadas** (*Layered Architecture*), promovendo alta coesão e baixo acoplamento:

```text
com.weverton.financas_api
├── controller   --> Endpoints REST (Usuario, Movimentacao, Categoria)
├── dto          --> Objetos de transferência de dados (Request/Response)
├── exception    --> Exceções customizadas e tratamento centralizado
├── model        --> Entidades do banco de dados (JPA Entities)
├── repository   --> Camada de acesso aos dados (Spring Data JPA)
└── service      --> Regras de negócio e validações
```

### Padrão DTO (Request/Response)

Toda comunicação de entrada e saída da API utiliza objetos de transferência de dados específicos, evitando expor as entidades do banco diretamente. Isso garante que dados sensíveis (como senhas criptografadas) nunca sejam retornados nas respostas, e que cada operação receba exatamente os dados que precisa — nem mais, nem menos.

Exemplos de DTOs implementados:
- `UsuarioRequestDTO` / `UsuarioResponseDTO`
- `LoginRequestDTO`
- `AtualizarPerfilRequestDTO`
- `MovimentacaoRequestDTO` / `MovimentacaoResponseDTO`

> A `CategoriaController` é uma exceção proposital ao padrão: por lidar com uma entidade simples, sem dados sensíveis, ela opera diretamente sobre o `CategoriaRepository`, sem camada de Service ou DTO — uma decisão consciente de simplicidade onde não há regra de negócio a proteger.

### Tratamento de Exceções Customizado

Em vez de exceções genéricas, o projeto define exceções específicas para cada categoria de erro, mapeadas para o status HTTP correto por um handler centralizado (`@RestControllerAdvice`):

| Exceção | Status HTTP | Uso |
|---|---|---|
| `RecursoNaoEncontradoException` | 404 Not Found | Busca de um recurso (usuário, categoria, movimentação) que não existe |
| `RecursoJaExisteException` | 409 Conflict | Tentativa de criar um recurso duplicado (ex: e-mail já cadastrado) |
| `DadosInvalidosException` | 422 Unprocessable Entity | Dados fornecidos que violam uma regra de negócio (ex: senha incorreta, valor negativo) |
| `AcessoNegadoException` | 403 Forbidden | Tentativa de acessar/modificar um recurso que não pertence ao usuário |

---

## 📦 Modelo de Dados (Entidades)

- **Usuario** — id, nome, email, senha (criptografada com BCrypt)
- **Categoria** — id, nome, tipo (RECEITA ou DESPESA)
- **TipoMovimentacao** (Enum) — RECEITA, DESPESA
- **Movimentacao** — id, descricao, valor, data, categoria (relação `@ManyToOne`), usuario (relação `@ManyToOne`)

---

## ⚙️ Camadas de Serviço

### `UsuarioService`
- Cadastro com validação de e-mail duplicado e criptografia de senha
- Autenticação (login) com verificação segura de senha via BCrypt
- Busca de usuário por ID
- Atualização de perfil (nome/e-mail), com validação de e-mail já em uso
- Alteração de senha, exigindo confirmação da senha atual

### `MovimentacaoService`
- Criação de movimentações, com validação de usuário, categoria e valor (não pode ser zero ou negativo)
- Listagem de movimentações por usuário, por período (data início/fim) e por tipo (RECEITA/DESPESA)
- Atualização de movimentações existentes
- Exclusão de movimentações, com **verificação de propriedade do recurso** (um usuário não pode alterar ou excluir movimentações de outro)

---

## 🌐 Endpoints da API

### Usuários (`/usuarios`)
| Verbo | Rota | Descrição |
|---|---|---|
| POST | `/usuarios` | Cadastrar novo usuário |
| POST | `/usuarios/login` | Autenticar usuário |
| GET | `/usuarios/{id}` | Buscar usuário por ID |
| PUT | `/usuarios/{id}` | Atualizar perfil (nome/e-mail) |
| PUT | `/usuarios/{id}/senha` | Alterar senha |

### Movimentações (`/movimentacoes`)
| Verbo | Rota | Descrição |
|---|---|---|
| POST | `/movimentacoes` | Criar movimentação |
| GET | `/movimentacoes?idUsuario=` | Listar movimentações do usuário |
| GET | `/movimentacoes/periodo?idUsuario=&dataInicio=&dataFim=` | Listar por período |
| GET | `/movimentacoes/tipo?idUsuario=&tipo=` | Listar por tipo (RECEITA/DESPESA) |
| PUT | `/movimentacoes/{id}?idUsuario=` | Atualizar movimentação |
| DELETE | `/movimentacoes/{id}?idUsuario=` | Excluir movimentação |

### Categorias (`/categorias`)
| Verbo | Rota | Descrição |
|---|---|---|
| POST | `/categorias` | Criar categoria |
| GET | `/categorias` | Listar todas as categorias |
| PUT | `/categorias/{id}` | Atualizar categoria |
| DELETE | `/categorias/{id}` | Excluir categoria |

---

## 🧪 Cobertura de Testes Unitários

A qualidade das camadas de serviço é garantida por testes unitários utilizando JUnit 5 e Mockito, aplicando o padrão AAA (Arrange, Act, Assert):

- [x] **UsuarioService**: cadastro, login, busca por ID, atualização de perfil e alteração de senha — cenários de sucesso e de erro (e-mail duplicado, senha incorreta, usuário não encontrado).
- [x] **MovimentacaoService**: criação, listagem (geral, por período, por tipo), atualização e exclusão — incluindo validação de propriedade do recurso.
- [x] **Mocks de Repositório**: isolamento total da camada de persistência com `Mockito.when()` e `Mockito.verify()`.
- [x] **Testes de integração manual**: todos os endpoints validados via Postman, incluindo cenários de erro (404, 409, 422, 403).

---

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos
- Java 17 instalado
- Maven instalado
- Docker (para o banco de dados MySQL)

### Passo a Passo

Clone o repositório:
```bash
git clone https://github.com/WevertonMathias/financas-api.git
```

Acesse a pasta do projeto:
```bash
cd financas-api
```

Suba o banco de dados MySQL via Docker:
```bash
docker-compose up -d
```

Configure o banco de dados no arquivo `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/financas_db?createDatabaseIfNotExist=true&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true
    username: ${DB_USERNAME:financas_user}
    password: ${DB_PASSWORD:sua_senha_aqui}
  jpa:
    hibernate:
      ddl-auto: update
```

Execute os testes unitários:
```bash
mvn test
```

Inicie a aplicação:
```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

> ⚠️ **Nota sobre segurança**: o projeto utiliza uma configuração temporária do Spring Security (`SecurityConfig`) que libera todas as rotas para fins de desenvolvimento. Autenticação real via JWT ainda está no roadmap.

---

## 📌 Próximos Passos (Roadmap)

- [x] Implementação da camada `UsuarioService` com criptografia BCrypt
- [x] Implementação da camada `MovimentacaoService` com CRUD completo
- [x] Migração de ambas as camadas para o padrão DTO (Request/Response)
- [x] Suíte de testes unitários para `UsuarioService` e `MovimentacaoService`
- [x] Implementação dos Controllers (`UsuarioController`, `MovimentacaoController`, `CategoriaController`) com testes via Postman
- [x] Tratamento de exceções customizado com `@RestControllerAdvice`
- [ ] Implementação de relatórios (gastos por categoria, saldo mensal, comparativo receita x despesa)
- [ ] Implementação de Spring Security com autenticação JWT
- [ ] Integração com IA para categorização automática e insights financeiros
- [ ] Documentação da API com Swagger/OpenAPI
- [ ] Front-end com Vaadin

---

## 👤 Desenvolvedor

**Weverton Mathias Rocha**
Desenvolvedor Backend Java | Estudante de Análise e Desenvolvimento de Sistemas (Unifacvest)