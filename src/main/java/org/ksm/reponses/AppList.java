package org.ksm.reponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppList {
    List<ApplicationList> applicationList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
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

