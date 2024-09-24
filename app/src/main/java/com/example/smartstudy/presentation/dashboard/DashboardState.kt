package com.example.smartstudy.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.example.smartstudy.domain.model.Session
import com.example.smartstudy.domain.model.Subject

data class DashboardState(
    val totalSubjectCount: Int = 0,
    val totalStudiedHour: Float = 0f,
    val totalGoalStudyHour: Float = 0f,
    val subjects: List<Subject> = emptyList(),
    val subjectName: String = "",
    val goalStudyHour: String = "",
    val subjectCardColor: List<Color> = Subject.subjectCardColors.random(),
    val session: Session? = null
)
