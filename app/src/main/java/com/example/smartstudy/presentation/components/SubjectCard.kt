package com.example.smartstudy.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartstudy.R

@Composable
fun SubjectCard(
    modifier: Modifier = Modifier,
    subjectName:String,
    gradientColor:List<Color>,
    onClick: () -> Unit
){
    Box(modifier = modifier
        .size(150.dp)
        .clickable { onClick() }
        .background(
            Brush.verticalGradient(gradientColor),
            shape = MaterialTheme.shapes.medium
        )
        .verticalScroll(rememberScrollState())
    )
    {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .size(80.dp),
                painter = painterResource(id = R.drawable.books),
                contentDescription = subjectName
            )
            Text(
                modifier = Modifier.fillMaxSize(),
                text = subjectName,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }
    }
}