%dw 2.0
output application/java
var payloadContent=payload pluck($) default []
---
(payloadContent[0] pluck($) default [])[0].content.^raw default []