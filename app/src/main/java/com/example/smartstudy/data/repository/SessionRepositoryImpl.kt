package com.example.smartstudy.data.repository

import com.example.smartstudy.domain.model.Session
import com.example.smartstudy.domain.repository.SessionRepository
import com.example.smartstudy.data.room.dao.SessionDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(private val sessionDao: SessionDao):SessionRepository {

    override suspend fun insertSession(session: Session) {
        sessionDao.insertSession(session)
    }

    override suspend fun deleteSession(session: Session) {
        sessionDao.deleteSession(session)
    }

    override fun getAllSession(): Flow<List<Session>> {
        return sessionDao.getAllSession().map { session->
            session.sortedByDescending { it.date }
        }
    }

    override fun getRecentFiveSessions(): Flow<List<Session>> {
        return sessionDao.getAllSession()
            .map { session->
                session.sortedByDescending { it.date }
            }.take(count = 5)
    }

    override fun getRecentTenSessionsForSubject(subjectId: Int): Flow<List<Session>> {
        return sessionDao.getRecentSessionForSubject(subjectId)
            .map { session->
                session.sortedByDescending { it.date }
            }.take(count = 10)
    }

    override fun getTotalSessionsDuration(): Flow<Long> {
        return sessionDao.getTotalSessionsDuration()
    }

    override fun getTotalSessionsDurationBySubject(subjectId: Int): Flow<Long> {
       return sessionDao.getTotalSessionsDurationBySubject(subjectId)
    }
}





