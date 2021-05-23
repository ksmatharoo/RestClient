package org.ksm;

import org.junit.Assert;
import org.junit.Test;
import org.ksm.reponses.AppList;

import java.io.FileReader;
import java.io.FileWriter;
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
    public void jsonTest() throws Exception {
        AppList.ApplicationList obj = new AppList.ApplicationList(
                "test", "test", "test1", "time", 10,
                "", Collections.EMPTY_LIST
        );
        AppList.ApplicationList obj1 = new AppList.ApplicationList(
                "test", "test", "test1", "time", 10,
                "", Collections.EMPTY_LIST
        );
        List<AppList.ApplicationList> list = new ArrayList<>();
        list.add(obj);
        list.add(obj1);
        AppList appResponse1 = new AppList(list);

        JsonCodec<AppList> jsonCodec = new JsonCodec();

        String s = jsonCodec.objectToString(appResponse1);
        System.out.println(s);

        FileWriter fileWriter = new FileWriter("src/test/resources/out.json");
        fileWriter.write(s);
        fileWriter.close();

        AppList appResponse11 = jsonCodec.jsonToObj(s, AppList.class);
        AppList appResponse12 = jsonCodec.
                jsonToObj(new FileReader("src/test/resources/out.json"), AppList.class);

        Assert.assertEquals("compare", appResponse11.toString(), appResponse12.toString());
    }
}