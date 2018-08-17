package com.xiiilab.calculator.core.operand;

/**
 * Created by XIII-th on 17.08.2018
 */
public class Operand implements IOperand {

    private final float mValue;

    public Operand(String numberString) throws NumberFormatException {
        mValue = Float.parseFloat(numberString);
    }

    @Override
    public float getValue() {
        return mValue;
    }

    @Override
    public byte getPriority() {
        return 0;
    }
}
