package com.xiiilab.calculator;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.xiiilab.calculator.databinding.ActivityCalculatorBinding;
import com.xiiilab.calculator.vm.CalculatorViewModel;
import com.xiiilab.calculator.vm.CalculatorVmFactory;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCalculatorBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_calculator);
        setSupportActionBar(binding.toolbar);

        CalculatorViewModel viewModel = ViewModelProviders.of(this, CalculatorVmFactory.getInstance()).
                get(CalculatorViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setVm(viewModel);
    }
}
