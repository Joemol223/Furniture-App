package com.example.joemol_joy_project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentActivity extends AppCompatActivity {
    ImageView backBtn;
    Button orderBtn;
    TextInputEditText cardNumberEditText, expiryDateEditText, cvvEditText, cardholderNameEditText;
    TextInputLayout cardNumberLayout, expiryDateLayout, cvvLayout, cardholderNameLayout;
    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        backBtn = findViewById(R.id.backBtn);
        orderBtn = findViewById(R.id.orderBtn);
        cardNumberEditText = findViewById(R.id.textInputEditNumber);
        expiryDateEditText = findViewById(R.id.textInputEditDate);
        cvvEditText = findViewById(R.id.textInputEditCvv);
        cardholderNameEditText = findViewById(R.id.textInputEditCardname);
        cardNumberLayout = findViewById(R.id.textInputNumberBox);
        expiryDateLayout = findViewById(R.id.textInputDateBox);
        cvvLayout = findViewById(R.id.textInputCvvBox);
        cardholderNameLayout = findViewById(R.id.textInputCardnameBox);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CheckoutActivity.class);
            startActivity(intent);
        });

        orderBtn.setOnClickListener(view -> {
            boolean isValid = true;

            if (isEmpty(cardNumberEditText)) {
                cardNumberLayout.setError("Card number cannot be empty");
                isValid = false;
            } else {
                cardNumberLayout.setError(null);
                if (!isValidCardNumber(cardNumberEditText)) {
                    cardNumberLayout.setError("Invalid card number");
                    isValid = false;
                }
            }

            if (isEmpty(expiryDateEditText)) {
                expiryDateLayout.setError("Expiry date cannot be empty");
                isValid = false;
            } else {
                expiryDateLayout.setError(null);
                if (!isValidExpiryDate(expiryDateEditText)) {
                    expiryDateLayout.setError("Invalid expiry date");
                    isValid = false;
                } else if (isExpired(expiryDateEditText)) {
                    expiryDateLayout.setError("Expiry date cannot be in the past");
                    isValid = false;
                }
            }

            if (isEmpty(cvvEditText)) {
                cvvLayout.setError("CVV cannot be empty");
                isValid = false;
            } else {
                cvvLayout.setError(null);
                if (!isValidCVV(cvvEditText)) {
                    cvvLayout.setError("Invalid CVV");
                    isValid = false;
                }
            }

            if (isEmpty(cardholderNameEditText)) {
                cardholderNameLayout.setError("Cardholder name cannot be empty");
                isValid = false;
            } else {
                cardholderNameLayout.setError(null);
            }
            if (isValid && awesomeValidation.validate()) {
                Intent intent = new Intent(this, ThankyouPageActivity.class);
                startActivity(intent);
            }
        });
    }
    private boolean isEmpty(TextInputEditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private boolean isValidCardNumber(TextInputEditText editText) {
        String cardNumber = editText.getText().toString().trim();
        return cardNumber.matches("^[0-9]{16}$");
    }

    private boolean isValidExpiryDate(TextInputEditText editText) {
        String expiryDate = editText.getText().toString().trim();
        return expiryDate.matches("^(0[1-9]|1[0-2])/[0-9]{2}$");
    }

    private boolean isExpired(TextInputEditText editText) {
        String expiryDateString = editText.getText().toString().trim();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy");
        try {
            Date expiryDate = dateFormat.parse(expiryDateString);
            Date currentDate = new Date();
            return expiryDate.before(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }

    private boolean isValidCVV(TextInputEditText editText) {
        String cvv = editText.getText().toString().trim();
        return cvv.matches("^[0-9]{3}$");
    }
}