package com.example.smartstudy.data.repository

import com.example.smartstudy.data.room.dao.SessionDao
import com.example.smartstudy.domain.model.Subject
import com.example.smartstudy.domain.repository.SubjectRepository
import com.example.smartstudy.data.room.dao.SubjectDao
import com.example.smartstudy.data.room.dao.TaskDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao,
    private val taskDao: TaskDao,
    private val sessionDao: SessionDao,
):SubjectRepository {

    override suspend fun upsertSubject(subject: Subject) {
        subjectDao.upsertSubject(subject)
    }

    override fun getTotalSubjectCount(): Flow<Int> {
        return subjectDao.getTotalSubjectCount()
    }

    override fun getTotalGoalHour(): Flow<Float> {
        return subjectDao.getTotalGoalHour()
    }

    override suspend fun getSubjectById(subjectId: Int): Subject? {
        return subjectDao.getSubjectById(subjectId)
    }

    override suspend fun deleteSubject(subjectId: Int) {
     taskDao.deleteTaskBySubject(subjectId)
        sessionDao.deleteSessionsBySubjectId(subjectId)
        subjectDao.deleteSubject(subjectId)
    }

    override fun getAllSubject(): Flow<List<Subject>> {
       return subjectDao.getAllSubject()
    }
}