%dw 2.0
output application/json
---
{
  "status": "Error",
  "correlationId":  correlationId,
  "errorCode": "GE0001",
  "errorCategory": "General SFTP Exception",
  "userMessage": "General Exception. Please try resubmitting the request again later.",
  "developerMessage": error.errorType.asString default ""
 }
 