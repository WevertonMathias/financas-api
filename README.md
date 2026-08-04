# 💰 Finanças API - Sistema de Gestão Financeira Pessoal

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![JUnit5](https://img.shields.io/badge/JUnit-5-25A162.svg)](https://junit.org/junit5/)
[![Mockito](https://img.shields.io/badge/Mockito-5.x-yellow.svg)](https://site.mockito.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

API RESTful para controle e gerenciamento financeiro pessoal desenvolvida em **Java** e **Spring Boot**. O projeto conta com arquitetura em camadas, validações de regras de negocio, criptografia de senhas e **cobertura de testes unitários** com **JUnit 5** e **Mockito**.

---

## 🎯 Objetivo do Projeto

Esta aplicação foi construída para solucionar o gerenciamento de finanças pessoais (receitas, despesas e relatórios) com foco em **boas práticas de desenvolvimento backend**, **código limpo** e **segurança das informações**.

---

## 🛠️ Tecnologias e Ferramentas Utilizadas

- **Linguagem:** Java 17
- **Framework Principal:** Spring Boot 3.x
- **Persistência de Dados:** Spring Data JPA + MySQL
- **Segurança:** BCrypt Password Encoder
- **Testes Automatizados:** JUnit 5 + Mockito
- **Gerenciador de Dependências:** Apache Maven
- **IDE:** IntelliJ IDEA

---

## 🧩 Arquitetura do Sistema

O projeto segue a **Arquitetura em Camadas** (*Layered Architecture*), promovendo alta coesão e baixo acoplamento:

```text
com.weverton.financas_api
├── controller   --> Endpoints REST (Em breve)
├── model        --> Entidades do banco de dados (JPA Entities)
├── repository   --> Camada de acesso aos dados (Spring Data JPA)
└── service      --> Regras de negócio e validações