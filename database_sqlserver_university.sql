/*
  DATABASE HỢP NHẤT: UNIVERSITY DB
  - Bao gồm: Full Tables (User, Class, Assignment, Chat, Feedback)
  - Bao gồm: Triggers tự động thông báo
  - Bao gồm: Dữ liệu mẫu (Seed Data) để test App
*/

USE master;
GO

IF EXISTS (SELECT * FROM sys.databases WHERE name = N'UniversityDB')
BEGIN
    ALTER DATABASE UniversityDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE UniversityDB;
END
GO

CREATE DATABASE UniversityDB;
GO
USE UniversityDB;
GO

-----------------------------
-- 1. TẠO CÁC BẢNG (TABLES)
-----------------------------

-- Bảng Quyền (Role)
CREATE TABLE dbo.Role (
    RoleId TINYINT IDENTITY(1,1) PRIMARY KEY,
    RoleName NVARCHAR(50) NOT NULL UNIQUE
);

-- Bảng User (Sửa lại cột Password cho khớp file cũ)
CREATE TABLE dbo.[User] (
    UserId INT IDENTITY(1,1) PRIMARY KEY,
    Username NVARCHAR(100) NOT NULL UNIQUE,
    Email NVARCHAR(320) NULL UNIQUE,
    [Password] NVARCHAR(72) NOT NULL, -- Giữ nguyên tên cột Password
    RoleId TINYINT NOT NULL REFERENCES dbo.Role(RoleId),
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME(),
    LastLoginAt DATETIME2(3) NULL
);

-- Bảng Student
CREATE TABLE dbo.Student (
    StudentId INT PRIMARY KEY REFERENCES dbo.[User](UserId),
    StudentNumber NVARCHAR(30) NOT NULL UNIQUE,
    FullName NVARCHAR(200) NOT NULL,
    DateOfBirth DATE NULL,
    Faculty NVARCHAR(150) NULL,
    Year INT NULL
);

-- Bảng Lecturer
CREATE TABLE dbo.Lecturer (
    LecturerId INT PRIMARY KEY REFERENCES dbo.[User](UserId),
    StaffNumber NVARCHAR(30) NOT NULL UNIQUE,
    FullName NVARCHAR(200) NOT NULL,
    Department NVARCHAR(150) NULL
);

-- Bảng Course
CREATE TABLE dbo.Course (
    CourseId INT IDENTITY(1,1) PRIMARY KEY,
    CourseCode NVARCHAR(50) NOT NULL UNIQUE,
    CourseName NVARCHAR(300) NOT NULL,
    Credit INT NOT NULL DEFAULT 3,
    Description NVARCHAR(MAX) NULL
);

-- Bảng Class
CREATE TABLE dbo.Class (
    ClassId INT IDENTITY(1,1) PRIMARY KEY,
    CourseId INT NOT NULL REFERENCES dbo.Course(CourseId) ON DELETE CASCADE,
    ClassCode NVARCHAR(50) NOT NULL UNIQUE,
    Semester NVARCHAR(20) NOT NULL,
    Capacity INT NOT NULL DEFAULT 50,
    LecturerId INT NULL REFERENCES dbo.Lecturer(LecturerId),
    CreatedBy INT NULL REFERENCES dbo.[User](UserId),
    CreatedAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME()
);

-- Bảng ClassSchedule
CREATE TABLE dbo.ClassSchedule (
    ScheduleId INT IDENTITY(1,1) PRIMARY KEY,
    ClassId INT NOT NULL REFERENCES dbo.Class(ClassId) ON DELETE CASCADE,
    DayOfWeek TINYINT NOT NULL CHECK (DayOfWeek BETWEEN 1 AND 7),
    StartTime TIME(0) NOT NULL,
    EndTime TIME(0) NOT NULL,
    Room NVARCHAR(100) NULL,
    CONSTRAINT UQ_ClassSchedule_Class_Day_Start UNIQUE (ClassId, DayOfWeek, StartTime)
);

-- Bảng Registration
CREATE TABLE dbo.Registration (
    RegistrationId INT IDENTITY(1,1) PRIMARY KEY,
    StudentId INT NOT NULL REFERENCES dbo.Student(StudentId) ON DELETE CASCADE,
    ClassId INT NOT NULL REFERENCES dbo.Class(ClassId) ON DELETE CASCADE,
    RegisteredAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME(),
    Status NVARCHAR(20) NOT NULL DEFAULT 'Registered',
    Unique (StudentId, ClassId)
);

