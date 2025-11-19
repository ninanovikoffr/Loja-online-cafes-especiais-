# 📋 Regras de Verificação e Análise de Requisitos

## 1. Definições Importantes

### ✅ Requisitos Funcionais (RF)
São as funcionalidades que o sistema **deve executar**, descrevendo o comportamento esperado.  
Exemplo:  
- RF01: O sistema deve permitir que o usuário realize login com e-mail e senha.

### ⚙️ Requisitos Não Funcionais (RNF)
São restrições ou qualidades que o sistema deve possuir, **sem representar uma funcionalidade direta**.  
Exemplo:  
- RNF01: O sistema deve responder em no máximo 3 segundos após o envio de um formulário.

---

## 2. Regras de Verificação e Análise

Para que um requisito seja considerado válido, ele deve atender **a todas as regras abaixo**:

### 🧠 Regra 1 — Clareza e Objetividade
Cada requisito deve ser descrito de forma **clara, direta e sem ambiguidades**, evitando termos subjetivos como “rápido”, “fácil” ou “melhor”.

✅ **Critério de aceitação:**  
O requisito pode ser entendido **da mesma forma por todos os membros da equipe** e stakeholders.

---

### 🧩 Regra 2 — Identificação e Rastreabilidade
Todo requisito deve possuir um **identificador único** (por exemplo: RF01, RNF02) e estar **associado a um caso de uso, história de usuário ou funcionalidade específica**.

✅ **Critério de aceitação:**  
É possível rastrear o requisito em todas as etapas — desde o documento inicial até o código e os testes.

---

### 🔍 Regra 3 — Testabilidade
Cada requisito deve ser **passível de verificação**, ou seja, deve ser possível criar **um teste ou caso de validação** para confirmar seu cumprimento.

✅ **Critério de aceitação:**  
Existe uma forma objetiva de verificar se o requisito foi implementado corretamente.

---

### ⚖️ Regra 4 — Consistência
Os requisitos devem ser **compatíveis entre si**, sem contradições lógicas ou funcionais.

✅ **Critério de aceitação:**  
Nenhum requisito entra em conflito com outro já aprovado.

---

### 🧱 Regra 5 — Viabilidade Técnica
Os requisitos devem ser **realizáveis com as tecnologias e recursos disponíveis** para o projeto.

✅ **Critério de aceitação:**  
A equipe confirma que possui meios técnicos e tempo hábil para implementar o requisito.

---

## 3. Padrão de Nomeação
Todos os requisitos seguirão o padrão abaixo:

| Tipo | Prefixo | Exemplo | Descrição |
|------|----------|----------|-----------|
| Funcional | RF | RF01 | “O sistema deve permitir o cadastro de novos usuários.” |
| Não Funcional | RNF | RNF01 | “O sistema deve utilizar autenticação JWT para login.” |

---
