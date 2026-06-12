package com.wesley.beefree.ui.components.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wesley.beefree.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeeTimePickerField(
    selectedHour: Int?,
    selectedMinute: Int?,
    onTimeSelect: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    emptyText: String,
) {
    val showTimePickerState = rememberSaveable { mutableStateOf(false) }
    var showTimePicker by showTimePickerState
    val timePickerState =
        rememberTimePickerState(
            initialHour = selectedHour ?: 12,
            initialMinute = selectedMinute ?: 0,
            is24Hour = true,
        )

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTimeSelect(timePickerState.hour, timePickerState.minute)
                        showTimePicker = false
                    },
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            text = {
                TimePicker(state = timePickerState)
            },
        )
    }

    Surface(
        onClick = { showTimePicker = true },
        shape = RoundedCornerShape(BeeSpacing.M),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = BeeSpacing.L),
        ) {
            Text(
                text =
                    if (selectedHour != null && selectedMinute != null) {
                        String.format("%02d:%02d", selectedHour, selectedMinute)
                    } else {
                        emptyText
                    },
                style = MaterialTheme.typography.displaySmall,
                color =
                    if (selectedHour != null) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                textAlign = TextAlign.Center,
            )
        }
    }
}
