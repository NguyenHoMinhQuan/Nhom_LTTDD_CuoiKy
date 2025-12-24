package com.example.client.api;

import android.app.DownloadManager;

import com.example.client.HocVien.Models.HocVien_NhomLopDto;
import com.example.client.HocVien.Models.LichHocSinhVienModel;
import com.example.client.HocVien.Models.SoYeuLyLichModel;
import com.example.client.Login.LoginRequest;
import com.example.client.Login.LoginResponse;

import com.example.client.lecturer.model.Announcement;
import com.example.client.lecturer.model.AssignmentDTO;
import com.example.client.lecturer.model.ChatMessageDTO;
import com.example.client.lecturer.model.ClassDTO;
import com.example.client.lecturer.model.NotificationItem;
import com.example.client.lecturer.model.ScheduleItem;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/api/class-schedules/lecturer/{lecturerId}/today")
    Call<List<ScheduleItem>> getTodayScheduleByLecturerId(@Path("lecturerId") Integer lecturerId);

    @GET("/api/class-schedules/lecturer/{lecturerId}")
    Call<List<ScheduleItem>> getScheduleByLecturerId(@Path("lecturerId") Integer lecturerId);

    // --- THÔNG BÁO (NOTIFICATION) ---
    // 1. Lấy thông báo CHƯA ĐỌC cho Dashboard
    @GET("/api/notifications/user/{userId}/unread")
    Call<List<NotificationItem>> getUnreadNotifications(@Path("userId") Integer userId);

    // 2. Lấy TẤT CẢ thông báo cho màn hình View All
    @GET("/api/notifications/user/{userId}")
    Call<List<NotificationItem>> getAllNotifications(@Path("userId") Integer userId);

    // 3. Đánh dấu đã đọc khi click vào thông báo
    @PUT("/api/notifications/{id}/read")
    Call<Void> markAsRead(@Path("id") Integer notificationId);

    @POST("/api/announcements")
    Call<Announcement> postAnnouncement(@Body Announcement announcement);

    @GET("/api/classes/lecturer/{lecturerId}")
    Call<List<ClassDTO>> getClassesByLecturer(@Path("lecturerId") Integer lecturerId);

    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("/api/hocvien/soyeulilich") // dành cho học viên - đụng t chặt tay
    Call<SoYeuLyLichModel> LaySoYeuLyLich(@Query("Username") String username);

    @GET("/api/lichhoc/view") // dành cho học viên - đụng t chặt tay
    Call<List<LichHocSinhVienModel>> LayLichHocSinhVien(@Query("Username") String username);

    @GET("/api/hocvien/nhomlop") // dành cho học viên - đụng t chặt tay
    Call<List<HocVien_NhomLopDto>> LayNhomLopSinhVien(@Query("Username") String username);

    @GET("api/assignments/{id}")
    Call<AssignmentDTO> getAssignmentById(@Path("id") Integer id);

    @GET("api/assignments/byClass/{classId}")
    Call<List<AssignmentDTO>> getAssignmentsByClassId(@Path("classId") Integer classId);

    @POST("api/assignments")
    Call<AssignmentDTO> saveAssignment(@Body AssignmentDTO assignmentDTO);
    @DELETE("api/assignments/{id}")
    Call<Void> deleteAssignment(@Path("id") Integer id);

    @GET("api/assignments/lecturer/{lecturerId}")
    Call<List<AssignmentDTO>> getAssignmentsByLecturer(@Path("lecturerId") Integer lecturerId);

    @GET("/api/messages/history/{classId}")
    Call<List<ChatMessageDTO>> getChatHistory(@Path("classId") Integer classId);
}
