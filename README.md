# mirth-aws-example-app
This application contains functions which can be called via Mirth Connect
Example channel implementations will be added to repository

Instructions to [build this package using Maven](http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-project-maven.html).

Instructions for [invoking custom java code in Mirth Connect](http://www.mirthcorp.com/community/wiki/display/mirth/How+to+create+and+invoke+custom+Java+code+in+Mirth+Connect).

Example Javascript Writer in Mirth Connect using sample code:
```
var uuid = UUIDGenerator.getUUID();
channelMap.put("awsBlueButtonMapId", uuid);
var awsFileId = globalMap.get("awsFileMapId");

var currentDate = DateUtil.getCurrentDate("MM-dd-yyyy-HH-mm");

//loading DynamoDB java code
var awsObj = new Packages.org.mirth.project.MCAWS();

var ccdJsonData = connectorMessage.getRawData();

//logger.info(ccdJsonData);

awsObj.dynamoInsertJson(ccdJsonData,"mirthdata",awsFileId,currentDate);
```

Links to additional implementation examples will be provided when available.
