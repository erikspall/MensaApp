package de.erikspall.mensaapp.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    testItems: List<String> = emptyList()
) {

    Scaffold(
    ) {
        LazyColumn(modifier = modifier.padding(it)) {
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewDetailScreen() {
    DetailScreen()
}