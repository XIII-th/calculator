package com.xiiilab.calculator.core.operator;

/**
 * Created by XIII-th on 20.08.2018
 */
public enum Bracket implements IOperator {
    LEFT('(', 3),
    RIGHT(')', 3);

    private final char mSymbol;
    private final byte mPriority;

    Bracket(char symbol, int priority) {
        mSymbol = symbol;
        mPriority = (byte) priority;
    }

    @Override
    public char getSymbol() {
        return mSymbol;
    }

    @Override
    public byte getPriority() {
        return mPriority;
    }


    @Override
    public String toString() {
        return String.valueOf(getSymbol());
    }
}
