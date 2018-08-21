package com.xiiilab.calculator.core;

import com.xiiilab.calculator.core.operand.Operand;
import com.xiiilab.calculator.core.operator.*;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by XIII-th on 17.08.2018
 */
public class TokenProcessorTest {

    private static final String[] TOKEN_ARRAY = {"1.1", "+", "(", "-", "2", "*", "3.", ")", "/", "4.4"};

    @Test
    public void addBinaryOperators() {
        addOperatorsTest(BinaryOperator.values());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addBinaryOperators_duplication() {
        int count = BinaryOperator.values().length;
        IBinaryOperator[] operators = Arrays.copyOf(BinaryOperator.values(), count + 1);
        operators[count] = operators[0];
        addOperatorsTest(operators);
    }

    @Test
    public void addUnaryOperators() {
        addOperatorsTest(UnaryOperator.values());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUnaryOperators_duplication() {
        int count = UnaryOperator.values().length;
        UnaryOperator[] operators = Arrays.copyOf(UnaryOperator.values(), count + 1);
        operators[count] = operators[0];
        addOperatorsTest(operators);
    }

    @Test
    public void getSupportedOperators() {
        Set<Character> expected = new HashSet<>();
        for (IBinaryOperator operator : BinaryOperator.values())
            expected.add(operator.getSymbol());
        for (IUnaryOperator operator : UnaryOperator.values())
            expected.add(operator.getSymbol());
        for (Bracket bracket : Bracket.values())
            expected.add(bracket.getSymbol());

        TokenProcessor processor = new TokenProcessor();
        processor.
                addOperators(BinaryOperator.values()).
                addOperators(Bracket.values()).
                addOperators(UnaryOperator.values());
        assertEquals(expected, processor.getSupportedOperators());
    }

    @Test
    public void toRpn_simple() {
        TokenProcessor processor = new TokenProcessor();
        processor.
                addOperators(BinaryOperator.values()).
                addOperators(UnaryOperator.values()).
                addOperators(Bracket.values());
        List<IToken> tokenList = processor.toTokenList("3 + 4 * 2 / ( 1 - 5 ) + 3 * 5".split("\\s"));
        Queue<IToken> expected = new LinkedList<>(), actual = processor.toRpn(tokenList);
        //3 4 2 * 1 5 - / + 3 5 * +
        expected.add(new Operand("3"));
        expected.add(new Operand("4"));
        expected.add(new Operand("2"));
        expected.add(BinaryOperator.MULTIPLY);
        expected.add(new Operand("1"));
        expected.add(new Operand("5"));
        expected.add(BinaryOperator.MINUS);
        expected.add(BinaryOperator.DIVIDE);
        expected.add(BinaryOperator.PLUS);
        expected.add(new Operand("3"));
        expected.add(new Operand("5"));
        expected.add(BinaryOperator.MULTIPLY);
        expected.add(BinaryOperator.PLUS);
        assertEquals(expected, actual);
    }

    @Test
    public void toTokenList() {
        TokenProcessor processor = new TokenProcessor();
        processor.
                addOperators(BinaryOperator.values()).
                addOperators(Bracket.values()).
                addOperators(UnaryOperator.values());
        List<IToken> actual = processor.toTokenList(TOKEN_ARRAY),
                expected = new LinkedList<>();
        expected.add(new Operand("1.1"));
        expected.add(BinaryOperator.PLUS);
        expected.add(Bracket.LEFT);
        expected.add(UnaryOperator.MINUS);
        expected.add(new Operand("2"));
        expected.add(BinaryOperator.MULTIPLY);
        expected.add(new Operand("3."));
        expected.add(Bracket.RIGHT);
        expected.add(BinaryOperator.DIVIDE);
        expected.add(new Operand("4.4"));
        assertEquals(expected, actual);
    }

    @Test
    public void getUnaryOperator() {
        assertEquals(UnaryOperator.MINUS, getUnaryOperator(1, "(", "-", "1"));
    }

    @Test
    public void getUnaryOperator_startOfExpression() {
        assertNull(getUnaryOperator(0, "-", "1"));
    }

    @Test
    public void getUnaryOperator_endOfExpression() {
        assertNull(getUnaryOperator(1, "(", "-"));
    }

    @Test
    public void getUnaryOperator_noBrackets() {
        assertNull(getUnaryOperator(2, "2", "+", "-", "1"));
    }


    private void addOperatorsTest(IOperator... operators) {
        TokenProcessor processor = new TokenProcessor();
        if (operators instanceof IBinaryOperator[])
            processor.addOperators((IBinaryOperator[]) operators);
        else if (operators instanceof IUnaryOperator[])
            processor.addOperators((IUnaryOperator[])operators);
        else
            throw new IllegalArgumentException("Unexpected operators array type " + operators.getClass());
        Set<Character> expected = new HashSet<>();
        for (IOperator operator : operators)
            expected.add(operator.getSymbol());
        assertEquals(expected, processor.getSupportedOperators());
    }

    private IUnaryOperator getUnaryOperator(int ptr, String... arr) {
        TokenProcessor processor = new TokenProcessor();
        processor.addOperators(UnaryOperator.values());
        IUnaryOperator operator = UnaryOperator.MINUS;
        return processor.getUnaryOperator(operator, arr, ptr);
    }
}