
# API de Produtos

A **API de Produtos** permite gerenciar um catálogo de produtos. Os usuários podem criar, listar, atualizar e deletar produtos. Esta API é projetada para ser simples e eficiente, oferecendo operações básicas de CRUD.

## Tecnologias utilizadas

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring-Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Postgres](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## URL da API
A API está disponível em [https://productsapi.fly.dev/api](https://productsapi.fly.dev/api).

## Endpoints da API

### 1. Criar Produto

- **Método:** `POST`
- **Endpoint:** `/api/products`
- **Descrição:** Cria um novo produto.
- **Corpo da Requisição:**

  ```json
  {
    "name": "Camiseta",
    "value": 29.90
  }
  ```

- **Exemplo de Resposta:**
  - **Status:** `201 Created`
  - **Corpo:**

  ```json
  {
    "idProduct": "19b9158d-a521-40ad-993f-57ab2da144fs",
    "name": "Camiseta",
    "value": 29.90
  }
  ```

### 2. Listar Produtos

- **Método:** `GET`
- **Endpoint:** `/api/products`
- **Descrição:** Retorna uma lista de produtos.
- **Parâmetros:**
  - `page`: Número da página (default: 0)
  - `size`: Tamanho da página (default: 10)

- **Exemplo de Resposta:**
  - **Status:** `200 OK`
  - **Corpo:**

  ```json
  [
    {
      "idProduct": "19b9056d-a521-42ad-993f-57ab2da144ff",
      "name": "Camiseta",
      "value": 29.90
    },
    {
      "idProduct": "19b9058d-a521-40ad-993f-57ab2da144fw",
      "name": "Calça",
      "value": 49.90
    }
  ]
  ```

### 3. Atualizar Produto

- **Método:** `PUT`
- **Endpoint:** `/api/products/{id}`
- **Descrição:** Atualiza as informações de um produto existente usando o Id.
- **Corpo da Requisição:**

  ```json
  {
    "name": "Camiseta Atualizada",
    "value": 34.90
  }
  ```

- **Exemplo de Resposta:**
  - **Status:** `200 OK`
  - **Corpo:**

  ```json
  {
    "idProduct": "19b9058d-a521-40ad-993f-57ab2da144ff",
    "name": "Camiseta Atualizada",
    "value": 34.90
  }
  ```

### 4. Deletar Produto

- **Método:** `DELETE`
- **Endpoint:** `/api/products/{id}`
- **Descrição:** Remove um produto existente usando o Id.
- **Exemplo de Resposta:**
  - **Status:** `200 OK`
  - **Corpo:**

  ```json
  {
    "message": "successfully deleted"
  }
  ```

## Deploy da Aplicação

Para realizar o deploy da sua API, você pode utilizar o Docker. Siga os passos abaixo para configurar e rodar a aplicação:

1. **Construa a Imagem Docker**:
   ```bash
   docker build -t products-api .
   ```

2. **Execute o Contêiner Docker**:
   Inicie o contêiner com o mapeamento de porta e as variáveis de ambiente necessárias para a conexão com o banco de dados. O comando abaixo inicia o contêiner:

   ```bash
   docker run -d -p 8080:8080 --name nome-do-container \
     -e DB_HOST="nome-da-host" \
     -e DB_NAME="nome-da-database" \
     -e DB_USER="nome-de-usuario-da-database" \
     -e DB_PASSWORD="senha-da-database" \
   products-api

   ```
   Certifique-se de substituir `nome-do-container`, `nome-da-host`, `nome-da-database`, `nome-de-usuario-da-database`, e `senha-da-database` pelos valores corretos da sua configuração.

## Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir um problema ou enviar um pull request.

## Licença

Este projeto está sob a Licença MIT. Consulte o arquivo [LICENSE](LICENSE) para mais informações.
