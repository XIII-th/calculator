package com.xiiilab.calculator.core.operand;

import com.xiiilab.calculator.core.CalculatorException;

import java.util.Objects;

/**
 * Created by XIII-th on 17.08.2018
 */
public class Operand implements IOperand {

    private double mValue;

    public Operand(String numberString) {
        try {
            mValue = Double.parseDouble(numberString);
        } catch (NumberFormatException e) {
            throw new CalculatorException(CalculatorException.Type.NUMBER_FORMAT, numberString);
        }
    }

    @Override
    public double getValue() {
        return mValue;
    }

    @Override
    public void setValue(double v) {
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

        return Double.compare(operand.mValue, mValue) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mValue);
    }
}
