# API Message

API em Java com Spring Boot para agendar envios diários de mensagens via WhatsApp ou e-mail em um horário específico.

## Executar

```bash
mvn spring-boot:run
```

## Endpoints

### Agendar WhatsApp

`POST /api/schedules/whatsapp`

```json
{
  "recipient": "+5511999999999",
  "message": "Olá!",
  "time": "09:30",
  "timeZone": "America/Sao_Paulo",
  "whatsappFrom": "+14155238886"
}
```

### Agendar e-mail

`POST /api/schedules/email`

```json
{
  "recipient": "cliente@exemplo.com",
  "message": "Bom dia!",
  "time": "09:30",
  "timeZone": "America/Sao_Paulo"
}
```

### Listar agendamentos

`GET /api/schedules`

### Cancelar agendamento

`DELETE /api/schedules/{id}`

## Observações

- O envio real é simulado com logs (`MessageSenderService`).
- Integre com provedores de WhatsApp (ex: Twilio) ou e-mail (SMTP) no serviço de envio.

## Integração com Twilio

Configure as variáveis abaixo (por `application.yml`, env vars ou secrets) para habilitar o envio real via Twilio:

```yaml
twilio:
  accountSid: ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
  authToken: your_auth_token
  whatsappFrom: +14155238886
```

O campo `whatsappFrom` pode ser enviado no payload para sobrescrever o número padrão do Twilio.
