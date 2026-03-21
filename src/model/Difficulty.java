package model;

import java.math.BigDecimal;

public enum Difficulty {
    EASY(new BigDecimal("1.0")),
    MEDIUM(new BigDecimal("1.2")),
    HARD(new BigDecimal("1.5"));

    private final BigDecimal multiplier;

    Difficulty(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }
}
