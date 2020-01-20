package io.mywish.duc.blockchain.condition;

public interface BlockConditional extends Conditional {
    String getNetwork();
    int getLastBlock();
}
