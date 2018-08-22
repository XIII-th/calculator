package com.xiiilab.calculator.core.operand;

/**
 * Created by XIII-th on 17.08.2018
 */
public class Operand implements IOperand {

    private float mValue;

    public Operand(String numberString) throws NumberFormatException {
        mValue = Float.parseFloat(numberString);
    }

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public void setValue(float v) {
        mValue = v;
    }

    @Override
    public byte getPriority() {
        return 0;
    }

    @Override
    public String toString() {
        return String.valueOf(mValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operand)) return false;

        Operand operand = (Operand) o;

        return Float.compare(operand.mValue, mValue) == 0;
    }

    @Override
    public int hashCode() {
        return (mValue != +0.0f ? Float.floatToIntBits(mValue) : 0);
    }
}
