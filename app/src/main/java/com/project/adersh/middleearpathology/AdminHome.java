package com.project.adersh.middleearpathology;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.adersh.middleearpathology.adapter.DoctorsListAdapter;
import com.project.adersh.middleearpathology.model.Root;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHome extends AppCompatActivity implements DelClickListner {

    private FloatingActionButton fabBt;
    private RecyclerView docRv;

    private String[] docName = {"Alin", "Anoop", "Shihab", "Adersh",
            "Jithin", "Jeeva", "Soumya", "Radhul", "Vinayak", "Haresh",
            "Nujoom", "Kiran", "Akash"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        initView();

        fabBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DocReg.class);
                startActivity(intent);
            }
        });

        listDocApi();


    }

    private void listDocApi() {
        try {
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            apiService.showAllDoctors().enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {
                    if (response.isSuccessful()) {
                        Root root = response.body();
                        if (root.status) {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                            docRv.setLayoutManager(linearLayoutManager);
                            DoctorsListAdapter doctorsListAdapter = new DoctorsListAdapter(getApplicationContext(), root, AdminHome.this);
                            docRv.setAdapter(doctorsListAdapter);


                        } else {
                            Toast.makeText(AdminHome.this, root.message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(AdminHome.this, response.message(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {
                    Toast.makeText(AdminHome.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {

        }
    }

    private void initView() {
        fabBt = findViewById(R.id.fab_bt);
        docRv = findViewById(R.id.doc_rv);
    }

    @Override
    public void delClick(int docId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.docDel(docId).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        finish();
                        startActivity(getIntent());
                    } else {
                        Toast.makeText(AdminHome.this, root.message.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminHome.this, response.message().toString(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(AdminHome.this, t.getMessage().toString().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}