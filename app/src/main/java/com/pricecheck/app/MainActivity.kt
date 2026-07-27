package com.pricecheck.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pricecheck.app.data.AppDatabase
import com.pricecheck.app.ui.CalculatorScreen
import com.pricecheck.app.ui.CompareScreen
import com.pricecheck.app.ui.theme.PriceCheckTheme

private data class Tab(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val tabs = listOf(
    Tab("calculator", "Calculator", Icons.Filled.Calculate),
    Tab("compare", "Compare", Icons.Filled.CompareArrows),
    Tab("scanner", "Scanner", Icons.Filled.QrCodeScanner),
    Tab("history", "History", Icons.Filled.History),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.get(applicationContext)
        setContent {
            PriceCheckTheme {
                AppRoot(db)
            }
        }
    }
}

@Composable
private fun AppRoot(db: AppDatabase) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "calculator",
            modifier = Modifier.padding(padding)
        ) {
            composable("calculator") { CalculatorScreen(db) }
            composable("compare") { CompareScreen(db) }
            composable("scanner") { ComingSoon("Barcode scanning") }
            composable("history") { ComingSoon("Full history") }
        }
    }
}

@Composable
private fun ComingSoon(feature: String) {
    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Text("$feature coming soon", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
