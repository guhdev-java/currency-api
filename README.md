# Currency API 💱

API REST para conversão de moedas em tempo real usando Spring Boot.

## 🚀 Tecnologias
- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Validation
- Spring Actuator
- Springdoc OpenAPI (Swagger)
- Maven

## 📌 Funcionalidades
- Converter valores entre moedas (`from`, `to`, `amount`)
- Buscar taxa em API externa de câmbio
- Cache simples em memória (TTL de 5 minutos)
- Tratamento global de erros
- Endpoint de health check

## ▶️ Como rodar localmente

### Pré-requisitos
- Java 17+
- Maven (ou usar `./mvnw`)

### Executar
```bash
./mvnw clean spring-boot:run
```

A aplicação sobe em:
- `http://localhost:8080`

## 📖 Documentação (Swagger)
- `http://localhost:8080/swagger-ui/index.html`

## 🔍 Endpoints

### Converter moeda
`GET /api/convert?from=USD&to=BRL&amount=100`

#### Exemplo de resposta
```json
{
  "from": "USD",
  "to": "BRL",
  "amount": 100,
  "rate": 5.4,
  "convertedAmount": 540.00,
  "timestamp": "2026-06-19T18:03:26.608662195Z"
}
```

### Health check
`GET /actuator/health`

#### Exemplo
```json
{
  "status": "UP"
}
```

## ⚠️ Tratamento de erros
A API retorna erros padronizados para entradas inválidas (ex.: moeda inexistente, amount inválido).

Exemplo:
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Moeda de destino não suportada: XXX",
  "timestamp": "2026-06-19T18:10:00Z"
}
```

## 🛣️ Próximos passos
- Adicionar testes unitários e de integração
- Persistir histórico de conversões (PostgreSQL)
- Implementar autenticação (API Key/JWT)
- Deploy em nuvem (Render/Railway/Fly.io)
- Dockerizar aplicação

## 👨‍💻 Autor
Desenvolvido por **guhdev-java**.
