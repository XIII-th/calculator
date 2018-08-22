package com.xiiilab.calculator.core.operand;

import com.xiiilab.calculator.core.IToken;

/**
 * Created by XIII-th on 17.08.2018
 */
public interface IOperand extends IToken {

    float getValue();

    void setValue(float v);
}
