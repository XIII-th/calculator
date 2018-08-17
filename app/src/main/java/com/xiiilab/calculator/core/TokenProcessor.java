package com.xiiilab.calculator.core;

import com.xiiilab.calculator.core.operator.IBinaryOperator;
import com.xiiilab.calculator.core.operator.IUnaryOperator;

import java.util.*;

/**
 * Created by XIII-th on 16.08.2018
 */
public class TokenProcessor {

    private final Map<Character, IBinaryOperator> mIBinaryOperatorMap;
    private final Map<Character, IUnaryOperator> mIUnaryOperatorMap;
    private final Set<Character> mIgnoredTokens;

    public TokenProcessor() {
        mIBinaryOperatorMap = new HashMap<>();
        mIUnaryOperatorMap = new HashMap<>();
        mIgnoredTokens = new HashSet<>();
    }

    public TokenProcessor addOperators(IBinaryOperator... operators) {
        for (IBinaryOperator operator : operators)
            if (mIBinaryOperatorMap.put(operator.getSymbol(), operator) != null)
                throw new IllegalArgumentException("Unexpected duplication of binary operator " + operator.getSymbol());
        return this;
    }

    public TokenProcessor addOperators(IUnaryOperator... operators) {
        for (IUnaryOperator operator : operators)
            if (mIUnaryOperatorMap.put(operator.getSymbol(), operator) != null)
                throw new IllegalArgumentException("Unexpected duplication of unary operator " + operator.getSymbol());
        return this;
    }

    public TokenProcessor addIgnoredCharacters(Character... chars) {
        mIgnoredTokens.addAll(Arrays.asList(chars));
        return this;
    }

    public Set<Character> getSupportedOperators() {
        // add ignored characters as supported tokens
        Set<Character> characterSet = new HashSet<>(mIgnoredTokens);
        // add all binary operators as tokens
        characterSet.addAll(mIBinaryOperatorMap.keySet());
        // and unary operators
        characterSet.addAll(mIUnaryOperatorMap.keySet());
        return characterSet;
    }
}
