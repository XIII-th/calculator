package com.xiiilab.calculator.vm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.StringRes;
import com.xiiilab.calculator.AsyncCalculator;
import com.xiiilab.calculator.R;
import com.xiiilab.calculator.core.CalculatorException;

/**
 * Created by XIII-th on 23.08.2018
 */
public class CalculatorViewModel extends AndroidViewModel implements AsyncCalculator.CalculationListener {

    private final MutableLiveData<String> mExpressionError;
    private final MutableLiveData<String> mResult;
    private final MutableLiveData<Boolean> mInProgress;
    private final AsyncCalculator mCalculator;

    private String mExpression;

    public CalculatorViewModel(Application application) {
        super(application);
        mExpressionError = new MutableLiveData<>();
        mResult = new MutableLiveData<>();
        mInProgress = new MutableLiveData<>();
        mCalculator = new AsyncCalculator();
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
        String expression = getExpression();
        if (expression != null && !expression.isEmpty())
            mCalculator.calculate(expression, this);
    }

    @Override
    protected void onCleared() {
        mCalculator.shutdown();
    }

    @Override
    public void onStart() {
        mExpressionError.postValue(null);
        mResult.postValue(null);
        mInProgress.postValue(Boolean.TRUE);
    }

    @Override
    public void onError(Exception e) {
        String message;
        if (e instanceof CalculatorException) {
            CalculatorException.Type type = ((CalculatorException) e).mType;
            switch (type) {
                case NUMBER_FORMAT:
                    message = error(R.string.number_format_error, e);
                    break;
                case BRACKET_MISMATCH:
                    message = error(R.string.bracket_mismatch, e);
                    break;
                case EMPTY_BRACKET:
                    message = error(R.string.empty_bracket, e);
                    break;
                case OPERATOR_SEQUENCE:
                    message = error(R.string.unexpected_sequence_error, e);
                    break;
                default:
                    message = error(R.string.unexpected_error, e);
            }
        } else
            message = error(R.string.unexpected_error, e);
        mExpressionError.postValue(message);
        mInProgress.postValue(Boolean.FALSE);
    }

    @Override
    public void onStop(double result) {
        mResult.postValue(String.valueOf(result));
        mInProgress.postValue(Boolean.FALSE);
    }

    private String error(@StringRes int id, Exception e) {
        return getApplication().getString(id, e.getLocalizedMessage());
    }
}
