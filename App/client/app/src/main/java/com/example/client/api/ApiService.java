package com.example.client.api;

import com.example.client.HocVien.Models.LichHocSinhVienModel;
import com.example.client.HocVien.Models.SoYeuLyLichModel;
import com.example.client.Login.LoginRequest;
import com.example.client.Login.LoginResponse;
import com.example.client.lecturer.model.ScheduleItem;
import com.example.client.lecturer.model.Announcement;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Lấy Thời khóa biểu Hôm nay
    @GET("/api/schedule-schedules/today")
    Call<List<ScheduleItem>> getTodaySchedule();

    // Lấy Thông báo Gần đây
    @GET("/api/announcements/recent")
    Call<List<Announcement>> getRecentAnnouncements();

    // Lấy thời khóa biểu theo giảng viên
    @GET("/api/class-schedules/lecturer/{lecturerId}")
    Call<List<ScheduleItem>> getScheduleByLecturerId(@Path("lecturerId") Integer lecturerId);
    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("/api/hocvien/soyeulilich") // dành cho học viên - đụng t chặt tay
    Call<SoYeuLyLichModel> LaySoYeuLyLich(@Query("Username") String username);

    @GET("/api/lichhoc/view") // dành cho học viên - đụng t chặt tay
    Call<List<LichHocSinhVienModel>> LayLichHocSinhVien(@Query("Username") String username);

    // Lấy danh sách thông báo từ bảng Announcement
    @GET("/api/announcements")
    Call<List<ThongBaoModel>> getListAnnouncements();
}
