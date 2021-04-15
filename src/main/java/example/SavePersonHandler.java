package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import pojo.PersonRequest;

public class SavePersonHandler implements RequestHandler<PersonRequest, String> {

    public String handleRequest(PersonRequest personRequest, Context context) {
        context.getLogger().log("input" + personRequest.toString());

        DynamoDBUtil.persistWithMapper(personRequest);

        return String.format("Item {%s} has been saved Successfully. List of users %s",
                DynamoDBUtil.getItem(personRequest.getId()).toString(), DynamoDBUtil.findPersonsWithAgeLessThan( "43"));
    }

}
