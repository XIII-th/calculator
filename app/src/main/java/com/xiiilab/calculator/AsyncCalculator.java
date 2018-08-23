package com.xiiilab.calculator;

import android.support.annotation.MainThread;
import android.util.Log;
import com.xiiilab.calculator.core.Calculator;
import com.xiiilab.calculator.core.IToken;
import com.xiiilab.calculator.core.InputPreprocessor;
import com.xiiilab.calculator.core.TokenProcessor;
import com.xiiilab.calculator.core.operator.BinaryOperator;
import com.xiiilab.calculator.core.operator.Bracket;
import com.xiiilab.calculator.core.operator.UnaryOperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by XIII-th on 23.08.2018
 */
public class AsyncCalculator {

    private final InputPreprocessor mInputPreprocessor;
    private final TokenProcessor mTokenProcessor;
    private final Calculator mCalculator;
    private final ExecutorService mExecutorService;
    private final Map<String, Future<?>> mTasks;

    public AsyncCalculator() {
        mTokenProcessor = new TokenProcessor();
        mTokenProcessor.
                addOperators(BinaryOperator.values()).
                addOperators(UnaryOperator.values()).
                addOperators(Bracket.values());
        mInputPreprocessor = new InputPreprocessor(mTokenProcessor.getSupportedOperators());
        mCalculator = new Calculator();
        mExecutorService = Executors.newSingleThreadExecutor();
        mTasks = new HashMap<>();
    }

    @MainThread
    public void calculate(String expression, CalculationListener listener) {
        synchronized (mTasks) {
            for (Future<?> task : mTasks.values())
                task.cancel(true);
            mTasks.put(expression, mExecutorService.submit(new CalculationTask(expression, listener)));
        }
    }

    public void shutdown() {
        // https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html
        mExecutorService.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!mExecutorService.awaitTermination(1, TimeUnit.SECONDS)) {
                mExecutorService.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!mExecutorService.awaitTermination(1, TimeUnit.SECONDS))
                    Log.e(getClass().getName(), "Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            mExecutorService.shutdownNow();
        }
    }

    public interface CalculationListener {
        void onStart();
        void onError(Exception e);
        void onStop(double result);
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
                String[] stringTokens;
                if (!Thread.currentThread().isInterrupted())
                    stringTokens = mInputPreprocessor.getStringTokens(mExpression);
                else
                    return;

                Queue<IToken> rpnQueue;
                if (!Thread.currentThread().isInterrupted())
                    rpnQueue = mTokenProcessor.toRpn(stringTokens);
                else
                    return;

                double result;
                if (!Thread.currentThread().isInterrupted())
                    result = mCalculator.calculate(rpnQueue);
                else
                    return;

                mListener.onStop(result);
            } catch (Exception e) {
                mListener.onError(e);
            } finally {
                synchronized (mTasks) {
                    mTasks.remove(mExpression);
                }
            }
        }
    }
}
