package com.example.client.api;

import android.app.DownloadManager;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;

import com.example.client.HocVien.Models.HocVien_BaiTapDto;
import com.example.client.HocVien.Models.HocVien_NhomLopDto;
import com.example.client.HocVien.Models.HocVien_XemDiemDto;
import com.example.client.HocVien.Models.LichHocSinhVienModel;
import com.example.client.HocVien.Models.SoYeuLyLichModel;
import com.example.client.Login.LoginRequest;
import com.example.client.Login.LoginResponse;
import com.example.client.Models.HocVien_NopBaiDto;
import com.example.client.lecturer.model.Announcement;
import com.example.client.lecturer.model.AssignmentDTO;
import com.example.client.lecturer.model.ChatMessageDTO;
import com.example.client.lecturer.model.ClassDTO;
import com.example.client.lecturer.model.LecturerProfileDTO;
import com.example.client.lecturer.model.NotificationItem;
import com.example.client.lecturer.model.ScheduleItem;

import com.example.client.lecturer.model.SubmissionDTO;

import com.example.client.HocVien.Models.ThongBaoModel;
import com.example.client.HocVien.Models.TinNhanModel;
import com.example.client.HocVien.Models.DanhGiaModel;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
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

    @POST("api/assignment/submit")
    Call<Void> nopBaiTap(@Body HocVien_NopBaiDto request);
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


    @GET("api/lecturers/profile/{id}")
    Call<LecturerProfileDTO> getLecturerProfile(@Path("id") Integer id);

    @PUT("api/lecturers/profile/{id}")
    Call<LecturerProfileDTO> updateLecturerProfile(@Path("id") Integer id, @Body LecturerProfileDTO dto);
    @GET("/api/assignments/{id}/submissionCount")
    Call<Long> getSubmissionCount(@Path("id") Integer assignmentId);

    @GET("/api/assignments/{id}/submissions")
    Call<List<SubmissionDTO>> getSubmissions(@Path("id") Integer assignmentId);
    @POST("/api/assignments/grade/{submissionId}")
    Call<SubmissionDTO> gradeSubmission(
            @Path("submissionId") Integer submissionId,
            @Query("grade") Double grade
    );



    @GET("/api/schedule-schedules/today")
    Call<List<ScheduleItem>> getTodaySchedule();

    @GET("/api/announcements/recent")
    Call<List<Announcement>> getRecentAnnouncements();

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
    Call<List<AdminResponse.ClassItem>> getClasses();

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



    // 1. Lấy danh sách (GET)
    @GET("api/admin/announcement/all")
    Call<List<AdminResponse.Announcement>> getAnnouncements();

    // 2. Thêm mới (POST) - Body là AdminResponse.Announcement
    @POST("api/admin/announcement/add")
    Call<Map<String, Object>> addAnnouncement(@Body AdminResponse.Announcement req);

    // 3. Cập nhật (PUT) - Body là AdminResponse.Announcement
    @PUT("api/admin/announcement/update")
    Call<Map<String, Object>> updateAnnouncement(@Body AdminResponse.Announcement req);

    // 4. Xóa (DELETE)
    @DELETE("api/admin/announcement/delete/{id}")
    Call<Map<String, Object>> deleteAnnouncement(@Path("id") Integer id);

    @GET("api/xemdiem/view") // api cho học viên - xem điểm
    Call<List<HocVien_XemDiemDto>> xemDiemSinhVien(
            @Query("Username") String username,
            @Query("MaLop") String classCode
    );
    @GET("/api/announcements")
    Call<List<ThongBaoModel>> getListAnnouncements();

    // --- CHAT ---
    @GET("/api/chat/lop/{id}")
    Call<List<TinNhanModel>> layTinNhan(@Path("id") int idLop);

    @POST("/api/chat/gui")
    Call<TinNhanModel> guiTinNhan(@Body TinNhanModel tinNhan);

    // --- FEEDBACK ---
    @POST("/api/feedback/gui")
    Call<Void> guiDanhGia(@Body DanhGiaModel danhGia);
    @GET("api/student/chat")
    Call<List<TinNhanModel>> layTinNhan(
            @Query("username") String username,
            @Query("classCode") String classCode
    );

}
