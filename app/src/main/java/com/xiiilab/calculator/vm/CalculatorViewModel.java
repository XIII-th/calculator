package com.xiiilab.calculator.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.xiiilab.calculator.AsyncCalculator;

/**
 * Created by XIII-th on 23.08.2018
 */
public class CalculatorViewModel extends ViewModel {

    private final MutableLiveData<String> mExpressionError;
    private final MutableLiveData<String> mResult;
    private final MutableLiveData<Boolean> mInProgress;
    private final AsyncCalculator mCalculator;
    private final CalculationResultListener mCalculationListener;

    private String mExpression;

    public CalculatorViewModel() {
        mExpressionError = new MutableLiveData<>();
        mResult = new MutableLiveData<>();
        mInProgress = new MutableLiveData<>();
        mCalculator = new AsyncCalculator();
        mCalculationListener = new CalculationResultListener(mExpressionError, mResult, mInProgress);
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
        mCalculator.calculate(getExpression(), mCalculationListener);
    }

    @Override
    protected void onCleared() {
        mCalculator.shutdown();
    }
}
