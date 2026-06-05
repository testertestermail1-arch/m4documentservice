%dw 2.0
import * from dw::core::Strings
output application/json
var origFileName=substringBeforeLast(attributes.queryParams.attachmentName,'.')
---
{
	"claimNumber": attributes.uriParams.'claimNumber',
	"sourceOriginator": attributes.queryParams.sourceOriginator,
	"outboundLocation": attributes.queryParams.targetLocation,
	"fileName": attributes.queryParams.attachmentName,
	"fileNameExtension": trim(substringAfterLast(origFileName,'.'))
}
