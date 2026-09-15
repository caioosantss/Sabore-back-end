# 🍳 Saborê - Plataforma de Compartilhamento de Receitas

> *Receitas que aproximam. Descubra, salve e compartilhe suas receitas favoritas.*

![Status](https://img.shields.io/badge/Status-Completo%20e%20Deployado-success)
![Java](https://img.shields.io/badge/Java-21-blue)
![Spring%20Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-green)
![Security](https://img.shields.io/badge/Security-BCrypt-brightgreen)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📋 Sumário Executivo

**Saborê** é uma plataforma web full-stack desenvolvida como projeto final do curso SENAI, focada no compartilhamento e descoberta de receitas culinárias. A aplicação permite que usuários explorem receitas diversas, salvem suas favoritas e encontrem inspiração culinária para o dia a dia.

### 🎯 Objetivo do Projeto
Criar uma experiência intuitiva e responsiva para descoberta de receitas, permitindo personalização através de favoritos e oferecendo filtros eficientes por categoria, tempo de preparo e popularidade.

### 🔐 Segurança em Primeiro Lugar
A aplicação implementa **criptografia BCrypt** para proteção de senhas, garantindo que as credenciais dos usuários sejam armazenadas de forma segura e nunca em texto puro.

---

## ✨ Principais Funcionalidades

### 👤 Para Usuários Não Autenticados
- 👀 Visualizar receitas em destaque
- 🔍 Explorar todas as receitas disponíveis
- 📝 Criar conta (Cadastro com senha criptografada)
- 🔐 Fazer login seguro

### ❤️ Para Usuários Autenticados
- **Favoritos** - Salvar receitas que você ama
- **Queridinhos** - Acessar destaques da semana (mais favoritadas)
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

## 🏗️ Arquitetura Técnica

### Stack Tecnológico

```
┌─────────────────────────────────────────────────────────────┐
│                       SABORÊ - Arquitetura                   │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  Frontend                Backend                  Banco      │
│  ┌────────────┐        ┌──────────────┐       ┌─────────┐   │
│  │  Next.js   │─HTTP──▶│ Spring Boot  │──────▶│ MySQL   │   │
│  │  Vercel    │◀─────── │  Java 21     │       │ Seguro  │   │
│  └────────────┘  JWT    │  + Spring    │       └─────────┘   │
│                  (HS256) │  Security    │                     │
│                         │  + BCrypt    │                     │
│                         └──────────────┘                     │
│                               │                              │
│                               │                              │
│                         ┌─────▼──────┐                      │
│                         │  Supabase  │                      │
│                         │  Storage   │                      │
│                         │ (Imagens)  │                      │
│                         └────────────┘                      │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

### Frontend
- **Framework**: Next.js (React)
- **Hospedagem**: Vercel (https://receitas-senai-frontend.vercel.app/)
- **URL de Produção**: https://receitas-senai-frontend.vercel.app/
- **Port**: 443 (HTTPS)

### Backend
- **Framework**: Spring Boot 4.1.1
- **Linguagem**: Java 21
- **Build Tool**: Maven
- **Segurança**: Spring Security + BCrypt
- **Hospedagem**: Railway
- **Port**: 8080 (HTTP local) / 443 (Railway/HTTPS)
- **Padrão de Arquitetura**: MVC + Service Layer

### Banco de Dados
- **SGBD**: MySQL
- **Host**: Railway (produção) / localhost:3306 (local)
- **Banco**: `receitas`
- **Estratégia**: JPA/Hibernate com `ddl-auto=update`

### Armazenamento de Arquivos
- **Serviço**: Supabase Storage
- **Bucket**: `receitas` (público)
- **Tipos permitidos**: Imagens (JPG, PNG, WebP)
- **Tamanho máximo**: 5MB por arquivo
- **Uso**: Armazenar imagens das receitas

### Autenticação & Autorização
- **Padrão**: JWT (JSON Web Tokens)
- **Algoritmo**: HS256 (HMAC SHA-256)
- **Duração do Token**: 7 dias (604800000ms)
- **Criptografia de Senhas**: BCrypt (strength 10)
- **Roles**: USER, ADMIN
- **Validação**: Header `Authorization: Bearer {token}`

---

## 📁 Estrutura do Projeto

### Backend (Java/Spring Boot)

```
sabore/
├── .mvn/                    # Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/com/projeto_final/receitas/
│   │   │   ├── ReceitasApplication.java          # Classe principal
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── AdminSeeder.java              # Inicializa admin na subida
│   │   │   │   ├── WebConfig.java                # Configurações CORS
│   │   │   │   └── Securiryconfig.java           # ⭐ Config BCrypt
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── receitaController.java        # Endpoints de receitas
│   │   │   │   ├── UsuarioController.java        # Autenticação e usuários
│   │   │   │   ├── FavoritoController.java       # Gerenciamento de favoritos
│   │   │   │   └── AdministradorController.java  # Gestão de admins
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── receitasService.java          # Lógica de receitas
│   │   │   │   ├── UsuarioService.java           # ⭐ Criptografia de senhas
│   │   │   │   ├── FavoritoService.java          # Lógica de favoritos
│   │   │   │   └── AdministradorService.java     # Gestão de admins
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── receitaRepository.java        # Queries customizadas
│   │   │   │   ├── UsuarioRepository.java
│   │   │   │   ├── FavoritoRepository.java
│   │   │   │   └── AdministradorRepository.java
│   │   │   │
│   │   │   ├── entity/
│   │   │   │   ├── Receita.java                  # Modelo de receita
│   │   │   │   ├── Usuario.java                  # Modelo de usuário
│   │   │   │   ├── Favorito.java                 # Associação usuário-receita
│   │   │   │   └── Administrador.java            # Extensão de usuário
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── RecipeResponse.java           # DTO de resposta (receita)
│   │   │   │   ├── UserResponse.java             # DTO de resposta (usuário)
│   │   │   │   └── LoginResponse.java            # DTO de resposta (login)
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── JwtService.java               # Geração e validação JWT
│   │   │   │   ├── JwtAuthFilter.java            # Filtro de autenticação
│   │   │   │   ├── CurrentUser.java              # Contexto do usuário atual
│   │   │   │   └── AuthenticatedUser.java        # Record do usuário autenticado
│   │   │   │
│   │   │   ├── storage/
│   │   │   │   └── SupabaseStorageService.java   # Integração com Supabase
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── globalExceptionHandler.java   # Handler centralizado
│   │   │   │   ├── businessException.java
│   │   │   │   ├── forbiddenException.java
│   │   │   │   ├── unauthorizedException.java
│   │   │   │   ├── resourceNotFoundException.java
│   │   │   │   └── standardError.java
│   │   │   │
│   │   │   └── ChaveComposta/
│   │   │       └── FavoritoId.java               # Chave composta de favoritos
│   │   │
│   │   └── resources/
│   │       └── application.properties            # Configurações da app
│   │
│   └── test/
│       └── java/.../ReceitasApplicationTests.java
│
├── pom.xml                  # Dependências Maven (com Spring Security)
├── mvnw / mvnw.cmd          # Maven Wrapper
├── Dockerfile               # Para containerização
├── SETUP.md                 # Guia de configuração
├── README.md                # README original (backend)
└── scripts/
    └── testar-supabase.sh   # Script para validar Supabase
```

---

## 🔐 Segurança: Implementação de BCrypt

### O Que é BCrypt?

BCrypt é um algoritmo de hashing de senhas baseado no Blowfish que é resistente a força bruta. Características principais:

- ✅ **Salting automático**: Cada hash inclui um "salt" aleatório
- ✅ **Adaptive**: Fica mais lento com o tempo, desafiando computadores mais rápidos
- ✅ **Força configurável**: Strength 10 balanceia segurança com performance
- ✅ **Padrão da indústria**: Usado por Google, Facebook, Twitter

### Implementação no Saborê

#### 1. **Configuração (Securiryconfig.java)**

```java
package com.projeto_final.receitas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration 
public class Securiryconfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // Usa strength=10 por padrão (recomendado)
    }
}
```

#### 2. **Criptografia na Criação de Usuário (UsuarioService)**

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
    // ⭐ Aqui: Senha é criptografada COM BCrypt
    obj.setPassword(passwordEncoder.encode(obj.getPassword()));
    obj.setEmail(email);
    
    return repository.save(obj);
}
```

**O que acontece:**
1. Usuário envia: `password = "minha_senha_123"`
2. BCrypt gera um salt aleatório
3. BCrypt aplica múltiplas iterações (2^strength = 2^10 = 1024 iterações)
4. Resulta em algo como: `$2a$10$...64_caracteres...`
5. Somente este hash é salvo no banco

#### 3. **Validação na Autenticação (UsuarioService)**

```java
@Transactional(readOnly = true)
public Usuario login(String email, String senha) {
    
    if (email == null || senha == null) {
        throw new unauthorizedException("Informe e-mail e senha.");
    }
    
    String emailNormalizado = email.trim().toLowerCase(Locale.ROOT);
    
    Optional<Usuario> usuario = repository.findByEmail(emailNormalizado);
    
    if (usuario.isPresent() &&
        // ⭐ Aqui: Senha é verificada usando BCrypt.matches()
        passwordEncoder.matches(senha, usuario.get().getPassword())) {
        
        return usuario.get();
    }
    
    throw new unauthorizedException("E-mail ou senha incorretos.");
}
```

**O que acontece:**
1. Usuário envia: `senha = "minha_senha_123"`
2. BCrypt aplica o mesmo hash com o salt do banco
3. Compara resultado com o hash armazenado
4. Se forem iguais → Login bem-sucedido
5. **Importante**: Senha original NUNCA é armazenada

### Fluxo de Segurança Completo

```
┌─────────────────────────────────────────────────────────────┐
│                    REGISTRO (Criar Conta)                    │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  User Input: "minhasenhaSuperSecreta123"                     │
│              ↓                                                │
│  validar(obj) - Verifica:                                    │
│    - Nome não vazio ✓                                        │
│    - Email contém @ ✓                                        │
│    - Senha tem mínimo 6 chars ✓                              │
│              ↓                                                │
│  passwordEncoder.encode()                                    │
│    1. Gera salt aleatório: $2a$10$1234567890...             │
│    2. Aplica 2^10 (1024) iterações                           │
│    3. Retorna hash único: $2a$10$...64_chars...             │
│              ↓                                                │
│  Banco de Dados:                                             │
│    id | email          | password                            │
│    1  | user@test.com  | $2a$10$...64_chars...             │
│              ↓                                                │
│  ✅ Resposta: "Usuário criado com sucesso"                   │
│                                                               │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                       LOGIN (Autenticação)                   │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  User Input: email="user@test.com",                          │
│              password="minhasenhaSuperSecreta123"            │
│              ↓                                                │
│  repository.findByEmail() → Busca no banco                   │
│              ↓                                                │
│  passwordEncoder.matches(inputPassword, dbPassword)         │
│    1. Extrai salt do hash do banco: $2a$10$...             │
│    2. Aplica senha input com o MESMO salt                   │
│    3. Gera novo hash                                         │
│    4. Compara novo hash === hash do banco                   │
│              ↓                                                │
│  if (hashesIguais) {                                         │
│    jwtService.generateToken(usuario.getId(), email, papel)  │
│    return LoginResponse com token JWT                        │
│  }                                                            │
│              ↓                                                │
│  ✅ Token JWT retornado ao usuário                           │
│  ✅ Token armazenado em localStorage no frontend             │
│              ↓                                                │
│  Requisições subsequentes: Authorization: Bearer {token}    │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

### Adicionar Spring Security no pom.xml

```xml
<!-- Já incluído no projeto! -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

Isso fornece automaticamente:
- ✅ `PasswordEncoder` bean
- ✅ `BCryptPasswordEncoder` implementação
- ✅ Segurança de requisições HTTP
- ✅ Proteção CSRF (desabilitada para API REST)

---

## 📊 Banco de Dados - Schema

### Tabelas Principais

#### `tb_usuario`
```sql
CREATE TABLE tb_usuario (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  name VARCHAR(255),
  password VARCHAR(255) NOT NULL,  -- ⭐ Agora armazena hash BCrypt
  dtype VARCHAR(31)  -- Para herança: 'Usuario' ou 'Administrador'
);

-- Exemplo de senha armazenada:
-- password: $2a$10$pXpqWj6kfI/Vdq5T/h5hKe9x6z2Q1p3T5n7m9l0k8j6h4g2f1d0

-- Hash BCrypt é sempre 60 caracteres
```

#### `tb_administrador`
```sql
CREATE TABLE tb_administrador (
  id BIGINT PRIMARY KEY,
  FOREIGN KEY (id) REFERENCES tb_usuario(id)
);
```

#### `receita`
```sql
CREATE TABLE receita (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(255) UNIQUE NOT NULL,
  descricao VARCHAR(1000) NOT NULL,
  tempo INT NOT NULL,
  url VARCHAR(255),
  categoria VARCHAR(50)
);
```

#### `tb_favorito`
```sql
CREATE TABLE tb_favorito (
  usuario_id BIGINT,
  receita_id BIGINT,
  PRIMARY KEY (usuario_id, receita_id),
  FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id),
  FOREIGN KEY (receita_id) REFERENCES receita(id)
);
```

---

## 🔑 Variáveis de Ambiente

### Backend (Railway)

```properties
# ========== BANCO DE DADOS ==========
MYSQLHOST=seu-host.mysql.railway.internal
MYSQLPORT=3306
MYSQLDATABASE=receitas
MYSQLUSER=seu_usuario
MYSQLPASSWORD=sua_senha_segura

# ========== JWT ==========
JWT_SECRET=sua_chave_super_secreta_e_longa_64_chars_minimo
JWT_EXPIRATION_MS=604800000  # 7 dias em ms

# ========== CORS ==========
CORS_ALLOWED_ORIGINS=http://localhost:3000,https://receitas-senai-frontend.vercel.app

# ========== SUPABASE STORAGE ==========
SUPABASE_URL=https://seu-projeto.supabase.co
SUPABASE_SERVICE_KEY=eyJhbGciOiJIUzI1NiIs...
SUPABASE_BUCKET=receitas

# ========== ADMIN INICIAL ==========
ADMIN_NAME=Administrador
ADMIN_EMAIL=admin@seudominio.com
ADMIN_PASSWORD=senha_inicial_forte

# ========== SERVER ==========
PORT=8080
```

### Frontend (Vercel)

```bash
NEXT_PUBLIC_API_URL=https://seu-backend.up.railway.app
```

---

## 🔗 Endpoints da API

### Base URL
- **Produção**: `https://seu-backend.up.railway.app`
- **Local**: `http://localhost:8080`

### Autenticação (Com BCrypt)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|-------------|
| `POST` | `/auth/register` | Criar conta (senha criptografada) | Pública |
| `POST` | `/auth/login` | Fazer login (valida BCrypt) | Pública |
| `GET` | `/auth/me` | Dados do usuário logado | ✅ Required |

**Exemplo - Registrar com BCrypt**:
```bash
POST /auth/register
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@example.com",
  "password": "minhasenhaSuperSegura123"
}

Response (201):
{
  "id": 1,
  "email": "joao@example.com",
  "name": "João Silva",
  "papel": "USER"
}

# No banco, a senha ficará assim:
# $2a$10$pXpqWj6kfI/Vdq5T/h5hKe9x6z2Q1p3T5n7m9l0k8j6h4g2f1d0
```

**Exemplo - Login com BCrypt**:
```bash
POST /auth/login
Content-Type: application/json

{
  "email": "joao@example.com",
  "password": "minhasenhaSuperSegura123"  # Senha em texto puro (HTTPS)
}

Response (200):
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "usuario": {
    "id": 1,
    "email": "joao@example.com",
    "name": "João Silva",
    "papel": "USER"
  }
}

# Internamente:
# 1. Backend busca o usuário por email
# 2. Extrai hash do banco: $2a$10$pXpqWj6kfI/Vdq5T/h5hKe9x6z2Q1p3T5n7m9l0k8j6h4g2f1d0
# 3. Valida com: passwordEncoder.matches("minhasenhaSuperSegura123", dbHash)
# 4. BCrypt verifica e retorna true/false (nunca compara texto puro)
```

### Receitas

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|-------------|
| `GET` | `/recipes` | Listar receitas | Opcional |
| `GET` | `/recipes/{id}` | Detalhe de uma receita | Opcional |
| `POST` | `/recipes` | Criar receita | ✅ Admin |
| `PUT` | `/recipes/{id}` | Atualizar receita | ✅ Admin |
| `DELETE` | `/recipes/{id}` | Deletar receita | ✅ Admin |

### Favoritos

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|-------------|
| `GET` | `/favoritos` | Listar favoritos do user | ✅ Required |
| `POST` | `/favoritos/{receitaId}` | Adicionar aos favoritos | ✅ Required |
| `DELETE` | `/favoritos/{receitaId}` | Remover dos favoritos | ✅ Required |

---

## 🔒 Autenticação e Segurança (Detalhado)

### 1. Validação de Senha

Na criação de conta, valida:

```java
private void validar(Usuario obj) {
    // Nome obrigatório
    if (obj.getName() == null || obj.getName().trim().isEmpty()) {
        throw new businessException("O nome é obrigatório.");
    }
    
    // Email válido
    if (obj.getEmail() == null || !obj.getEmail().contains("@")) {
        throw new businessException("Informe um e-mail válido.");
    }
    
    // Senha mínimo 6 caracteres
    if (obj.getPassword() == null || obj.getPassword().length() < 6) {
        throw new businessException("A senha precisa ter pelo menos 6 caracteres.");
    }
}
```

### 2. Fluxo de Autenticação

```
1. Usuário acessa a aplicação
   ↓
2. Opção: Login ou Registro
   ↓
3. Envia credenciais via HTTPS
   ↓
4. Backend valida com BCrypt
   ↓
5. Se válido → Gera JWT
   ↓
6. Retorna token ao frontend
   ↓
7. Frontend armazena em localStorage
   ↓
8. Em requisições posteriores, envia:
   Authorization: Bearer {token}
   ↓
9. JwtAuthFilter valida o token
   ↓
10. Se válido → Requisição processada
    Se inválido → 401 Unauthorized
```

### 3. Estrutura do Token JWT

```
Header: {
  "alg": "HS256",
  "typ": "JWT"
}

Payload: {
  "sub": "1",                    // ID do usuário
  "email": "usuario@example.com",
  "papel": "USER",               // USER ou ADMIN
  "iat": 1234567890,
  "exp": 1234567890 + 604800000  // 7 dias
}

Signature: HMAC-SHA256(secret)   // Assinado com JWT_SECRET
```

### 4. CORS (Cross-Origin Resource Sharing)

Configurado em `application.properties`:

```
CORS_ALLOWED_ORIGINS=http://localhost:3000,https://*.vercel.app
```

Protege:
- ✅ Apenas origens autorizadas acessam a API
- ✅ Curinga `*.vercel.app` cobre todos os deploys Vercel

### 5. Validação de Entrada

- ✅ Email deve conter @
- ✅ Senha mínimo 6 caracteres
- ✅ Nome não pode estar vazio
- ✅ Normalização de email (trim + lowercase)

### 6. Níveis de Acesso

| Endpoint | Público | User | Admin |
|----------|---------|------|-------|
| GET /recipes | ✅ | ✅ | ✅ |
| POST /recipes | ❌ | ❌ | ✅ |
| PUT /recipes/{id} | ❌ | ❌ | ✅ |
| DELETE /recipes/{id} | ❌ | ❌ | ✅ |
| GET /favoritos | ❌ | ✅ | ✅ |
| POST /favoritos | ❌ | ✅ | ✅ |
| GET /usuarios | ❌ | ❌ | ✅ |
| POST /administrador | ❌ | ❌ | ✅ |

---

## 🚀 Como Instalar e Executar

### Pré-requisitos
- ✅ Java 21+
- ✅ Maven 3.6+
- ✅ MySQL 8.0+
- ✅ Node.js 18+ e npm/yarn (para frontend)

### Backend

#### 1. Clonar e navegar
```bash
git clone https://github.com/caioosantss/sabore.git
cd sabore
```

#### 2. Configurar variáveis de ambiente
```bash
export MYSQLHOST=localhost
export MYSQLPORT=3306
export MYSQLDATABASE=receitas
export MYSQLUSER=root
export MYSQLPASSWORD=root
export JWT_SECRET=minha-chave-super-secreta-minimo-64-chars
export CORS_ALLOWED_ORIGINS=http://localhost:3000
export ADMIN_EMAIL=admin@localhost.com
export ADMIN_PASSWORD=admin123
```

#### 3. Criar banco de dados
```bash
mysql -u root -p
mysql> CREATE DATABASE receitas;
mysql> exit;
```

#### 4. Rodar o backend
```bash
./mvnw clean install
./mvnw spring-boot:run
```

Backend disponível em: `http://localhost:8080`

---

## 📊 Comparação: Antes vs Depois

### Antes (Texto Puro) ❌

```
Registro:
- Senha: "minhasenhaSuperSegura123"
- Banco: "minhasenhaSuperSegura123"  ← Qualquer pessoa com acesso ao BD consegue ler!

Login:
- Entrada: "minhasenhaSuperSegura123"
- Comparação: string.equals(entrada, banco)  ← Simples concatenação
- Risco: Se o BD vazar, todas as senhas são expostas
```

### Depois (BCrypt) ✅

```
Registro:
- Senha: "minhasenhaSuperSegura123"
- Processamento: BCrypt (1024 iterações + salt aleatório)
- Banco: "$2a$10$pXpqWj6kfI/Vdq5T/h5hKe9x6z2Q1p3T5n7m9l0k8j6h4g2f1d0"
         (sempre 60 caracteres, diferente a cada execução)

Login:
- Entrada: "minhasenhaSuperSegura123"
- Processamento: BCrypt.matches(entrada, banco)
- Comparação: Hash temporário === Hash do banco
- Risco: Mesmo com acesso ao BD, a senha ORIGINAL não pode ser recuperada
- Força bruta: 2^10 = 1024 iterações tornam brute-force impraticável
```

---

## ✨ Melhorias Implementadas

| Recurso | Status | Detalhes |
|---------|--------|----------|
| **BCrypt** | ✅ Implementado | Strength 10, salt aleatório |
| **Spring Security** | ✅ Implementado | Integração completa |
| **PasswordEncoder** | ✅ Implementado | Injeção de dependência |
| **JWT** | ✅ Implementado | HS256, 7 dias |
| **Validação** | ✅ Implementado | Email, senha mínima, nome |
| **CORS** | ✅ Implementado | Origens autorizadas |
| **Normalização Email** | ✅ Implementado | Trim + lowercase |
| **Exceções** | ✅ Implementado | Handler centralizado |

---

## 🐛 Problemas Conhecidos Resolvidos

### ✅ ANTES: Senhas em Texto Puro
**Status**: ✅ **RESOLVIDO** com BCrypt

### Próximos Passos (Roadmap)
- [ ] Adicionar validação de força de senha (regex)
- [ ] Implementar refresh tokens
- [ ] Adicionar rate limiting por IP
- [ ] Adicionar autenticação de dois fatores (2FA)
- [ ] Implementar logout com token blacklist
- [ ] Adicionar log de tentativas de login

---

## 🧪 Testes

### Executar testes
```bash
./mvnw test
```

### Cobertura de testes
```bash
./mvnw test jacoco:report
open target/site/jacoco/index.html
```

### Teste Manual: Registrar e Login

```bash
# 1. Registrar novo usuário
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Teste User",
    "email": "teste@example.com",
    "password": "testeSenha123"
  }'

Resposta esperada (201):
{
  "id": 1,
  "name": "Teste User",
  "email": "teste@example.com",
  "papel": "USER"
}

# 2. Fazer login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@example.com",
    "password": "testeSenha123"
  }'

Resposta esperada (200):
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "usuario": {
    "id": 1,
    "name": "Teste User",
    "email": "teste@example.com",
    "papel": "USER"
  }
}

# 3. Usar token em requisição segura
curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  http://localhost:8080/auth/me

Resposta esperada (200):
{
  "id": 1,
  "name": "Teste User",
  "email": "teste@example.com",
  "papel": "USER"
}
```

---

## 🚀 Deploy em Produção

### Railway (Backend)

1. **Conectar repositório GitHub** no Railway
2. **Definir variáveis de ambiente** (incluindo JWT_SECRET forte)
3. **Deploy automático** a cada push em `main`

URL gerada: `https://seu-servico.up.railway.app`

### Vercel (Frontend)

1. **Conectar repositório GitHub** no Vercel
2. **Configurar**: `NEXT_PUBLIC_API_URL=https://seu-backend.railway.app`
3. **Deploy automático** a cada push

URL gerada: `https://seu-projeto.vercel.app`

### Checklist Pré-Deploy

- [ ] BCrypt implementado ✅
- [ ] JWT_SECRET é forte (64+ chars) ✅
- [ ] CORS_ALLOWED_ORIGINS configurado corretamente ✅
- [ ] MySQL password é seguro ✅
- [ ] Nenhum console.log com dados sensíveis ✅
- [ ] .env não commitado (.gitignore) ✅
- [ ] HTTPS ativado em produção ✅

---

## 📞 Contato e Suporte

- **Autor**: [@caioosantss](https://github.com/caioosantss)
- **Repositório**: [GitHub - Saborê](https://github.com/caioosantss/sabore)
- **Issues**: [Report um bug](https://github.com/caioosantss/sabore/issues)
- **Instituição**: SENAI
- **Tipo**: Projeto Final de Curso

---

## 📄 Licença

Este projeto está licenciado sob a Licença MIT.

---

## 🎉 Agradecimentos

- **SENAI** - Por fornecer estrutura e mentoria
- **Spring Boot Community** - Excelente framework
- **BCrypt** - Algoritmo seguro de hashing
- **Railway & Vercel** - Plataformas de deploy
- **Supabase** - Storage confiável

---

<div align="center">

**Desenvolvido com ❤️ e Segurança em Primeiro Lugar**

*Saborê - Receitas que aproximam*

![Java](https://img.shields.io/badge/-Java%2021-blue?style=flat-square&logo=java)
![Spring](https://img.shields.io/badge/-Spring%20Boot-green?style=flat-square&logo=spring)
![Security](https://img.shields.io/badge/-Spring%20Security-brightgreen?style=flat-square&logo=spring)
![BCrypt](https://img.shields.io/badge/-BCrypt-darkgreen?style=flat-square)
![MySQL](https://img.shields.io/badge/-MySQL-blue?style=flat-square&logo=mysql)
![Next.js](https://img.shields.io/badge/-Next.js-black?style=flat-square&logo=next.js)
![Railway](https://img.shields.io/badge/-Railway-0b0d0e?style=flat-square&logo=railway)
![Vercel](https://img.shields.io/badge/-Vercel-black?style=flat-square&logo=vercel)

*Última atualização: Setembro de 2026 - Com BCrypt Implementado ✅*

</div>
