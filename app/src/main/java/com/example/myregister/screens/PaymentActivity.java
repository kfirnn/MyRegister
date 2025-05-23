package com.example.myregister.screens;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myregister.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class PaymentActivity extends AppCompatActivity {
    private TextInputLayout tilCardNumber;
    private TextInputLayout tilCardHolder;
    private TextInputLayout tilExpiry;
    private TextInputLayout tilCvv;
    private TextInputEditText etCardNumber;
    private TextInputEditText etCardHolder;
    private TextInputEditText etExpiry;
    private TextInputEditText etCvv;
    private TextView tvTotalAmount;
    private Button btnPayNow;
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initializeViews();
        setupListeners();
        
        // Get total amount from intent
        totalAmount = getIntent().getDoubleExtra("total_amount", 0.0);
        tvTotalAmount.setText(String.format("Total Amount: ₪%.2f", totalAmount));
    }

    private void initializeViews() {
        tilCardNumber = findViewById(R.id.tilCardNumber);
        tilCardHolder = findViewById(R.id.tilCardHolder);
        tilExpiry = findViewById(R.id.tilExpiry);
        tilCvv = findViewById(R.id.tilCvv);
        etCardNumber = findViewById(R.id.etCardNumber);
        etCardHolder = findViewById(R.id.etCardHolder);
        etExpiry = findViewById(R.id.etExpiry);
        etCvv = findViewById(R.id.etCvv);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnPayNow = findViewById(R.id.btnPayNow);
    }

    private void setupListeners() {
        setupCardNumberFormatting();
        setupExpiryDateFormatting();
        setupPaymentButton();
    }

    private void setupCardNumberFormatting() {
        etCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 16) {
                    s.delete(16, s.length());
                }
            }
        });
    }

    private void setupExpiryDateFormatting() {
        etExpiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().contains("/")) {
                    if (s.length() == 2) {
                        s.append("/");
                    }
                }
            }
        });
    }

    private void setupPaymentButton() {
        btnPayNow.setOnClickListener(v -> {
            if (validatePaymentDetails()) {
                // Here you would typically integrate with a payment gateway
                // For now, we'll just show a success message
                Toast.makeText(this, "Payment Successful!", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private boolean validatePaymentDetails() {
        boolean isValid = true;

        // Validate Card Number
        if (etCardNumber.getText().toString().length() < 16) {
            tilCardNumber.setError("Please enter a valid card number");
            isValid = false;
        } else {
            tilCardNumber.setError(null);
        }

        // Validate Card Holder Name
        if (etCardHolder.getText().toString().isEmpty()) {
            tilCardHolder.setError("Please enter card holder name");
            isValid = false;
        } else {
            tilCardHolder.setError(null);
        }

        // Validate Expiry Date
        String expiry = etExpiry.getText().toString();
        if (!Pattern.matches("^(0[1-9]|1[0-2])/([0-9]{2})$", expiry)) {
            tilExpiry.setError("Please enter a valid expiry date (MM/YY)");
            isValid = false;
        } else {
            tilExpiry.setError(null);
        }

        // Validate CVV
        if (etCvv.getText().toString().length() < 3) {
            tilCvv.setError("Please enter a valid CVV");
            isValid = false;
        } else {
            tilCvv.setError(null);
        }

        return isValid;
    }
} 