package com.xiiilab.calculator.vm;

import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import com.xiiilab.calculator.core.Calculator;
import com.xiiilab.calculator.core.InputPreprocessor;
import com.xiiilab.calculator.core.TokenProcessor;
import com.xiiilab.calculator.core.operator.BinaryOperator;
import com.xiiilab.calculator.core.operator.Bracket;
import com.xiiilab.calculator.core.operator.UnaryOperator;

/**
 * Created by XIII-th on 23.08.2018
 */
public class CalculatorVmFactory implements ViewModelProvider.Factory {

    private static final CalculatorVmFactory INSTANCE = new CalculatorVmFactory();

    // TODO: use IoCC (dagger for example)
    private final InputPreprocessor mInputPreprocessor;
    private final TokenProcessor mTokenProcessor;
    private final Calculator mCalculator;

    private CalculatorVmFactory() {
        mTokenProcessor = new TokenProcessor();
        mTokenProcessor.
                addOperators(BinaryOperator.values()).
                addOperators(UnaryOperator.values()).
                addOperators(Bracket.values());
        mInputPreprocessor = new InputPreprocessor(mTokenProcessor.getSupportedOperators());
        mCalculator = new Calculator();
    }

    public static CalculatorVmFactory getInstance() {
        return INSTANCE;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public CalculatorViewModel create(@NonNull Class modelClass) {
        if (modelClass != CalculatorViewModel.class)
            throw new IllegalArgumentException("Unexpected model class " + modelClass);
        return new CalculatorViewModel(mInputPreprocessor, mTokenProcessor, mCalculator);
    }
}