-- Bảng Announcement
CREATE TABLE dbo.Announcement (
    AnnouncementId INT IDENTITY(1,1) PRIMARY KEY,
    Title NVARCHAR(300) NOT NULL,
    Body NVARCHAR(MAX) NOT NULL,
    AuthorId INT NOT NULL REFERENCES dbo.[User](UserId),
    IsGlobal BIT NOT NULL DEFAULT 0,
    TargetClassId INT NULL REFERENCES dbo.Class(ClassId),
    CreatedAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME(),
    UpdatedAt DATETIME2(3) NULL
);

-- Bảng Notification
CREATE TABLE dbo.Notification (
    NotificationId INT IDENTITY(1,1) PRIMARY KEY,
    UserId INT NOT NULL REFERENCES dbo.[User](UserId) ON DELETE CASCADE,
    AnnouncementId INT NOT NULL REFERENCES dbo.Announcement(AnnouncementId) ON DELETE CASCADE,
    IsRead BIT NOT NULL DEFAULT 0,
    CreatedAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME(),
    CONSTRAINT UQ_Notification_User_Announcement UNIQUE (UserId, AnnouncementId)
);

-- Bảng Assignment (Giữ lại từ file của bạn)
CREATE TABLE dbo.Assignment (
    AssignmentId INT IDENTITY(1,1) PRIMARY KEY,
    ClassId INT NOT NULL REFERENCES dbo.Class(ClassId) ON DELETE CASCADE,
    Title NVARCHAR(200) NOT NULL,
    Description NVARCHAR(MAX) NULL,
    DueDate DATETIME2(0) NULL,
    CreatedAt DATETIME2(3) DEFAULT SYSUTCDATETIME()
);

-- Bảng Submission (Giữ lại từ file của bạn)
CREATE TABLE dbo.Submission (
    SubmissionId INT IDENTITY(1,1) PRIMARY KEY,
    AssignmentId INT NOT NULL REFERENCES dbo.Assignment(AssignmentId),
    StudentId INT NOT NULL REFERENCES dbo.Student(StudentId),
    FileUrl NVARCHAR(MAX) NULL,
    SubmittedAt DATETIME2(3) DEFAULT SYSUTCDATETIME(),
    Grade FLOAT NULL
);

-------------------------------------------------------
-- [MỚI] TÍNH NĂNG CHAT & FEEDBACK
-------------------------------------------------------

-- Bảng Tin nhắn lớp học
CREATE TABLE dbo.ClassMessage (
    MessageId INT IDENTITY(1,1) PRIMARY KEY,
    ClassId INT NOT NULL REFERENCES dbo.Class(ClassId) ON DELETE CASCADE,
    SenderId INT NOT NULL REFERENCES dbo.[User](UserId),
    Content NVARCHAR(MAX) NOT NULL,
    SentAt DATETIME2(3) DEFAULT SYSUTCDATETIME()
);

-- Bảng Feedback
CREATE TABLE dbo.Feedback (
    FeedbackId INT IDENTITY(1,1) PRIMARY KEY,
    ClassId INT NOT NULL REFERENCES dbo.Class(ClassId) ON DELETE CASCADE,
    StudentId INT NOT NULL REFERENCES dbo.Student(StudentId),
    Rating TINYINT CHECK (Rating BETWEEN 1 AND 5),
    Comment NVARCHAR(MAX) NULL,
    CreatedAt DATETIME2(3) DEFAULT SYSUTCDATETIME()
);

GO

-----------------------------
-- 2. TẠO TRIGGERS (Giữ lại từ file của bạn)
-----------------------------
CREATE TRIGGER trg_AfterInsertGlobalAnnouncement
ON dbo.Announcement
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    IF EXISTS (SELECT 1 FROM inserted WHERE IsGlobal = 1)
    BEGIN
        INSERT INTO dbo.Notification (UserId, AnnouncementId, IsRead, CreatedAt)
        SELECT u.UserId, i.AnnouncementId, 0, SYSUTCDATETIME()
        FROM dbo.[User] u
        CROSS JOIN inserted i
        WHERE i.IsGlobal = 1 AND u.IsActive = 1;
    END
