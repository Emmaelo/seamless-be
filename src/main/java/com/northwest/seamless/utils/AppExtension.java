package com.northwest.seamless.utils;

import java.math.BigDecimal;
import java.util.List;

public class AppExtension {

    public void validateTotalWeight(
            List<BigDecimal> weights,
            String message) {

        BigDecimal total = weights.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(new BigDecimal("100.00")) != 0) {
            throw new IllegalArgumentException(message);
        }
    }

}
