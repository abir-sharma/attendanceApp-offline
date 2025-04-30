package com.example.attendanceappoffline.data

import androidx.room.TypeConverter
import java.nio.ByteBuffer
import android.graphics.Bitmap
import android.graphics.BitmapFactory


class Converters {
    @TypeConverter
    fun fromFloatArray(floatArray: FloatArray): ByteArray {
        val buffer = ByteBuffer.allocate(floatArray.size * 4)
        floatArray.forEach { buffer.putFloat(it) }
        return buffer.array()
    }

    @TypeConverter
    fun toFloatArray(byteArray: ByteArray): FloatArray {
        val buffer = ByteBuffer.wrap(byteArray)
        val floatArray = FloatArray(byteArray.size / 4)
        for (i in floatArray.indices) {
            floatArray[i] = buffer.getFloat()
        }
        return floatArray
    }



}
