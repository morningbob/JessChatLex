package com.bitpunchlab.android.jesschatlex

interface Destinations {
    val route : String

}

object Login : Destinations {
    override val route: String = "Login"

}

object CreateAccount : Destinations {
    override val route: String = "CreateAccount"
}