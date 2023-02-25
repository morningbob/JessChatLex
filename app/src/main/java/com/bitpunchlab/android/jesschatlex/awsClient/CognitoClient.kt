package com.bitpunchlab.android.jesschatlex.awsClient

import android.util.Log
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignInOptions
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.*

object CognitoClient {

    suspend fun loginUser(email: String, password: String) : Boolean =
        suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
            val map = HashMap<String, String>()
            map.put("email", email)
            val options = AWSCognitoAuthSignInOptions.builder()
                //.authFlowType(AuthFlowType.USER_PASSWORD_AUTH)
                .metadata(map)
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = Amplify.Auth.signIn(username = email, password = password)
                    if (result.isSignedIn) {
                        Log.i("AuthQuickstart", "Sign in succeeded")
                    } else {
                        Log.e("AuthQuickstart", "Sign in not complete")
                    }
                } catch (error: AuthException) {
                    Log.e("AuthQuickstart", "Sign in failed", error)
                }
            }

        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun registerUser(name: String, email: String, password: String) : Boolean =
        suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
            val options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .userAttribute(AuthUserAttributeKey.name(), name)
                .build()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = Amplify.Auth.signUp(username = email, password = password, options)
                    Log.i("AuthQuickStart", "Result: $result")
                    cancellableContinuation.resume(true) {}
                } catch (error: AuthException) {
                    Log.e("AuthQuickStart", "Sign up failed", error)
                    cancellableContinuation.resume(false) {}
                }
            }
        }

    suspend fun logoutUser() {
        val signOutResult = Amplify.Auth.signOut()

        when(signOutResult) {
            is AWSCognitoAuthSignOutResult.CompleteSignOut -> {
                // Sign Out completed fully and without errors.
                Log.i("AuthQuickStart", "Signed out successfully")
            }
            is AWSCognitoAuthSignOutResult.PartialSignOut -> {
                // Sign Out completed with some errors. User is signed out of the device.
                signOutResult.hostedUIError?.let {
                    Log.e("AuthQuickStart", "HostedUI Error", it.exception)
                    // Optional: Re-launch it.url in a Custom tab to clear Cognito web session.

                }
                signOutResult.globalSignOutError?.let {
                    Log.e("AuthQuickStart", "GlobalSignOut Error", it.exception)
                    // Optional: Use escape hatch to retry revocation of it.accessToken.
                }
                signOutResult.revokeTokenError?.let {
                    Log.e("AuthQuickStart", "RevokeToken Error", it.exception)
                    // Optional: Use escape hatch to retry revocation of it.refreshToken.
                }
            }
            is AWSCognitoAuthSignOutResult.FailedSignOut -> {
                // Sign Out failed with an exception, leaving the user signed in.
                Log.e("AuthQuickStart", "Sign out Failed", signOutResult.exception)
            }
        }
    }

}