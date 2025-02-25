package com.nirvana.common_ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Preview()
@Composable
fun ErrorDialog(
    message: String = "Default Message",
    onRefresh: () -> Unit = {}
) {
    Dialog(onDismissRequest = { }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = message
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onRefresh() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.retry))
                }
            }
        }
    }
}