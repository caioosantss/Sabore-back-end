# 🍳 Saborê - Plataforma de Compartilhamento de Receitas

> *Receitas que aproximam. Descubra, salve e compartilhe suas receitas favoritas.*

![Status](https://img.shields.io/badge/Status-Completo%20e%20Deployado-success)
![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-green)
![Next.js](https://img.shields.io/badge/Next.js-16-black)
![Security](https://img.shields.io/badge/Security-BCrypt%20%2B%20JWT-brightgreen)
![License](https://img.shields.io/badge/License-MIT-yellow)

| | Link |
|---|---|
| 🌐 **Aplicação (Front-end)** | https://sabore-gamma.vercel.app/ |
| ⚙️ **API (Back-end)** | https://sabore-back-end.onrender.com |
| 💻 **Repositório do Front-end** | https://github.com/caioosantss/sabore-front-end |
| 💻 **Repositório do Back-end** | https://github.com/caioosantss/Sabore-back-end |

> ⏳ **Aviso:** o back-end roda no plano gratuito do Render. Se ficar um tempo sem receber acessos, a primeira requisição pode levar cerca de 50 segundos para responder. Um monitor no UptimeRobot (veja [Monitoramento](#-monitoramento)) ajuda a reduzir isso.

---

## 📋 Sumário

1. [Sumário Executivo](#-sumário-executivo)
2. [Funcionalidades](#-principais-funcionalidades)
3. [Arquitetura e Infraestrutura](#-arquitetura-e-infraestrutura)
4. [Estrutura do Projeto](#-estrutura-do-projeto)
5. [Segurança: BCrypt e JWT](#-segurança-bcrypt-e-jwt)
6. [Banco de Dados](#-banco-de-dados---schema)
7. [Variáveis de Ambiente](#-variáveis-de-ambiente)
8. [Endpoints da API](#-endpoints-da-api)
9. [Autenticação, CORS e Níveis de Acesso](#-autenticação-cors-e-níveis-de-acesso)
10. [Como Executar Localmente](#-como-executar-localmente)
11. [Deploy em Produção](#-deploy-em-produção)
12. [Monitoramento](#-monitoramento)
13. [Testes](#-testes)
14. [Problemas Encontrados e Resolvidos](#-problemas-encontrados-e-resolvidos)
15. [Melhorias e Roadmap](#-melhorias-implementadas)

---

## 📋 Sumário Executivo

**Saborê** é uma plataforma web full-stack desenvolvida como projeto final do curso SENAI, focada no compartilhamento e descoberta de receitas culinárias. A aplicação permite que usuários explorem receitas diversas, salvem suas favoritas e encontrem inspiração culinária para o dia a dia.

### 🎯 Objetivo do Projeto
Criar uma experiência intuitiva e responsiva para descoberta de receitas, permitindo personalização através de favoritos e oferecendo filtros eficientes por categoria, tempo de preparo e popularidade.

### 🔐 Segurança em Primeiro Lugar
As senhas são protegidas com **BCrypt** (nunca ficam em texto puro no banco) e as rotas protegidas usam **JWT** com controle de acesso por perfil (`USER` e `ADMIN`).

---

## ✨ Principais Funcionalidades

### 👤 Para Usuários Não Autenticados
- 👀 Visualizar receitas em destaque
- 🔍 Explorar todas as receitas disponíveis
- 📝 Criar conta (cadastro com senha criptografada)
- 🔐 Fazer login seguro

### ❤️ Para Usuários Autenticados
- **Favoritos** - Salvar receitas que você ama
- **Queridinhos** - Destaques da semana (receitas mais favoritadas)
- **Receitas Rápidas** - Filtrar receitas de até 30 minutos
- **Receitas Salgadas** - Explorar pratos principais e acompanhamentos
- **Receitas Doces** - Encontrar sobremesas e doces
- **Descobrir** - Visualizar todas as receitas do catálogo

### 🛠️ Para Administradores
- ✏️ Criar, editar e deletar receitas
- 🖼️ Upload de imagens das receitas (via Supabase Storage)
- 👥 Gerenciar usuários
- 📊 Visualizar todas as contas registradas

---

## 🏗️ Arquitetura e Infraestrutura

```
                       ┌──────────────────────┐
                       │      Navegador       │
                       └──────────┬───────────┘
                                  │ HTTPS
                       ┌──────────▼───────────┐
                       │   Front-end (Vercel) │
                       │  Next.js + React     │
                       └──────────┬───────────┘
                                  │ HTTPS + JWT (Authorization: Bearer)
                                  │ CORS liberado só para o front
                       ┌──────────▼───────────┐        ┌───────────────┐
 ┌──────────────┐ ping │  Back-end (Render)   │ upload │   Supabase    │
 │ UptimeRobot  ├─────▶│  Spring Boot 4 / J21 ├───────▶│   Storage     │
 │ (monitor)    │      │  Spring Security     │        │  (imagens)    │
 └──────────────┘      │  + BCrypt + JWT      │        └───────────────┘
                       └──────────┬───────────┘
                                  │ JDBC (SSL)
                       ┌──────────▼───────────┐
                       │  MySQL (Aiven)       │
                       └──────────────────────┘
```

### Onde cada parte está hospedada

| Camada | Serviço | Observações |
|--------|---------|-------------|
| **Front-end** | **Vercel** | Deploy automático a cada push na `main` |
| **Back-end** | **Render** | Deploy via `Dockerfile` (build Maven → JRE 21) |
| **Banco de dados** | **Aiven (MySQL)** | Conexão com SSL; tabelas criadas pelo Hibernate (`ddl-auto=update`) |
| **Imagens** | **Supabase Storage** | Bucket público `receitas` |
| **Monitoramento** | **UptimeRobot** | Verifica a disponibilidade da API periodicamente |
| **Testes de API** | **Postman** | Testes de endpoints do ambiente local à produção |

### Front-end
- **Framework**: Next.js 16 (React 19) com TypeScript
- **Hospedagem**: Vercel (https://sabore-gamma.vercel.app/)
- **Comunicação com a API**: `fetch` centralizado em `lib/api.ts`, usando a variável `NEXT_PUBLIC_API_URL`
- **Sessão**: token JWT guardado no `localStorage` e enviado em `Authorization: Bearer {token}`

### Back-end
- **Framework**: Spring Boot 4.1.1
- **Linguagem**: Java 21
- **Build**: Maven (com Maven Wrapper)
- **Segurança**: Spring Security + BCrypt + JWT (jjwt 0.12.6)
- **Hospedagem**: Render (container Docker)
- **Porta**: `8080` local; em produção o Render injeta a variável `PORT`
- **Arquitetura**: MVC + Service Layer

### Banco de Dados
- **SGBD**: MySQL
- **Produção**: Aiven (MySQL gerenciado, conexão SSL)
- **Local**: `localhost:3306`
- **Estratégia**: JPA/Hibernate com `ddl-auto=update`

### Armazenamento de Arquivos
- **Serviço**: Supabase Storage
- **Bucket**: `receitas` (público)
- **Tipos permitidos**: JPG, PNG, WebP e GIF
- **Tamanho máximo**: 5 MB por arquivo

### Autenticação & Autorização
- **Padrão**: JWT (JSON Web Tokens)
- **Algoritmo**: HS256 (HMAC SHA-256)
- **Duração do token**: 7 dias (604800000 ms)
- **Senhas**: BCrypt (strength 10)
- **Perfis**: `USER`, `ADMIN`
- **Validação**: header `Authorization: Bearer {token}`

---

## 📁 Estrutura do Projeto

### Back-end (Java / Spring Boot)

```
Sabore-back-end/
├── .mvn/                        # Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/com/projeto_final/receitas/
│   │   │   ├── ReceitasApplication.java
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── AdminSeeder.java        # Cria o 1º administrador na subida
│   │   │   │   ├── WebConfig.java          # CORS do Spring MVC
│   │   │   │   └── Securiryconfig.java     # Bean do PasswordEncoder (BCrypt)
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── receitaController.java
│   │   │   │   ├── UsuarioController.java  # /auth/* e /usuarios
│   │   │   │   ├── FavoritoController.java
│   │   │   │   └── AdministradorController.java
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── receitasService.java
│   │   │   │   ├── UsuarioService.java
│   │   │   │   ├── FavoritoService.java
│   │   │   │   └── AdministradorService.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── receitaRepository.java
│   │   │   │   ├── UsuarioRepository.java
│   │   │   │   ├── FavoritoRepository.java
│   │   │   │   └── AdministradorRepository.java
│   │   │   │
│   │   │   ├── entity/
│   │   │   │   ├── Receita.java
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Favorito.java
│   │   │   │   └── Administrador.java
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── RecipeResponse.java
│   │   │   │   ├── UserResponse.java
│   │   │   │   └── LoginResponse.java
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── SecurityConfig.java     # Regras de acesso + CORS do Security
│   │   │   │   ├── JwtService.java
│   │   │   │   ├── JwtAuthFilter.java
│   │   │   │   ├── CurrentUser.java
│   │   │   │   └── AuthenticatedUser.java
│   │   │   │
│   │   │   ├── storage/
│   │   │   │   └── SupabaseStorageService.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── globalExceptionHandler.java
│   │   │   │   ├── businessException.java
│   │   │   │   ├── forbiddenException.java
│   │   │   │   ├── unauthorizedException.java
│   │   │   │   ├── resourceNotFoundException.java
│   │   │   │   └── standardError.java
│   │   │   │
│   │   │   └── ChaveComposta/
│   │   │       └── FavoritoId.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/java/.../ReceitasApplicationTests.java
│
├── Dockerfile                   # Build usado no deploy do Render
├── pom.xml
├── mvnw / mvnw.cmd
├── SETUP.md
├── .env.example
└── scripts/testar-supabase.sh   # Valida a integração com o Supabase
```

### Front-end (Next.js / TypeScript)

```
sabore-front-end/
├── app/
│   ├── layout.tsx
│   ├── page.tsx                 # Home: lista de receitas e filtros
│   ├── login/page.tsx
│   ├── cadastro/page.tsx
│   └── globals.css
├── components/
│   ├── Header.tsx
│   ├── Sidebar.tsx              # Descobrir, Favoritos, Queridinhos...
│   ├── RecipeCard.tsx
│   ├── RecipeDetail.tsx
│   └── RecipeForm.tsx           # Criar/editar receita (admin)
├── lib/
│   ├── api.ts                   # Cliente HTTP da API
│   └── auth-context.tsx         # Sessão / token JWT
├── types/
│   ├── auth.ts
│   └── recipe.ts
├── .env.example                 # Modelo das variáveis
└── .env.local                   # Valores locais (NÃO vai para o Git)
```

---

## 🔐 Segurança: BCrypt e JWT

### O que é BCrypt?

BCrypt é um algoritmo de hash de senhas, baseado no Blowfish, projetado para ser resistente a ataques de força bruta:

- ✅ **Salt automático**: cada hash inclui um valor aleatório, então senhas iguais geram hashes diferentes
- ✅ **Custo adaptável**: o número de iterações pode ser aumentado conforme o hardware evolui
- ✅ **Custo configurável**: strength 10 (2^10 = 1024 rodadas) equilibra segurança e desempenho
- ✅ **Padrão amplamente adotado** para armazenamento de senhas

### Implementação no Saborê

#### 1. Configuração (`Securiryconfig.java`)

```java
@Configuration
public class Securiryconfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // strength 10 por padrão
    }
}
```

#### 2. Criptografia na criação de usuário (`UsuarioService`)

```java
@Transactional
public Usuario create(Usuario obj) {
    validar(obj);

    String email = obj.getEmail().trim().toLowerCase(Locale.ROOT);

    if (repository.findByEmail(email).isPresent()) {
        throw new businessException("Este e-mail já está cadastrado.");
    }

    obj.setId(null);
    obj.setName(obj.getName().trim());
    // ⭐ A senha é criptografada com BCrypt antes de ir ao banco
    obj.setPassword(passwordEncoder.encode(obj.getPassword()));
    obj.setEmail(email);

    return repository.save(obj);
}
```

**O que acontece:**
1. O usuário envia `password = "minha_senha_123"`
2. O BCrypt gera um salt aleatório
3. Aplica 2^10 rodadas de custo
4. Resulta em algo como `$2a$10$...` (60 caracteres)
5. Somente esse hash é salvo no banco

#### 3. Validação no login (`UsuarioService`)

```java
@Transactional(readOnly = true)
public Usuario login(String email, String senha) {

    if (email == null || senha == null) {
        throw new unauthorizedException("Informe e-mail e senha.");
    }

    String emailNormalizado = email.trim().toLowerCase(Locale.ROOT);
    Optional<Usuario> usuario = repository.findByEmail(emailNormalizado);

    if (usuario.isPresent() &&
        // ⭐ Compara a senha digitada com o hash salvo
        passwordEncoder.matches(senha, usuario.get().getPassword())) {
        return usuario.get();
    }

    throw new unauthorizedException("E-mail ou senha incorretos.");
}
```

### Fluxo completo de segurança

```
REGISTRO                                   LOGIN
────────                                   ─────
senha em texto puro (via HTTPS)            e-mail + senha (via HTTPS)
        │                                          │
validar(): nome, e-mail com @,             busca usuário por e-mail
senha com mínimo de 6 caracteres                   │
        │                                  passwordEncoder.matches(senha, hash)
passwordEncoder.encode(senha)                      │
        │                                  se confere → JwtService.generateToken()
salva o hash no MySQL (Aiven)                      │
        │                                  front guarda o token e envia
responde 201 com os dados do usuário       Authorization: Bearer {token}
(sem a senha)                                      │
                                           JwtAuthFilter valida o token em cada
                                           requisição (inválido → 401)
```

> ℹ️ O pacote `spring-boot-starter-security` traz o `BCryptPasswordEncoder`, mas o **bean `PasswordEncoder` é declarado no projeto**, em `Securiryconfig.java`. O CSRF está desabilitado porque a API é stateless (JWT), sem cookies de sessão.

---

## 📊 Banco de Dados - Schema

O MySQL fica no **Aiven**. As tabelas são criadas e atualizadas pelo Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

A herança de `Usuario` usa a estratégia `JOINED`: `Administrador` é uma tabela própria ligada à de usuários pelo mesmo `id`.

```sql
-- Usuários (comuns e administradores)
CREATE TABLE tb_usuario (
  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  name      VARCHAR(255),
  email     VARCHAR(255) UNIQUE,
  password  VARCHAR(255)          -- hash BCrypt (60 caracteres)
);

-- Administradores (herdam de tb_usuario)
CREATE TABLE tb_administrador (
  id     BIGINT PRIMARY KEY,
  token  VARCHAR(255),
  FOREIGN KEY (id) REFERENCES tb_usuario(id)
);

-- Receitas
CREATE TABLE receita (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  nome       VARCHAR(255) UNIQUE NOT NULL,
  descricao  VARCHAR(1000) NOT NULL,
  tempo      INT NOT NULL,           -- minutos
  url        VARCHAR(255),           -- URL da imagem no Supabase
  categoria  VARCHAR(255)            -- "doce" ou "salgada"
);

-- Favoritos (chave composta)
CREATE TABLE tb_favorito (
  usuario_id       BIGINT,
  receita_id       BIGINT,
  data_adicionado  DATETIME,
  PRIMARY KEY (usuario_id, receita_id),
  FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id),
  FOREIGN KEY (receita_id) REFERENCES receita(id)
);
```

> O SQL acima é ilustrativo (o Hibernate gera o DDL real a partir das entidades). A categoria é guardada em minúsculas e a API a devolve como `Doce` / `Salgada`.

---

## 🔑 Variáveis de Ambiente

### Back-end (Render)

Configure em **Render → Environment**. Localmente, exporte no terminal ou defina nas configurações de execução da sua IDE.

```properties
# ========== BANCO DE DADOS (Aiven MySQL) ==========
# URL JDBC completa. O Aiven exige SSL.
MYSQLPROD_URL=jdbc:mysql://<host>.aivencloud.com:<porta>/<banco>?sslMode=REQUIRED
MYSQLUSERPROD_USER=seu_usuario
MYSQLPROD_PASSWORD=sua_senha

# ========== JWT ==========
# OBRIGATÓRIO em produção: string longa e aleatória (32+ caracteres)
JWT_SECRET=gere-uma-chave-longa-e-aleatoria
JWT_EXPIRATION_MS=604800000          # 7 dias

# ========== CORS ==========
# Origens autorizadas, separadas por vírgula, SEM barra no final.
# Aceita curinga. Se não definida, vale: http://localhost:3000,https://*.vercel.app
CORS_ALLOWED_ORIGINS=http://localhost:3000,https://sabore-gamma.vercel.app

# ========== SUPABASE STORAGE ==========
SUPABASE_URL=https://seu-projeto.supabase.co
SUPABASE_SERVICE_KEY=chave-service-role     # nunca exponha no front
SUPABASE_BUCKET=receitas

# ========== ADMIN INICIAL ==========
# Obrigatórias: o administrador é criado na subida se o e-mail não existir
ADMIN_NAME=Administrador
ADMIN_EMAIL=admin@seudominio.com
ADMIN_PASSWORD=senha_inicial_forte

# ========== SERVIDOR ==========
# O Render injeta PORT sozinho; localmente o padrão é 8080
```

> ⚠️ Se `CORS_ALLOWED_ORIGINS` existir no painel do Render, ela **sobrescreve** o valor padrão do código.

### Front-end (Vercel)

| Variável | Valor | Tipo na Vercel |
|----------|-------|----------------|
| `NEXT_PUBLIC_API_URL` | `https://sabore-back-end.onrender.com` | **Config** |

Pontos importantes:
- Use a URL **sem barra no final**.
- O prefixo **`NEXT_PUBLIC_` é obrigatório**: o Next.js só envia ao navegador as variáveis que começam com ele.
- Como o valor fica embutido no código gerado no build, **mudar a variável exige um novo deploy**.
- Na Vercel, escolha o tipo **Config** (e não *Secret*): a URL da API é pública e o valor precisa ser legível.
- **Nunca** coloque senhas ou chaves secretas em variáveis `NEXT_PUBLIC_`.

Para rodar localmente, copie o modelo e preencha:

```bash
cp .env.example .env.local
# .env.local
NEXT_PUBLIC_API_URL=http://localhost:8080
```

> O Next.js lê o `.env.local`, e não o `.env.example`. O `.env.local` não vai para o Git.

---

## 🔗 Endpoints da API

### Base URL
- **Produção**: `https://sabore-back-end.onrender.com`
- **Local**: `http://localhost:8080`

### Autenticação

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/auth/register` | Criar conta (senha com BCrypt) | Pública |
| `POST` | `/auth/login` | Fazer login (retorna o JWT) | Pública |
| `GET` | `/auth/me` | Dados do usuário logado | 🔒 Logado |

**Registrar**
```http
POST /auth/register
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@example.com",
  "password": "minhasenhaSuperSegura123"
}
```
```json
// 201 Created
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com",
  "role": "USER"
}
```

**Login**
```http
POST /auth/login
Content-Type: application/json

{
  "email": "joao@example.com",
  "password": "minhasenhaSuperSegura123"
}
```
```json
// 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "user": {
    "id": 1,
    "name": "João Silva",
    "email": "joao@example.com",
    "role": "USER"
  }
}
```

### Receitas

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `GET` | `/recipes` | Listar receitas (aceita `?filter=`) | Pública |
| `GET` | `/recipes/{id}` | Detalhe de uma receita | Pública |
| `POST` | `/recipes` | Criar receita | 🛡️ Admin |
| `PUT` | `/recipes/{id}` | Atualizar receita | 🛡️ Admin |
| `DELETE` | `/recipes/{id}` | Excluir receita | 🛡️ Admin |

**Filtros de `GET /recipes?filter=...`**

| Valor | Resultado | Menu no front |
|-------|-----------|---------------|
| *(vazio)* | Todas as receitas | Descobrir |
| `favorites` | Favoritas do usuário logado | Favoritos |
| `weekly` | Mais favoritadas | Queridinhos |
| `quick` | Até 30 minutos | Receitas rápidas |
| `savory` | Categoria salgada | Salgadas |
| `sweet` | Categoria doce | Doces |

**Formato de resposta de uma receita**
```json
{
  "id": 1,
  "title": "Bolo de chocolate",
  "description": "Ingredientes... Modo de preparo...",
  "imageUrl": "https://<projeto>.supabase.co/storage/v1/object/public/receitas/....webp",
  "prepTime": 60,
  "category": "Doce",
  "favorite": false
}
```

**Criar / editar receita** usa `multipart/form-data`:

| Campo | Tipo | Obrigatório |
|-------|------|-------------|
| `nome` | texto | ✅ |
| `desc` | texto | ✅ |
| `tempo` | número (minutos) | ✅ |
| `categoria` | texto (`doce` / `salgada`) | ❌ |
| `imagem` | arquivo (JPG, PNG, WebP, GIF até 5 MB) | ❌ |

### Favoritos

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `GET` | `/favoritos` | Listar favoritos do usuário | 🔒 Logado |
| `POST` | `/favoritos/{receitaId}` | Adicionar aos favoritos | 🔒 Logado |
| `DELETE` | `/favoritos/{receitaId}` | Remover dos favoritos | 🔒 Logado |

> O usuário dos favoritos sempre vem do **token JWT**, nunca da URL. Assim ninguém altera a lista de outra pessoa trocando um id.

### Usuários e administração

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `GET` | `/usuarios` | Listar todos os usuários | 🛡️ Admin |
| `GET` / `PUT` / `DELETE` | `/usuarios/{id}` | Ver, editar ou excluir conta | 🔒 Dono da conta ou 🛡️ Admin |
| `*` | `/administrador/**` | Gestão de administradores | 🛡️ Admin |
| `GET` | `/actuator/health` | Verificação de saúde (usada no monitoramento) | Pública |

---

## 🔒 Autenticação, CORS e Níveis de Acesso

### 1. Validação de entrada (cadastro)

- ✅ Nome obrigatório
- ✅ E-mail deve conter `@`
- ✅ Senha com no mínimo 6 caracteres
- ✅ E-mail normalizado (trim + minúsculas) e único

### 2. Estrutura do token JWT

```
Header:  { "alg": "HS256", "typ": "JWT" }

Payload: {
  "sub": "usuario@example.com",   // e-mail
  "userId": 1,
  "role": "USER",                 // USER ou ADMIN
  "iat": ...,
  "exp": ...                      // 7 dias
}

Signature: HMAC-SHA256(JWT_SECRET)
```

### 3. CORS (Cross-Origin Resource Sharing)

O navegador só deixa o front (Vercel) chamar a API (Render) se a API autorizar a origem. As origens permitidas vêm da propriedade `app.cors.allowed-origins`, alimentada pela variável `CORS_ALLOWED_ORIGINS`:

```properties
app.cors.allowed-origins=${CORS_ALLOWED_ORIGINS:http://localhost:3000,https://*.vercel.app}
```

- O `SecurityConfig` lê essa propriedade e usa `setAllowedOriginPatterns`, que aceita curinga.
- O curinga `https://*.vercel.app` cobre produção e os deploys de preview, mas também libera qualquer outro site `.vercel.app`. **Em produção, prefira o domínio exato:** `CORS_ALLOWED_ORIGINS=https://sabore-gamma.vercel.app`.
- As origens não podem ter barra no final.
- A API é chamada com `Authorization: Bearer` (sem cookies), por isso `allowCredentials` não é necessário.

> 💡 O Postman e o `curl` **não aplicam CORS**. Uma API que funciona neles pode ainda assim ser bloqueada pelo navegador.

### 4. Fluxo de autenticação

```
1. Usuário faz login ou cadastro
2. O front envia as credenciais via HTTPS
3. O back valida a senha com BCrypt
4. Se válido, gera um JWT e devolve ao front
5. O front guarda o token (localStorage)
6. Nas próximas chamadas: Authorization: Bearer {token}
7. JwtAuthFilter valida o token
8. Token ausente/inválido em rota protegida → 401; sem permissão → 403
```

### 5. Níveis de acesso

| Endpoint | Público | User | Admin |
|----------|:------:|:----:|:-----:|
| `GET /recipes`, `GET /recipes/{id}` | ✅ | ✅ | ✅ |
| `POST/PUT/DELETE /recipes` | ❌ | ❌ | ✅ |
| `GET/POST/DELETE /favoritos` | ❌ | ✅ | ✅ |
| `GET /auth/me` | ❌ | ✅ | ✅ |
| `GET /usuarios` | ❌ | ❌ | ✅ |
| `GET/PUT/DELETE /usuarios/{id}` | ❌ | só a própria conta | ✅ |
| `/administrador/**` | ❌ | ❌ | ✅ |
| `GET /actuator/health` | ✅ | ✅ | ✅ |

---

## 🚀 Como Executar Localmente

### Pré-requisitos
- Java 21+
- Maven 3.6+ (ou o `./mvnw` do projeto)
- MySQL 8.0+
- Node.js 20+ e npm

### Back-end

```bash
git clone https://github.com/caioosantss/Sabore-back-end.git
cd Sabore-back-end
```

**1. Crie o banco**
```sql
CREATE DATABASE receitas;
```

**2. Defina as variáveis de ambiente** (o back-end não sobe sem as de banco e de admin)
```bash
export MYSQLPROD_URL="jdbc:mysql://localhost:3306/receitas"
export MYSQLUSERPROD_USER=root
export MYSQLPROD_PASSWORD=root
export JWT_SECRET=uma-chave-longa-e-aleatoria-com-32-ou-mais-caracteres
export CORS_ALLOWED_ORIGINS=http://localhost:3000
export ADMIN_NAME=Administrador
export ADMIN_EMAIL=admin@localhost.com
export ADMIN_PASSWORD=admin123
# opcional (upload de imagens):
# export SUPABASE_URL=... SUPABASE_SERVICE_KEY=... SUPABASE_BUCKET=receitas
```

**3. Rode**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

API em `http://localhost:8080`.

### Front-end

```bash
git clone https://github.com/caioosantss/sabore-front-end.git
cd sabore-front-end
npm install
cp .env.example .env.local
```

Edite o `.env.local`:
```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

```bash
npm run dev
```

Aplicação em `http://localhost:3000`. Se alterar o `.env.local`, reinicie o servidor.

---

## ☁️ Deploy em Produção

O fluxo de deploy usa três serviços independentes, que só se conectam em tempo de execução, pelas URLs:

```
GitHub ──push──▶ Vercel (build do front)  ──┐
GitHub ──push──▶ Render (build do Docker) ──┼─▶ navegador → Vercel → Render → Aiven
Aiven (MySQL gerenciado, sempre ativo)    ──┘
```

### 1. Banco de dados: Aiven
1. Crie um serviço **MySQL** no Aiven.
2. Pegue host, porta, banco, usuário e senha na tela de conexão.
3. Monte a `MYSQLPROD_URL` com SSL: `jdbc:mysql://<host>:<porta>/<banco>?sslMode=REQUIRED`.
4. Se o serviço tiver lista de IPs permitidos, libere o acesso do Render.

### 2. Back-end: Render
1. Crie um **Web Service** conectado ao repositório do back-end (linguagem **Docker**, usando o `Dockerfile` da raiz).
2. Cadastre **todas** as variáveis da seção [Variáveis de Ambiente](#-variáveis-de-ambiente), incluindo um `JWT_SECRET` forte.
3. A cada push na `main`, o Render refaz o build e publica.
4. Confirme em `https://sabore-back-end.onrender.com/recipes` (deve devolver JSON).

### 3. Front-end: Vercel
1. Importe o repositório do front-end.
2. Em **Settings → Environment Variables**, crie `NEXT_PUBLIC_API_URL` (tipo **Config**) com a URL do Render, sem barra no final.
3. A cada push na `main`, a Vercel gera um novo deploy.

### ⚠️ Cuidados no deploy
- **Redeploy ≠ novo código.** O botão *Redeploy* da Vercel reconstrói **o mesmo commit** do deploy escolhido. Para publicar alterações, faça `git push`.
- **A variável `NEXT_PUBLIC_*` é lida no build.** Mudou o valor? Gere um deploy novo.
- **Atualize o domínio no CORS** do back-end se o endereço do front mudar.
- **Plano gratuito do Render:** o serviço "dorme" após um período sem acessos. Veja a seção abaixo.

---

## 📡 Monitoramento

O back-end é monitorado com o **UptimeRobot**:

- Faz requisições periódicas à API e registra se ela está no ar.
- Pode avisar (e-mail ou outro canal) quando o serviço fica indisponível.
- Como gera tráfego constante, também ajuda a evitar que o plano gratuito do Render coloque o serviço para dormir, o que reduz o tempo da primeira resposta.

A rota `GET /actuator/health` é pública e leve, adequada para esse tipo de verificação.

---

## 🧪 Testes

### Testes de API com Postman

Os endpoints foram testados com o **Postman** em todas as etapas do desenvolvimento:

| Etapa | Ambiente | Base URL |
|-------|----------|----------|
| 1. Desenvolvimento | API e MySQL locais | `http://localhost:8080` |
| 2. Produção | API no Render e banco no Aiven | `https://sabore-back-end.onrender.com` |
| 3. Integração | Front na Vercel consumindo a API | `https://sabore-gamma.vercel.app/` |

Roteiro de testes dos endpoints:

1. `POST /auth/register` → cria a conta (201)
2. `POST /auth/login` → retorna o `token`
3. `GET /auth/me` com `Authorization: Bearer {token}` → dados do usuário
4. `GET /recipes` (com e sem `?filter=`) → listagem
5. `POST /recipes` (multipart), `PUT` e `DELETE` com token de **admin**
6. Mesmas operações com token de **usuário comum** → deve responder 403
7. `GET`, `POST` e `DELETE` em `/favoritos`
8. Acesso sem token a rota protegida → 401

> Dica: use uma variável de ambiente no Postman (`baseUrl`) para alternar entre local e produção sem editar cada requisição.

> ℹ️ O Postman não aplica CORS. A integração com o navegador é validada na etapa 3.

### Testes automatizados

```bash
./mvnw test
```

### Teste manual com curl

```bash
BASE=https://sabore-back-end.onrender.com

# 1. Registrar
curl -X POST $BASE/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Teste User","email":"teste@example.com","password":"testeSenha123"}'

# 2. Login (guarde o token da resposta)
curl -X POST $BASE/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"teste@example.com","password":"testeSenha123"}'

# 3. Rota protegida
curl -H "Authorization: Bearer SEU_TOKEN" $BASE/auth/me
```

---

## 🐛 Problemas Encontrados e Resolvidos

Registro dos problemas da integração entre front, back-end e hospedagem, para consulta futura.

| Sintoma | Causa | Solução |
|---------|-------|---------|
| Front mostrava "Não foi possível falar com o servidor", mesmo com o Postman funcionando | **CORS**: o `SecurityConfig` tinha uma lista fixa de origens (`localhost:5173` e `seu-front.com`) que se sobrepunha à do `WebConfig` | `SecurityConfig` passou a ler `app.cors.allowed-origins`, a mesma propriedade do `WebConfig` 
| "CONFIG não foi configurada" no site publicado | Variável sem o prefixo `NEXT_PUBLIC_` (o Next.js não a envia ao navegador) | Voltar ao nome `NEXT_PUBLIC_API_URL` no código, no `.env.local` e na Vercel 
| Erro de configuração aparecia como "falha de conexão" | `getApiUrl()` era chamada dentro do `try/catch` do `fetch`, que escondia o erro real | Mover a chamada para fora do `try` e registrar a URL com falha no console |
| Primeira requisição muito lenta | Plano gratuito do Render "dorme" sem acessos | Monitor no UptimeRobot e aviso na documentação |

---

## ✨ Melhorias Implementadas

| Recurso | Status | Detalhes |
|---------|:------:|----------|
| **BCrypt** | ✅ | Strength 10, salt aleatório |
| **Spring Security** | ✅ | Regras por rota e por perfil |
| **JWT** | ✅ | HS256, 7 dias, claims `userId` e `role` |
| **Validação de cadastro** | ✅ | Nome, e-mail e senha mínima |
| **CORS configurável** | ✅ | Por variável de ambiente, com curinga |
| **Normalização de e-mail** | ✅ | Trim + minúsculas |
| **Tratamento de exceções** | ✅ | Handler centralizado |
| **Upload de imagens** | ✅ | Supabase Storage (até 5 MB) |
| **Deploy em nuvem** | ✅ | Vercel + Render + Aiven |
| **Monitoramento** | ✅ | UptimeRobot |
| **Testes de endpoints** | ✅ | Postman, do local à produção |

### Checklist de produção

- [ ] `JWT_SECRET` definido no Render (sem ele vale um segredo padrão que está no código-fonte)
- [ ] `CORS_ALLOWED_ORIGINS` restrito ao domínio do front
- [ ] `ADMIN_PASSWORD` forte e trocada após o primeiro acesso
- [ ] Chave `SUPABASE_SERVICE_KEY` apenas no back-end (nunca no front)


## 📄 Licença

Este projeto está licenciado sob a Licença MIT.

---

<div align="center">

**Desenvolvido como projeto final do meu curso de qualificação como programador back-end (SENAI)**

*Saborê - Receitas que aproximam*

![Java](https://img.shields.io/badge/-Java%2021-blue?style=flat-square&logo=java)
![Spring](https://img.shields.io/badge/-Spring%20Boot-green?style=flat-square&logo=spring)
![Security](https://img.shields.io/badge/-Spring%20Security-brightgreen?style=flat-square&logo=spring)
![MySQL](https://img.shields.io/badge/-MySQL-blue?style=flat-square&logo=mysql)
![Aiven](https://img.shields.io/badge/-Aiven-orange?style=flat-square)
![Next.js](https://img.shields.io/badge/-Next.js-black?style=flat-square&logo=next.js)
![Render](https://img.shields.io/badge/-Render-46E3B7?style=flat-square&logo=render&logoColor=black)
![Vercel](https://img.shields.io/badge/-Vercel-black?style=flat-square&logo=vercel)
![Supabase](https://img.shields.io/badge/-Supabase-3ECF8E?style=flat-square&logo=supabase&logoColor=white)
![UptimeRobot](https://img.shields.io/badge/-UptimeRobot-green?style=flat-square)
![Postman](https://img.shields.io/badge/-Postman-FF6C37?style=flat-square&logo=postman&logoColor=white)

*Última atualização: Outubro de 2026*

</div>
