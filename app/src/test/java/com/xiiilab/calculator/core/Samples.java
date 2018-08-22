package com.xiiilab.calculator.core;

import com.xiiilab.calculator.core.operand.Operand;
import com.xiiilab.calculator.core.operator.BinaryOperator;
import com.xiiilab.calculator.core.operator.Bracket;
import com.xiiilab.calculator.core.operator.UnaryOperator;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * Created by XIII-th on 22.08.2018
 */
class Samples {

    public static final Supplier<String> SAMPLE_1_EXPRESSION = () -> "1.1 + ( - 2 * 3. ) / 4.4";
    public static final Supplier<String[]> SAMPLE_1_SPLIT_EXPRESSION = () -> SAMPLE_1_EXPRESSION.get().split("\\s");
    public static final Supplier<Queue<IToken>> SAMPLE_1_TOKEN_EXPRESSION = () -> {
        Queue<IToken> tokenQueue = new LinkedList<>();
        tokenQueue.add(new Operand("1.1"));
        tokenQueue.add(BinaryOperator.PLUS);
        tokenQueue.add(Bracket.LEFT);
        tokenQueue.add(UnaryOperator.MINUS);
        tokenQueue.add(new Operand("2"));
        tokenQueue.add(BinaryOperator.MULTIPLY);
        tokenQueue.add(new Operand("3."));
        tokenQueue.add(Bracket.RIGHT);
        tokenQueue.add(BinaryOperator.DIVIDE);
        tokenQueue.add(new Operand("4.4"));
        return tokenQueue;
    };

    public static final Supplier<String> SAMPLE_2_EXPRESSION = () -> "3 + 4 * 2 / ( 1 - 5 ) + 3 * 5";
    public static final Supplier<String[]> SAMPLE_2_SPLIT_EXPRESSION = () -> SAMPLE_2_EXPRESSION.get().split("\\s");
    public static final Supplier<Queue<IToken>> SAMPLE_2_TOKEN_EXPRESSION = () -> {
        Queue<IToken> tokenQueue = new LinkedList<>();
        // 3 4 2 * 1 5 - / + 3 5 * +
        tokenQueue.add(new Operand("3"));
        tokenQueue.add(new Operand("4"));
        tokenQueue.add(new Operand("2"));
        tokenQueue.add(BinaryOperator.MULTIPLY);
        tokenQueue.add(new Operand("1"));
        tokenQueue.add(new Operand("5"));
        tokenQueue.add(BinaryOperator.MINUS);
        tokenQueue.add(BinaryOperator.DIVIDE);
        tokenQueue.add(BinaryOperator.PLUS);
        tokenQueue.add(new Operand("3"));
        tokenQueue.add(new Operand("5"));
        tokenQueue.add(BinaryOperator.MULTIPLY);
        tokenQueue.add(BinaryOperator.PLUS);
        return tokenQueue;
    };

    public static final Supplier<String> SAMPLE_3_EXPRESSION = () -> "( 8 + 2 * 5 ) / ( 1 + 3 * 2 - 4 )";
    public static final Supplier<String[]> SAMPLE_3_SPLIT_EXPRESSION = () -> SAMPLE_3_EXPRESSION.get().split("\\s");
    public static final Supplier<Queue<IToken>> SAMPLE_3_TOKEN_EXPRESSION = () -> {
        Queue<IToken> tokenQueue = new LinkedList<>();
        // 8 2 5 * + 1 3 2 * + 4 - /
        tokenQueue.add(new Operand("8"));
        tokenQueue.add(new Operand("2"));
        tokenQueue.add(new Operand("5"));
        tokenQueue.add(BinaryOperator.MULTIPLY);
        tokenQueue.add(BinaryOperator.PLUS);
        tokenQueue.add(new Operand("1"));
        tokenQueue.add(new Operand("3"));
        tokenQueue.add(new Operand("2"));
        tokenQueue.add(BinaryOperator.MULTIPLY);
        tokenQueue.add(BinaryOperator.PLUS);
        tokenQueue.add(new Operand("4"));
        tokenQueue.add(BinaryOperator.MINUS);
        tokenQueue.add(BinaryOperator.DIVIDE);
        return tokenQueue;
    };
}
