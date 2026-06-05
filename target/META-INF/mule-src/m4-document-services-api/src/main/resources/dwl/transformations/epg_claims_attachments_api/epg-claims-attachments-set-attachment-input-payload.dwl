%dw 2.0
output application/java
var payloadContent=payload pluck($) default []
---
payloadContent[0] pluck($) default []