# ☕ **Terroir — E-commerce de Cafés Especiais**

## 🧩 Descrição do Projeto
O **Terroir** é um e-commerce de cafés especiais desenvolvido para a disciplina de **Engenharia de Software (UFLA)**.  
O sistema implementa catálogo de produtos, autenticação JWT, carrinho funcional (com popup), registro/login de usuários e um painel administrativo para gestão de produtos.

Tecnologias principais:
- **Backend:** Java 17 + Spring Boot 3  
- **Frontend:** React + Vite  
- **Banco de Dados:** PostgreSQL  
- **Autenticação:** JWT  
- **Design:** Figma  

---

## 👥 **Tipos de Usuário**

| Tipo | Permissões |
|------|------------|
| **Visitante** | Visualiza catálogo, produtos populares e acessa login/registro. |
| **Cliente** | Login, registro, adicionar ao carrinho, aumentar/diminuir quantidades, finalizar compra. |
| **Administrador** | Todas as permissões de cliente + criar, editar, listar e excluir produtos. |

---

## 🧭 **Funcionalidades Implementadas**

### 🔹 Frontend
- Tela inicial com **produtos populares**
- Componente de **categorias** (UI pronta; funcionalidade futura)
- Tela de **login**
- Tela de **registro**
- **Pop-up do carrinho** com:
  - Aumentar quantidade
  - Diminuir quantidade
  - Remover item
- Painel **admin** com CRUD completo de produtos

### 🔹 Backend
- Autenticação com **JWT**
- Diferenciação entre **visitante**, **cliente** e **admin**
- CRUD completo de produtos
- Persistência de usuários, carrinho e itens

---

## 📡 **Principais Endpoints**

### 👤 Usuário
- `POST /usuarios` — Registrar
- `POST /login` — Autenticação

### 📦 Produtos (Admin)
- `GET /produtos`
- `POST /produtos`
- `PUT /produtos/{id}`
- `DELETE /produtos/{id}`

### 🛒 Carrinho
- `GET /carrinho/{idUsuario}`
- `POST /carrinho/adicionar`
- `PUT /carrinho/alteraQuantidade`
- `DELETE /carrinho/remover/{itemId}`

---

# ⚙️ **Instruções para Rodar o Projeto (Frontend + Backend)**

## 1) Pré-requisitos
- Node.js LTS (>=16)
- npm
- Java 17
- Maven
- PostgreSQL
- Git

---

## 2) Instalar Dependências

### 🔹 Backend
```bash
cd BackEnd
mvn clean install
```

### 🔹 Frontend
```bash
cd FrontEnd
npm install
```

---

## 3) Variáveis de Ambiente

### 🔹 Backend — `.env`
```
PORT=4000
DATABASE_URL=postgres://user:pass@localhost:5432/terroir
JWT_SECRET=sua_chave_secreta
FRONTEND_URL=http://localhost:5173
```

### 🔹 Frontend — `.env` ou `.env.local`
```
VITE_API_URL=http://localhost:4000
```

---

## 4) Configurar Banco de Dados (PostgreSQL)

Criar banco:
```sql
CREATE DATABASE terroir;
```

Ao rodar o backend, as **migrations Flyway** serão aplicadas automaticamente.

---

## 5) Executar os Servidores

### 🔹 Backend
```bash
cd BackEnd
mvn spring-boot:run
```

### 🔹 Frontend
```bash
cd FrontEnd
npm run dev
```

### 🔗 Endereços Padrão
- **Frontend:** http://localhost:5173  
- **Backend:** http://localhost:4000  

---

## 📁 Estrutura de Pastas

```
ProjectRoot/
│
├── BackEnd/        # API REST (Spring Boot)
├── FrontEnd/       # Aplicação React
└── Documentos/     # Docs, diagramas e relatórios
```

---

## 📦 Dependências Principais

### 🔹 Frontend
- react
- react-router-dom
- axios
- react-icons
- vite

### 🔹 Backend
- Spring Boot Web
- Spring Boot Security
- Spring Data JPA
- PostgreSQL Driver
- Flyway
- Lombok

---

## 🐞 Erros Comuns

| Erro | Causa | Solução |
|------|-------|----------|
| API não responde | Porta errada ou backend off | Verificar `.env` e rodar o backend |
| CORS | FRONTEND_URL incorreto | Ajustar no backend |
| Front não carrega | Falta dependências | `npm install` |
| Banco não conecta | DATABASE_URL incorreta | Ajustar credenciais |

---

## 🎨 Design (Figma)
https://www.figma.com/design/YRmCSWIV94Sm94X0SqDSXe/Projeto

---

## 👥 Autores
- **Backend:** @Gustavo-Martins610  
- **Frontend:** @lanamiranda17 e @ninanovikoffr  
- **Design:** @ninanovikoffr  

---

## 📘 Versão
`v0.2` — Telas de login e registro, popup de carrinho, CRUD de produtos no admin, autenticação, produtos populares na tela inicial.

