package com.xiiilab.calculator.core.operator;

import com.xiiilab.calculator.core.operand.IOperand;

/**
 * Created by XIII-th on 17.08.2018
 */
public enum BinaryOperator implements IBinaryOperator {
    PLUS('+', 1) {
        @Override
        public float apply(IOperand o1, IOperand o2) {
            return o1.getValue() + o2.getValue();
        }
    },
    MINUS('-', 1) {
        @Override
        public float apply(IOperand o1, IOperand o2) {
            return o1.getValue() - o2.getValue();
        }
    },
    MULTIPLY('*', 2) {
        @Override
        public float apply(IOperand o1, IOperand o2) {
            return o1.getValue() * o2.getValue();
        }
    },
    DIVIDE('/', 2) {
        @Override
        public float apply(IOperand o1, IOperand o2) {
            return o1.getValue() / o2.getValue();
        }
    };

    private final char mSymbol;
    private final byte mPriority;

    BinaryOperator(char symbol, int priority) {

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
        return getSymbol() + "b";
    }
}
