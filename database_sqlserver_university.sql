IF EXISTS (SELECT * FROM sys.databases WHERE name = N'UniversityDB')
BEGIN
    -- Đóng tất cả các kết nối đến cơ sở dữ liệu
    EXECUTE sp_MSforeachdb 'IF ''?'' = ''UniversityDB'' 
    BEGIN 
        DECLARE @sql AS NVARCHAR(MAX) = ''USE [?]; ALTER DATABASE [?] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;''
        EXEC (@sql)
    END'
    -- Xóa tất cả các kết nối tới cơ sở dữ liệu (thực hiện qua hệ thống master)
    USE master;

    -- Xóa cơ sở dữ liệu nếu tồn tại
    DROP DATABASE UniversityDB;
END
--Create database (optional) 
CREATE DATABASE UniversityDB;
GO
USE UniversityDB;
GO

-----------------------------
-- Table: Roles
-----------------------------
CREATE TABLE dbo.Role (
    RoleId TINYINT IDENTITY(1,1) PRIMARY KEY,
    RoleName NVARCHAR(50) NOT NULL UNIQUE -- e.g. 'Guest','Student','Lecturer','Admin'
);

-----------------------------
-- Table: [User]
-----------------------------
CREATE TABLE dbo.[User] (
    UserId INT IDENTITY(1,1) PRIMARY KEY,
    Username NVARCHAR(100) NOT NULL UNIQUE,
    Email NVARCHAR(320) NULL UNIQUE,
    [Password] NVARCHAR(72) NOT NULL, 
    RoleId TINYINT NOT NULL REFERENCES dbo.Role(RoleId),
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME(),
    LastLoginAt DATETIME2(3) NULL
);

-----------------------------
-- Table: Student
-----------------------------
CREATE TABLE dbo.Student (
    StudentId INT PRIMARY KEY REFERENCES dbo.[User](UserId),
    StudentNumber NVARCHAR(30) NOT NULL UNIQUE, -- Mã sinh viên
    FullName NVARCHAR(200) NOT NULL,
    DateOfBirth DATE NULL,
    Faculty NVARCHAR(150) NULL,
    Year INT NULL
);

-----------------------------
-- Table: Lecturer
-----------------------------
CREATE TABLE dbo.Lecturer (
    LecturerId INT PRIMARY KEY REFERENCES dbo.[User](UserId),
    StaffNumber NVARCHAR(30) NOT NULL UNIQUE,
    FullName NVARCHAR(200) NOT NULL,
    Department NVARCHAR(150) NULL
);

-----------------------------
-- Table: Course (a subject definition)
-----------------------------
CREATE TABLE dbo.Course (
    CourseId INT IDENTITY(1,1) PRIMARY KEY,
    CourseCode NVARCHAR(50) NOT NULL UNIQUE,
    CourseName NVARCHAR(300) NOT NULL,
    Credit INT NOT NULL DEFAULT 3,
    Description NVARCHAR(MAX) NULL
);

-----------------------------
-- Table: Class (a specific offering/section of a course)
-----------------------------
CREATE TABLE dbo.Class (
    ClassId INT IDENTITY(1,1) PRIMARY KEY,
    CourseId INT NOT NULL REFERENCES dbo.Course(CourseId) ON DELETE CASCADE,
    ClassCode NVARCHAR(50) NOT NULL UNIQUE,
    Semester NVARCHAR(20) NOT NULL, -- e.g. '2025 Spring'
    Capacity INT NOT NULL DEFAULT 50,
    LecturerId INT NULL REFERENCES dbo.Lecturer(LecturerId),
    CreatedBy INT NULL REFERENCES dbo.[User](UserId),
    CreatedAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME()
);

-----------------------------
-- Table: ClassSchedule (timetable entries)
-----------------------------
CREATE TABLE dbo.ClassSchedule (
    ScheduleId INT IDENTITY(1,1) PRIMARY KEY,
    ClassId INT NOT NULL REFERENCES dbo.Class(ClassId) ON DELETE CASCADE,
    DayOfWeek TINYINT NOT NULL CHECK (DayOfWeek BETWEEN 1 AND 7), -- 1=Mon .. 7=Sun
    StartTime TIME(0) NOT NULL,
    EndTime TIME(0) NOT NULL,
    Room NVARCHAR(100) NULL,
    CONSTRAINT UQ_ClassSchedule_Class_Day_Start UNIQUE (ClassId, DayOfWeek, StartTime)
);

