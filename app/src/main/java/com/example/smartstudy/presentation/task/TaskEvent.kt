package com.example.smartstudy.presentation.task

import androidx.compose.ui.graphics.Color
import com.example.smartstudy.domain.model.Session
import com.example.smartstudy.domain.model.Subject
import com.example.smartstudy.domain.model.Task
import com.example.smartstudy.presentation.subject.SubjectEvent
import com.example.smartstudy.util.Priority

sealed class TaskEvent {

    data class OnTitleChange(val title:String) : TaskEvent()

    data class OnDescriptionChange(val description:String) : TaskEvent()

    data class OnDateChange(val millis:Long?) : TaskEvent()

    data class OnPriorityChange(val priority:Priority) : TaskEvent()

    data class OnRelatedSubjectSelect(val subject: Subject) : TaskEvent()

    data object OnIsCompleteChange : TaskEvent()

    data object SaveTask : TaskEvent()

    data object DeleteTask : TaskEvent()
}