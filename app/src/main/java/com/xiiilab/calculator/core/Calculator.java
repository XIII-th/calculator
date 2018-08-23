package com.xiiilab.calculator.core;

import com.xiiilab.calculator.core.operand.IOperand;
import com.xiiilab.calculator.core.operator.IBinaryOperator;
import com.xiiilab.calculator.core.operator.IUnaryOperator;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by XIII-th on 22.08.2018
 */
public class Calculator {

    public double calculate(Queue<IToken> rpnExpression) {
        Deque<IOperand> stack = new LinkedList<>();
        while (!rpnExpression.isEmpty()) {
            IToken token = rpnExpression.poll();
            if (token instanceof IOperand)
                stack.add((IOperand) token);
            else if (token instanceof IUnaryOperator) {
                IUnaryOperator operator = (IUnaryOperator) token;
                IOperand operand = stack.peekLast();
                if (operand == null)
                    throw new IllegalStateException("Unable to apply operator " + operator + " to empty operand stack");
                operand.setValue(operator.apply(operand));
            } else if (token instanceof IBinaryOperator) {
                IBinaryOperator operator = (IBinaryOperator) token;
                IOperand right = stack.pollLast(), left = stack.peekLast();
                if (right == null || left == null)
                    throw new IllegalStateException("Not enough operands for operator " + operator);
                left.setValue(operator.apply(left, right));
            } else
                throw new IllegalStateException("Unexpected token " + token);
        }
        if (stack.size() != 1)
            throw new IllegalStateException("Operand and operator balance mismatch");
        return stack.pollLast().getValue();
    }
}
