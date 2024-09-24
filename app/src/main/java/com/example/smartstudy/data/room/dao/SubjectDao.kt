package com.example.smartstudy.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.smartstudy.domain.model.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Upsert
    suspend fun upsertSubject(subject: Subject)

    @Query("SELECT COUNT(*) From SUBJECT")
    fun getTotalSubjectCount():Flow<Int>

    @Query("SELECT SUM(goalHours) From SUBJECT")
    fun getTotalGoalHour():Flow<Float>

    @Query("SELECT * FROM Subject WHERE subjectId = :subjectId")
    suspend fun getSubjectById(subjectId:Int): Subject?

    @Query("DELETE FROM Subject WHERE subjectId = :subjectId")
    suspend fun deleteSubject(subjectId:Int)

    @Query("SELECT * FROM Subject")
    fun getAllSubject():Flow<List<Subject>>
}
























