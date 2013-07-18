package com.swooli.bo.ranking;

/**
 * "Z" value for a normal distribution.
 * @author jmcarey
 */
public enum ZValue {

    Z_70(1.04),
    Z_75(1.15),
    Z_80(1.28),
    Z_85(1.44),
    Z_90(1.645),
    Z_95(1.96),
    Z_99(2.575);

    private double zValue;

    ZValue(final double zValue) {
        this.zValue = zValue;
    }

    public double zValue() {
        return zValue;
    }
}
