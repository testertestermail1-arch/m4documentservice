%dw 2.0
import * from dw::core::Strings
output application/json
---
{
	"status": "Success",
	"correlationId": correlationId,
	"message": "Claim attachments submission for " ++ (substringBeforeLast(vars.claimAttachmentProperties.fileName,'.') default "(blank)") ++ " processing completed successfully."	
}