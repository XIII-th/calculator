package com.xiiilab.calculator.core;

import com.xiiilab.calculator.core.operator.BinaryOperator;
import com.xiiilab.calculator.core.operator.Bracket;
import com.xiiilab.calculator.core.operator.UnaryOperator;
import org.junit.Test;

import java.util.Random;
import java.util.function.Function;

import static org.junit.Assert.*;

/**
 * Created by XIII-th on 15.08.2018
 */
public class InputPreprocessorTest {

    private static final TokenProcessor TOKEN_PROCESSOR;

    static {
        TOKEN_PROCESSOR = new TokenProcessor();
        TOKEN_PROCESSOR.
                addOperators(BinaryOperator.values()).
                addOperators(UnaryOperator.values()).
                addOperators(Bracket.values());
    }

    private static final Random RANDOM = new Random();
    private static final char[] SPACE_CHARS = {' ', '\t', '\n'};
    private static final byte MAX_SPACE_COUNT = 5;
    private static final InputPreprocessor INPUT_PREPROCESSOR = new InputPreprocessor(TOKEN_PROCESSOR);

    @Test(expected = IllegalArgumentException.class)
    public void removeSpaces_emptyString() {
        String result = INPUT_PREPROCESSOR.removeSpaces("");
    }

    @Test
    public void removeSpaces() {
        String result = INPUT_PREPROCESSOR.removeSpaces("\t1 2 \r\n 3 4    56    7  8 \t \n 9\t0   \t");
        assertEquals("1234567890", result);
    }

    @Test
    public void getStringTokens_simpleExpression() {
        String expression = "1 + 2 - 3 * 4 / 5";
        expressionTest(expression, INPUT_PREPROCESSOR::getStringTokens);
    }

    @Test
    public void getStringTokens_multiCharacterIntegers() {
        String expression = "11 + 22 - 33 * 44 / 55";
        expressionTest(expression, INPUT_PREPROCESSOR::getStringTokens);
    }

    @Test
    public void getStringTokens_floatingPointNumbers() {
        String expression = ".1 + 2.2 - 33. * 0.44 / 55.0";
        expressionTest(expression, INPUT_PREPROCESSOR::getStringTokens);
    }

    @Test
    public void getStringTokens_mixedNumbers() {
        String expression = ".1 + 2.2 - 33. * 0.44 / 55";
        expressionTest(expression, INPUT_PREPROCESSOR::getStringTokens);
    }

    @Test
    public void getStringTokens_brackets() {
        String expression = "( 1 + 2 ) / 3 - ( 4 * ( 5 + 6 ) )";
        expressionTest(expression, INPUT_PREPROCESSOR::getStringTokens);
    }

    @Test
    public void getStringTokens_illegalBracketsOrder() {
        String expression = "( 1 + 2 ) / 3 - ( 4 * ( 5 + 6 ) )";
        expressionTest(expression, INPUT_PREPROCESSOR::getStringTokens);
    }

    @Test
    public void getStringTokens_startUnaryOperator() {
        String expression = "- 1 + 2";
        expressionTest(expression, INPUT_PREPROCESSOR::getStringTokens);
    }

    @Test
    public void getStringTokens_bracketsUnaryOperator() {
        String expression = "1 + ( - 2 )";
        expressionTest(expression, INPUT_PREPROCESSOR::getStringTokens);
    }

    @Test
    public void getStringTokens_bracketsStartUnaryOperator() {
        String expression = "1 + ( - 2 * 3 )";
        expressionTest(expression, INPUT_PREPROCESSOR::getStringTokens);
    }

    private void expressionTest(String expression, Function<String, String[]> f) {
        String[] tokens = trivialSplit(expression);
        expression = expressionComposer(tokens);
        System.out.print("Test with expression '" + expression + '\'');
        assertArrayEquals(tokens, f.apply(expression));
        System.out.println(" --> PASSED");
    }

    private String[] trivialSplit(String expression) {
        return expression.split("\\s");
    }

    private String expressionComposer(String[] tokenArray) {
        StringBuilder expression = new StringBuilder();
        for (String token : tokenArray) {
            appendSpaces(expression);
            expression.append(token);
        }
        appendSpaces(expression);
        return expression.toString();
    }

    private void appendSpaces(StringBuilder expression) {
        for (byte i = 0, limit = (byte) RANDOM.nextInt(MAX_SPACE_COUNT); i < limit; i++)
            expression.append(SPACE_CHARS[RANDOM.nextInt(SPACE_CHARS.length - 1)]);
    }
}