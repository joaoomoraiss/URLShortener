# URL Shortener 🔗

Um encurtador de URLs moderno e completo desenvolvido com Spring Boot, incluindo autenticação de usuários, cache Redis e persistência PostgreSQL.

## 📋 Índice

- [Características](#características)
- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Configuração](#instalação-e-configuração)
- [Executando o Projeto](#executando-o-projeto)
- [API Endpoints](#api-endpoints)
- [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Troubleshooting](#troubleshooting)
- [Deploy AWS](#deploy-aws)

## ✨ Características

- 🔐 **Autenticação completa** com JWT (Access Token e Refresh Token)
- 📧 **Confirmação de email** e recuperação de senha
- 🔗 **Encurtamento de URLs** com códigos únicos
- ⚡ **Cache Redis** para melhor performance
- 🗄️ **PostgreSQL** para persistência de dados
- 🐳 **Docker e Docker Compose** para fácil deployment
- 🔒 **Spring Security** para proteção de endpoints
- 📊 **Hibernate/JPA** para ORM

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 4.1.0-M1**
  - Spring Web MVC
  - Spring Security
  - Spring Data JPA
  - Spring Data Redis
  - Spring Mail
  - Spring Validation
- **PostgreSQL 15**
- **Redis 7**
- **Maven**
- **Docker & Docker Compose**
- **JWT (JSON Web Tokens)**
- **MapStruct** para mapeamento de objetos
- **Lombok** para redução de boilerplate

## 📦 Pré-requisitos

Antes de começar, você precisa ter instalado:

- **Java 21** ou superior
- **Maven 3.9+**
- **Docker** e **Docker Compose**
- **Git**

## ⚙️ Instalação e Configuração

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd URLShortener
```

### 2. Configure as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto ou configure as seguintes variáveis de ambiente:

```properties
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/database
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root

# Redis
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379

# AWS
AWS_REGION=us-east-1

# JWT Secrets
JWT_SECRET_ACCESS=your_secure_access_secret_key_here
JWT_SECRET_REFRESH=your_secure_refresh_secret_key_here
JWT_SECRET_EMAIL=your_secure_email_confirmation_key_here
JWT_SECRET_PASSWORD=your_secure_password_reset_key_here

# Email Configuration (Gmail example)
MAIL_USERNAME=seu-email@gmail.com
MAIL_PASSWORD=sua-senha-de-app

# URLs
CONFIRMATION_URL=http://localhost:8080/auth/confirm-email?token=
RESETPASSWORD_URL=http://localhost:8080/auth/reset-password?token=

# Cookie
COOKIE_SECURE=false
```

### 3. Configuração de Email (Gmail)

Para usar o Gmail como servidor SMTP:

1. Acesse sua conta Google
2. Ative a verificação em duas etapas
3. Gere uma "Senha de app" em https://myaccount.google.com/apppasswords
4. Use essa senha na variável `MAIL_PASSWORD`

## 🐳 Executando o Projeto

### Opção 1: Com Docker Compose (Recomendado)

#### **IMPORTANTE: Remova containers antigos antes de subir novos**

Se você já executou o projeto antes, limpe os containers antigos:

```powershell
# Pare e remova todos os containers
docker-compose down

# Remova containers específicos (se necessário)
docker rm -f postgres_db redis urlshortener

# Opcional: Remova volumes (ATENÇÃO: isso apaga os dados!)
docker-compose down -v
```

#### Execute o projeto:

```powershell
# Suba todos os serviços
docker-compose up -d

# Veja os logs
docker-compose logs -f

# Veja logs de um serviço específico
docker-compose logs -f app
```

A aplicação estará disponível em: `http://localhost:8080`

#### Parar os serviços:

```powershell
docker-compose down
```

### Opção 2: Executar Localmente (Desenvolvimento)

#### 1. Inicie PostgreSQL e Redis com Docker:

```powershell
docker-compose up -d db redis
```

#### 2. Execute a aplicação Spring Boot:

```powershell
# Com Maven Wrapper (Windows)
.\mvnw.cmd spring-boot:run

# Ou compile e execute
.\mvnw.cmd clean package
java -jar target/URLShortener-1.0.0.jar
```

### Opção 3: Build Manual

```powershell
# Compile o projeto
.\mvnw.cmd clean package -DskipTests

# Execute o JAR
java -jar target/URLShortener-1.0.0.jar
```

## 📡 API Endpoints

### Autenticação (`/api/auth`)

#### Registrar Usuário
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

**Resposta:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs..."
}
```
+ Cookie `refreshToken` (HttpOnly)

#### Refresh Token
```http
POST /api/auth/refresh-token
Cookie: refreshToken=<refresh_token>
```

#### Confirmar Email
```http
POST /api/auth/confirm-email/{token}
```

#### Solicitar Recuperação de Senha
```http
POST /api/auth/send-password-reset
Content-Type: application/json

{
  "email": "john@example.com"
}
```

#### Resetar Senha
```http
POST /api/auth/reset-password/{token}
Content-Type: application/json

{
  "newPassword": "NewSecurePass123"
}
```

#### Logout
```http
POST /api/auth/logout
Cookie: refreshToken=<refresh_token>
```

### URLs (`/api/v1/url`)

#### Criar URL Encurtada
```http
POST /api/v1/url
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "originalUrl": "https://www.example.com/very/long/url"
}
```

**Resposta:**
```json
{
  "id": 1,
  "shortCode": "abc123",
  "originalUrl": "https://www.example.com/very/long/url",
  "createdAt": "2026-02-13T10:30:00"
}
```

#### Redirecionar para URL Original
```http
GET /api/v1/url/{shortCode}
```

**Resposta:**
```
https://www.example.com/very/long/url
```

## 🔧 Variáveis de Ambiente

| Variável | Descrição | Padrão | Obrigatória |
|----------|-----------|--------|-------------|
| `SPRING_DATASOURCE_URL` | URL de conexão PostgreSQL | - | ✅ |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | - | ✅ |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | - | ✅ |
| `SPRING_REDIS_HOST` | Host do Redis | `redis` | ✅ |
| `SPRING_REDIS_PORT` | Porta do Redis | `6379` | ❌ |
| `MAIL_USERNAME` | Email SMTP | - | ✅ |
| `MAIL_PASSWORD` | Senha SMTP | - | ✅ |
| `CONFIRMATION_URL` | URL base para confirmação | `http://localhost:8080/auth/confirm-email?token=` | ❌ |
| `RESETPASSWORD_URL` | URL base para reset de senha | `http://localhost:8080/auth/reset-password?token=` | ❌ |
| `COOKIE_SECURE` | Cookie seguro (HTTPS) | `false` | ❌ |

## 🐛 Troubleshooting

### Problema: Container "postgres_db" já existe

**Erro:**
```
Error response from daemon: Conflict. The container name "/postgres_db" is already in use
```

**Solução:**
```powershell
# Remova o container antigo
docker rm -f postgres_db

# Ou remova todos os containers do projeto
docker-compose down
```

### Problema: Conexão com PostgreSQL falha

**Erro:**
```
java.net.UnknownHostException: db
```

**Causas e Soluções:**

1. **Rodando localmente sem Docker:**
   - Altere `SPRING_DATASOURCE_URL` para `jdbc:postgresql://localhost:5432/database`

2. **Container não está na mesma rede:**
   - Certifique-se de usar `docker-compose up` para todos os serviços

3. **PostgreSQL não está pronto:**
   - Aguarde alguns segundos após iniciar o banco
   - Adicione `depends_on` e health checks no docker-compose.yml

### Problema: Redis não conecta

**Solução:**
```powershell
# Verifique se o Redis está rodando
docker ps | grep redis

# Reinicie o Redis
docker-compose restart redis

# Verifique os logs
docker-compose logs redis
```

### Problema: Erro de compilação Maven

**Solução:**
```powershell
# Limpe o cache do Maven
.\mvnw.cmd clean

# Recompile
.\mvnw.cmd clean install -DskipTests
```

### Problema: "Port already in use"

**Solução:**
```powershell
# Descubra qual processo está usando a porta
netstat -ano | findstr :8080

# Mate o processo (substitua PID)
taskkill /F /PID <PID>

# Ou mude a porta no application.properties
server.port=8081
```


## 📝 Estrutura do Projeto

```
URLShortener/
├── src/main/java/com/URLShortener/URLShortener/
│   ├── config/          # Configurações (Redis, Security)
│   ├── controller/      # Controllers REST
│   ├── domain/          # DTOs e Entities
│   ├── exceptions/      # Exceções customizadas
│   ├── mappers/         # MapStruct mappers
│   ├── repositories/    # JPA Repositories
│   ├── security/        # Configurações de segurança
│   └── services/        # Lógica de negócio
├── src/main/resources/
│   └── application.properties
├── docker-compose.yml   # Orquestração Docker
├── Dockerfile           # Build da aplicação
└── pom.xml             # Dependências Maven
```

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença [MIT](LICENSE).

## 📧 Contato

Para dúvidas ou sugestões, abra uma issue no GitHub.

---

**Desenvolvido com ❤️ usando Spring Boot**
