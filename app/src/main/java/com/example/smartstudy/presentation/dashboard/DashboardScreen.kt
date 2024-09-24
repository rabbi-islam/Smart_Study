package com.example.smartstudy.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartstudy.R
import com.example.smartstudy.domain.model.Session
import com.example.smartstudy.domain.model.Subject
import com.example.smartstudy.domain.model.Task
import com.example.smartstudy.presentation.components.AddSubjectDialog
import com.example.smartstudy.presentation.components.CountCard
import com.example.smartstudy.presentation.components.DeleteDialog
import com.example.smartstudy.presentation.components.SubjectCard
import com.example.smartstudy.presentation.components.studySessionList
import com.example.smartstudy.presentation.components.tasksList
import com.example.smartstudy.presentation.destinations.SessionScreenRouteDestination
import com.example.smartstudy.presentation.destinations.SubjectScreenRouteDestination
import com.example.smartstudy.presentation.destinations.TaskScreenRouteDestination
import com.example.smartstudy.presentation.subject.SubjectScreenNavArgs
import com.example.smartstudy.presentation.task.TaskScreenNavArgs
import com.example.smartstudy.util.SnackBarEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@RootNavGraph(start = true)
@Destination
@Composable
fun DashboardScreenRoute(
    navigator: DestinationsNavigator
) {
    val viewModel: DashboardViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tasks by viewModel.task.collectAsStateWithLifecycle()
    val recentSession by viewModel.recentSession.collectAsStateWithLifecycle()

    DashboardScreen(
        state = state,
        tasks = tasks,
        recentSession = recentSession,
        snackbarEvent = viewModel.snackbarEventFlow,
        onEvent = viewModel::onEvent,
        onSubjectCardClick = { subjectId ->
            subjectId?.let {
                val navArgs = SubjectScreenNavArgs(subjectId)
                navigator.navigate(SubjectScreenRouteDestination(navArgs))
            }
        },
        onTaskCardClick = { taskId ->
            val navArgs = TaskScreenNavArgs(taskId, subjectId = null)
            navigator.navigate(TaskScreenRouteDestination(navArgs))
        },
        onStartSessionButtonClick = {
            navigator.navigate(SessionScreenRouteDestination())
        }
    )
}

@Composable
private fun DashboardScreen(
    tasks:List<Task>,
    recentSession: List<Session>,
    onSubjectCardClick: (Int?) -> Unit,
    onTaskCardClick: (Int?) -> Unit,
    onStartSessionButtonClick: () -> Unit,
    onEvent: (DashboardEvent) -> Unit,
    snackbarEvent:SharedFlow<SnackBarEvent>,
    state: DashboardState
) {

    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState  = remember{SnackbarHostState()}

    LaunchedEffect(key1 = true) {
        snackbarEvent.collectLatest { event ->
            when (event) {
                is SnackBarEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackBarEvent.NavigateUp -> {}
            }
        }
    }

    AddSubjectDialog(
        isDialogOpen = isAddSubjectDialogOpen,
        onDismissRequest = { isAddSubjectDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(DashboardEvent.SaveSubject)
            isAddSubjectDialogOpen = false
        },
        subjectName = state.subjectName,
        goalHour = state.goalStudyHour,
        onGoalHourChange = { onEvent(DashboardEvent.OnGoalStudyHoursChange(it)) },
        onSubjectNameChange = { onEvent(DashboardEvent.OnSubjectNameChange(it)) },
        onColorChange = { onEvent(DashboardEvent.OnSubjectCardColorChange(it)) },
        selectedColor = state.subjectCardColor
    )
    DeleteDialog(
        isDialogOpen = isDeleteDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure, you want to delete this session? Your study tie will be reduce.",
        onDismissRequest = { isDeleteDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(DashboardEvent.DeleteSession)
            isDeleteDialogOpen = false
        }
    )


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        topBar = { DashboardScreenTopBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
        {
            item {
                CountCardSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjectCount = state.totalSubjectCount,
                    studyHour = state.totalStudiedHour.toString(),
                    goalStudyHour = state.totalGoalStudyHour.toString()
                )
            }
            item {
                SubjectCardSection(
                    modifier = Modifier.fillMaxSize(),
                    subjectList = state.subjects,
                    onAddIconClick = {
                        isAddSubjectDialogOpen = true
                    },
                    onSubjectCardClick = onSubjectCardClick
                )
            }
            item {
                Button(
                    onClick = onStartSessionButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp)

                ) {
                    Text(text = "Start Study Session ")
                }
            }
            tasksList(
                "UPCOMING TASKS",
                "You don't have any upcoming tasks.\n" +
                        " Click the + button in subject screen to add new task.",
                tasks,
                onCheckBoxClick = {
                    onEvent(DashboardEvent.OnTaskIsCompleteChange(it))
                },
                onTaskCardClick = onTaskCardClick
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            studySessionList(
                "RECENT STUDY SESSIONS",
                "You don't have any recend study sessions.\n" +
                        "Start a new study session to begin recording your progress.",
                recentSession,
                onDeleteIconClick = {
                    onEvent(DashboardEvent.OnDeleteSessionButtonClick(it))
                    isDeleteDialogOpen = true
                }
            )

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar() {
    CenterAlignedTopAppBar(title = {
        Text(text = "Smart Study", style = MaterialTheme.typography.headlineMedium)
    })
}


@Composable
private fun CountCardSection(
    modifier: Modifier,
    subjectCount: Int,
    studyHour: String,
    goalStudyHour: String
) {

    Row(modifier = modifier) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Subject Count",
            countText = "$subjectCount"
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Studied Hour",
            countText = studyHour
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Study Hour",
            countText = goalStudyHour
        )
    }
}

@Composable
private fun SubjectCardSection(
    modifier: Modifier,
    subjectList: List<Subject>,
    emptyListText: String = "You don't have any subjects.?\n Click the + button to add new subject.",
    onAddIconClick: () -> Unit,
    onSubjectCardClick: (Int?) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SUBJECTS",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 15.dp)
            )
            IconButton(onClick = onAddIconClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Subject"
                )
            }
        }

        if (subjectList.isEmpty()) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.books),
                contentDescription = emptyListText
            )
            Text(
                modifier = Modifier.fillMaxSize(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(subjectList) { subject ->
                SubjectCard(
                    subjectName = subject.name,
                    gradientColor = subject.colors.map { Color(it) },
                    onClick = { onSubjectCardClick(subject.subjectId) }
                )
            }
        }
    }
}





















