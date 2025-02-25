package com.nirvana.feature_fetchhiring.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nirvana.common_ui.ErrorDialog
import com.nirvana.common_ui.LoadingIndicator
import com.nirvana.feature_fetchhiring.model.BaseDTO
import com.nirvana.feature_fetchhiring.model.HeadingDTO
import com.nirvana.feature_fetchhiring.model.HiringDTO
import com.nirvana.feature_fetchhiring.viewmodel.FetchHiringViewModel
import com.nirvana.feature_fetchhiring.viewmodel.HiringState

@Composable
fun FetchHiringScreen(
    viewModel: FetchHiringViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            is HiringState.Loading -> LoadingIndicator()
            is HiringState.Error -> ErrorDialog(
                message = (state as HiringState.Error).message,
                onRefresh = viewModel::fetchHiringData
            )

            is HiringState.Success -> HiringList(
                hiringItems = (state as HiringState.Success).data
            )
        }
    }
}


@Composable
private fun HiringList(hiringItems: List<BaseDTO>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(hiringItems, key = { it.hashCode() }) { item ->
            when (item) {
                is HeadingDTO -> ListHeader(listId = item.heading)
                is HiringDTO -> HiringItem(item = item)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ListHeader(listId: Int) {
    Text(
        text = "List ID: $listId",
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun HiringItem(item: HiringDTO) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "ID: ${item.id}"
            )
            Text(
                text = item.name ?: "",
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}