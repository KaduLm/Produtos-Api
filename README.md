# 📦 API de Gerenciamento de Produtos (Spring Boot 2 + JWT)

Esta é uma API REST desenvolvida como parte de um desafio técnico para o cargo de Desenvolvedor Back-End. O projeto foca em organização de código, segurança com JWT e documentação interativa de alta qualidade.

---

## 🛠️ Tecnologias e Ferramentas

* **Framework:** Spring Boot 2.x
* **Linguagem:** Java 17
* **Segurança:** Spring Security + JWT (JSON Web Token)
* **Criptografia:** BCryptPasswordEncoder
* **Banco de Dados:** H2 (In-memory)
* **Persistência:** Spring Data JPA / Hibernate
* **Documentação:** Swagger (OpenAPI 3) & ReDoc
* **Qualidade:** Análise via SonarLint/SonarQube (IntelliJ) e Testes Unitários (JUnit 5/Mockito)

---

## 📋 Pré-requisitos (Como baixar Java e Maven)

Para rodar este projeto, você precisa do Java 17 e do Maven configurados.

### 1. Java 17 (JDK)
* **O que é:** O kit de desenvolvimento necessário para rodar aplicações Java.
* **Como baixar:** Acesse o [Adoptium (OpenJDK 17)](https://adoptium.net/temurin/releases/?version=17) ou o site da [Oracle](https://www.oracle.com/java/technologies/downloads/#java17).
* **Instalação:** Baixe o instalador (.msi para Windows, .pkg para macOS) e siga as instruções padrão.
* **Verificação:** No terminal, digite `java -version`.

### 2. Apache Maven
* **O que é:** Ferramenta que gerencia as bibliotecas e faz o "build" do projeto.
* **Como baixar:** Acesse o site oficial do [Apache Maven](https://maven.apache.org/download.cgi).
* **Instalação:** Extraia o arquivo zip e adicione o caminho da pasta `bin` às Variáveis de Ambiente (PATH) do seu sistema.
* **Verificação:** No terminal, digite `mvn -version`.

---

## 🚀 Como Executar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/KaduLm/Produtos-Api.git](https://github.com/KaduLm/Produtos-Api.git)
    cd Produtos-Api
    ```

2.  **Instale as dependências e compile:**
    ```bash
    mvn clean install
    ```

3.  **Execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```
    *A API estará disponível em: `http://localhost:8080`*

---

## 👥 Usuários para Teste

A API utiliza um script para provisionar automaticamente um usuário administrador no banco H2 ao iniciar:

| Perfil | Login | Senha |
| :--- | :--- | :--- |
| **ADMIN** | `admin@exemplo.com` | `admin123` |

---

## 📡 Testando os Endpoints

### 1. Importar no Insomnia
Disponibilizei uma collection completa na pasta `Endpoints Insomnia`.
* Abra o **Insomnia** > **Create** > **Import**.
* Selecione o arquivo JSON dentro da pasta do projeto.
* Clique em **Scan** e **Import**.


---

## 📖 Documentação da API

O projeto gera automaticamente duas interfaces de documentação:

### Swagger UI (Interativo)
Ideal para testar os endpoints em tempo real.
* **🔗 Link:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### ReDoc (Visualização Técnica)
Focada na leitura limpa da estrutura da API.
* **🔗 Link:** [http://localhost:8080/redoc](http://localhost:8080/redoc.html)

---

## 🧪 Testes e Qualidade

### Executar Testes Unitários
Para validar as regras de negócio e a integridade dos Controllers e Services:
```bash
mvn test