END;
GO

CREATE TRIGGER trg_AfterInsertClassAnnouncement
ON dbo.Announcement
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    IF EXISTS (SELECT 1 FROM inserted WHERE TargetClassId IS NOT NULL)
    BEGIN
        INSERT INTO dbo.Notification (UserId, AnnouncementId, IsRead, CreatedAt)
        SELECT r.StudentId, i.AnnouncementId, 0, SYSUTCDATETIME()
        FROM dbo.Registration r
        JOIN inserted i ON r.ClassId = i.TargetClassId
        WHERE r.Status = 'Registered';
    END
END;
GO

-----------------------------
-- 3. DỮ LIỆU MẪU (SEED DATA) - QUAN TRỌNG ĐỂ TEST
-----------------------------
-- Role
INSERT INTO dbo.Role (RoleName) VALUES ('Guest'), ('Student'), ('Lecturer'), ('Admin');

-- Admin (Pass: Admin@123) - Lưu ý: Password ở đây đang để dạng text để dễ test, thực tế Spring Boot sẽ mã hóa
INSERT INTO dbo.[User] (Username, Email, [Password], RoleId)
VALUES ('admin', 'admin@university.edu', 'Admin@123', 4);

-- Lecturer (Pass: 123)
INSERT INTO dbo.[User] (Username, Email, [Password], RoleId) 
VALUES ('gv1', 'gv1@uni.edu', '123', 3);
INSERT INTO dbo.Lecturer (LecturerId, StaffNumber, FullName, Department) 
VALUES ((SELECT UserId FROM dbo.[User] WHERE Username='gv1'),'GV001', N'Nguyễn Văn Hùng','Khoa CNTT');

-- Student (Pass: 123)
INSERT INTO dbo.[User] (Username, Email, [Password], RoleId) 
VALUES ('sv1', 'sv1@uni.edu', '123', 2);
INSERT INTO dbo.Student (StudentId, StudentNumber, FullName, DateOfBirth, Faculty, Year) 
VALUES ((SELECT UserId FROM dbo.[User] WHERE Username='sv1'),'SV001', N'Thương Én', '2005-01-01','CNTT',3);

-- Course & Class
INSERT INTO dbo.Course (CourseCode, CourseName, Credit) VALUES ('IT001',N'Lập trình Android',3);

DECLARE @gvId INT = (SELECT LecturerId FROM dbo.Lecturer WHERE StaffNumber='GV001');
DECLARE @courseId INT = (SELECT CourseId FROM dbo.Course WHERE CourseCode='IT001');
DECLARE @adminId INT = (SELECT UserId FROM dbo.[User] WHERE Username='admin');

INSERT INTO dbo.Class (CourseId, ClassCode, Semester, Capacity, LecturerId, CreatedBy)
VALUES (@courseId, 'ANDROID-01', 'HK1 2025', 40, @gvId, @adminId);

DECLARE @classId INT = (SELECT ClassId FROM dbo.Class WHERE ClassCode='ANDROID-01');

-- Registration
DECLARE @svId INT = (SELECT StudentId FROM dbo.Student WHERE StudentNumber='SV001');
INSERT INTO dbo.Registration (StudentId, ClassId, Status) VALUES (@svId, @classId, 'Registered');

-- Sample Chat
DECLARE @gvUserId INT = (SELECT UserId FROM dbo.[User] WHERE Username='gv1');
DECLARE @svUserId INT = (SELECT UserId FROM dbo.[User] WHERE Username='sv1');

INSERT INTO dbo.ClassMessage (ClassId, SenderId, Content, SentAt)
VALUES 
(@classId, @gvUserId, N'Chào cả lớp Android!', DATEADD(MINUTE, -10, SYSUTCDATETIME())),
(@classId, @svUserId, N'Em chào thầy ạ.', DATEADD(MINUTE, -5, SYSUTCDATETIME()));

-- Sample Feedback
INSERT INTO dbo.Feedback (ClassId, StudentId, Rating, Comment)
VALUES (@classId, @svId, 5, N'Môn học rất bổ ích!');

GO