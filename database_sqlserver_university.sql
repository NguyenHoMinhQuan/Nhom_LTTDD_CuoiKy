/*
  Database schema for University Management (SQL Server)
  Features: authentication, roles, students/lecturers/admin, classes, registration,
  timetable, announcements, basic stored procedures and example data.
  ---
  NOTES (ĐÃ CẬP NHẬT): Mật khẩu sẽ được quản lý bởi Spring Security dùng BCrypt.
  CSDL chỉ lưu trữ chuỗi hash BCrypt.
*/

-- Create database (optional)
-- CREATE DATABASE UniversityDB;
-- GO
-- USE UniversityDB;
-- GO

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
    
    --- THAY ĐỔI: Gộp 2 cột Hash/Salt thành 1 cột
    PasswordHash NVARCHAR(72) NOT NULL, -- BCrypt hash (thường 60 chars)
    
    RoleId TINYINT NOT NULL REFERENCES dbo.Role(RoleId),
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDETIME(),
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
    CreatedAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDETIME()
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
    RegisteredAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDETIME(),
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
    CreatedAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDETIME(),
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
    CreatedAt DATETIME2(3) NOT NULL DEFAULT SYSUTCDETIME(),
    CONSTRAINT UQ_Notification_User_Announcement UNIQUE (UserId, AnnouncementId)
);

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

--- THAY ĐỔI: Chèn hash BCrypt
-- Mật khẩu cho tài khoản này là: 'Admin@123'
-- Hash này được tạo bằng BCrypt (cost=10)
DECLARE @adminHash NVARCHAR(72) = N'$2a$10$3zZ.N.g6Y.f.Ua.hG9j/k.S.VL7.wQJcK.x.b/7.P4.7eS.bC.e';

INSERT INTO dbo.[User] (Username, Email, PasswordHash, RoleId)
VALUES ('admin', 'admin@university.edu', @adminHash, (SELECT RoleId FROM dbo.Role WHERE RoleName='Admin'));

-----------------------------
-- Stored procedure: Login (ĐÃ XÓA)
-----------------------------
--- THAY ĐỔI: Xóa bỏ sp_Login
--- Spring Security sẽ xử lý việc này trong ứng dụng.


-----------------------------
-- Stored procedure: Admin creates class
-----------------------------
CREATE PROCEDURE dbo.sp_CreateClass
    @CourseCode NVARCHAR(50),
    @ClassCode NVARCHAR(50),
    @Semester NVARCHAR(20),
    @Capacity INT,
    @LecturerUserName NVARCHAR(100), -- nullable
    @CreatedByUserName NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @courseId INT;
        SELECT @courseId = CourseId FROM dbo.Course WHERE CourseCode = @CourseCode;
        IF @courseId IS NULL
        BEGIN
            -- create course if not exists (optional)
            INSERT INTO dbo.Course (CourseCode, CourseName) VALUES (@CourseCode, @CourseCode);
            SET @courseId = SCOPE_IDENTITY();
        END

        DECLARE @lecturerId INT = NULL;
        IF @LecturerUserName IS NOT NULL
            SELECT @lecturerId = U.UserId FROM dbo.[User] U JOIN dbo.Lecturer L ON U.UserId = L.LecturerId WHERE U.Username = @LecturerUserName;

        DECLARE @createdBy INT = (SELECT UserId FROM dbo.[User] WHERE Username = @CreatedByUserName);

        INSERT INTO dbo.Class (CourseId, ClassCode, Semester, Capacity, LecturerId, CreatedBy)
        VALUES (@courseId, @ClassCode, @Semester, @Capacity, @lecturerId, @createdBy);

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK;
        THROW;
    END CATCH
END
GO

