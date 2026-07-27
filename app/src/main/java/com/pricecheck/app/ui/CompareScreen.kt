package com.pricecheck.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pricecheck.app.data.AppDatabase
import com.pricecheck.app.data.SavedItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private enum class SortMode { RECENT, UNIT_PRICE }

@Composable
fun CompareScreen(db: AppDatabase) {
    val items by db.savedItemDao().getAll().collectAsState(initial = emptyList())
    var sortMode by remember { mutableStateOf(SortMode.RECENT) }
    val scope = rememberCoroutineScope()

    val sorted = when (sortMode) {
        SortMode.RECENT -> items.sortedByDescending { it.timestamp }
        SortMode.UNIT_PRICE -> items.sortedBy { it.unitPrice }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Saved Comparisons", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Row {
                FilterChip(
                    selected = sortMode == SortMode.RECENT,
                    onClick = { sortMode = SortMode.RECENT },
                    label = { Text("Recent") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primary, selectedLabelColor = MaterialTheme.colorScheme.onPrimary)
                )
                FilterChip(
                    selected = sortMode == SortMode.UNIT_PRICE,
                    onClick = { sortMode = SortMode.UNIT_PRICE },
                    label = { Text("Best Unit") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primary, selectedLabelColor = MaterialTheme.colorScheme.onPrimary)
                )
            }
        }

        if (sorted.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No items compared yet — save a comparison from the Calculator tab.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 16.dp)) {
                items(sorted, key = { it.id }) { item ->
                    val isBest = sorted.first().id == item.id && sortMode == SortMode.UNIT_PRICE
                    ComparisonCard(item = item, isBest = isBest, onDelete = {
                        scope.launch(Dispatchers.IO) { db.savedItemDao().delete(item) }
                    })
                }
            }
        }
    }
}

@Composable
private fun ComparisonCard(item: SavedItem, isBest: Boolean, onDelete: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            if (isBest) {
                Text(
                    "ABSOLUTE BEST DEAL",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(Modifier.weight(1f)) {
                    Text(item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("${item.size} x ${item.qty} units", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("$" + "%.2f".format(item.totalPrice), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                    Text(
                        "$" + "%.3f".format(item.unitPrice) + " / 100u",
                        color = if (isBest) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
