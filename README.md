# 📅 Agendador de Tarefas - Microserviço

## 📌 Visão Geral

O **Agendador de Tarefas** é um microserviço responsável pelo gerenciamento de tarefas de usuários, permitindo:

* Criação de tarefas
* Consulta por usuário
* Consulta por período
* Atualização de dados
* Alteração de status
* Remoção de tarefas

O serviço faz parte de uma arquitetura de microserviços e depende de um **microserviço de usuário** para validação, utilizando autenticação baseada em **JWT**.

---

## 🧱 Tecnologias Utilizadas

* Java 21
* Spring Boot
* Spring Web
* Spring Security
* Spring Data MongoDB
* OpenFeign
* MapStruct
* Lombok
* Springdoc OpenAPI (Swagger)

---

## 🗂️ Estrutura do Projeto

```id="e9q2i5"
com.dominick.agendadortarefas
├── business
│   ├── dto
│   ├── mapper
│   └── service
├── controller
│   └── TarefasController
├── infrastructure
│   ├── client
│   │   └── UsuarioClient
│   ├── entity
│   │   └── TarefasEntity
│   ├── enums
│   │   └── StatusNotificacaoEnum
│   ├── exceptions
│   │   ├── GlobalExceptionHandler
│   │   ├── ResourceNotFoundException
│   │   └── UnauthorizedException
│   ├── repository
│   │   └── TarefasRepository
│   └── security
│       ├── JwtRequestFilter
│       ├── JwtUtil
│       ├── SecurityConfig
│       └── UserDetailsServiceImpl
```

---

## 🔐 Segurança (Implementação Real)

A aplicação utiliza autenticação via **JWT** com configuração explícita no Spring Security.

### 🔹 Como funciona no projeto

* Todas as rotas exigem autenticação:

```java id="mbh9tb"
.anyRequest().authenticated()
```

* A autenticação é feita através de um filtro customizado:

**`JwtRequestFilter`**

* Intercepta todas as requisições

* Extrai o token do header `Authorization`

* Valida o token utilizando `JwtUtil`

* Carrega o usuário via `UserDetailsServiceImpl`

* Utilitário JWT:

**`JwtUtil`**

* Geração de token

* Validação de token

* Extração do email do usuário

* Configuração de segurança:

**`SecurityConfig`**

* Desabilita CSRF
* Define política stateless
* Registra o filtro JWT

### ⚠️ Limitações atuais

* Não há controle por roles/perfis
* Todas as rotas são protegidas (não há endpoints públicos)
* Autorização baseada apenas no usuário autenticado

---

## 🔗 Integração com Microserviço de Usuário

Realizada via **Feign Client**:

```java id="dnb3np"
@FeignClient(name = "usuario", url = "${usuario.url}")
```

### Endpoint consumido:

```
GET /usuario?email={email}
```

### Uso no projeto:

* Validar se o usuário existe
* Garantir integridade ao criar tarefas

---

## 🗃️ Banco de Dados

* Banco utilizado: **MongoDB**
* Repositório: `TarefasRepository extends MongoRepository`

### 📌 Estrutura da entidade

```json id="ps33wd"
{
  "id": "string",
  "nomeTarefa": "string",
  "descricao": "string",
  "dataCriacao": "datetime",
  "dataEvento": "datetime",
  "emailUsuario": "string",
  "dataAteracao": "datetime",
  "statusNotificacaoEnum": "PENDENTE | NOTIFICADO | CANCELADO"
}
```

---

## 📬 Endpoints (Baseado no Controller Real)

### 🔹 Criar tarefa

```
POST /tarefas
```

* Recebe DTO no body
* Recebe token via header `Authorization`

---

### 🔹 Buscar tarefas por usuário

```
GET /tarefas
```

* Retorna lista de tarefas vinculadas ao usuário autenticado

---

### 🔹 Buscar tarefas por período

```
GET /tarefas/eventos?dataInicial={ISO_DATE}&dataFinal={ISO_DATE}
```

* Usa:

```java id="4w0lbd"
@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
```

---

### 🔹 Atualizar tarefa

```
PUT /tarefas?id={id}
```

---

### 🔹 Alterar status

```
PATCH /tarefas?status={STATUS}&id={id}
```

Valores:

* `PENDENTE`
* `NOTIFICADO`
* `CANCELADO`

---

### 🔹 Deletar tarefa

```
DELETE /tarefas?id={id}
```

---

## 📖 Swagger / OpenAPI

Disponível em:

```
http://localhost:8086/swagger-ui/index.html
```

### ✔️ O que existe no projeto

* Documentação automática de todos os endpoints
* Schemas de request/response
* Interface interativa para testes
* Suporte a envio de Authorization header

### ⚠️ Como está implementado (realidade do código)

* Documentação **gerada automaticamente**
* Não há uso explícito de:

    * `@Operation`
    * `@ApiResponse`
* Respostas de erro não são padronizadas (retorno simples)

---

## ⚙️ Regras de Negócio

* Ao criar uma tarefa:

    * `dataCriacao` é preenchida automaticamente
    * `statusNotificacaoEnum` inicia como `PENDENTE`
    * O email do usuário é extraído do token JWT

* Atualização:

    * Não substitui a entidade inteira
    * Atualiza apenas campos específicos

* Consulta por período:

    * Filtra tarefas dentro do intervalo informado

---

## ⚠️ Tratamento de Exceções

Implementado via:

```java id="qk8tjs"
@ControllerAdvice
public class GlobalExceptionHandler
```

### Exceções existentes:

* `ResourceNotFoundException`
* `UnauthorizedException`

### Comportamento atual:

* Tratamento global funcionando
* Retorno das exceções feito como **String simples**

### Limitação

* Não há padrão estruturado de resposta de erro (JSON)

---

## ▶️ Como Executar

```bash id="kx1w2k"
git clone <repo>
cd agendador-tarefas
./gradlew bootRun
```

---

## ⚙️ Configuração

```properties id="w0h71l"
spring.data.mongodb.uri=mongodb://localhost:27017/agendador
usuario.url=http://localhost:8081
server.port=8086
```

---

## 🧪 Testes

O projeto **não possui testes automatizados implementados**.

---

## 🚀 Melhorias Sugeridas (Baseadas no Código Real)

1. **Implementar testes automatizados**

    * Unitários (Service)
    * Integração (Controller + MongoDB)
    * Segurança (JWT)

2. **Padronizar resposta de erro no GlobalExceptionHandler**

    * Criar modelo JSON (`timestamp`, `status`, `message`)
    * Evitar retorno direto de `String`

3. **Aprimorar documentação Swagger**

    * Adicionar `@Operation` e `@ApiResponse`
    * Melhorar descrição dos endpoints

4. **Evoluir segurança**

    * Implementar controle por roles
    * Definir endpoints públicos quando necessário

---

## 👨‍💻 Autor

**Jhonatan Dominick**
