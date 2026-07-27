package com.pricecheck.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

private data class ItemInput(
    val price: String = "",
    val size: String = "",
    val qty: String = "1"
)

// price / (size * qty), scaled to "per 100 units" like the mockup
private fun ItemInput.unitPrice(): Double {
    val p = price.toDoubleOrNull() ?: return 0.0
    val s = size.toDoubleOrNull() ?: return 0.0
    val q = qty.toDoubleOrNull() ?: 1.0
    if (p <= 0 || s <= 0 || q <= 0) return 0.0
    return (p / (s * q)) * 100
}

@Composable
fun CalculatorScreen(db: AppDatabase) {
    var a by remember { mutableStateOf(ItemInput()) }
    var b by remember { mutableStateOf(ItemInput()) }
    val scope = rememberCoroutineScope()

    val unitA = a.unitPrice()
    val unitB = b.unitPrice()
    val bestA = unitA > 0 && (unitB == 0.0 || unitA < unitB)
    val bestB = unitB > 0 && (unitA == 0.0 || unitB < unitA)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "SMART COMPARISON",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    "Unit Price Master",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Enter details to reveal the hidden value.",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            }
        }

        ItemCard(label = "A", input = a, onChange = { a = it }, unitPrice = unitA, isBest = bestA)
        ItemCard(label = "B", input = b, onChange = { b = it }, unitPrice = unitB, isBest = bestB)

        Button(
            onClick = {
                if (unitA > 0 && unitB > 0) {
                    scope.launch(Dispatchers.IO) {
                        val now = System.currentTimeMillis()
                        val dao = db.savedItemDao()
                        dao.insert(
                            SavedItem(
                                name = "Item A", store = "", totalPrice = a.price.toDouble(),
                                size = a.size.toDouble(), qty = a.qty.toDoubleOrNull() ?: 1.0,
                                unitPrice = unitA, timestamp = now
                            )
                        )
                        dao.insert(
                            SavedItem(
                                name = "Item B", store = "", totalPrice = b.price.toDouble(),
                                size = b.size.toDouble(), qty = b.qty.toDoubleOrNull() ?: 1.0,
                                unitPrice = unitB, timestamp = now
                            )
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Filled.Save, contentDescription = null)
            Spacer(Modifier.padding(4.dp))
            Text("Save Comparison")
        }

        OutlinedButton(
            onClick = { a = ItemInput(); b = ItemInput() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Filled.Restore, contentDescription = null)
            Spacer(Modifier.padding(4.dp))
            Text("Clear All")
        }
    }
}

@Composable
private fun ItemCard(
    label: String,
    input: ItemInput,
    onChange: (ItemInput) -> Unit,
    unitPrice: Double,
    isBest: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Item $label", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                if (isBest) {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary), shape = RoundedCornerShape(50)) {
                        Text(
                            "BEST DEAL",
                            color = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            Spacer(Modifier.padding(4.dp))
            OutlinedTextField(
                value = input.price,
                onValueChange = { onChange(input.copy(price = it)) },
                label = { Text("Total Price ($)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.padding(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = input.size,
                    onValueChange = { onChange(input.copy(size = it)) },
                    label = { Text("Size (g/ml)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = input.qty,
                    onValueChange = { onChange(input.copy(qty = it)) },
                    label = { Text("Qty") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.padding(8.dp))
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Price per 100 units", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    if (unitPrice > 0) "$" + "%.3f".format(unitPrice) else "$0.00",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isBest) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
