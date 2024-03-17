package com.project.adersh.middleearpathology;

import com.project.adersh.middleearpathology.model.Root;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @GET("view-all-doctors/")
    Call<Root> showAllDoctors();

    @POST("login/")
    Call<Root> docSignIn(@Body Map<String, String> params);

    @POST("signup/")
    Call<Root> docReg(@Body Map<String, String> params);

    @Multipart
    @POST("upload_video/")
    Call<Root> uploadVideo(
            @Part MultipartBody.Part videoFile
    );
    @DELETE("doctors/{docId}/")
    Call<Root> docDel(@Path("docId") int docId);

}
