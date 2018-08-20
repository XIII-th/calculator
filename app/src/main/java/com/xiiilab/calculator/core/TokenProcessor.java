package com.xiiilab.calculator.core;

import com.xiiilab.calculator.core.operand.Operand;
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

    public List<IToken> toTokenList(String... tokenArray) {
        List<IToken> tokenList = new LinkedList<>();
        for (int ptr = 0; ptr < tokenArray.length; ptr++) {
            String tokenStr = tokenArray[ptr];
            if (tokenStr.length() == 1) {
                Character c = tokenStr.charAt(0);
                if (mIgnoredTokens.contains(c))
                    continue;
                IToken token = null;
                if (mIUnaryOperatorMap.containsKey(c))
                    token = getUnaryOperator(mIUnaryOperatorMap.get(c), tokenArray, ptr);
                if (token == null)
                    token = mIBinaryOperatorMap.get(c);
                if (token == null)
                    token = new Operand(tokenStr);
                tokenList.add(token);
            } else
                tokenList.add(new Operand(tokenStr));
        }
        return tokenList;
    }

    /**
     * Unary operator format (operator operand)
     */
    protected IUnaryOperator getUnaryOperator(IUnaryOperator operator, String[] tokenArray, int ptr) {
        // start or end of expression
        if (ptr == 0 || ptr == tokenArray.length - 1)
            return null;
        String prev = tokenArray[ptr - 1];
        // previous token is operand
        if (prev.length() > 1)
            return null;
        Character c = prev.charAt(0);
        return c == '(' ? operator : null;
    }
}
