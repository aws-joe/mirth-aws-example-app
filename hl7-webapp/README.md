#hl7-webapp Use and Install

Installation:
	Create a working directory for the HL7 conversion service.
	Ensure that node.js and npm are installed
```
npm install xml2js util http querystring
node service-xml-to-json.js &
```

The service will now be listening on TCP port 8001 on localhost by default.
