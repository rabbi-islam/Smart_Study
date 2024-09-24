package com.example.smartstudy.data.room.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.smartstudy.domain.model.Session
import com.example.smartstudy.domain.model.Subject
import com.example.smartstudy.domain.model.Task

@Database(
    entities = [Subject::class, Session::class, Task::class],
    version = 1
)
@TypeConverters(ColorListConverter::class)
 abstract class RoomDatabase:RoomDatabase() {

   abstract fun subjectDao(): SubjectDao
   abstract fun sessionDao(): SessionDao
   abstract fun taskDao(): TaskDao
}



