package com.xiiilab.calculator.core.operator;

import com.xiiilab.calculator.core.operand.IOperand;

/**
 * Created by XIII-th on 17.08.2018
 */
public enum UnaryOperator implements IUnaryOperator {

    MINUS('-', 3) {
        @Override
        public float apply(IOperand o) {
            return - o.getValue();
        }
    };

    private final char mSymbol;
    private final byte mPriority;

    UnaryOperator(char symbol, int priority) {
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
}
