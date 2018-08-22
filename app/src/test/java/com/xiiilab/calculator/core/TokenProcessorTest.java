package com.xiiilab.calculator.core;

import com.xiiilab.calculator.core.operator.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by XIII-th on 17.08.2018
 */
public class TokenProcessorTest {

    private static final Supplier<TokenProcessor> TOKEN_PROCESSOR_SUPPLIER = () -> {
        TokenProcessor processor = new TokenProcessor();
        processor.
                addOperators(BinaryOperator.values()).
                addOperators(Bracket.values()).
                addOperators(UnaryOperator.values());
        return processor;
    };

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
        TokenProcessor processor = TOKEN_PROCESSOR_SUPPLIER.get();
        List<IToken> tokenList = processor.toTokenList(Samples.SAMPLE_2_SPLIT_EXPRESSION.get());
        assertEquals(Samples.SAMPLE_2_TOKEN_EXPRESSION.get(), processor.toRpn(tokenList));
    }

    @Test
    public void toRpn_doubleBracket() {
        TokenProcessor processor = TOKEN_PROCESSOR_SUPPLIER.get();
        // sample from https://habr.com/post/100869/
        List<IToken> tokenList = processor.toTokenList(Samples.SAMPLE_3_SPLIT_EXPRESSION.get());
        assertEquals(Samples.SAMPLE_3_TOKEN_EXPRESSION.get(), processor.toRpn(tokenList));
    }

    @Test
    public void toTokenList() {
        TokenProcessor processor = TOKEN_PROCESSOR_SUPPLIER.get();
        List<IToken> tokenList = processor.toTokenList(Samples.SAMPLE_1_SPLIT_EXPRESSION.get());
        assertEquals(Samples.SAMPLE_1_TOKEN_EXPRESSION.get(), tokenList);
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
            processor.addOperators((IUnaryOperator[]) operators);
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