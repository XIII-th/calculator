package com.xiiilab.calculator.core;

import android.support.annotation.NonNull;
import com.xiiilab.calculator.core.operand.IOperand;
import com.xiiilab.calculator.core.operand.Operand;
import com.xiiilab.calculator.core.operator.Bracket;
import com.xiiilab.calculator.core.operator.IBinaryOperator;
import com.xiiilab.calculator.core.operator.IOperator;
import com.xiiilab.calculator.core.operator.IUnaryOperator;

import java.util.*;

/**
 * Created by XIII-th on 16.08.2018
 */
public class TokenProcessor {

    private final Map<Character, IOperator> mOperatorMap;
    private final Map<Character, IUnaryOperator> mIUnaryOperatorMap;

    public TokenProcessor() {
        mOperatorMap = new HashMap<>();
        mIUnaryOperatorMap = new HashMap<>();
    }

    public TokenProcessor addOperators(IOperator... operators) {
        for (IOperator operator : operators)
            if (mOperatorMap.put(operator.getSymbol(), operator) != null)
                throw new IllegalArgumentException("Unexpected duplication of operator " + operator.getSymbol());
        return this;
    }

    public TokenProcessor addOperators(IUnaryOperator... operators) {
        for (IUnaryOperator operator : operators)
            if (mIUnaryOperatorMap.put(operator.getSymbol(), operator) != null)
                throw new IllegalArgumentException("Unexpected duplication of unary operator " + operator.getSymbol());
        return this;
    }

    public Set<Character> getSupportedOperators() {
        // add all binary operators as tokens
        Set<Character> characterSet = new HashSet<>(mOperatorMap.keySet());
        // and unary operators
        characterSet.addAll(mIUnaryOperatorMap.keySet());
        return characterSet;
    }

    public Queue<IToken> toRpn(String... tokens) {
        List<IToken> tokenList = toTokenList(tokens);
        checkBrackets(tokenList);
        checkOperatorSequence(tokenList);
        return toRpn(tokenList);
    }

    protected Queue<IToken> toRpn(List<IToken> tokens) {
        Queue<IToken> out = new LinkedList<>();
        Deque<IToken> stack = new LinkedList<>();
        // https://en.wikipedia.org/wiki/Shunting-yard_algorithm#The_algorithm_in_detail
        for (IToken token : tokens) {
            if (token instanceof IOperand)
                out.add(token);
            else if (token == Bracket.LEFT)
                stack.add(token);
            else if (token == Bracket.RIGHT) {
                IToken op;
                // TODO: issue with empty brackets
                while ((op = stack.pollLast()) != Bracket.LEFT && !stack.isEmpty())
                    out.add(op);
                if (op != Bracket.LEFT)
                    throw new IllegalStateException("Unable to obtain open bracket");
            } else if (token instanceof IOperator) {
                IToken op;
                while ((op = stack.peekLast()) != null && op.getPriority() >= token.getPriority() && op != Bracket.LEFT)
                    out.add(stack.pollLast());
                stack.add(token);
            }
        }
        IToken token;
        while ((token = stack.pollLast()) != null)
            if (token == Bracket.LEFT)
                throw new IllegalStateException("Unable to obtain open bracket");
            else
                out.add(token);
        return out;
    }

    protected List<IToken> toTokenList(String... tokenArray) {
        List<IToken> tokenList = new LinkedList<>();
        for (int ptr = 0; ptr < tokenArray.length; ptr++) {
            String tokenStr = tokenArray[ptr];
            if (tokenStr.length() == 1) {
                Character c = tokenStr.charAt(0);
                IToken token = null;
                if (mIUnaryOperatorMap.containsKey(c))
                    token = getUnaryOperator(mIUnaryOperatorMap.get(c), tokenArray, ptr);
                if (token == null)
                    token = mOperatorMap.get(c);
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
        String prev = tokenArray[ptr - 1], next = tokenArray[ptr + 1];
        // previous token is operand
        if (prev.length() > 1)
            return null;
        Character c = prev.charAt(0);
        return c == '(' && next.matches("^[0-9.(]+$") ? operator : null;
    }

    protected void checkBrackets(List<IToken> tokenList) {
        int counter = 0;
        IToken prev = null;
        for (IToken token : tokenList) {
            if (token == Bracket.LEFT)
                counter++;
            else if (token == Bracket.RIGHT) {
                if (prev == Bracket.LEFT)
                    throw new CalculatorException(CalculatorException.Type.EMPTY_BRACKET);
                counter--;
            }
            prev = token;
            if (counter < 0)
                throw new CalculatorException(CalculatorException.Type.BRACKET_MISMATCH);
        }
        if (counter > 0)
            throw new CalculatorException(CalculatorException.Type.BRACKET_MISMATCH);
    }

    protected void checkOperatorSequence(List<IToken> tokenList) {
        if (tokenList.isEmpty())
            return;

        Iterator<IToken> tokenIterator = tokenList.iterator();
        IToken left = tokenIterator.next();
        if (tokenList.size() < 2){
            if (!(left instanceof IOperand))
                throw getSequenceError(left, null, null);
            return;
        }
        IToken center = tokenIterator.next(), right;
        if (left instanceof IBinaryOperator)
            throw getSequenceError(left, center, null);
        if (!tokenIterator.hasNext()) {
            if (center instanceof IOperand)
                throw getSequenceError(left, center, null);
        } else while (tokenIterator.hasNext()) {
            right = tokenIterator.next();
            // TODO: replace to predicate's set
            if (center instanceof IBinaryOperator && (!leftOperand(left) || !rightOperand(right)))
                throw getSequenceError(left, center, right);
            else if (center instanceof IUnaryOperator && (left != Bracket.LEFT || !rightOperand(right)))
                throw getSequenceError(left, center, right);
            else if (center == Bracket.LEFT && leftOperand(left))
                throw getSequenceError(left, center, right);
            else if (center == Bracket.RIGHT && rightOperand(right))
                throw getSequenceError(left, center, right);
            left = center;
            center = right;
        }
        if (!(center instanceof IOperand) && center != Bracket.RIGHT)
            throw getSequenceError(left, center, null);
    }

    @NonNull
    private CalculatorException getSequenceError(IToken left, IToken center, IToken right) {
        return new CalculatorException(CalculatorException.Type.OPERATOR_SEQUENCE,
                String.format(Locale.getDefault(), "%s%s%s",
                        left == null ? "" : left,
                        center == null ? "" : center,
                        right == null ? "" : right));
    }

    private boolean leftOperand(IToken left) {
        return left == Bracket.RIGHT || left instanceof IOperand;
    }

    private boolean rightOperand(IToken right) {
        return right == Bracket.LEFT || right instanceof IOperand;
    }
}
