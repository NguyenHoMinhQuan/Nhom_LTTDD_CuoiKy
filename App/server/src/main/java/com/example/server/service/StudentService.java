package com.example.server.service;

import com.example.server.entity.Student;
import java.util.List;
public interface StudentService  {
    List<Student> getAllStudents();
    Student getStudentByNumber(String studentNumber);
}
