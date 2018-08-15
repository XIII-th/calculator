package com.xiiilab.calculator.core;

import androidx.annotation.NonNull;

/**
 * Created by XIII-th on 15.08.2018
 */
public class InputPreprocessor {

    private final String mTokenSplitRegex;

    public InputPreprocessor() {
        // TODO: 15.08.2018 get operator list from tocken processor
        String operators = "-+*/()";
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
