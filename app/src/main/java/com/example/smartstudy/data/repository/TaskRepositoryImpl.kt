package com.example.smartstudy.data.repository

import com.example.smartstudy.domain.model.Task
import com.example.smartstudy.domain.repository.TaskRepository
import com.example.smartstudy.data.room.dao.TaskDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao):TaskRepository {

    override suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task)
    }

    override suspend fun deleteTask(taskId: Int) {
        taskDao.deleteTask(taskId)
    }

    override suspend fun getTaskById(taskId: Int?): Task? {
       return taskDao.getTaskById(taskId)
    }

    override fun getUpcomingTaskForSubject(subjectId: Int): Flow<List<Task>> {
       return taskDao.getTaskForSubject(subjectId)
           .map { tasks-> tasks.filter {it.isCompleted.not() } }
           .map { tasks-> sortTasks(tasks) }
    }

    override fun getCompletedTaskForSubject(subjectId: Int): Flow<List<Task>> {
        return taskDao.getTaskForSubject(subjectId)
            .map { tasks-> tasks.filter {it.isCompleted } }
            .map { tasks-> sortTasks(tasks) }
    }

    override fun getAllUpcomingTask(): Flow<List<Task>> {
       return taskDao.getAllTask()
           .map { tasks-> tasks.filter {it.isCompleted.not() } }
           .map { tasks-> sortTasks(tasks) }
    }

    private fun sortTasks(tasks: List<Task>): List<Task> {
        return tasks.sortedWith(compareBy<Task> { it.dueDate }.thenByDescending { it.priority })

    }
}






