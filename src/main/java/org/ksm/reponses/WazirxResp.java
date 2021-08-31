package org.ksm.reponses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WazirxResp {
        String base_unit;
        String quote_unit;
        String low;
        String high;
        String last;
        String type;
        String open;
        String volume;
        String sell;
        String buy;
        String at;
        String name;
}
