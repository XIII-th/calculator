package com.xiiilab.calculator.core;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by XIII-th on 15.08.2018
 */
public class InputPreprocessor {

    private static final Set<Character> ESCAPING_REQUIRED;

    static {
        ESCAPING_REQUIRED = new HashSet<>(5, 1F);
        // https://stackoverflow.com/a/19976308/3926506
        ESCAPING_REQUIRED.add('^');
        ESCAPING_REQUIRED.add('-');
        ESCAPING_REQUIRED.add(']');
        ESCAPING_REQUIRED.add('\\');
        ESCAPING_REQUIRED.add('/');
    }

    private final String mTokenSplitRegex;

    public InputPreprocessor(TokenProcessor tokenProcessor) {
        Set<Character> operatorSet = tokenProcessor.getSupportedOperators();
        // build complete operators string
        StringBuilder builder = new StringBuilder(operatorSet.size());
        for (Character character : operatorSet) {
            if (ESCAPING_REQUIRED.contains(character))
                builder.append('\\');
            builder.append(character.charValue());
        }
        String operators = builder.toString();
        mTokenSplitRegex = "(?<=[" + operators + "])|(?=[" + operators + "])";
    }

    public String[] getStringTokens(@NonNull String expression) {
        return removeSpaces(expression).split(mTokenSplitRegex);
    }

    public String removeSpaces(@NonNull String expression) {
        expression = expression.replaceAll("\\s", "");
        if (expression.isEmpty())
            throw new IllegalArgumentException("Unexpected empty expression");
        return expression;
    }
}
