# Catálogo do Sábio

Este projeto é uma API RESTful desenvolvida em Java para gerenciar um catálogo de livros. Ele foi projetado seguindo os
princípios da **Clean Architecture**, garantindo modularidade, escalabilidade e facilidade de manutenção.

## Arquitetura

A aplicação segue os princípios da **Clean Architecture**, dividindo o código em camadas bem definidas:

- **Domain**: Contém as entidades principais e as regras de negócio. É independente de frameworks e bibliotecas
  externas.
- **Application**: Contém os casos de uso e comandos que orquestram as operações da aplicação.
- **Infrastructure**: Contém implementações específicas de infraestrutura, como repositórios, clientes HTTP e
  integrações externas.
- **Resource**: Contém os controladores REST que expõem os endpoints da API.

## Dependências

- **Java**: 21
- **Maven**: 3.8.6
- **Docker**: Para rodar o banco de dados MongoDB e a aplicação.

## Como Iniciar o Projeto

### Passo 1: Configurar o Ambiente

Certifique-se de que você possui as dependências instaladas:

- Java 21
- Maven 3.8.6
- Docker

### Passo 2: Iniciar o Projeto

Execute o script `start.sh` para iniciar o banco de dados MongoDB e a aplicação:

```bash
./start.sh
```

O script irá:

1. Subir um container Docker com o MongoDB.
2. Construir e iniciar a aplicação.

### Endpoints Disponíveis

A API expõe os seguintes endpoints:

- **GET /books**: Recupera uma lista de todos os livros. (Paginação opcional para grandes conjuntos de dados)
- **GET /books/genre/:genre**: Recupera livros por um gênero específico.
- **GET /books/author/:author**: Recupera livros por um autor específico.

### Exemplos de Chamadas

#### Recuperar Todos os Livros

```bash
curl -X GET http://localhost:8080/books
```

#### Recuperar Livros por Gênero

```bash
curl -X GET http://localhost:8080/books/genre/ficcao
```

#### Recuperar Livros por Autor

```bash
curl -X GET http://localhost:8080/books/author/joao
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

## Contribuição

Sinta-se à vontade para abrir issues ou enviar pull requests para melhorias no projeto.
