package com.xiiilab.calculator.vm;

import android.arch.lifecycle.MutableLiveData;
import com.xiiilab.calculator.AsyncCalculator;

/**
 * Created by XIII-th on 23.08.2018
 */
class CalculationResultListener implements AsyncCalculator.CalculationListener {

    private final MutableLiveData<String> mExpressionError;
    private final MutableLiveData<String> mResult;
    private final MutableLiveData<Boolean> mInProgress;

    CalculationResultListener(
            MutableLiveData<String> expressionError,
            MutableLiveData<String> result,
            MutableLiveData<Boolean> inProgress) {
        mExpressionError = expressionError;
        mResult = result;
        mInProgress = inProgress;
    }


    @Override
    public void onStart() {
        mExpressionError.postValue(null);
        mResult.postValue(null);
        mInProgress.postValue(Boolean.TRUE);
    }

    @Override
    public void onError(Exception e) {
        mExpressionError.postValue("Error: " + e.getLocalizedMessage());
        mInProgress.postValue(Boolean.FALSE);
    }

    @Override
    public void onStop(float result) {
        mResult.postValue(String.valueOf(result));
        mInProgress.postValue(Boolean.FALSE);
    }
}
