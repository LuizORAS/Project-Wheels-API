# Project Wheels API 🚲

Esta é a API REST do **Project Wheels**, responsável por gerenciar usuários, planos de aluguel de bicicletas, integração com o cliente CLI do sistema e toda a lógica de aluguel, devolução, multas e geração de recibos.

## Visão Geral

A API foi desenvolvida em **Java (Spring Boot)**, seguindo as melhores práticas de REST. Utiliza persistência por arquivos CSV para usuários e bicicletas, facilitando testes, demonstrações e integração com qualquer frontend ou CLI.

---

## Endpoints Disponíveis

Abaixo estão listados **todos os endpoints reais** expostos pela API, conforme implementados nos controllers.

---

### **Usuários**

- `GET /users`
    - Retorna todos os usuários cadastrados.

- `GET /users/{email}`
    - Retorna os dados do usuário pelo email.

- `POST /users`
    - Cria um novo usuário.
    - **Body:** JSON com os dados do usuário.

- `PUT /users/{email}`
    - Atualiza os dados do usuário.
    - **Body:** JSON com os dados atualizados.

- `DELETE /users/{email}`
    - Remove o usuário do sistema.

---

### **Planos**

- `GET /users/{email}/plan`
    - Retorna o plano atual do usuário.

- `PUT /users/{email}/plan`
    - Troca o plano do usuário para um novo plano.
    - **Body:** `"BASIC" | "GOLD" | "DIAMOND" | "FREE"`

- `PUT /users/{email}/plan/cancel`
    - Cancela o plano do usuário (volta para FREE).

---

### **Multa**

- `GET /users/{email}/multa`
    - Retorna o valor da multa atual do usuário (se houver).

---

### **Bicicletas**

- `GET /bikes`
    - Retorna todas as bicicletas cadastradas.

- `GET /bikes/available?type={type}`
    - Lista todas as bicicletas disponíveis de um determinado tipo (`COMUM`, `COMFORT`, `ELECTRIC`).

- `GET /bikes/{id}`
    - Retorna os dados de uma bicicleta pelo seu ID.

- `PUT /bikes/{id}/rent?userEmail={email}`
    - Aluga a bicicleta de ID especificado para o usuário informado por email.

- `PUT /bikes/{id}/return?userEmail={email}`
    - Devolve a bicicleta de ID especificado para o usuário informado por email.

---

## Como Usar

1. **Clonar o repositório**
    ```bash
    git clone https://github.com/LuizORAS/Project-Wheels-API.git
    cd Project-Wheels-API
    ```

2. **Configurar o ambiente**
    - É necessário ter o Java 17+ instalado.
    - Não é preciso configurar banco de dados: a persistência é feita por arquivos CSV em `src/main/resources/csv`.

3. **Rodar a aplicação**
    ```bash
    ./mvnw spring-boot:run
    ```
   ou
    ```bash
    ./gradlew bootRun
    ```
   ou
    ```bash
    java -jar target/project-wheels-api.jar
    ```

4. **Testar os endpoints**
    - Use ferramentas como Postman, Insomnia ou o cliente CLI do Project Wheels.
    - Exemplo de requisição para aluguel de bicicleta:
        ```http
        PUT /bikes/12/rent?userEmail=exemplo@email.com
        ```

---

## Exemplo de Fluxo

1. **Criar usuário:** `POST /users`
2. **Consultar usuário:** `GET /users/{email}`
3. **Trocar de plano:** `PUT /users/{email}/plan`
4. **Alugar bicicleta:** `PUT /bikes/{id}/rent?userEmail={email}`
5. **Devolver bicicleta:** `PUT /bikes/{id}/return?userEmail={email}`
6. **Consultar multa:** `GET /users/{email}/multa`

---

## Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Jackson (JSON)
- Persistência em arquivos CSV (não usa banco relacional)
- [Bibliotecas auxiliares conforme necessário]

---

**Dúvidas ou sugestões?**  
Abra uma issue ou entre em contato!
