package com.example.client.api;

import com.example.client.Login.LoginRequest;
import com.example.client.Login.LoginResponse;
import com.example.client.lecturer.model.ScheduleItem;
import com.example.client.lecturer.model.Announcement;

import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;

public interface ApiService {

    // ==================================================
    // 1. XÁC THỰC & CHUNG (AUTH & GENERAL)
    // ==================================================
    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("/api/schedule-schedules/today")
    Call<List<ScheduleItem>> getTodaySchedule();

    @GET("/api/announcements/recent")
    Call<List<Announcement>> getRecentAnnouncements();

    @GET("/api/class-schedules/lecturer/{lecturerId}")
    Call<List<ScheduleItem>> getScheduleByLecturerId(@Path("lecturerId") Integer lecturerId);

    // ==================================================
    // 2. ADMIN - QUẢN LÝ NGƯỜI DÙNG (USER)
    // ==================================================
    @GET("api/admin/users")
    Call<List<AdminResponse.User>> getUsers();

    @POST("api/admin/user/add")
    Call<ResponseBody> addUser(@Body AdminResponse.UserRequest req);

    @PUT("api/admin/user/update")
    Call<ResponseBody> updateUser(@Body AdminResponse.UserRequest req);

    @DELETE("api/admin/user/delete/{id}")
    Call<ResponseBody> deleteUser(@Path("id") int id);

    // ==================================================
    // 3. ADMIN - QUẢN LÝ KHÓA HỌC (COURSE)
    // ==================================================
    @GET("api/admin/courses")
    Call<List<AdminResponse.CourseRow>> getCourses();

    @POST("api/admin/course/add")
    Call<ResponseBody> addCourse(@Body AdminResponse.CourseRequest req);

    @PUT("api/admin/course/update")
    Call<ResponseBody> updateCourse(@Body AdminResponse.CourseRequest req);

    @DELETE("api/admin/course/delete/{id}")
    Call<ResponseBody> deleteCourse(@Path("id") int id);

    // ==================================================
    // 4. ADMIN - QUẢN LÝ LỚP HỌC (CLASS)
    // ==================================================
    @GET("api/admin/classes")
    Call<List<AdminResponse.ClassRow>> getAdminClasses();
    @POST("api/admin/class/add")
    Call<Map<String, Object>> addClass(@Body AdminResponse.ClassRequest request);
    @PUT("api/admin/class/update")
    Call<Map<String, Object>> updateClass(@Body AdminResponse.ClassRequest request);
    @DELETE("api/admin/class/delete/{id}")
    Call<Map<String, Object>> deleteClass(@Path("id") Integer id);

    // ==================================================
    // 5. METADATA (DỮ LIỆU BỔ TRỢ)
    // ==================================================
    @GET("api/admin/metadata/departments")
    Call<List<String>> getDepartments();

    @GET("api/admin/metadata/coursenames")
    Call<List<String>> getCourseNames();
}