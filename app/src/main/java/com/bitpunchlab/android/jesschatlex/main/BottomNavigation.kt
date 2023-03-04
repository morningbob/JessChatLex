package com.bitpunchlab.android.jesschatlex.main

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bitpunchlab.android.jesschatlex.*
import com.bitpunchlab.android.jesschatlex.ui.theme.JessChatLex

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val bottomItems = listOf<Destinations>(
        Main,
        Records,
        Profile,
        Logout
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinations = navBackStackEntry?.destination

    BottomNavigation(
        backgroundColor = JessChatLex.blueBackground,
        modifier = Modifier.height(70.dp)
    ) {
        bottomItems.forEach { item ->

            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.route,
                        modifier = Modifier.size(40.dp))
                       },
                selectedContentColor = Color.White,
                unselectedContentColor = MaterialTheme.colors.primaryVariant,
                selected = currentDestinations?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { dest ->
                            popUpTo(dest) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }

}