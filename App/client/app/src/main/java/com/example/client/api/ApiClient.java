package com.example.client.api;
import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;

/**
 * Lớp cấu hình Retrofit để khởi tạo kết nối với Backend.
 * Tích hợp OkHttpClient Interceptor để tự động gửi kèm JWT Token trong Header.
 */
public class ApiClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://192.168.2.18:8080/";
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            // Truy cập vào SharedPreferences để lấy Token đã lưu khi đăng nhập
                            SharedPreferences prefs = context.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE);
                            String token = prefs.getString("JWT_TOKEN", null);

                            Request.Builder builder = chain.request().newBuilder();

                            // Nếu tồn tại token (người dùng đã đăng nhập), đính kèm vào header Authorization
                            if (token != null) {
                                builder.addHeader("Authorization", "Bearer " + token);
                            }

                            return chain.proceed(builder.build());
                        }
                    })
                    .build();

            // Khởi tạo đối tượng Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient) // Sử dụng client có gắn Interceptor
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}