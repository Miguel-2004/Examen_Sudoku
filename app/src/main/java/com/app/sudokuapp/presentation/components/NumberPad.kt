package com.app.sudokuapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.sudokuapp.domain.model.SudokuSize

@Composable
fun SudokuNumberPad(
    sudokuSize: SudokuSize,
    onNumberSelected: (Int) -> Unit,
    onClearSelected: () -> Unit,
    onNoteSelected: (Int) -> Unit,
    isNoteModeActive: Boolean,
    modifier: Modifier = Modifier
) {
    val numberList = (1..sudokuSize.dimension).toList()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botones Clear y Notes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButtonPad(
                icon = Icons.Default.Backspace,
                onClick = onClearSelected,
                backgroundColor = MaterialTheme.colorScheme.errorContainer,
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            IconButtonPad(
                icon = Icons.Default.Create,
                onClick = { /* cambiar a modo notas */ },
                backgroundColor = if (isNoteModeActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                tint = if (isNoteModeActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botones de números
        val rows = numberList.chunked(5)
        rows.forEach { rowNumbers ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowNumbers.forEach { number ->
                    NumberButtonPad(
                        number = number,
                        onClick = {
                            if (isNoteModeActive) {
                                onNoteSelected(number)
                            } else {
                                onNumberSelected(number)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun IconButtonPad(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    backgroundColor: androidx.compose.ui.graphics.Color,
    tint: androidx.compose.ui.graphics.Color
) {
    Surface(
        shape = CircleShape,
        color = backgroundColor,
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint
            )
        }
    }
}

@Composable
fun NumberButtonPad(
    number: Int,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}
