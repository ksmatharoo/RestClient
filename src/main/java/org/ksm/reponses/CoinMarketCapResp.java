package org.ksm.reponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@Data
public class CoinMarketCapResp {

    List<Item> data;

    public void sort() {
        data.sort(new Comparator<Item>() {
            @Override
            public int compare(Item l, Item r) {
                return l.symbol.compareTo(r.symbol);
            }
        });
    }

    public Item.Quote find(String symbol) {
        Item item = new Item();
        item.setSymbol(symbol);

        int i = Collections.binarySearch(data, item, new Comparator<Item>() {
            @Override
            public int compare(Item l, Item r) {
                return l.getSymbol().compareTo(r.getSymbol());
            }
        });

        if(i < 0)
            return null;

        return data.get(i).getQuote();
    }

    @NoArgsConstructor
    @Data
    public static class Item {
        String id;
        String name;
        String symbol;
        String max_supply;
        String circulating_supply;
        String cmc_rank;
        Quote quote;

        @NoArgsConstructor
        @Data
        public static class Quote {
            @JsonProperty("USD")
            USD usd;

            @NoArgsConstructor
            @Data
            public static class USD {
                double price;
                double volume_24h;
            }
        }
    }

}
