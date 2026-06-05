%dw 2.0
output application/json
---
{
  "status": "Error",
  "correlationId":  correlationId,
  "errorCode": "GE0001",
  "errorCategory": "General Exception",
  "userMessage": "General Exception. Please try to resubmit the request later.",
  "developerMessage": error.errorType.asString default ""
 }
 