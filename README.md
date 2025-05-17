# Catálogo do Sábio

Este projeto é uma API RESTful desenvolvida em Java para gerenciar um catálogo de livros.
Ele foi projetado seguindo os princípios da **Clean Architecture**, garantindo modularidade, escalabilidade e facilidade de manutenção.

## I. Arquitetura de Solução e Arquitetura Técnica

### Solução Implementada

A solução consiste em uma API RESTful que permite gerenciar um catálogo de livros. 
A aplicação foi desenvolvida utilizando **Java 21** e o framework **Quarkus**, que oferece alta performance e suporte nativo para aplicações baseadas em microserviços. 
O banco de dados utilizado é o **MongoDB**, executado em um container Docker.

### Tecnologias Usadas

- **Java 21**: Linguagem de programação principal.
- **Quarkus**: Framework para desenvolvimento de aplicações Java modernas.
- **MongoDB**: Banco de dados NoSQL para armazenamento de dados.
- **Docker**: Para containerização do banco de dados e da aplicação.
- **Maven**: Gerenciador de dependências e build.
- **REST**: Arquitetura para comunicação entre cliente e servidor.

### Decisões de Design

- **Clean Architecture**: A aplicação foi estruturada em camadas bem definidas:
  - **Domain**: Contém as entidades principais. É independente de frameworks e bibliotecas externas.
  - **Application**: Contém os casos de uso e comandos que orquestram as operações da aplicação.
  - **Infrastructure**: Contém implementações específicas de infraestrutura, como repositórios, clientes HTTP e integrações externas.
  - **Resource**: Contém os controladores REST que expõem os endpoints da API.
- **Containerização**: O uso de Docker garante que o ambiente seja consistente e facilita o deploy da aplicação.
- **MongoDB**: Escolhido por sua flexibilidade e suporte a grandes volumes de dados.

---

## II. Explicação sobre o Case Desenvolvido (Plano de Implementação)

### Objetivo

O objetivo do case foi desenvolver uma API que permita gerenciar um catálogo de livros, oferecendo funcionalidades para recuperar todos os livros, 
buscar livros por ID, gênero ou autor.

### Funcionalidades Implementadas

#### Endpoints da API

- **GET /books**: Recupera uma lista de todos os livros.
- **GET /books/:id**: Recupera um livro específico pelo seu ID.
- **GET /books/genre/:genre**: Recupera livros por um gênero específico.
- **GET /books/author/:author**: Recupera livros por um autor específico.

#### Lógica de Negócios

- **GET /books**: Este endpoint utiliza o repositório para buscar todos os livros armazenados no banco de dados MongoDB.
- **GET /books/:id**: Este endpoint busca um livro específico pelo seu ID único.
- **GET /books/genre/:genre**: Este endpoint filtra os livros pelo gênero especificado, retornando todos os livros que correspondem ao critério.
- **GET /books/author/:author**: Este endpoint filtra os livros pelo autor especificado, retornando todos os livros que correspondem ao critério.

#### Estrutura de Dados

Os livros são armazenados no MongoDB com a seguinte estrutura:

```json
{
  "_id": "integer",
  "title": "string",
  "author": "string",
  "genre": "string",
  "description": "string"
}
```

## III. Melhorias e Considerações Finais

### Melhorias Futuras

1. **Autenticação e Autorização**:
  - Implementar autenticação para proteger os endpoints da API.
  - Adicionar autorização para controlar o acesso a determinados recursos.

2. **Paginação e Filtros Avançados**:
  - Melhorar o endpoint `GET /books` para suportar filtros avançados e ordenação.
  - Implementar paginação para grandes conjuntos de dados.

3. **Cache**:
  - Utilizar um sistema de cache (como Redis ou Infinispan) para melhorar a performance em consultas frequentes.

4. **Testes Automatizados**:
  - Expandir a cobertura de testes unitários e de integração para garantir a qualidade do código.

5. **Documentação da API**:
  - Adicionar documentação detalhada utilizando ferramentas como Swagger ou OpenAPI.

### Desafios Encontrados

- **Configuração de Rede no Docker**:
  - Foi necessário ajustar a comunicação entre os containers para garantir que a API conseguisse se conectar ao MongoDB.
- **Gerenciamento de Dependências**:
  - A escolha das dependências foi feita com cuidado para garantir compatibilidade e performance.

## Como Iniciar o Projeto

### Passo 1: Configurar o Ambiente

Certifique-se de que você possui as dependências instaladas:

- **Java**: 21
- **Maven**: 3.8.6
- **Docker**: Para rodar o banco de dados MongoDB e a aplicação.

### Passo 2: Iniciar o Projeto

Execute o comando abaixo para iniciar o banco de dados MongoDB e a aplicação:

```bash
docker run --name mongo -d -p 27017:27017 mongo
```
```bash
mvn clean install
```
```bash
mvn quarkus:dev
```

Ou se preferir executar a aplicação diretamente com docker compose rode apenas:
```bash
docker-compose up --build
```

### Endpoints Disponíveis

#### Recuperar Todos os Livros

```bash
curl -X GET http://localhost:8080/books
```

#### Recuperar Livro por ID

```bash
curl -X GET http://localhost:8080/books/{id}
```

#### Recuperar Livros por Gênero

```bash
curl -X GET http://localhost:8080/books/genre/{genre}
```

#### Recuperar Livros por Autor

```bash
curl -X GET http://localhost:8080/books/author/{author}
```

## Estrutura do Projeto

```
/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/
│   │   │       └── com/
│   │   │           └── santander/
│   │   │               ├── application/
│   │   │               ├── domain/
│   │   │               ├── infrastructure/
│   │   │               └── resource/
│   │   └── resources/
│   └── test/
├── docker-compose.yml
├── start.sh
├── pom.xml
└── README.md
```

## Observações

- Certifique-se de que a porta `8080` está disponível no seu ambiente local.
- O banco de dados MongoDB será iniciado no container Docker e estará acessível na porta `27017`.
