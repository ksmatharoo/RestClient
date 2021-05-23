package org.ksm;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.ksm.reponses.CoinMarketCapResp;
import org.ksm.reponses.ZebPayResponse;

import java.util.*;
import java.util.stream.Collectors;

public class AppMain {
    public static void main(String[] args) throws Exception {
        Options options = OptionsParser.buildOptions();
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, args, false);
        Properties properties = OptionsParser.retrieveAllProperties(cmd, null);

        RestApiClient restApiClient = new RestApiClient(500, 500,
                null, null, null);
        final String response = restApiClient.processGETRequest(properties.getProperty(Constants.ZEBPAY_URL));

        double usdToInr = Double.parseDouble(properties.getProperty(Constants.USD_INR));

        JsonCodec<ZebPayResponse> jsonCodec = new JsonCodec<>();
        ZebPayResponse[] zebPayResponse = jsonCodec.getMapper().readValue(response, ZebPayResponse[].class);

        List<ZebPayResponse> zeblist = Arrays.stream(zebPayResponse).
                filter(obj -> (obj.getCurrency().contains("INR") && obj.getVolume() != null)).map(obj -> {
            obj.setBuy(obj.getBuy() / usdToInr);
            obj.setSellUSD(obj.getSell() / usdToInr);
            obj.setMarket(obj.getMarket() / usdToInr);
            return obj;
        }).collect(Collectors.toList());

        zeblist.sort(new Comparator<ZebPayResponse>() {
            @Override
            public int compare(ZebPayResponse l, ZebPayResponse r) {
                return l.getVirtualCurrency().compareTo(r.getVirtualCurrency());
            }
        });

        System.out.println("Printing data");
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        zeblist.forEach(obj -> {
            String s = String.format("%s Buy: %, 10.2f $  Sell: %, 10.2f $  Market: %, 10.2f $",
                    padString(obj.getVirtualCurrency()),
                    obj.getBuy(), obj.getSellUSD(), obj.getMarket());
            System.out.println(s);
        });
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("USD to INR used :" + usdToInr);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        Map<String, String> headers = new HashMap<>();
        headers.put(properties.getProperty(Constants.API_KEY),
                properties.getProperty(Constants.API_KEY_VALUE));

        restApiClient.setHeaders(headers);
        final String response1 = restApiClient.
                processGETRequest(properties.getProperty(Constants.COIN_MARKET_CAP_URL));

        JsonCodec<CoinMarketCapResp> jsonCodec1 = new JsonCodec<>();
        CoinMarketCapResp capResp = jsonCodec1.getMapper().readValue(response1, CoinMarketCapResp.class);
        //sort by symbol
        capResp.sort();

        System.out.println(capResp);


        for (ZebPayResponse zeb : zeblist) {
            CoinMarketCapResp.Item.Quote quote = capResp.find(zeb.getVirtualCurrency());
            if (Objects.nonNull(quote)) {
                zeb.setRatio(((zeb.getSellUSD() - quote.getUsd().getPrice()) / quote.getUsd().getPrice()) * 100);
                zeb.setCoinCapPrice(quote.getUsd().getPrice());
            } else {
                zeb.setRatio(new Double(-1.0));
                zeb.setCoinCapPrice(new Double(-1.0));
            }
        }

        List<ZebPayResponse> copy = new ArrayList<>(zeblist);

        zeblist.sort(new Comparator<ZebPayResponse>() {
            @Override
            public int compare(ZebPayResponse l, ZebPayResponse r) {
                return l.getRatio().compareTo(r.getRatio());
            }
        });

        System.out.println("Printing data");
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("USD to INR used :" + usdToInr);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("         ZebPaySell    ZebPaySell(INR)     CoinMktCap   Ratio");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        zeblist.forEach(obj -> {
            String s = String.format("%s  %, 10.2f $    %, 15.2f   %, 10.2f $   %, 5.2f",
                    padString(obj.getVirtualCurrency()),
                    obj.getSellUSD(), obj.getSell(), obj.getCoinCapPrice(), obj.getRatio());
            System.out.println(s);
        });
        System.out.println("");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

    }

    public static String padString(String in) {
        if (in.length() == 3) {
            return "  " + in;
        } else if (in.length() == 4) {
            return " " + in;
        } else {
            return in;
        }
    }

}
