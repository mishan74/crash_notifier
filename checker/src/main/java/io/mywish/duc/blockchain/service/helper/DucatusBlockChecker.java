package io.mywish.duc.blockchain.service.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mywish.duc.blockchain.condition.BlockConditional;
import io.mywish.duc.blockchain.condition.ducatus.DucBlock;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

@Slf4j
public class DucatusBlockChecker {
    private final BlockConditional blockCondition;
    private final String uri;
    private final String suffix;
    private Integer lastBlock;

    public DucatusBlockChecker(BlockConditional blockCondition, String uri, String suffix) {
        this.blockCondition = blockCondition;
        this.uri = uri;
        this.suffix = suffix;
    }


    public void doRead() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DucBlock[] blocks = objectMapper.readValue(new URL(uri.concat(suffix)), DucBlock[].class);
            lastBlock = blocks[0].getHeight();
        } catch (IOException e) {
            log.warn("can't to connect to {} with Exception message: {}", blockCondition.getNetwork(), e.getMessage());
            log.warn("Exception stack trace: {}", Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        } finally {
            blockCondition.updateCondition(lastBlock == null ? blockCondition.getLastBlock() : lastBlock);
        }
    }
}
