package com.project.adersh.middleearpathology;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.project.adersh.middleearpathology.model.Root;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocReg extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    private TextInputEditText nameEt;
    @NotEmpty(message = "enter a valid phone number with country code")
    private TextInputEditText phoneEt;
    @NotEmpty
    @Email
    private TextInputEditText emailEt;
    @Password(min = 6)
    @NotEmpty
    private TextInputEditText passwordEt;
    @ConfirmPassword
    private TextInputEditText retypePasswordEt;
    @NotEmpty
    private TextInputEditText hospitalNameEt;
    @NotEmpty
    private TextInputEditText hospitalIdEt;
    private TextView regBt;

    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_reg);
        initView();

        validator = new Validator(this);
        validator.setValidationListener(this);

        regBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();

            }
        });

    }

    private void initView() {
        nameEt = findViewById(R.id.name_et);
        phoneEt = findViewById(R.id.phone_et);
        emailEt = findViewById(R.id.email_et);
        passwordEt = findViewById(R.id.password_et);
        retypePasswordEt = findViewById(R.id.retype_password_et);
        hospitalNameEt = findViewById(R.id.hospital_name_et);
        hospitalIdEt = findViewById(R.id.hospital_id_et);
        regBt = findViewById(R.id.reg_bt);
    }

    private void regApi() {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Map<String, String> params = new HashMap<>();
        params.put("name", nameEt.getText().toString());
        params.put("mobile_number", phoneEt.getText().toString());
        params.put("email", emailEt.getText().toString());
        params.put("hospital_name", hospitalNameEt.getText().toString());
        params.put("doctor_id_number", hospitalIdEt.getText().toString());
        params.put("password", retypePasswordEt.getText().toString());

        apiService.docReg(params).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                // Log.i("tagg",response.message());
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Toast.makeText(DocReg.this, root.message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AdminHome.class));
                    } else {
                        Toast.makeText(DocReg.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DocReg.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(DocReg.this, t.getMessage() + "dark", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onValidationSucceeded() {
        regApi();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }


    }
}