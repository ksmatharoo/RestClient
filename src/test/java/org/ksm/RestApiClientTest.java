package org.ksm;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestApiClientTest {

    @Test
    public void test() throws URISyntaxException {
        RestApiClient client = new RestApiClient(5000, 500, "UTF-8", null, null);
        String s = client.processGETRequest("https://www.google.com/");
        System.out.println(s);
    }

    @Test
    public void testFailed() throws URISyntaxException {

        RestApiClient client = new RestApiClient(5000, 500, "UTF-8", null, null);
        String s = client.processGETRequest("https://mvnrepository.com/artifact/org.slf4j/slf4j-simple/1.7.15");
        System.out.println(s);
    }

    @Test
    public void jsonTest() throws IOException {
        JsonUtils.ApplicationList obj = new JsonUtils.ApplicationList(
                "test", "test", "test1", "time", 10,
                "", Collections.EMPTY_LIST
        );
        JsonUtils.ApplicationList obj1 = new JsonUtils.ApplicationList(
                "test", "test", "test1", "time", 10,
                "", Collections.EMPTY_LIST
        );
        List<JsonUtils.ApplicationList> list = new ArrayList<>();
        list.add(obj);
        list.add(obj1);
        JsonUtils.AppResponse1 appResponse1 = new JsonUtils.AppResponse1(list);

        String s = JsonUtils.objectToString(appResponse1);
        System.out.println(s);

        FileWriter fileWriter = new FileWriter("src/test/resources/out.json");
        fileWriter.write(s);
        fileWriter.close();

        JsonUtils.AppResponse1 appResponse11 = JsonUtils.jsonToObj(s);
        JsonUtils.AppResponse1 appResponse12 = JsonUtils.
                jsonToObj(new FileReader("src/test/resources/out.json"));

        Assert.assertEquals("compare", appResponse11.toString(), appResponse12.toString());
    }
}