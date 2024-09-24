package com.example.smartstudy.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.smartstudy.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsertTask(task: Task)

    @Query("DELETE FROM Task WHERE taskId = :taskId")
    suspend fun deleteTask(taskId:Int)

    @Query("DELETE FROM Task WHERE taskSubjectId = :subjectId")
    suspend fun deleteTaskBySubject(subjectId:Int)


    @Query("SELECT * FROM Task WHERE taskId = :taskId")
    suspend fun getTaskById(taskId:Int?): Task?

    @Query("SELECT * FROM Task WHERE taskSubjectId = :subjectId")
    fun getTaskForSubject(subjectId: Int): Flow<List<Task>>

    @Query("SELECT * FROM Task")
    fun getAllTask():Flow<List<Task>>


}