-----------------------------
-- Stored procedure: Student registers to class
-----------------------------
CREATE PROCEDURE dbo.sp_RegisterToClass
    @StudentUserName NVARCHAR(100),
    @ClassCode NVARCHAR(50)
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @studentId INT = (SELECT UserId FROM dbo.[User] WHERE Username = @StudentUserName AND RoleId = (SELECT RoleId FROM dbo.Role WHERE RoleName='Student'));
        IF @studentId IS NULL
            RAISERROR('Student not found',16,1);

        DECLARE @classId INT = (SELECT ClassId FROM dbo.Class WHERE ClassCode = @ClassCode);
        IF @classId IS NULL
            RAISERROR('Class not found',16,1);

        -- check capacity
        DECLARE @cap INT = (SELECT Capacity FROM dbo.Class WHERE ClassId = @classId);
        DECLARE @cur INT = (SELECT COUNT(1) FROM dbo.Registration WHERE ClassId = @classId AND Status='Registered');
        IF @cur >= @cap
        BEGIN
            INSERT INTO dbo.Registration (StudentId, ClassId, Status) VALUES (@studentId, @classId, 'Waitlist');
        END
        ELSE
        BEGIN
            INSERT INTO dbo.Registration (StudentId, ClassId, Status) VALUES (@studentId, @classId, 'Registered');
        END

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK;
        THROW;
    END CATCH
END
GO

-----------------------------
-- Stored procedure: Post announcement
-----------------------------
CREATE PROCEDURE dbo.sp_PostAnnouncement
    @AuthorUserName NVARCHAR(100),
    @Title NVARCHAR(300),
    @Body NVARCHAR(MAX),
    @IsGlobal BIT = 0,
    @TargetClassCode NVARCHAR(50) = NULL
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @authorId INT = (SELECT UserId FROM dbo.[User] WHERE Username = @AuthorUserName);
    DECLARE @targetClassId INT = NULL;
    IF @TargetClassCode IS NOT NULL
        SELECT @targetClassId = ClassId FROM dbo.Class WHERE ClassCode = @TargetClassCode;

    INSERT INTO dbo.Announcement (Title, Body, AuthorId, IsGlobal, TargetClassId)
    VALUES (@Title, @Body, @authorId, @IsGlobal, @targetClassId);

    -- Optionally create notifications for relevant users
    DECLARE @AnnID INT = SCOPE_IDENTITY();
    IF @IsGlobal = 1
    BEGIN
        INSERT INTO dbo.Notification (UserId, AnnouncementId)
        SELECT U.UserId, @AnnID FROM dbo.[User] U WHERE U.IsActive = 1;
    END
    ELSE IF @targetClassId IS NOT NULL
    BEGIN
        INSERT INTO dbo.Notification (UserId, AnnouncementId)
        SELECT R.StudentId, @AnnID FROM dbo.Registration R WHERE R.ClassId = @targetClassId AND R.Status = 'Registered';
    END
END
GO

-----------------------------
-- Query examples as stored procs: get announcements for a user, get timetable
-----------------------------
CREATE PROCEDURE dbo.sp_GetAnnouncementsForUser
    @UserName NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @uid INT = (SELECT UserId FROM dbo.[User] WHERE Username = @UserName);

    -- Announcements that are global OR targeted to classes the user registered to OR posted by the user
    SELECT A.AnnouncementId, A.Title, A.Body, A.AuthorId, U.Username as AuthorUserName, A.IsGlobal, A.TargetClassId, A.CreatedAt
    FROM dbo.Announcement A
    JOIN dbo.[User] U ON A.AuthorId = U.UserId
    WHERE A.IsGlobal = 1
        OR A.AuthorId = @uid
        OR A.TargetClassId IN (SELECT ClassId FROM dbo.Registration WHERE StudentId = @uid AND Status = 'Registered')
    ORDER BY A.CreatedAt DESC;
END
GO

CREATE PROCEDURE dbo.sp_GetTimetableForStudent
    @StudentUserName NVARCHAR(100),
    @Semester NVARCHAR(50) = NULL
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @sid INT = (SELECT UserId FROM dbo.[User] WHERE Username = @StudentUserName);

    SELECT C.ClassCode, CO.CourseCode, CO.CourseName, CS.DayOfWeek, CS.StartTime, CS.EndTime, CS.Room, L.FullName as LecturerName
    FROM dbo.Registration R
    JOIN dbo.Class C ON R.ClassId = C.ClassId
    JOIN dbo.Course CO ON C.CourseId = CO.CourseId
    LEFT JOIN dbo.Lecturer L ON C.LecturerId = L.LecturerId
    JOIN dbo.ClassSchedule CS ON C.ClassId = CS.ClassId
    WHERE R.StudentId = @sid AND R.Status = 'Registered'
    AND (@Semester IS NULL OR C.Semester = @Semester)
    ORDER BY CS.DayOfWeek, CS.StartTime;
