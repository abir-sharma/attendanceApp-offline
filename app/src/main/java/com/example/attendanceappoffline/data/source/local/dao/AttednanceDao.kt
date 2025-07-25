package com.example.attendanceappoffline.data.attendance

import androidx.room.*
import com.example.attendanceappoffline.data.source.local.entity.AttendanceRecord
import com.example.attendanceappoffline.data.StudentWithAttendance
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceRecord(record: AttendanceRecord)

    @Query("SELECT * FROM attendance_records WHERE studentHash = :studentHash AND date = :date")
    suspend fun getAttendanceRecord(studentHash: String, date: String): AttendanceRecord?


    @Query("""
    SELECT * FROM attendance_records 
    WHERE studentHash = (
        SELECT studentHash FROM face_embeddings 
        WHERE className = :className
        AND fullName = :fullName
        AND schoolId = :schoolId
        LIMIT 1
    ) AND date = :date
""")
    suspend fun getAttendanceForDateAndStudent(
        className: String,
        date: String,
        fullName: String,
        schoolId: String
    ): AttendanceRecord?



    @Query("""
    UPDATE attendance_records 
    SET status = :status
    WHERE studentHash = (
        SELECT studentHash 
        FROM face_embeddings 
        WHERE className = :className 
        AND fullName = :fullName 
        AND rollNumber = :rollNumber
        LIMIT 1
    ) 
    AND date = :date
""")
    suspend fun updateAttendance(
        className: String,
        fullName: String,
        rollNumber: String,
        date: String,
        status: String,
    )


    // Get attendance for all students of class "10A" on "2025-04-05"
    @Query("""
    SELECT face_embeddings.*, attendance_records.status = 'present' AS isPresent
    FROM face_embeddings
    LEFT JOIN attendance_records 
    ON face_embeddings.studentHash = attendance_records.studentHash AND attendance_records.date = :selectedDate
    WHERE face_embeddings.className = :className
""")
    fun getStudentsWithAttendance(className: String, selectedDate: String): Flow<List<StudentWithAttendance>>


    @Query("SELECT * FROM attendance_records WHERE studentHash = :studentHash AND date = :date LIMIT 1")
    suspend fun getAttendanceByStudentIdAndDate(studentHash: Int, date: String): AttendanceRecord?


    @Query("SELECT * FROM attendance_records WHERE isSynced = 0")
    suspend fun getUnsyncedRecords(): List<AttendanceRecord>

    @Query("UPDATE attendance_records SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Int)
}

