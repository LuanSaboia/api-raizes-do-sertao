# API RESTful — Rede Raízes do Nordeste

Projeto multidisciplinar (UNINTER 2026) desenvolvido em **Java + Spring Boot 3**, estruturado em arquitetura em camadas (`Domain`, `Application`, `Infrastructure`, `Controller`), com autenticação *stateless* via **JWT**, controle de acesso por perfis (`RBAC`), rastreabilidade de **multicanalidade**, controle de estoque descentralizado por unidade e logs de auditoria em conformidade com a **LGPD**.

---

## Requisitos e Tecnologias Utilizadas

* **Linguagem:** Java 17 ou 21 (JDK)
* **Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA, Spring Security, Bean Validation)
* **Banco de Dados:** H2 Database (em memória, com carga inicial automática via `DatabaseSeeder`)
* **Segurança e Criptografia:** JWT (`io.jsonwebtoken` / `jjwt`) + Hash de senhas com `BCrypt`
* **Documentação Interativa:** SpringDoc OpenAPI 3 / Swagger UI
* **Gerenciador de Dependências:** Maven (com Maven Wrapper `./mvnw` incluído no repositório)
* **Utilitários:** Lombok

---

## Configuração de Ambiente (`.env.example`)

A aplicação já vem pré-configurada no `application.properties` para rodar localmente sem depender de serviços externos. Caso queira customizar portas, banco de dados ou chaves de segurança, crie um arquivo `.env` na raiz do projeto tomando como base o arquivo `.env.example` já disponível no repositório:

```env
SERVER_PORT=8080
SPRING_DATASOURCE_URL=jdbc:h2:mem:raizesdb
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=
JWT_SECRET=RaizesDoNordesteChaveSecretaSuperSegura2026UNINTER
JWT_EXPIRATION_MS=3600000
```

---

## Estrutura do Projeto

```text
api-raizes-do-sertao/
├── .mvn/                                  # Suporte do Maven Wrapper
├── src/
│   └── main/
│       └── java/com/raizesdonordeste/api/
│           ├── domain/
│           │   ├── enums/                 # CanalPedido, StatusPedido, PerfilUsuario
│           │   ├── exception/             # RecursoNaoEncontradoException, RegraNegocioException
│           │   └── model/                 # Usuario, Unidade, Produto, EstoqueUnidade, Pedido, ItemPedido, LogAuditoria
│           ├── application/
│           │   └── service/               # ProdutoService, PedidoService, AuditoriaService
│           ├── infrastructure/
│           │   ├── config/                # SecurityConfig, OpenApiConfig
│           │   ├── mock/                  # PagamentoMockGateway
│           │   ├── repository/            # Interfaces Spring Data JPA
│           │   ├── security/              # JwtService, JwtAuthenticationFilter
│           │   └── DatabaseSeeder.java
│           └── controller/
│               ├── dto/                   # Contratos Request/Response e ErroPadraoDTO
│               ├── AuthController.java
│               ├── ProdutoController.java
│               ├── PedidoController.java
│               └── GlobalExceptionHandler.java
├── .env.example                           # Modelo de variáveis de ambiente
├── mvnw / mvnw.cmd                        # Executáveis do Maven Wrapper
├── pom.xml                                # Dependências e build do projeto
├── raizes-do-nordeste-postman.json        # Coleção de testes para Postman/Insomnia
└── README.md                              # Documentação de execução e arquitetura
```

---

## Como Instalar Dependências, Criar Banco e Iniciar a API

1. Clone este repositório e acesse a pasta raiz do projeto:
   ```bash
   git clone https://github.com/LuanSaboia/api-raizes-do-sertao.git
   cd api-raizes-do-sertao
   ```

2. Instale as dependências e compile o projeto (você pode usar o `mvn` instalado na máquina ou o wrapper `./mvnw` / `mvnw.cmd`):
   ```bash
   ./mvnw clean install
   ```

3. Inicie a aplicação Spring Boot:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Banco de Dados e Carga Inicial (Seed):**
   Ao iniciar, o Hibernate gera automaticamente o esquema relacional em memória (`raizesdb`) e a classe `DatabaseSeeder` popula as tabelas com unidades, produtos, saldos de estoque e usuários de teste.
   * **Console Web do H2:** `http://localhost:8080/h2-console`
   * **JDBC URL:** `jdbc:h2:mem:raizesdb` | **User Name:** `sa` | **Password:** *(vazio)*

---

## Documentação Interativa (Swagger) e Testes (Postman)

* **Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **Contrato OpenAPI (JSON):** `http://localhost:8080/v3/api-docs`
* **Coleção Postman:** Importe o arquivo `raizes-do-nordeste-postman.json` localizado na raiz deste repositório para executar todos os fluxos positivos (login, pedido multicanal, pagamento mock, filtro por canal) e cenários de erro padronizados (`400`, `401`, `403`, `404`, `409`, `422`).

---

## Credenciais de Teste (Pré-cadastradas)

| Perfil | E-mail | Senha | Permissões |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin@raizes.com.br` | `123456` | Acesso total, incluindo consulta global e filtrada por canal de pedidos (`GET /pedidos`). |
| **CLIENTE** | `maria@email.com` | `123456` | Consulta de cardápio (`GET /produtos`), criação de pedidos (`POST /pedidos`) e pagamento (`POST /pedidos/{id}/pagamento`). |

---

## Principais Endpoints e Regras de Negócio

* **`POST /auth/login`**: Autentica o usuário e emite o token `Bearer` JWT válido por 1 hora.
* **`GET /produtos`**: Lista o cardápio público da rede.
* **`POST /pedidos`**: Cria um pedido exigindo obrigatoriamente o campo `canalPedido` (`APP`, `TOTEM`, `BALCAO`, `PICKUP`, `WEB`), valida disponibilidade no estoque da unidade selecionada, realiza a baixa transacional (`@Transactional`) e calcula o valor total no servidor.
* **`POST /pedidos/{id}/pagamento`**: Integra com o gateway simulado (`PagamentoMockGateway`), aprovando ou recusando a transação e atualizando o status do pedido para `EM_PREPARO`.
* **`GET /pedidos?canalPedido=TOTEM`**: Filtra pedidos por canal de origem (restrito aos perfis `ADMIN` e `ATENDENTE`, retornando HTTP `403 Forbidden` para `CLIENTE`).

---

## Evidências de Entrega

* **Repositório Git:** https://github.com/LuanSaboia/api-raizes-do-sertao
* **Rota do Swagger Local:** `http://localhost:8080/swagger-ui.html`
* **Coleção de Testes:** `./raizes-do-nordeste-postman.json`