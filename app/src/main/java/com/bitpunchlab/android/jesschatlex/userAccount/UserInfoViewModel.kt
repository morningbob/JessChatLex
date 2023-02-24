package com.bitpunchlab.android.jesschatlex.userAccount

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex.database.ChatDao
import com.bitpunchlab.android.jesschatlex.database.ChatDatabase
import com.bitpunchlab.android.jesschatlex.models.Message
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class UserInfoViewModel(application: Application) : AndroidViewModel(application) {
//class UserInfoViewModel : ViewModel() {
    val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn : StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    var currentMessageList = ArrayList<Message>()

    @OptIn(InternalCoroutinesApi::class)
    val database = ChatDatabase.getInstance(application.applicationContext)

    val _allMessages = MutableStateFlow<List<Message>>(emptyList())
    var allMessages : StateFlow<List<Message>> = _allMessages
    //var allMessages = database.chatDAO.getAllMessage()

    init {
        viewModelScope.launch {
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

    fun getAllMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            database.chatDAO.getAllMessage().collect() { messages ->
                _allMessages.value = messages
            }
        }
    }

    fun insertMessage(message: Message) {
        viewModelScope.launch {
            database.chatDAO.insertMessages(message)
        }
    }

    private fun updateIsLoggedIn(newValue: Boolean) {
        if (isLoggedIn.value != newValue) {
            Log.i("user info", "update isLoggedIn $newValue")
            _isLoggedIn.value = newValue
        }
    }
}

class UserInfoViewModelFactory(private val application: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
            return UserInfoViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}