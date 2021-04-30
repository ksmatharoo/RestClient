package org.ksm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Reader;
import java.util.List;

public class JsonUtils {
    static Gson gson = null;

    public static Gson getGsonInstance() {
        if (gson == null) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        }
        return gson;
    }

    public static String objectToString(AppResponse1 obj) {
        return getGsonInstance().toJson(obj);
    }

    public static AppResponse1 jsonToObj(Reader json) {
        return getGsonInstance().fromJson(json, AppResponse1.class);
    }

    public static AppResponse1 jsonToObj(String json) {
        return getGsonInstance().fromJson(json, AppResponse1.class);
    }

    @Data
    @AllArgsConstructor
    public static class AppResponse1 {
        List<ApplicationList> applicationList;
    }

    @Data
    @AllArgsConstructor
    public static class ApplicationList {
        String name;
        String camr;
        String appSubscriptionID;
        String registerTimestamp;
        int maxPageSize;
        String status;
        List<String> entityEnrolled;
    }

}
