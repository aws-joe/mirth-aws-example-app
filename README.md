# mirth-aws-example-app
This application contains functions which can be called via Mirth Connect
Example channel implementations will be added to repository

Instructions to [build this package using Maven](http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-project-maven.html).

Instructions for [invoking custom java code in Mirth Connect](http://www.mirthcorp.com/community/wiki/display/mirth/How+to+create+and+invoke+custom+Java+code+in+Mirth+Connect).

Quick build instructions once Maven and Java are installed and ready per instructions in AWS documentation.
```
git clone https://github.com/aws-joe/mirth-aws-example-app.git
cd mirth-aws-example-app
mvn clean package
```

In Example 1, we assume the data received from connectorMessage.getRawData() is formatted JSON.

Example Javascript Writer in Mirth Connect using sample code:
```
//Example 1
//create unique ID during insert
//this unique ID can be written to channel map for use in other areas
var awsFileId = UUIDGenerator.getUUID();
channelMap.put("awsFileMapId", awsFileId);
var currentDate = DateUtil.getCurrentDate("MM-dd-yyyy-HH-mm");

//loading DynamoDB java code
var awsObj = new Packages.org.mirth.project.MCAWS();

var ccdJsonData = connectorMessage.getRawData();

//ccdJsonData - JSON formatted text
//"mirthdata" - DynamoDB table name
//awsFileId - unique ID for use as DynamoDB primary key
//currentDate - the current date generated above
awsObj.dynamoInsertJson(ccdJsonData,"mirthdata",awsFileId,currentDate);
```

Links to additional implementation examples will be provided when available.
