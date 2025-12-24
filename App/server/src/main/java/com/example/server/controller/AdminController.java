package com.example.server.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.dto.AdminDTO;
import com.example.server.entity.Announcement;
import com.example.server.repository.AdminRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    // =============================================================
    // 1. GET DATA (GIỮ NGUYÊN)
    // =============================================================
    @GetMapping("/users")
    public List<AdminDTO.UserRow> getAllUsers() {
        return adminRepository.getAdminUsers();
    }

    @GetMapping("/courses")
    public List<AdminDTO.CourseRow> getAllCourses() {
        return adminRepository.getAdminCourses();
    }

    @GetMapping("/classes")
    public List<AdminDTO.ClassRow> getAllClasses() {
        return adminRepository.getAdminClasses();
    }

    // =============================================================
    // 2. METADATA (GIỮ NGUYÊN)
    // =============================================================
    @GetMapping("/metadata/departments")
    public List<String> getDepartments() {
        return adminRepository.getAllDepartments();
    }

    @GetMapping("/metadata/coursenames")
    public List<String> getCourseNames() {
        return adminRepository.getAllCourseNames();
    }

    // =============================================================
    // 3. QUẢN LÝ USER (ĐÃ SỬA LỖI KIỂU TRẢ VỀ)
    // =============================================================

    @PostMapping("/user/add")
    public ResponseEntity<?> addUser(@RequestBody AdminDTO.UserRequest req) {
        try {
            // Validate sơ bộ trả về JSON thay vì String
            if (req.email == null || req.roleId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Thiếu thông tin Email hoặc Role!"));
            }

            String username = (req.username != null && !req.username.isEmpty()) 
                              ? req.username 
                              : req.email.split("@")[0];

            boolean isActive = (req.status != null && req.status == 1);

            adminRepository.insertUser(username, req.email, req.password, req.roleId, isActive);
            Integer newUserId = adminRepository.getLastUserId();

            if (req.roleId == 2) {
                String studentCode = "SV" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                adminRepository.insertStudent(newUserId, req.fullName, req.department, studentCode);
            } else if (req.roleId == 3) {
                String staffCode = "GV" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                adminRepository.insertLecturer(newUserId, req.fullName, req.department, staffCode);
            }

            // Trả về JSON chuẩn
            return ResponseEntity.ok(Map.of("message", "Thêm thành công"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestBody AdminDTO.UserRequest req) {
        try {
            boolean isActive = (req.status != null && req.status == 1);
            adminRepository.updateUser(req.id, req.email, req.roleId, isActive);

            if (req.roleId != null) {
                if (req.roleId == 2) {
                    adminRepository.updateStudent(req.id, req.fullName, req.department);
                } else if (req.roleId == 3) {
                    adminRepository.updateLecturer(req.id, req.fullName, req.department);
                }
            }

            return ResponseEntity.ok(Map.of("message", "Cập nhật thành công"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            adminRepository.deleteStudent(id);
            adminRepository.deleteLecturer(id);
            adminRepository.deleteUser(id);

            return ResponseEntity.ok(Map.of("message", "Đã xóa thành công"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    // =============================================================
    // 4. QUẢN LÝ KHÓA HỌC (MỚI - ĐÃ ĐỒNG BỘ TRƯỜNG DỮ LIỆU)
    // =============================================================

    /**
     * API THÊM KHÓA HỌC MỚI
     */
    @PostMapping("/course/add")
    public ResponseEntity<?> addCourse(@RequestBody AdminDTO.CourseRequest req) {
        try {
            // Chú ý: Dùng đúng các trường maMon, tenMon, tinChi, moTa từ DTO
            adminRepository.insertCourse(req.maMon, req.tenMon, req.tinChi, req.moTa);
            return ResponseEntity.ok(Map.of("message", "Thêm khóa học thành công"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi SQL: " + e.getMessage()));
        }
    }

    /**
     * API CẬP NHẬT KHÓA HỌC
     */
    @PutMapping("/course/update")
    public ResponseEntity<?> updateCourse(@RequestBody AdminDTO.CourseRequest req) {
        try {
            adminRepository.updateCourse(req.id, req.maMon, req.tenMon, req.tinChi, req.moTa);
            return ResponseEntity.ok(Map.of("message", "Cập nhật khóa học thành công"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi SQL: " + e.getMessage()));
        }
    }

    /**
     * API XÓA KHÓA HỌC
     */
    @DeleteMapping("/course/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Integer id) {
        try {
            adminRepository.deleteCourse(id);
            return ResponseEntity.ok(Map.of("message", "Xóa khóa học thành công"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi khi xóa: " + e.getMessage()));
        }
    }
    // =============================================================
    // QUẢN LÝ LỚP HỌC (THÊM / SỬA / XÓA)
    // =============================================================

    @PostMapping("/class/add")
    public ResponseEntity<?> addClass(@RequestBody AdminDTO.ClassRequest req) {
        try {
            // Validate dữ liệu
            if (req.classCode == null || req.classCode.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Mã lớp không được để trống!"));
            }

            // Gọi Repository (SQL Server sẽ tự ép kiểu chuỗi "HH:mm" sang Time)
            adminRepository.insertClass(
                req.classCode,
                req.lecturerName,
                req.dayOfWeek,
                req.startTime,
                req.endTime,
                req.room
            );

            return ResponseEntity.ok(Map.of("message", "Thêm lịch học thành công"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi: " + e.getMessage()));
        }
    }

    @PutMapping("/class/update")
    public ResponseEntity<?> updateClass(@RequestBody AdminDTO.ClassRequest req) {
        try {
            if (req.scheduleId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Thiếu ScheduleId!"));
            }
            
            // Kiểm tra dữ liệu bắt buộc
            if (req.classCode == null || req.startTime == null) {
                 return ResponseEntity.badRequest().body(Map.of("error", "Thiếu thông tin lớp hoặc giờ!"));
            }

            // Gọi Repository với đúng tên tham số
            adminRepository.updateClass(
                req.classCode,
                req.lecturerName,
                req.dayOfWeek,
                req.startTime,
                req.endTime,
                req.room,
                req.scheduleId // Quan trọng: ID phải được truyền vào cuối
            );

            return ResponseEntity.ok(Map.of("message", "Cập nhật thành công"));
        } catch (Exception e) {
            e.printStackTrace(); // Xem lỗi trong Console của IntelliJ/Eclipse
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi Server: " + e.getMessage()));
        }
    }

    @DeleteMapping("/class/delete/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Integer id) {
        try {
            adminRepository.deleteClass(id);
            return ResponseEntity.ok(Map.of("message", "Xóa lịch học thành công"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi khi xóa: " + e.getMessage()));
        }
    }
    // =======================================================
    // API QUẢN LÝ THÔNG BÁO
    // =======================================================

    // 1. Xem danh sách
    // GET: /api/admin/announcement/all
    @GetMapping("/announcement/all")
    public List<Announcement> getAnnouncements() {
        return adminRepository.getAllAnnouncements();
    }
// 2. Thêm thông báo
    @PostMapping("/announcement/add")
    public ResponseEntity<?> addAnnouncement(@RequestBody AdminDTO.AnnouncementRequest req) {
        try {
            if (req.title == null || req.title.isEmpty() || req.body == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Tiêu đề và nội dung không được để trống"));
            }

            String author = (req.authorId != null) ? req.authorId : "1"; // Mặc định ID admin là 1 nếu null
            Boolean isGlobal = (req.isGlobal != null) ? req.isGlobal : true;

            // --- SỬA LỖI TẠI ĐÂY ---
            // Nếu là Global -> targetClass là null
            // Nếu không -> Lấy targetClassId từ request
            String targetClass = (isGlobal) ? null : req.targetClassId;

            adminRepository.insertAnnouncement(req.title, req.body, author, isGlobal, targetClass);

            return ResponseEntity.ok(Map.of("message", "Thêm thông báo thành công"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi Server: " + e.getMessage()));
        }
    }

    // 3. Cập nhật thông báo
    @PutMapping("/announcement/update")
    public ResponseEntity<?> updateAnnouncement(@RequestBody AdminDTO.AnnouncementRequest req) {
        try {
            if (req.announcementId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Thiếu ID thông báo"));
            }

            Boolean isGlobal = (req.isGlobal != null) ? req.isGlobal : true;
            
            // --- SỬA LỖI TẠI ĐÂY ---
            String targetClass = (isGlobal) ? null : req.targetClassId;

            adminRepository.updateAnnouncement(req.announcementId, req.title, req.body, isGlobal, targetClass);

            return ResponseEntity.ok(Map.of("message", "Cập nhật thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    // 4. Xóa thông báo
    // DELETE: /api/admin/announcement/delete/{id}
    @DeleteMapping("/announcement/delete/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Integer id) {
        try {
            adminRepository.deleteAnnouncement(id);
            return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}