package com.bitpunchlab.android.jesschatlex.awsClient

import android.util.Log
import aws.smithy.kotlin.runtime.ServiceException
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient
import com.amazonaws.services.cognitoidentity.model.CognitoIdentityProvider
import com.amazonaws.services.cognitoidentity.model.NotAuthorizedException
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignInOptions
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.security.InvalidParameterException

object CognitoClient {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun loginUser(email: String, password: String) : Boolean =
        suspendCancellableCoroutine<Boolean>() { cancellableContinuation ->
            val map = HashMap<String, String>()
            map.put("email", email)
            val options = AWSCognitoAuthSignInOptions.builder()
                //.authFlowType(AuthFlowType.USER_PASSWORD_AUTH)
                .metadata(map)
                .build()

            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result =  Amplify.Auth.signIn(username = email, password = password)

                    if (result.isSignedIn) {
                        Log.i("Cognito Sign in", "Sign in succeeded")
                        cancellableContinuation.resume(true) {}
                    } else if (!result.isSignedIn) {
                        Log.e("Cognito Sign in", "Sign in not complete")
                        cancellableContinuation.resume(false) {}
                    }
                    } catch (exception: AuthException) {
                        Log.e("Cognito Sign in", "Sign in failed", exception)
                        cancellableContinuation.resume(false) {}
                } catch (exception: InvalidParameterException) {
                    Log.i("Cognito Sign in", "inputs are invalid: $exception")
                    cancellableContinuation.resume(false) {}
                } catch (exception: NotAuthorizedException) {
                    Log.i("Cognito Sign in", "wrong email or password : $exception")
                    cancellableContinuation.resume(false) {}
                } catch (exception: Exception) {
                    Log.i("Cognito Sign in", "all exception: $exception")
                    cancellableContinuation.resume(false) {}
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
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result = Amplify.Auth.signUp(username = email, password = password, options)
                    Log.i("registration", "Result: $result")
                    cancellableContinuation.resume(true) {}
                } catch (error: Exception) {
                    Log.e("registration", "Sign up failed", error)
                    cancellableContinuation.resume(false) {}
                }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getUserNameEmail() : Pair<String, String>? =
        suspendCancellableCoroutine<Pair<String, String>?> { cancellableContinuation ->
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result = Amplify.Auth.fetchUserAttributes()
                    var name : String? = null
                    var email : String? = null
                    Log.i("Cognito Client, getUserName", "got back $result")
                    //parseUserName(result)
                    for (each in result) {
                        if (each.key == AuthUserAttributeKey.name()) {
                            Log.i("each, name value", each.value)
                            name = each.value
                        }
                        if (each.key == AuthUserAttributeKey.email()) {
                            Log.i("each, email value", each.value)
                            email = each.value
                        }
                        if (name != null && email != null) {
                            cancellableContinuation.resume(Pair(name, email)) {}
                        }
                    }
                    cancellableContinuation.resume(null) {}

                } catch (exception: Exception) {
                    Log.i("Cognito Client, getUserName", "failed")
                    cancellableContinuation.resume(null) {}
                }
            }

    }

    private fun parseUserName(jsonString: String) : String? {
        try {
            val dataJson = JSONObject(jsonString)
            Log.i("parsing user name", "the parsed object: $dataJson")
        } catch (e: JSONException) {
            Log.e("JSON Parser", "Error parsing data $e")
        }
        return ""
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun recoverUser(email: String) : Boolean =
        suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result = Amplify.Auth.resetPassword(username = email)
                    Log.i("password reset", "result: $result")
                    cancellableContinuation.resume(true) {}
                } catch (exception: Exception) {
                    Log.i("password reset", "error: $exception")
                    //sendVerificationCode(email)
                    cancellableContinuation.resume(false) {}
                }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun confirmVerificationCode(email: String, code: String) : Boolean =
        suspendCancellableCoroutine<Boolean> {  cancellableContinuation ->
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result = Amplify.Auth.confirmSignUp(email, code)
                    Log.i("send verification code", "result $result")
                    cancellableContinuation.resume(true) {}
                } catch (exception: Exception) {
                    Log.i("send verification code", "failed $exception")
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

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        //println("Handle $exception in CoroutineExceptionHandler")
        when (exception) {
            is NotAuthorizedException -> { Log.i("sign in exception", "not authorized")}
            is InvalidParameterException -> { Log.i("sign in exception", "invalid parameters") }
            is ServiceException -> { Log.i("sign in exception", "caught service exception")}
            is Exception -> {
                Log.i("sign in exception", "general exception : $exception")

            }
            else -> { Log.i("sign in exception", "default")}
        }
    }

}