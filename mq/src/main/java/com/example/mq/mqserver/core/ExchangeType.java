package com.example.mq.mqserver.core;

public enum ExchangeType {
    Direct(0),
    FANOUT(1),
    TOPIC(2);

    private final int type;

    private ExchangeType(int type) {
        this.type = type;
    }

    private int getType() {
        return type;
    }
}
