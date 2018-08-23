package com.xiiilab.calculator;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.xiiilab.calculator.core.Calculator;
import com.xiiilab.calculator.core.InputPreprocessor;
import com.xiiilab.calculator.core.TokenProcessor;

/**
 * Created by XIII-th on 23.08.2018
 */
public class CalculatorViewModel extends ViewModel {

    private final InputPreprocessor mInputPreprocessor;
    private final TokenProcessor mTokenProcessor;
    private final Calculator mCalculator;
    private final MutableLiveData<String> mExpressionError;
    private final MutableLiveData<String> mResult;
    private final MutableLiveData<Boolean> mInProgress;

    private String mExpression;

    public CalculatorViewModel(
            InputPreprocessor inputPreprocessor,
            TokenProcessor tokenProcessor,
            Calculator calculator) {
        mInputPreprocessor = inputPreprocessor;
        mTokenProcessor = tokenProcessor;
        mCalculator = calculator;
        mExpressionError = new MutableLiveData<>();
        mResult = new MutableLiveData<>();
        mInProgress = new MutableLiveData<>();
    }

    public String getExpression() {
        return mExpression;
    }

    public void setExpression(String expression) {
        mExpression = expression;
    }

    public LiveData<String> getExpressionError() {
        return mExpressionError;
    }

    public LiveData<String> getResult() {
        return mResult;
    }

    public LiveData<Boolean> getInProgress() {
        return mInProgress;
    }

    public void calculate() {

    }
}
