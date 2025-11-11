# ☕ **Terroir — E-commerce de Cafés Especiais**

## 🧩 Descrição do Projeto
O **Terroir** é um sistema completo de **e-commerce de cafés especiais**, desenvolvido como projeto da disciplina de **Engenharia de Software** na Universidade Federal de Lavras (UFLA).  
O objetivo do sistema é oferecer uma plataforma para comercialização de cafés especiais e produtos relacionados, permitindo a administração de produtos, pedidos e clientes, com autenticação segura e interface intuitiva.  

O sistema conta com **módulo de backend (API REST)** desenvolvido em **Spring Boot** e um **frontend moderno** construído em **React + Vite**, com design elaborado no **Figma**.

---

## 👥 **Tipos de Usuário**

| Tipo de Usuário | Permissões Principais |
|-----------------|-----------------------|
| **Visitante** | Navegar pelo catálogo, visualizar detalhes e criar conta. |
| **Cliente** | Gerenciar conta, endereços, carrinho e pedidos. |
| **Administrador** | Gerenciar produtos, pedidos e clientes. |

---

## 🧭 **Principais Funcionalidades**

### 🔹 Visitante
- Visualizar catálogo e detalhes dos cafés.
- Filtrar produtos por categoria (grãos, cápsulas, kits).
- Criar conta (e-mail/senha ou Google).

### 🔹 Cliente
- Login e autenticação via JWT.
- CRUD de endereços com integração ViaCEP.
- Adição/remoção de itens no carrinho.
- Checkout e geração de pedidos.
- Acompanhamento de pedidos ativos e entregues.

### 🔹 Administrador
- CRUD de produtos.
- Atualização de status de pedidos (aguardando, enviado, entregue etc).
- Gerenciamento de clientes e endereços.

---

## 💾 **Arquitetura do Sistema**

O sistema segue uma arquitetura **Full Stack**, composta por:
- **Backend (API REST)**: Java + Spring Boot  
- **Frontend (Interface do Usuário)**: React + Vite (JavaScript + HTML + CSS)
- **Banco de Dados**: PostgreSQL  
- **Controle de Versão**: Git + GitHub  
- **Migrações**: Flyway  
- **Autenticação**: JWT + Google OAuth2  
- **Containerização e Deploy**: Docker + Railway  

---

## 🛠️ **Tecnologias Utilizadas**

