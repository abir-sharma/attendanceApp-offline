package com.example.attendanceappoffline.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "school_classes")
data class SchoolClassEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
