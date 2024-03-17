package com.project.adersh.middleearpathology;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.project.adersh.middleearpathology.model.Root;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocHome extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST_CODE = 123;
    private VideoView videoPreview;
    private AppCompatButton btnPickVideo;
    private AppCompatButton btnUploadVideo;
    private TextView tvResult;
    private ProgressBar progressBar;
    private LinearLayout progressBarLayout;
    private CardView cardViewVideo;
    private Uri selectedVideoUri;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_home);
        initView();

        btnPickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickVideo();
            }
        });
        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedVideoUri != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBarLayout.setVisibility(View.VISIBLE);
                    apiCall(selectedVideoUri);
                } else {
                    Toast.makeText(DocHome.this, "Please select a video first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void pickVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_VIDEO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                selectedVideoUri = data.getData();
                videoPreview.setVideoURI(selectedVideoUri);
                videoPreview.start();
            } else {
                Toast.makeText(this, "Video picking canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        videoPreview = findViewById(R.id.videoPreview);
        btnPickVideo = findViewById(R.id.btnPickVideo);
        btnUploadVideo = findViewById(R.id.btnUploadVideo);
        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progress_bar);
        progressBarLayout = findViewById(R.id.progressbar_layout);
        cardViewVideo = findViewById(R.id.cardViewVideo);
        imgView = findViewById(R.id.img_view);
    }

    private void apiCall(Uri videoUri) {


        Context context = getApplicationContext();

// Step 1: Get the file directory for your app's cache directory
        File cacheDir = context.getCacheDir();

// Step 2: Create a new file within the cache directory
        File videoFile = new File(cacheDir, "video.mp4");

// Step 3: Open an input stream for the video URI
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(videoUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

// Step 4: Open an output stream for the new file
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(videoFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

// Step 5: Copy the content from the input stream to the output stream to save the video file
        if (inputStream != null && outputStream != null) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Close streams in the finally block to ensure they're always closed
                try {
                    inputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        // File videoFile = new File(videoUri.getPath());

        // Create a request body for the video file
        RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFile);

        // Call the uploadVideo method with the video file
        ApiService service = ApiClient.getClientForVideo().create(ApiService.class);

        service.uploadVideo(videoPart).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root != null && root.status) {
                        tvResult.setText(root.best_prediction.toString());

                        String base64String = root.best_image;
                        byte[] imageBytes = Base64.decode(base64String, Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        imgView.setImageBitmap(decodedImage);


                    }
                    Toast.makeText(DocHome.this, "Upload successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DocHome.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
                Log.i("errrr", t.getMessage());
                Toast.makeText(DocHome.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
