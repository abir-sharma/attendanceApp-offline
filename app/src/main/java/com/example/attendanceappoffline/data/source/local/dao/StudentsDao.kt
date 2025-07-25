package com.example.attendanceappoffline.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.attendanceappoffline.data.source.local.entity.StudentEntity

@Dao
interface StudentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmbedding(embedding: StudentEntity)

    @Query("SELECT * FROM face_embeddings WHERE className = :className AND schoolId = :schoolId")
    suspend fun getAllEmbeddings(className: String,schoolId: String): List<StudentEntity>

    @Query("SELECT * FROM face_embeddings")
    suspend fun getAllFaceEmbeddings(): List<StudentEntity>

    @Query("""
    SELECT studentHash FROM face_embeddings 
    WHERE className = :className
    AND fullName = :fullName 
    AND schoolId = :schoolId
    LIMIT 1
""")

    suspend fun getStudentIdByDetails(
        className: String,
        fullName: String,
        schoolId: String
    ): String

}
