package org.ksm.reponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ZebPayResponse {
        double buy ;
        double sell ;
        double market;
        String volume;
        String pricechange;
        @JsonProperty("24hoursHigh")
        String hoursHigh;
        @JsonProperty("24hoursLow")
        String hoursLow;
        String pair;
        String virtualCurrency;
        String currency;
        String instantBuy;
        String instantSell;

        Double ratio;
        Double coinCapPrice;
        double sellUSD ;
}


