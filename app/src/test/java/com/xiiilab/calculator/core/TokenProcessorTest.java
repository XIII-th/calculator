package com.xiiilab.calculator.core;

import com.xiiilab.calculator.core.operator.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by XIII-th on 17.08.2018
 */
public class TokenProcessorTest {

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
    public void addIgnoredCharacters() {
        Character[] chars = {'(', ')'};
        TokenProcessor processor = new TokenProcessor();
        processor.addIgnoredCharacters(chars);
        Set<Character> expected = new HashSet<>(Arrays.asList(chars));
        Assert.assertEquals(expected, processor.getSupportedOperators());
    }

    @Test
    public void getSupportedOperators() {
        Set<Character> expected = new HashSet<>();
        for (IBinaryOperator operator : BinaryOperator.values())
            expected.add(operator.getSymbol());
        for (IUnaryOperator operator : UnaryOperator.values())
            expected.add(operator.getSymbol());
        Character[] characters = {'(', ')'};
        expected.addAll(Arrays.asList(characters));

        TokenProcessor processor = new TokenProcessor();
        processor.
                addOperators(BinaryOperator.values()).
                addOperators(UnaryOperator.values()).
                addIgnoredCharacters(characters);
        Assert.assertEquals(expected, processor.getSupportedOperators());
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
        Assert.assertEquals(expected, processor.getSupportedOperators());
    }
}