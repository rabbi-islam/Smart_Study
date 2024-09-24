package com.example.smartstudy.data.di

import android.app.Application
import androidx.room.Room
import com.example.smartstudy.data.room.dao.RoomDatabase
import com.example.smartstudy.data.room.dao.SessionDao
import com.example.smartstudy.data.room.dao.SubjectDao
import com.example.smartstudy.data.room.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): RoomDatabase {
        return Room.databaseBuilder(
            application,
            RoomDatabase::class.java,
            "studysmart_db"
        ).build()
    }
    @Provides
    @Singleton
    fun provideSubjectDao(database: RoomDatabase): SubjectDao {
        return database.subjectDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: RoomDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideSessionDao(database: RoomDatabase): SessionDao {
        return database.sessionDao()
    }
}