-----------------------------
-- Table: Registration (student registers to a class)
-----------------------------
CREATE TABLE dbo.Registration (
    RegistrationId INT IDENTITY(1,1) PRIMARY KEY,
    StudentId INT NOT NULL REFERENCES dbo.Student(StudentId) ON DELETE CASCADE,
    ClassId INT NOT NULL REFERENCES dbo.Class(ClassId) ON DELETE CASCADE,
    RegisteredAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME(),
    Status NVARCHAR(20) NOT NULL DEFAULT 'Registered', -- Registered, Waitlist, Dropped
    Unique (StudentId, ClassId)
);

-----------------------------
-- Table: Announcement
-----------------------------
CREATE TABLE dbo.Announcement (
    AnnouncementId INT IDENTITY(1,1) PRIMARY KEY,
    Title NVARCHAR(300) NOT NULL,
    Body NVARCHAR(MAX) NOT NULL,
    AuthorId INT NOT NULL REFERENCES dbo.[User](UserId),
    IsGlobal BIT NOT NULL DEFAULT 0, -- if true -> visible to everyone
    TargetClassId INT NULL REFERENCES dbo.Class(ClassId), -- if set -> only to that class
    CreatedAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME(),
    UpdatedAt DATETIME2(3) NULL
);

-----------------------------
-- Table: Notification (optional)
-----------------------------
CREATE TABLE dbo.Notification (
    NotificationId INT IDENTITY(1,1) PRIMARY KEY,
    UserId INT NOT NULL REFERENCES dbo.[User](UserId) ON DELETE CASCADE,
    AnnouncementId INT NOT NULL REFERENCES dbo.Announcement(AnnouncementId) ON DELETE CASCADE,
    IsRead BIT NOT NULL DEFAULT 0,
    CreatedAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME(),
    CONSTRAINT UQ_Notification_User_Announcement UNIQUE (UserId, AnnouncementId)
);
CREATE TABLE dbo.Assignment (
    AssignmentId INT IDENTITY(1,1) PRIMARY KEY,
    ClassId INT NOT NULL REFERENCES dbo.Class(ClassId) ON DELETE CASCADE,
    Title NVARCHAR(200) NOT NULL,
    Description NVARCHAR(MAX) NULL,
    DueDate DATETIME2(0) NULL, -- Hạn nộp
    CreatedAt DATETIME2(3) DEFAULT GETUTCDATE()
);

-- Sinh viên nộp bài (Optional - nếu bạn cần tính năng nộp file)
CREATE TABLE dbo.Submission (
    SubmissionId INT IDENTITY(1,1) PRIMARY KEY,
    AssignmentId INT NOT NULL REFERENCES dbo.Assignment(AssignmentId),
    StudentId INT NOT NULL REFERENCES dbo.Student(StudentId),
    FileUrl NVARCHAR(MAX) NULL, -- Link file bài tập đã nộp
    SubmittedAt DATETIME2(3) DEFAULT GETUTCDATE(),
    Grade FLOAT NULL -- Điểm cho bài tập này
);

-------------------------------------------------------
-- 3. TẠO BẢNG FEEDBACK (PHẢN HỒI CỦA SINH VIÊN)
-------------------------------------------------------
CREATE TABLE dbo.Feedback (
    FeedbackId INT IDENTITY(1,1) PRIMARY KEY,
    ClassId INT NOT NULL REFERENCES dbo.Class(ClassId),
    StudentId INT NOT NULL REFERENCES dbo.Student(StudentId),
    Rating TINYINT CHECK (Rating BETWEEN 1 AND 5), -- Đánh giá 1-5 sao
    Comment NVARCHAR(MAX) NULL,
    CreatedAt DATETIME2(3) DEFAULT GETUTCDATE()
);

-------------------------------------------------------
-- 4. TẠO BẢNG MESSAGES (CHAT TRONG LỚP)
-------------------------------------------------------
-- Lưu lịch sử chat của lớp học
CREATE TABLE dbo.ClassMessage (
    MessageId INT IDENTITY(1,1) PRIMARY KEY,
    ClassId INT NOT NULL REFERENCES dbo.Class(ClassId) ON DELETE CASCADE,
    SenderId INT NOT NULL REFERENCES dbo.[User](UserId), -- Ai nhắn? (GV hoặc SV)
    Content NVARCHAR(MAX) NOT NULL,
    SentAt DATETIME2(3) DEFAULT GETUTCDATE()
);
GO
-----------------------------
-- Index suggestions
-----------------------------
CREATE INDEX IX_Registration_Student ON dbo.Registration(StudentId);
CREATE INDEX IX_Registration_Class ON dbo.Registration(ClassId);
CREATE INDEX IX_Class_Lecturer ON dbo.Class(LecturerId);
CREATE INDEX IX_ClassSchedule_Class ON dbo.ClassSchedule(ClassId);
CREATE INDEX IX_Announcement_TargetClass ON dbo.Announcement(TargetClassId);

