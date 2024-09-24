package com.example.smartstudy.domain.repository

import com.example.smartstudy.domain.model.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {

    suspend fun upsertSubject(subject: Subject)

    fun getTotalSubjectCount(): Flow<Int>

    fun getTotalGoalHour(): Flow<Float>

    suspend fun getSubjectById(subjectId:Int): Subject?

    suspend fun deleteSubject(subjectId:Int)

    fun getAllSubject(): Flow<List<Subject>>


}