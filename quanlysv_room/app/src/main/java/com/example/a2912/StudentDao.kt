package com.example.a2912

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

import com.example.a2912.Student

@Dao
interface StudentDao {

    @Insert
    suspend fun insert(student: Student): Long

    @Update
    suspend fun update(student: Student): Int

    @Delete
    suspend fun delete(student: Student): Int

    @Query("SELECT * FROM student_table ORDER BY id ASC")
    fun getAllStudents(): LiveData<List<Student>>
}