-----------------------------
-- Seed roles and an admin user example (BCrypt)
-----------------------------
INSERT INTO dbo.Role (RoleName) VALUES ('Guest'), ('Student'), ('Lecturer'), ('Admin');

INSERT INTO dbo.[User] (Username, Email, [Password], RoleId)
VALUES ('admin', 'admin@ute.edu.vn', '123456', 4);

INSERT INTO dbo.[User] (Username, Email, [Password], RoleId)
VALUES
('lect1', 'lect1@ute.edu.vn', '123456', 3),
('lect2', 'lect2@ute.edu.vn', '123456', 3),
('lect3', 'lect3@ute.edu.vn', '123456', 3),
('lect4', 'lect4@ute.edu.vn', '123456', 3);

INSERT INTO dbo.Lecturer (LecturerId, StaffNumber, FullName, Department)
VALUES
((SELECT UserId FROM dbo.[User] WHERE Username='lect1'), 'GV001', N'Nguyễn Văn A', N'Công nghệ Phần mềm'),
((SELECT UserId FROM dbo.[User] WHERE Username='lect2'), 'GV002', N'Trần Thị B', N'Hệ thống Thông tin'),
((SELECT UserId FROM dbo.[User] WHERE Username='lect3'), 'GV003', N'Phạm Văn C', N'Mạng máy tính'),
((SELECT UserId FROM dbo.[User] WHERE Username='lect4'), 'GV004', N'Lê Thị D', N'Khoa học máy tính');

DECLARE @i INT = 1;
WHILE @i <= 40
BEGIN
    DECLARE @username NVARCHAR(100) = CONCAT('student', @i);
    DECLARE @email NVARCHAR(320) = CONCAT('student', @i, '@ute.edu.vn');
    DECLARE @fullname NVARCHAR(200) = CONCAT(N'Student ', @i);
    DECLARE @mssv NVARCHAR(30) = CONCAT('SV', FORMAT(@i, '000'));

    INSERT INTO dbo.[User] (Username, Email, [Password], RoleId)
    VALUES (@username, @email, '123456', 2);

    INSERT INTO dbo.Student (StudentId, StudentNumber, FullName, Faculty, Year)
    VALUES (
        (SELECT UserId FROM dbo.[User] WHERE Username = @username),
        @mssv,
        @fullname,
        N'Công nghệ Thông tin',
        2023
    );

    SET @i = @i + 1;
END

INSERT INTO dbo.Course (CourseCode, CourseName, Credit)
VALUES
('CT101', N'Nhập môn CNTT', 3),
('CT102', N'Lập trình C', 3),
('CT103', N'Lập trình Java', 3),
('CT104', N'Cơ sở dữ liệu', 3),
('CT105', N'Web cơ bản', 3),
('CT106', N'Web nâng cao', 3),
('CT107', N'Mạng máy tính', 3),
('CT108', N'Phân tích thiết kế HTTT', 3),
('CT109', N'AI cơ bản', 3),
('CT110', N'Hệ điều hành', 3);

INSERT INTO dbo.Class (CourseId, ClassCode, Semester, Capacity, LecturerId, CreatedBy)
SELECT CourseId, CONCAT(CourseCode, '_A'), '2025 Spring', 50,
    (SELECT TOP 1 LecturerId FROM dbo.Lecturer ORDER BY NEWID()),
    (SELECT UserId FROM dbo.[User] WHERE Username='admin')
FROM dbo.Course;

INSERT INTO dbo.Class (CourseId, ClassCode, Semester, Capacity, LecturerId, CreatedBy)
SELECT CourseId, CONCAT(CourseCode, '_B'), '2025 Spring', 50,
    (SELECT TOP 1 LecturerId FROM dbo.Lecturer ORDER BY NEWID()),
    (SELECT UserId FROM dbo.[User] WHERE Username='admin')
FROM dbo.Course;

DECLARE @Day1 TINYINT, @Day2 TINYINT, @Day3 TINYINT;
DECLARE @Index INT = 1;

