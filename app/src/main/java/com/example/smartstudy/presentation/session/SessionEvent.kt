package com.example.smartstudy.presentation.session

import com.example.smartstudy.domain.model.Session
import com.example.smartstudy.domain.model.Subject

sealed class SessionEvent {

    data class OnRelatedSubjectChange(val subject: Subject): SessionEvent()

    data class SaveSession(val duration: Long): SessionEvent()

    data class OnDeleteSessionButtonClick(val session: Session): SessionEvent()

    data object DeleteSession:SessionEvent()

    data object NotifyToUpdateSubject:SessionEvent()

    data class UpdateSubjectAndRelatedSubject(
        val subjectId:Int?,
        val relatedToSubject:String?
    ):SessionEvent()

}