package com.xiiilab.calculator;

import android.support.annotation.MainThread;
import com.xiiilab.calculator.core.Calculator;
import com.xiiilab.calculator.core.IToken;
import com.xiiilab.calculator.core.InputPreprocessor;
import com.xiiilab.calculator.core.TokenProcessor;
import com.xiiilab.calculator.core.operator.BinaryOperator;
import com.xiiilab.calculator.core.operator.Bracket;
import com.xiiilab.calculator.core.operator.UnaryOperator;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by XIII-th on 23.08.2018
 */
public class AsyncCalculator {

    private final InputPreprocessor mInputPreprocessor;
    private final TokenProcessor mTokenProcessor;
    private final Calculator mCalculator;
    private final ExecutorService mExecutorService;
    private Future<?> mTask;

    public AsyncCalculator() {
        mTokenProcessor = new TokenProcessor();
        mTokenProcessor.
                addOperators(BinaryOperator.values()).
                addOperators(UnaryOperator.values()).
                addOperators(Bracket.values());
        mInputPreprocessor = new InputPreprocessor(mTokenProcessor.getSupportedOperators());
        mCalculator = new Calculator();
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    @MainThread
    public void calculate(String expression, CalculationListener listener) {
        if (mTask != null)
            mTask.cancel(true);
        mTask = mExecutorService.submit(new CalculationTask(expression, listener));
    }

    public interface CalculationListener {
        void onStart();
        void onError(Exception e);
        void onStop(float result);
    }

    private class CalculationTask implements Runnable {

        private final String mExpression;
        private final CalculationListener mListener;

        private CalculationTask(String expression, CalculationListener listener) {
            mExpression = expression;
            mListener = listener;
        }

        @Override
        public void run() {
            mListener.onStart();
            try {
                String[] stringTokens = mInputPreprocessor.getStringTokens(mExpression);
                Queue<IToken> rpnQueue = mTokenProcessor.toRpn(stringTokens);
                float result = mCalculator.calculate(rpnQueue);
                mListener.onStop(result);
            } catch (Exception e) {
                mListener.onError(e);
            }
        }
    }
}
