package com.example.smartstudy.domain.repository

import com.example.smartstudy.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun upsertTask(task: Task)

    suspend fun deleteTask(taskId:Int)

    suspend fun getTaskById(taskId:Int?): Task?

    fun getUpcomingTaskForSubject(subjectId: Int): Flow<List<Task>>

    fun getCompletedTaskForSubject(subjectId: Int): Flow<List<Task>>

    fun getAllUpcomingTask(): Flow<List<Task>>
}