DECLARE class_cursor CURSOR FOR 
SELECT ClassId FROM dbo.Class ORDER BY ClassId;

OPEN class_cursor;

DECLARE @ClassId INT;

WHILE 1=1
BEGIN
    FETCH NEXT FROM class_cursor INTO @ClassId;
    IF @@FETCH_STATUS <> 0 BREAK;

    -- Tính 3 ngày xoay vòng, đảm bảo phủ đủ 7 thứ
    SET @Day1 = ((@Index - 1) % 7) + 1;
    SET @Day2 = ((@Index + 1 - 1) % 7) + 1;
    SET @Day3 = ((@Index + 3 - 1) % 7) + 1;

    -- Thêm 3 lịch học cho mỗi class
    INSERT INTO dbo.ClassSchedule (ClassId, DayOfWeek, StartTime, EndTime, Room)
    VALUES 
        (@ClassId, @Day1, '07:00', '09:00', 'A101'),
        (@ClassId, @Day2, '09:00', '11:00', 'A102'),
        (@ClassId, @Day3, '13:00', '15:00', 'A201');

    SET @Index = @Index + 1;
END

CLOSE class_cursor;
DEALLOCATE class_cursor;

DECLARE @sid INT = 1;

WHILE @sid <= 40
BEGIN
    DECLARE @StudentId INT = (SELECT UserId FROM dbo.[User] WHERE Username = CONCAT('student', @sid));
    
    INSERT INTO dbo.Registration (StudentId, ClassId, Status)
    SELECT TOP 3 @StudentId, ClassId, 'Registered'
    FROM dbo.Class
    ORDER BY NEWID();

    SET @sid = @sid + 1;
END

INSERT INTO dbo.Announcement (Title, Body, AuthorId, IsGlobal)
VALUES
(N'Lịch nghỉ Tết', N'Truờng sẽ nghỉ Tết theo lịch Bộ GD.', (SELECT UserId FROM dbo.[User] WHERE Username='admin'), 1),
(N'Lịch thi giữa kỳ', N'Các lớp chuẩn bị thi giữa kỳ.', (SELECT UserId FROM dbo.[User] WHERE Username='lect1'), 1);

DECLARE @x INT = 1;
WHILE @x <= 8
BEGIN
    INSERT INTO dbo.Announcement (Title, Body, AuthorId, IsGlobal)
    VALUES (
        CONCAT(N'Thông báo số ', @x),
        CONCAT(N'Nội dung thông báo mẫu ', @x),
        (SELECT TOP 1 LecturerId FROM dbo.Lecturer ORDER BY NEWID()),
        0
    );
    SET @x = @x + 1;
END

INSERT INTO dbo.Assignment (ClassId, Title, Description, DueDate)
SELECT 
    ClassId,
    N'Bài tập ' + CAST(ROW_NUMBER() OVER (ORDER BY ClassId) AS NVARCHAR),
    N'Mô tả bài tập cho lớp',
    DATEADD(DAY, 7, GETUTCDATE())
FROM dbo.Class;

INSERT INTO dbo.Submission (AssignmentId, StudentId, FileUrl, Grade)
SELECT 
    a.AssignmentId,
    s.StudentId,
    N'https://drive.fake/' + s.StudentNumber + '.pdf',
    ROUND(RAND(CHECKSUM(NEWID())) * 4 + 6, 1) -- điểm 6.0 – 10.0
FROM dbo.Assignment a
JOIN dbo.Student s ON s.StudentId <= 40;

INSERT INTO dbo.Feedback (ClassId, StudentId, Rating, Comment)
SELECT TOP 100
    r.ClassId,
    r.StudentId,
    (ABS(CHECKSUM(NEWID())) % 5) + 1,
    N'Giảng viên dạy dễ hiểu'
FROM dbo.Registration r
ORDER BY NEWID();
INSERT INTO dbo.ClassMessage (ClassId, SenderId, Content)
SELECT TOP 200
    c.ClassId,
    u.UserId,
    N'Tin nhắn mẫu trong lớp'
FROM dbo.Class c
JOIN dbo.[User] u ON u.RoleId IN (2,3) -- Student, Lecturer
ORDER BY NEWID();
INSERT INTO dbo.Notification (UserId, AnnouncementId)
SELECT 
    u.UserId,
    a.AnnouncementId
FROM dbo.[User] u
CROSS JOIN dbo.Announcement a
WHERE a.IsGlobal = 1;

