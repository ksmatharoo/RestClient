package org.ksm;
import org.junit.Test;
import java.net.URISyntaxException;

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
}