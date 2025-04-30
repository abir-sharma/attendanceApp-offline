package com.example.attendanceappoffline.data.attendance

import androidx.room.*
import com.example.attendanceappoffline.data.source.local.entity.AttendanceRecord
import com.example.attendanceappoffline.data.StudentWithAttendance
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceRecord(record: AttendanceRecord)

    @Query("SELECT * FROM attendance_records WHERE studentId = :studentId AND date = :date")
    suspend fun getAttendanceRecord(studentId: String, date: String): AttendanceRecord?


    @Query("""
    SELECT * FROM attendance_records 
    WHERE studentId = (
        SELECT studentId FROM face_embeddings 
        WHERE className = :className 
        AND firstName = :firstName 
        AND lastName = :lastName
        LIMIT 1
    ) AND date = :date
""")
    suspend fun getAttendanceForDateAndStudent(
        className: String,
        date: String,
        firstName: String,
        lastName: String
    ): AttendanceRecord?



    @Query("""
    UPDATE attendance_records 
    SET isPresent = :isPresent 
    WHERE studentId = (
        SELECT studentId 
        FROM face_embeddings 
        WHERE className = :className 
        AND section = :section 
        AND firstName = :firstName 
        AND lastName = :lastName
        LIMIT 1
    ) 
    AND date = :date
""")
    suspend fun updateAttendance(
        className: String,
        section: String,
        firstName: String,
        lastName: String,
        date: String,
        isPresent: Boolean
    )


    // Get attendance for all students of class "10A" on "2025-04-05"
    @Query("""
    SELECT face_embeddings.*, attendance_records.isPresent
    FROM face_embeddings
    LEFT JOIN attendance_records 
    ON face_embeddings.studentId = attendance_records.studentId AND attendance_records.date = :selectedDate
    WHERE face_embeddings.className = :className AND face_embeddings.section = :section
""")
    fun getStudentsWithAttendance(className: String,section: String, selectedDate: String): Flow<List<StudentWithAttendance>>

    @Query("SELECT * FROM attendance_records WHERE studentId = :studentId AND date = :date LIMIT 1")
    suspend fun getAttendanceByStudentIdAndDate(studentId: Int, date: String): AttendanceRecord?


    @Query("SELECT * FROM attendance_records WHERE isSynced = 0")
    suspend fun getUnsyncedRecords(): List<AttendanceRecord>

    @Query("UPDATE attendance_records SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Int)
}