| Categoria | Tecnologia | Versão / Observações |
|------------|-------------|----------------------|
| **Linguagem Backend** | Java | 17 |
| **Framework Web** | Spring Boot | 3.x |
| **ORM / Persistência** | Spring Data JPA | - |
| **Migração de Banco** | Flyway | - |
| **Banco de Dados** | PostgreSQL | 15 |
| **Segurança** | Spring Security + JWT + Google OAuth2 | - |
| **Frontend** | React + Vite | Node 20 / NPM 10 |
| **Linguagens de Frontend** | JavaScript, HTML, CSS | - |
| **Design** | [Figma](https://www.figma.com/design/YRmCSWIV94Sm94X0SqDSXe/Projeto?node-id=0-1&p=f&t=tPH0TYHDsv4IJ3qk-0) | Protótipo oficial |
| **Servidor / Deploy** | Docker + Railway | - |
| **IDE** | VS Code / IntelliJ IDEA | - |
| **Gerenciador de Dependências** | Maven | - |
| **Controle de Versão** | Git + GitHub | - |

---

## 🧱 **Modelagem de Dados (Entidades Principais)**

- **Usuário:** id, nome, cpf, email, senha, tipo, createdAt  
- **Endereço:** id, cep, rua, número, complemento, bairro, cidade, estado, isPadrao  
- **Produto:** id, nome, descrição, preço, estoque, categoria, imagemUrl  
- **Carrinho / Itens:** gerenciamento de produtos adicionados e total dinâmico  
- **Pedido / Itens:** registro de compras e histórico por usuário  

---

## 🔐 **Segurança**
- Autenticação via **JWT Token** e **Google OAuth2**.  
- Proteção de rotas e perfis de acesso (visitante, cliente, admin).  
- Criptografia de senhas com **BCrypt**.  
- Permissões configuradas em nível de endpoint via Spring Security.

---

## 📡 **Endpoints Principais (API REST)**

### 👤 Usuário
- `POST /usuarios` — Cadastrar novo usuário  
- `POST /login` — Autenticação (gera JWT)  
- `GET /usuarios/{id}` — Consultar perfil  

### 📦 Produto
- `GET /produtos` — Listar produtos  
- `POST /produtos` — Criar produto (admin)  
- `PUT /produtos/{id}` — Atualizar produto  
- `DELETE /produtos/{id}` — Excluir produto  

### 🛒 Carrinho
- `GET /carrinho` — Ver carrinho do usuário  
- `POST /carrinho/adicionar` — Adicionar item  
- `DELETE /carrinho/remover/{itemId}` — Remover item  

### 📜 Pedido
- `POST /pedidos` — Criar pedido a partir do carrinho  
- `GET /pedidos` — Listar pedidos do usuário  
- `PUT /pedidos/{id}/status` — Atualizar status (admin)

---

## 🧠 **Fluxos Essenciais**

1. **Cadastro/Login** — Criação de conta com e-mail ou Google; autenticação com JWT.  
2. **Adicionar ao Carrinho** — Cliente seleciona produto; sistema valida estoque.  
3. **Gerenciar Carrinho** — Atualizar quantidades ou remover produtos.  
4. **Checkout** — Cliente escolhe endereço e confirma compra; pedido gerado.  
5. **Acompanhar Pedidos** — Cliente visualiza pedidos ativos/histórico.  
6. **Gerenciar Produtos** — Admin cadastra, edita ou remove produtos.

---

## ⚖️ **Regras e Restrições Importantes**
- Visitante não pode adicionar produtos ao carrinho.  
- Clientes podem ter múltiplos endereços (um é padrão).  
- Endereços são validados automaticamente via **API ViaCEP**.  
- Estoque validado ao adicionar ou finalizar pedidos.  
- Apenas administradores podem alterar produtos/pedidos.  
- Pedidos só podem ser criados com endereço válido.  

---

## 🧪 **Testes**
- Testes manuais via **Postman** / **Insomnia**.  
- Testes unitários com **JUnit 5** para Services.  
- Testes de integração com **MockMvc** para Controllers.  
- Validação de migrações e schema via **Flyway**.

---

## 🐳 **Deploy**
- Configuração com **Dockerfile** e **docker-compose** (API + Banco).  
- Deploy realizado em ambiente **Railway/Render/Heroku**.  
- Uso de variáveis de ambiente (JWT_SECRET, DB_USER, DB_PASS etc).

---

## 🧠 **Autores**
**Equipe Terroir – E-commerce de Cafés Especiais**  
Universidade Federal de Lavras (UFLA)  
Disciplina: Engenharia de Software  
Professor Responsável: *Antônio*  

**Desenvolvimento:**  
- Backend: [@Gustavo-Martins610](https://github.com/Gustavo-Martins610)
- Frontend: [@lanamiranda17](https://github.com/lanamiranda17)
- Design: [@ninanovikoffr](https://github.com/ninanovikoffr)

---

## 🖼️ **Design e Protótipos**
O design foi desenvolvido no **Figma**, contemplando:  
- Tela de login/cadastro  
- Catálogo de produtos  
- Carrinho e checkout  
- Painel de pedidos  
- Páginas administrativas  

🔗 **Protótipo oficial:** [Acessar no Figma](https://www.figma.com/design/YRmCSWIV94Sm94X0SqDSXe/Projeto?node-id=0-1&p=f&t=tPH0TYHDsv4IJ3qk-0)

---

## 📚 **Versão**
`v1.0.0` – Primeira release (Sprint 0 concluída).  
Inclui setup do backend, banco de dados com Flyway, autenticação JWT e base do frontend cm parte da primeira tela feita.

---

## 📎 **Licença**
Este projeto é de uso acadêmico, desenvolvido exclusivamente para fins de aprendizagem e avaliação da disciplina **Engenharia de Software** (UFLA).

---

👉 **Entregável:** o link do repositório GitHub contendo este README será postado na **ABA GRUPOS** da planilha da disciplina.
