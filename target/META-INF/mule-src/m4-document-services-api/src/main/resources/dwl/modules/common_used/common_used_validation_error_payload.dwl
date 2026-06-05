%dw 2.0
output application/json
---
{
	correlationId: correlationId,
	status: "Error",
	errorType: error.errorType.namespace,
	errors: [{
		errorCode: "DVEX01",
		errorDescription:error.description
		}]
}