package org.ksm;

import org.junit.Test;



public class RestApiClientTest {

    @Test
    public void test(){
        RestApiClient client = new RestApiClient(5000,500,"UTF-8",null);
        String s = client.processGETRequest("https://www.google.com/");
        System.out.println(s);
    }

    @Test
    public void testFailed(){

        RestApiClient client = new RestApiClient(5000,500,"UTF-8",null);
        String s = client.processGETRequest("https://mvnrepository.com/artifact/org.slf4j/slf4j-simple/1.7.15");
        System.out.println(s);
    }


}