package com.example.server.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.dto.ClassScheduleDTO;
import com.example.server.dto.ClassDTO;
import com.example.server.dto.CourseDTO;
import com.example.server.entity.ClassSchedule;
import com.example.server.repository.ClassScheduleRepository;

@Service
public class ClassScheduleServiceImpl implements ClassScheduleService {

    @Autowired
    private ClassScheduleRepository classScheduleRepository;
    
    // Tiêm các Service khác để thực hiện Aggregate Data
    @Autowired 
    private ClassService classService; 
    
    @Autowired
    private CourseService courseService;

    // =================================================================
    // PHƯƠNG THỨC MAPPING VÀ CHUYỂN ĐỔI (SỬA ĐẦY ĐỦ)
    // =================================================================

    // Dùng cho việc trả về DTO (Bao gồm logic tra cứu courseName và DayOfWeek)
    public ClassScheduleDTO convertToDTO(ClassSchedule classSchedule) {
        ClassScheduleDTO classScheduleDTO = new ClassScheduleDTO();
        
        // 1. Ánh xạ các trường trực tiếp (Đã điền đầy đủ)
        classScheduleDTO.setScheduleId(classSchedule.getScheduleId());
        classScheduleDTO.setDayOfWeek(convertDayNumberToDayName(classSchedule.getDayOfWeek())); 
        classScheduleDTO.setStartTime(classSchedule.getStartTime());
        classScheduleDTO.setEndTime(classSchedule.getEndTime());
        classScheduleDTO.setRoom(classSchedule.getRoom());
        
        // 2. Logic TỔNG HỢP: Tra cứu Course Name
        Integer classId = classSchedule.getClassId();
        Optional<ClassDTO> classOptional = classService.findClassById(classId); 
        
        classOptional.ifPresent(classDTO -> {
            Integer courseId = classDTO.getCourseId(); 
            Optional<CourseDTO> courseOptional = courseService.findCourseById(courseId);
            
            courseOptional.ifPresent(courseDTO -> {
                classScheduleDTO.setCourseName(courseDTO.getCourseName());
            });
        });
        
        return classScheduleDTO;
    }

    // Dùng cho việc lưu Entity vào Repository (Đã điền đầy đủ)
    public ClassSchedule convertToEntity(ClassScheduleDTO classScheduleDTO) {
        ClassSchedule classSchedule = new ClassSchedule();
        
        // Ánh xạ các trường DTO
        classSchedule.setScheduleId(classScheduleDTO.getScheduleId());
        
        // Cần phải có ClassId để lưu vào Entity
        // DÙ DTO KHÔNG CÓ TRƯỜNG ClassId (khi tạo mới), nhưng nó phải được set khi gọi save()
        // Giả sử DTO vẫn giữ classId khi cập nhật
        classSchedule.setClassId(classScheduleDTO.getClassId()); 
        
        // LƯU Ý: Nếu dayOfWeek trong Entity là Integer, bạn cần chuyển String về Integer
        // Tạm thời giữ nguyên
        // classSchedule.setDayOfWeek(Integer.valueOf(classScheduleDTO.getDayOfWeek())); 
        // Thay vì chuyển đổi ngược, ta giữ dayOfWeek ở đây là Integer nếu Entity là Integer.
        // Tuy nhiên, vì DTO của bạn dùng String, ta sẽ giả định Entity dùng String.
        // Hoặc cần sử dụng một DTO trung gian khác nếu ClassScheduleDTO dùng String.
        
        // Giữ nguyên theo DTO, nhưng cần cẩn thận nếu Entity là Integer
        // Tạm thời, giữ nguyên logic Entity cũ của bạn
        classSchedule.setDayOfWeek(Integer.valueOf(classScheduleDTO.getDayOfWeek())); 
        classSchedule.setStartTime(classScheduleDTO.getStartTime());
        classSchedule.setEndTime(classScheduleDTO.getEndTime());
        classSchedule.setRoom(classScheduleDTO.getRoom());
        
        return classSchedule;
    }

    // =================================================================
    // TRIỂN KHAI CÁC PHƯƠNG THỨC TỪ INTERFACE (ĐÃ SỬA TÊN)
    // =================================================================
    
