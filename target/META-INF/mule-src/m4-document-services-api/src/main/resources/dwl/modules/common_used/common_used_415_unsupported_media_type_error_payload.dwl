%dw 2.0
output application/json
---
{
	correlationId: correlationId,
	status: "Error",
	errorType: error.errorType.namespace,
	errors: [{
		errorCode: "GEEX01",
		errorDescription: "Unsupported media type"
		}]
}
