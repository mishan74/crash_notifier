package io.mywish.duc.blockchain.condition.ducatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class DucBlock {
    private String _id;
    private String chain;
    private String network;
    private String hash;
    private Integer height;
    private long version;
    private String merkleRoot;
    private String time;
}
