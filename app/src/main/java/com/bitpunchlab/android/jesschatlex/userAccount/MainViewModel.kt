package com.bitpunchlab.android.jesschatlex.userAccount

import android.app.Application
import android.util.Log
import androidx.compose.runtime.currentRecomposeScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.awsClient.AmazonLexClient
import com.bitpunchlab.android.jesschatlex.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex.database.ChatDatabase
import com.bitpunchlab.android.jesschatlex.helpers.WhoSaid
import com.bitpunchlab.android.jesschatlex.models.Message
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn : StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userInput = MutableStateFlow<String>("")
    val userInput : StateFlow<String> = _userInput.asStateFlow()

    var currentMessageList = ArrayList<Message>()

    @OptIn(InternalCoroutinesApi::class)
    val database = ChatDatabase.getInstance(application.applicationContext)

    val _allMessages = MutableStateFlow<List<Message>>(emptyList())
    var allMessages : StateFlow<List<Message>> = _allMessages.asStateFlow()
    //var allMessages = database.chatDAO.getAllMessage()
    val _newMessage = MutableStateFlow<Message?>(null)
    var newMessage : StateFlow<Message?> = _newMessage
    private val _loadingAlpha = MutableStateFlow<Float>(0f)
    val loadingAlpha: StateFlow<Float> = _loadingAlpha.asStateFlow()

    // listen to login status
    init {
        listenLoginStatus()
        listenLexMessages()
    }

    private fun listenLoginStatus() {
        CoroutineScope(Dispatchers.Default).launch {
            Amplify.Hub.subscribe(HubChannel.AUTH).collect {
                when (it.name) {
                    // this is the case when user logged in previously
                    // the app should automatically navigate to main page
                    InitializationStatus.SUCCEEDED.toString() -> {
                        Log.i("AuthQuickstart", "Auth successfully initialized")
                        //val authSession = Amplify.Auth.fetchAuthSession()
                        val authSessionDeferred = CoroutineScope(Dispatchers.IO).async {
                            Amplify.Auth.fetchAuthSession()
                        }
                        val authSession = authSessionDeferred.await()
                        if (authSession.isSignedIn) {
                            Log.i("auth listener", "signed in")
                            _isLoggedIn.value = true
                            //updateIsLoggedIn(true)
                            Log.i("isLoggedIn", "is passed to variable")
                            //cancellableContinuation.resume(true) {}
                            //emit(true)
                        } else {
                            Log.i("auth listener", "not signed in")
                            //cancellableContinuation.resume(false) {}
                            _isLoggedIn.value = false
                            //updateIsLoggedIn(false)
                            //emit(false)
                        }
                    }
                    InitializationStatus.FAILED.toString() ->
                        Log.i("AuthQuickstart", "Auth failed to succeed")
                    else -> when (AuthChannelEventName.valueOf(it.name)) {
                        AuthChannelEventName.SIGNED_IN -> {
                            Log.i("AuthQuickstart", "Auth just became signed in.")
                            //isSignedIn = true
                            //cancellableContinuation.resume(true) {}
                            _isLoggedIn.value = true
                            //updateIsLoggedIn(true)
                        }
                        AuthChannelEventName.SIGNED_OUT -> {
                            Log.i("AuthQuickstart", "Auth just became signed out.")
                            //cancellableContinuation.resume(false) {}
                            _isLoggedIn.value = false
                            //updateIsLoggedIn(false)
                        }
                        AuthChannelEventName.SESSION_EXPIRED -> {
                            Log.i("AuthQuickstart", "Auth session just expired.")
                            //cancellableContinuation.resume(false) {}
                            _isLoggedIn.value = false
                            //updateIsLoggedIn(false)
                        }
                        AuthChannelEventName.USER_DELETED -> {
                            Log.i("AuthQuickstart", "User has been deleted.")
                            //cancellableContinuation.resume(false) {}
                            _isLoggedIn.value = false
                            //updateIsLoggedIn(false)
                        }
                    }
                }
            }
        }
    }

    private fun listenLexMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            AmazonLexClient.messageState.collect() {
                if (it != "") {
                    val message = Message(
                        UUID.randomUUID().toString(),
                        WhoSaid.Lex,
                        it
                    )
                    _newMessage.value = message
                    currentMessageList.add(message)
                    insertMessage(message)
                    _loadingAlpha.value = 0f
                }
            }
        }
    }

    fun sendMessage(messageString: String) {
        _loadingAlpha.value = 1f
        if (messageString != "") {
            AmazonLexClient.sendMessage(messageString)
            val message = Message(
                UUID.randomUUID().toString(),
                WhoSaid.User,
                messageString
            )
            currentMessageList.add(message)
            CoroutineScope(Dispatchers.IO).launch {
                insertMessage(message)
            }
        }
    }

    fun getAllMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            database.chatDAO.getAllMessage().collect() { messages ->
                _allMessages.value = messages
            }
        }
    }

    private fun insertMessage(message: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            //withContext(uiScope.coroutineContext) { // Use instead of the default context
            database.chatDAO.insertMessages(message)
        }
    }

    fun logoutUser() {
        CoroutineScope(Dispatchers.IO).launch {
            CognitoClient.logoutUser()
        }
    }
}

class MainViewModelFactory(private val application: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}