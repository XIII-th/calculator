package com.xiiilab.calculator.core.operator;

import com.xiiilab.calculator.core.operand.IOperand;

/**
 * Created by XIII-th on 17.08.2018
 */
public interface IUnaryOperator extends IOperator {

    float apply(IOperand o);
}
