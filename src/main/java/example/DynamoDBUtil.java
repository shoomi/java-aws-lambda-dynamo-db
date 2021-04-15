package example;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import pojo.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDBUtil {

    protected static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private static DynamoDBMapper mapper = new DynamoDBMapper(client);

    public static void persistWithMapper(PersonRequest personRequest) {
        mapper.save(personRequest);
    }

    public static PersonRequest getItem(int hashKey) {
        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
                .build();

        return mapper.load(PersonRequest.class, hashKey, config);
    }

    public static List<PersonRequest> findPersonsWithAgeLessThan(String value) {
        List<PersonRequest> personRequests = new ArrayList<>();
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withN(value));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("age < :val1").withExpressionAttributeValues(eav);

        List<PersonRequest> scanResult = mapper.scan(PersonRequest.class, scanExpression);

        for (PersonRequest person : scanResult) {
            personRequests.add(person);
            System.out.println(person);
        }
        return personRequests;
    }


//    private PutItemOutcome persistData(PersonRequest personRequest)
//            throws ConditionalCheckFailedException {
//       this.dynamoDb = new DynamoDB(client);
//        String DYNAMODB_TABLE_NAME = "Person";
//        return this.dynamoDb.getTable(DYNAMODB_TABLE_NAME)
//                .putItem(
//                        new PutItemSpec().withItem(new Item()
//                                .withPrimaryKey("id", personRequest.getId())
//                                .withString("firstName", personRequest.getFirstName())
//                                .withString("lastName", personRequest.getLastName())));
//    }

}
