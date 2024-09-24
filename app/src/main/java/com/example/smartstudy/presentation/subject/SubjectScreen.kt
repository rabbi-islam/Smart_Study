package com.example.smartstudy.presentation.subject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartstudy.presentation.components.AddSubjectDialog
import com.example.smartstudy.presentation.components.CountCard
import com.example.smartstudy.presentation.components.DeleteDialog
import com.example.smartstudy.presentation.components.studySessionList
import com.example.smartstudy.presentation.components.tasksList
import com.example.smartstudy.presentation.destinations.TaskScreenRouteDestination
import com.example.smartstudy.presentation.task.TaskScreenNavArgs
import com.example.smartstudy.util.SnackBarEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

data class SubjectScreenNavArgs(
    val subjectId: Int
)

@Destination(navArgsDelegate = SubjectScreenNavArgs::class)
@Composable
fun SubjectScreenRoute(
    navigator: DestinationsNavigator
) {
    val subjectViewModel: SubjectViewModel = hiltViewModel()
    val state by subjectViewModel.state.collectAsStateWithLifecycle()

    SubjectScreen(
        state = state,
        onEvent = subjectViewModel::onEvent,
        snackbarEvent = subjectViewModel.snackbarEventFlow,
        onBackButtonIconClick = {
            navigator.navigateUp()
        },
        onAddTaskButtonClick = {
            val navArgs = TaskScreenNavArgs(null, state.currentSubjectId)
            navigator.navigate(TaskScreenRouteDestination(navArgs))
        },
        onTaskCardClick = { taskId ->
            val navArgs = TaskScreenNavArgs(taskId, null)
            navigator.navigate(TaskScreenRouteDestination(navArgs))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreen(
    state: SubjectState,
    onEvent: (SubjectEvent) -> Unit,
    onBackButtonIconClick: () -> Unit,
    onAddTaskButtonClick: () -> Unit,
    snackbarEvent:SharedFlow<SnackBarEvent>,
    onTaskCardClick: (Int?) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()
    val isFABExtended by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState  = remember{ SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        snackbarEvent.collectLatest { event ->
            when (event) {
                is SnackBarEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackBarEvent.NavigateUp -> {
                    onBackButtonIconClick()
                }
            }
        }
    }
    
    LaunchedEffect(key1 = state.studiedHours, key2 = state.goalStudyHours) {
        onEvent(SubjectEvent.UpdateProgress)
    }

    AddSubjectDialog(
        isDialogOpen = isAddSubjectDialogOpen,
        onDismissRequest = { isAddSubjectDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(SubjectEvent.UpdateSubject)
            isAddSubjectDialogOpen = false
        },
        subjectName = state.subjectName,
        goalHour = state.goalStudyHours,
        onGoalHourChange = { onEvent(SubjectEvent.OnGoalStudyHoursChange(it)) },
        onSubjectNameChange = { onEvent(SubjectEvent.OnSubjectNameChange(it)) },
        onColorChange = { onEvent(SubjectEvent.OnSubjectCardColorChange(it)) },
        selectedColor = state.subjectCardColors
    )
    DeleteDialog(
        isDialogOpen = isDeleteSubjectDialogOpen,
        title = "Delete Subject?",
        bodyText = "Are you sure, you want to delete this subject? Your all task and study will be deleted permanently.",
        onDismissRequest = { isDeleteSubjectDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(SubjectEvent.DeleteSubject)
            isDeleteSubjectDialogOpen = false
        }
    )
    DeleteDialog(
        isDialogOpen = isDeleteDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure, you want to delete this session? Your study tie will be reduce.",
        onDismissRequest = { isDeleteDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(SubjectEvent.DeleteSession)
            isDeleteDialogOpen = false

        }
    )
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SubjectScreenTopBar(
                title = state.subjectName,
                onBackButtonIconClick = onBackButtonIconClick,
                onDeleteButtonIconClick = { isDeleteSubjectDialogOpen = true },
                onEditButtonIconClick = { isAddSubjectDialogOpen = true },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Add Task") },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add") },
                onClick = onAddTaskButtonClick,
                expanded = isFABExtended
            )
        }

    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SubjectOverviewSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    studiedHour = state.studiedHours.toString(),
                    goalHour = state.goalStudyHours,
                    progress = state.progress
                )
            }
            tasksList(
                "UPCOMING TASKS",
                "You don't have any upcoming tasks.\n" +
                        " Click the + button to add new task.",
                state.upcomingTasks,
                onCheckBoxClick = {
                    onEvent(SubjectEvent.OnTaskIsCompleteChange(it))
                },
                onTaskCardClick = onTaskCardClick
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            tasksList(
                "COMPLETED TASKS",
                "You don't have any completed tasks.\n" +
                        " Click the checkbox on completion of task.",
                state.completedTasks,
                onCheckBoxClick = {
                    onEvent(SubjectEvent.OnTaskIsCompleteChange(it))
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
                state.recentSessions,
                onDeleteIconClick = {
                    isDeleteDialogOpen = true
                    onEvent(SubjectEvent.OnDeleteSessionButtonClick(it))
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreenTopBar(
    title: String,
    onBackButtonIconClick: () -> Unit,
    onDeleteButtonIconClick: () -> Unit,
    onEditButtonIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {

    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onBackButtonIconClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate Back"
                )
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            IconButton(onClick = onDeleteButtonIconClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Subject"
                )
            }
            IconButton(onClick = onEditButtonIconClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Subject"
                )
            }
        }

    )


}


@Composable
private fun SubjectOverviewSection(
    modifier: Modifier,
    studiedHour: String,
    goalHour: String,
    progress: Float
) {
    val percentageProgress = remember(progress) {
        (progress * 100).toInt().coerceIn(0, 100)
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Study Hour",
            countText = goalHour
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Studied Hours",
            countText = studiedHour
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier.size(75.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = 1f,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = progress,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round
            )
            Text(text = "$percentageProgress%")
        }
    }
}











