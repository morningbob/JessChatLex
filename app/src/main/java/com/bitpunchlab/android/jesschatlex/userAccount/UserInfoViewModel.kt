package com.bitpunchlab.android.jesschatlex.userAccount

import android.util.Log
import androidx.lifecycle.ViewModel
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserInfoViewModel : ViewModel() {

    //val isLoggedIn = mutableStateOf<Boolean?>(null)

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn : StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    //private val loggedIn = mutableStateOf<Boolean?>(null)

    private fun listen() =
        CoroutineScope(Dispatchers.IO).launch {
            //var deferredResult = authListening()
            //deferredResult
            //return@launch authListening()
        }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _isLoggedIn.value = authListening()
        }
    }


    private suspend fun authListening() : Boolean =
        suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
            CoroutineScope(Dispatchers.IO).launch {
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
                                cancellableContinuation.resume(true) {}

                            } else {
                                Log.i("auth listener", "not signed in")
                                cancellableContinuation.resume(false) {}
                            }
                        }
                        InitializationStatus.FAILED.toString() ->
                            Log.i("AuthQuickstart", "Auth failed to succeed")
                        else -> when (AuthChannelEventName.valueOf(it.name)) {
                            AuthChannelEventName.SIGNED_IN -> {
                                Log.i("AuthQuickstart", "Auth just became signed in.")
                                //isSignedIn = true
                                cancellableContinuation.resume(true) {}
                            }
                            AuthChannelEventName.SIGNED_OUT -> {
                                Log.i("AuthQuickstart", "Auth just became signed out.")
                                cancellableContinuation.resume(false) {}
                            }
                            AuthChannelEventName.SESSION_EXPIRED -> {
                                Log.i("AuthQuickstart", "Auth session just expired.")
                                cancellableContinuation.resume(false) {}
                            }
                            AuthChannelEventName.USER_DELETED -> {
                                Log.i("AuthQuickstart", "User has been deleted.")
                                cancellableContinuation.resume(false) {}
                            }
                        }
                    }
                }
            }
        }


}