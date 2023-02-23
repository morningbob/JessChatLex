package com.bitpunchlab.android.jesschatlex.models

import com.bitpunchlab.android.jesschatlex.helpers.WhoSaid

data class Message(
    val whoSaid: WhoSaid,
    val message: String
)