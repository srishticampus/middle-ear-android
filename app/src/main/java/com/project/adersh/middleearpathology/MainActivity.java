package com.project.adersh.middleearpathology;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.project.adersh.middleearpathology.model.Root;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    private TextInputEditText phoneNumberEt;
    private TextInputEditText passwordEt;
    private SwitchMaterial sellerLoginToggle;
    private TextView loginBt;

    private Boolean toggleStatus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        initView();



        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (toggleStatus) {

                    if (phoneNumberEt.getText().toString().isEmpty() || passwordEt.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    } else {
                        if (phoneNumberEt.getText().toString().equals("1234567890") && passwordEt.getText().toString().equals("password")) {
                            Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                            startActivity(intent);                        }
                    }

//                    Intent intent = new Intent(getApplicationContext(), AdminHome.class);
//                    startActivity(intent);
                } else {
//                    Intent intent = new Intent(getApplicationContext(), DocHome.class);
//                    startActivity(intent);
                    loginApi();
                }

            }
        });

        sellerLoginToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    toggleStatus = true;
                } else {
                    toggleStatus = false;

                }
            }
        });


    }

    private void initView() {
        phoneNumberEt = findViewById(R.id.phone_number_et);
        passwordEt = findViewById(R.id.password_et);
        sellerLoginToggle = findViewById(R.id.seller_login_toggle);
        loginBt = findViewById(R.id.login_bt);
    }

    private void loginApi() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Map<String, String> params = new HashMap<>();
        params.put("mobile_number", phoneNumberEt.getText().toString());
        params.put("password", passwordEt.getText().toString());

        apiService.docSignIn(params).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()){
                    Root root = response.body();
                    if (root.status){
                        if (root.status){
                            Intent intent = new Intent(getApplicationContext(), DocHome.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(MainActivity.this, root.message, Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("hello",t.getMessage());
            }
        });

    }


}