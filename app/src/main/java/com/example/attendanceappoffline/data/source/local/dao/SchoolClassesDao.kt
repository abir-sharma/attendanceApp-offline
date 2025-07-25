package com.example.attendanceappoffline.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.attendanceappoffline.data.source.local.entity.SchoolClassEntity

@Dao
interface SchoolClassDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(classes: List<SchoolClassEntity>)

    @Query("SELECT * FROM school_classes")
    suspend fun getAllClasses(): List<SchoolClassEntity>

    @Query("DELETE FROM school_classes")
    suspend fun deleteAll()
}
