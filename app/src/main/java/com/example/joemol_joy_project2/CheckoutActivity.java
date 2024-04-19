package com.example.joemol_joy_project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.validators.TextInputLayoutValidator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CheckoutActivity extends AppCompatActivity {

    ImageView backBtn;
    Button continueBtn;
    TextInputEditText nameEditText, phoneEditText, addressEditText, cityEditText, pinEditText;
    TextInputLayout textInputNameBox, textInputPhoneBox, textInputAddressBox, textInputCityBox, textInputPinBox;
    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        backBtn = findViewById(R.id.backBtn);
        continueBtn = findViewById(R.id.continueBtn);
        nameEditText = findViewById(R.id.textInputEditName);
        phoneEditText = findViewById(R.id.textInputEditPhone);
        addressEditText = findViewById(R.id.textInputEditAdress);
        cityEditText = findViewById(R.id.textInputEditCity);
        pinEditText = findViewById(R.id.textInputEditPin);
        textInputNameBox = findViewById(R.id.textInputNameBox);
        textInputPhoneBox = findViewById(R.id.textInputPhoneBox);
        textInputAddressBox = findViewById(R.id.textInputAddressBox);
        textInputCityBox = findViewById(R.id.textInputCityBox);
        textInputPinBox = findViewById(R.id.textInputPinBox);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        continueBtn.setOnClickListener(view -> {
            boolean isValid = true;

            if (isEmpty(nameEditText)) {
                textInputNameBox.setError("Name cannot be empty");
                isValid = false;
            } else {
                textInputNameBox.setError(null);
                if (!isText(nameEditText)) {
                    textInputNameBox.setError("Name must contain only text");
                    isValid = false;
                }
            }

            if (isEmpty(phoneEditText)) {
                textInputPhoneBox.setError("Phone number cannot be empty");
                isValid = false;
            } else {
                textInputPhoneBox.setError(null);
                if (!isPhoneNumber(phoneEditText)) {
                    textInputPhoneBox.setError("Phone number must be 10 digits");
                    isValid = false;
                }
            }

            if (isEmpty(addressEditText)) {
                textInputAddressBox.setError("Address cannot be empty");
                isValid = false;
            } else {
                textInputAddressBox.setError(null);
            }

            if (isEmpty(cityEditText)) {
                textInputCityBox.setError("City cannot be empty");
                isValid = false;
            } else {
                textInputCityBox.setError(null);
                if (!isText(cityEditText)) {
                    textInputCityBox.setError("City must contain only text");
                    isValid = false;
                }
            }

            if (isEmpty(pinEditText)) {
                textInputPinBox.setError("PIN cannot be empty");
                isValid = false;
            } else {
                textInputPinBox.setError(null);
                if (!isPin(pinEditText)) {
                    textInputPinBox.setError("PIN must be 6 digits");
                    isValid = false;
                }
            }

                if (isValid && awesomeValidation.validate()) {
                    Intent intent = new Intent(this, PaymentActivity.class);
                    startActivity(intent);
                }

        });
    }

    private boolean isEmpty(TextInputEditText textInputEditText) {
        return TextUtils.isEmpty(textInputEditText.getText().toString().trim());
    }

    private boolean isText(TextInputEditText textInputEditText) {
        String text = textInputEditText.getText().toString().trim();
        return text.matches("[a-zA-Z]+");
    }

    private boolean isPhoneNumber(TextInputEditText textInputEditText) {
        String phone = textInputEditText.getText().toString().trim();
        return phone.matches("^[0-9]{10}$");
    }

    private boolean isPin(TextInputEditText textInputEditText) {
        String pin = textInputEditText.getText().toString().trim();
        return pin.matches("^[a-zA-Z0-9]{6}$");
    }
}
