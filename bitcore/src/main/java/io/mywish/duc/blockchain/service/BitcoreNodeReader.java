package io.mywish.duc.blockchain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mywish.duc.blockchain.model.DucBlock;
import io.mywish.duc.blockchain.model.DucatusCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

@Slf4j
public class BitcoreNodeReader {
    @Autowired
    private DucatusCondition condition;
    private Integer lastBlock;

    @Scheduled(cron = "${io.mywish.duc.blockchain.cron.sheduler}")
    public void doRead() {
        log.info("start sheduling ducatus request");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("try to connect ducatus bitcore");
            DucBlock[] ducBlocks = objectMapper.readValue(new URL("https://ducapi.rocknblock.io/api/DUC/mainnet/block?limit=1"), DucBlock[].class);
            lastBlock = ducBlocks[0].getHeight();
        } catch (IOException e) {
            log.warn("can't to connect to ducatus bitcore with Exception message: {}", e.getMessage());
            log.warn("Exception stack trace: {}", Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        } finally {
            condition.setConditions(lastBlock == null ? condition.getLastBlock() : lastBlock);
        }
    }
}