    @Override
    public List<ClassScheduleDTO> findAllClassSchedules() {
        List<ClassSchedule> classSchedules = classScheduleRepository.findAll();
        
        return classSchedules.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassScheduleDTO> findScheduleByLecturerIdForToday(Integer lecturerId) {
        // Lấy thứ hiện tại (Hệ thống Java: 1=Thứ Hai ... 7=Chủ Nhật)
        int todayValue = LocalDate.now().getDayOfWeek().getValue();
        
        // CHÚ Ý: Cần khớp với quy ước trong DB của bạn. 
        // Nếu DB lưu Thứ Hai = 2, Chủ Nhật = 1, hãy dùng logic chuyển đổi:
        Integer dbDayValue = (todayValue == 7) ? 1 : todayValue + 1;

        // Tận dụng hàm findScheduleByLecturerId đã viết ở trên
        List<ClassScheduleDTO> fullSchedule = findScheduleByLecturerId(lecturerId);

        // Lọc danh sách theo ngày hiện tại
        return fullSchedule.stream()
                .filter(item -> {
                    // Nếu convertToDTO đã chuyển số thành chữ (ví dụ: "Thứ Ba")
                    // hãy so sánh chuỗi. Nếu vẫn để số, hãy so sánh số.
                    String todayString = convertDayNumberToDayName(dbDayValue);
                    return item.getDayOfWeek().equals(todayString);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassScheduleDTO> findScheduleByLecturerId(Integer lecturerId) {
        
        // 1. Lấy tất cả các lớp (Class) mà giảng viên này dạy
        List<ClassDTO> classesTaught = classService.findAllClassesByLecturerId(lecturerId);
        
        // Nếu giảng viên không dạy lớp nào, trả về danh sách trống
        if (classesTaught.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. Lấy danh sách ClassId
        List<Integer> classIds = classesTaught.stream()
                .map(ClassDTO::getClassId)
                .collect(Collectors.toList());

        // 3. Lấy tất cả ClassSchedule entities thuộc về các ClassIds này
        // Cần phải có phương thức findAllByClassIdIn(List<Integer> ids) trong ClassScheduleRepository
        List<ClassSchedule> rawSchedules = classScheduleRepository.findAllByClassIdIn(classIds);
        
        // 4. Ánh xạ, tổng hợp CourseName và DayOfWeek cho từng buổi học
        return rawSchedules.stream()
                .map(this::convertToDTO) // convertToDTO đã bao gồm logic tra cứu Course/DayOfWeek
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ClassScheduleDTO> findClassScheduleById(Integer id) {
        return classScheduleRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    @Override
    public List<ClassScheduleDTO> findAllByClassId(Integer classId) {
        // Sử dụng phương thức repository đã thêm vào
        List<ClassSchedule> schedules = classScheduleRepository.findAllByClassId(classId);
        
        // Chuyển đổi kết quả sang DTO (đã bao gồm logic tổng hợp)
        return schedules.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // SỬA TÊN PHƯƠNG THỨC: saveClassScheduleDTO -> saveClassSchedule
    @Override
    public ClassScheduleDTO saveClassSchedule(ClassScheduleDTO classScheduleDTO) {
        ClassSchedule classScheduleToSave = convertToEntity(classScheduleDTO);
        ClassSchedule savedClassSchedule = classScheduleRepository.save(classScheduleToSave);
        return convertToDTO(savedClassSchedule);
    }

    // SỬA TÊN PHƯƠNG THỨC: deleteClassScheduleDTO -> deleteClassSchedule
    @Override
    public void deleteClassSchedule(Integer id) {
        classScheduleRepository.deleteById(id);
    }
    
    // =================================================================
    // PHƯƠNG THỨC HỖ TRỢ CHUYỂN ĐỔI NGÀY
    // =================================================================

    private String convertDayNumberToDayName(int dayNumber) {
        switch (dayNumber) {
            case 1: return "Chủ Nhật";
            case 2: return "Thứ Hai";
            case 3: return "Thứ Ba";
            case 4: return "Thứ Tư";
            case 5: return "Thứ Năm";
            case 6: return "Thứ Sáu";
            case 7: return "Thứ Bảy";
            default: return "";
        }
    }
}