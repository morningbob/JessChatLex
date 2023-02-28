package com.bitpunchlab.android.jesschatlex.main

import com.bitpunchlab.android.jesschatlex.R

sealed class BottomNavItem(title: String, icon: Int, route: String) {
    object Home : BottomNavItem("Home", R.mipmap.home, "Main")
    object Records : BottomNavItem("Records", R.mipmap.records, "Records")
    object Logout : BottomNavItem("Logout", R.mipmap.logout, "Logout")
}