package org.mirth.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.FileInputStream;
import java.util.UUID;
import java.util.Iterator;
import org.apache.commons.io.IOUtils;
import org.json.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;



/**
 * Mirth Connect, AWS sample integrations
 *
 */
public class MCAWS 
{

    public static void main( String[] args ) {
	//String bucketName = "mirth-blog-data";
	System.out.println("Mirth Example Exec");
	//listbucketsitems(bucketName);

        //try {
        //    FileInputStream inputStream = new FileInputStream("/var/mirth/ccd-bb/output/ccd.xml");
        //    ccdJson = IOUtils.toString(inputStream);
        //} catch (IOException e) { System.out.println("ERROR ON FILE"); e.printStackTrace(); }

	//dynamoInsertJson(ccdJson, "mirthdata", "222333444UUID", "10-26-2016");

	//try {
	//JSONObject obj = new JSONObject(ccdJson);
	//String firstName = obj.getJSONObject("data").getJSONObject("demographics").getJSONObject("name").getString("first");
	//} catch (org.json.JSONException e) { System.out.println("JSON ERROR"); }

	//dynamoInsertJson(ccdJson, "mirthdata", "888222333444UUID", "10-26-2016");
	//System.out.println("Demographics: "+firstName+" "+lastName+" "+DOB);

	//String directory = "/tmp/";
	//String fileName = "test.txt";
	//String fileLocation = directory + fileName;
	//String key = "filename" + UUID.randomUUID() + ".txt";
	//String fileContents = "asdfhapfuapf-98rfiosafj\nap9sdfpa9udsfa\n98asdp9f8a\n";

	//putMirthS3(bucketName, key, fileLocation, fileContents);
	}

    public static void putMirthS3(String bucketName, String key, String fileLocation, String fileContents) {
	AmazonS3 s3 = new AmazonS3Client();
	Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	s3.setRegion(usWest2);
	try {
	s3.putObject(new PutObjectRequest(bucketName, key, createTmpFile(fileContents)));
	} catch (Exception e) { System.out.println("ERROR ON FILE"); }
    }

    public static void dynamoInsertJson(String ccdJson, String mirthTable, String mirthId, String mirthDate) {
        System.out.println( "Performing insert into DynamoDB" );
	String firstName = "";
	String lastName = "";
	String dob = "";
	String docType = "ccda";

        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.withRegion(Regions.US_WEST_2);
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable(mirthTable);

	//System.out.println(ccdJson);

        try {
        JSONObject obj = new JSONObject(ccdJson);
	
        firstName = obj.getJSONObject("data").getJSONObject("demographics").getJSONObject("name").getString("first");
        lastName = obj.getJSONObject("data").getJSONObject("demographics").getJSONObject("name").getString("last");
        dob = obj.getJSONObject("data").getJSONObject("demographics").getJSONObject("dob").getJSONObject("point").getString("date");
	if(docType.length() < 2) { docType = "NONE"; }
	if(firstName.length() < 2) { firstName = "NONE"; }
	if(lastName.length() < 2) { lastName = "NONE"; }
	if(dob.length() < 4) { dob = "NONE"; }

        //System.out.println(firstName);
        } catch (org.json.JSONException e) { System.out.println("JSON ERROR"); }

	ccdJson = ccdJson.replaceAll("\"\"","\"NONE\"");

        Item item = 
            new Item()
                .withPrimaryKey("mirthid", mirthId)
                .withString("mirthdate", mirthDate)
		.withString("type", docType)
                .withString("FirstName", firstName)
                .withString("LastName", lastName)
                .withString("DOB", dob)
                .withJSON("document", ccdJson);

        table.putItem(item);
    }

    public static void dynamoInsertDicom(String dicomJson, String mirthTable, String mirthId, String mirthDate) {
        System.out.println( "Performing insert into DynamoDB" );
        String firstName = "EMPTY";
        String lastName = "EMPTY";
        String dob = "EMPTY";
        String docType = "dicom";

        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.withRegion(Regions.US_WEST_2);
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable(mirthTable);

        try {
        JSONObject obj = new JSONObject(dicomJson);

        //firstName = obj.getJSONObject("data").getJSONObject("demographics").getJSONObject("name").getString("first");
        lastName = obj.getJSONObject("00100010").getJSONObject("Value").getString("Alphabetic");
        dob = obj.getJSONObject("00100030").getJSONObject("Value").getString("Alphabetic");
        if(docType.length() < 2) { docType = "NONE"; }
        if(firstName.length() < 2) { firstName = "NONE"; }
        if(lastName.length() < 2) { lastName = "NONE"; }
        if(dob.length() < 4) { dob = "NONE"; }

        } catch (org.json.JSONException e) { System.out.println("JSON ERROR"); }

        //ccdJson = ccdJson.replaceAll("\"\"","\"NONE\"");

        Item item =
            new Item()
                .withPrimaryKey("mirthid", mirthId)
                .withString("mirthdate", mirthDate)
                .withString("type", docType)
                .withString("FirstName", firstName)
                .withString("LastName", lastName)
                .withString("DOB", dob)
                .withJSON("document", dicomJson);

        table.putItem(item);
    }

    public static void listbucketsitems(String bucketName) {
	System.out.println( "Connecting to AWS" );
	System.out.println( "Listing files in bucket "+ bucketName );
	AmazonS3 s3 = new AmazonS3Client();
	Region usWest2 = Region.getRegion(Regions.US_WEST_2);
	s3.setRegion(usWest2);
	System.out.println("Listing buckets");
	ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
		.withBucketName(bucketName));
	for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
		System.out.println(" - " + objectSummary.getKey() + "  " +
		"(size = " + objectSummary.getSize() + ")");
		}
	System.out.println();
	}

    private static File createTmpFile(String fileContents) throws IOException {
        File file = File.createTempFile("mirth-ccd-tmp-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write(fileContents);
        writer.close();

        return file;
    }

}
