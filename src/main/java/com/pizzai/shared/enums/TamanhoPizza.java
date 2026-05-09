package com.pizzai.shared.enums;

import java.math.BigDecimal;

public enum TamanhoPizza {
    PEQUENA(4, new BigDecimal("25.00")),
    MEDIA(6, new BigDecimal("38.00")),
    GRANDE(8, new BigDecimal("52.00")),
    FAMILIA(12, new BigDecimal("68.00"));

    private final int fatias;
    private final BigDecimal precoBase;

    TamanhoPizza(int fatias, BigDecimal precoBase) {
        this.fatias = fatias;
        this.precoBase = precoBase;
    }

    public int getFatias() {
        return fatias;
    }

    public BigDecimal getPrecoBase() {
        return precoBase;
    }
}
