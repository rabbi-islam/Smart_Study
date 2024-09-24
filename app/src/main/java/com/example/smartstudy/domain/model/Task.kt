package com.example.smartstudy.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId:Int?=null,
    val taskSubjectId:Int,
    val title:String,
    val description:String,
    val dueDate:Long,
    val priority:Int,
    val relatedToSubject:String,
    val isCompleted:Boolean
)