END
GO

CREATE PROCEDURE dbo.sp_GetTimetableForLecturer
    @LecturerUserName NVARCHAR(100),
    @Semester NVARCHAR(50) = NULL
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @lid INT = (SELECT UserId FROM dbo.[User] WHERE Username = @LecturerUserName);

    SELECT C.ClassCode, CO.CourseCode, CO.CourseName, CS.DayOfWeek, CS.StartTime, CS.EndTime, CS.Room
    FROM dbo.Class C
    JOIN dbo.Course CO ON C.CourseId = CO.CourseId
    JOIN dbo.ClassSchedule CS ON C.ClassId = CS.ClassId
    WHERE C.LecturerId = @lid
    AND (@Semester IS NULL OR C.Semester = @Semester)
    ORDER BY CS.DayOfWeek, CS.StartTime;
END
GO

-----------------------------
-- Example data (small)
-----------------------------
INSERT INTO dbo.Course (CourseCode, CourseName, Credit) VALUES ('CS101','Lập trình Cơ bản',3),('CS201','Cấu trúc dữ liệu',3);

--- THAY ĐỔI: Dùng BCrypt cho các user mẫu
-- Example lecturer user (Password: 'Lect@123')
DECLARE @lectHash NVARCHAR(72) = N'$2a$10$T1q.V3p5.L01.h/mG7.PSeFqExi.3m.xT5.e.c/E.1R.a/j.U.5.6';
INSERT INTO dbo.[User] (Username, Email, PasswordHash, RoleId) VALUES ('lect01','lect01@uni.edu',@lectHash,(SELECT RoleId FROM dbo.Role WHERE RoleName='Lecturer'));
INSERT INTO dbo.Lecturer (LecturerId, StaffNumber, FullName, Department) VALUES ((SELECT UserId FROM dbo.[User] WHERE Username='lect01'),'GV001','Nguyen Van A','Khoa CNTT');

-- Example student user (Password: 'Stud@123')
DECLARE @stuHash NVARCHAR(72) = N'$2a$10$WqB.o7.R.j.q/s/S.c.u/uV.z/c.s.T.j/a.z/y.x/j.w.u.z.w';
INSERT INTO dbo.[User] (Username, Email, PasswordHash, RoleId) VALUES ('sv001','sv001@uni.edu',@stuHash,(SELECT RoleId FROM dbo.Role WHERE RoleName='Student'));
INSERT INTO dbo.Student (StudentId, StudentNumber, FullName, DateOfBirth, Faculty, Year) VALUES ((SELECT UserId FROM dbo.[User] WHERE Username='sv001'),'SV001','Tran Thi B', '2002-05-10','CNTT',3);

-- Create a class and schedule
INSERT INTO dbo.Class (CourseId, ClassCode, Semester, Capacity, LecturerId, CreatedBy)
VALUES ((SELECT CourseId FROM dbo.Course WHERE CourseCode='CS101'),'CS101-01','2025 Spring',40,(SELECT LecturerId FROM dbo.Lecturer WHERE StaffNumber='GV001'), (SELECT UserId FROM dbo.[User] WHERE Username='admin'));

INSERT INTO dbo.ClassSchedule (ClassId, DayOfWeek, StartTime, EndTime, Room)
VALUES ((SELECT ClassId FROM dbo.Class WHERE ClassCode='CS101-01'),1,'08:00','09:30','A101'),
        ((SELECT ClassId FROM dbo.Class WHERE ClassCode='CS101-01'),3,'10:00','11:30','A101');

-- Register student to class
EXEC dbo.sp_RegisterToClass @StudentUserName='sv001', @ClassCode='CS101-01';

-- Post an announcement to class
EXEC dbo.sp_PostAnnouncement @AuthorUserName='lect01', @Title='Lịch học thay đổi', @Body='Buổi học thứ 3 chuyển sang phòng B201', @IsGlobal=0, @TargetClassCode='CS101-01';

-- End of script