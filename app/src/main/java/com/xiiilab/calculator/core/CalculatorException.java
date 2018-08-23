package com.xiiilab.calculator.core;

/**
 * Created by XIII-th on 23.08.2018
 */
public class CalculatorException extends IllegalArgumentException {

    public final Type mType;

    public CalculatorException(Type type, String message) {
        super(message);
        mType = type;
    }

    public CalculatorException(Type type) {
        this(type, null);
    }

    public enum Type {
        NUMBER_FORMAT,
        BRACKET_MISMATCH,
        EMPTY_BRACKET,
        OPERATOR_BALANCE
    }
}
