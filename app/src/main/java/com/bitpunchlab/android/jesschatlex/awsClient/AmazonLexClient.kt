package com.bitpunchlab.android.jesschatlex.awsClient

import android.content.Context
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobileconnectors.lex.interactionkit.InteractionClient
import com.amazonaws.mobileconnectors.lex.interactionkit.Response
import com.amazonaws.mobileconnectors.lex.interactionkit.config.InteractionConfig
import com.amazonaws.mobileconnectors.lex.interactionkit.continuations.LexServiceContinuation
import com.amazonaws.mobileconnectors.lex.interactionkit.listeners.InteractionListener
import com.amazonaws.regions.Regions
import com.amazonaws.services.lexrts.model.DialogState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

object AmazonLexClient {

    var lexClient : InteractionClient? = null
    //val messageList = ArrayList<String>()
    private val _messagesState = MutableStateFlow<String>("")
    val messagesState : StateFlow<String> = _messagesState.asStateFlow()

    fun initializeLex(context: Context) {
        AWSMobileClient.getInstance().initialize(context, object :
            Callback<UserStateDetails> {
            override fun onResult(result: UserStateDetails?) {
                Log.d("AWSClient", "initialize.onResult, userState: " + result?.getUserState().toString())
                val cred = AWSMobileClient.getInstance().awsCredentials

                val id = AWSMobileClient.getInstance().identityId
                Log.i("AWSClient", "id: $id")
                var botName = ""
                var botAlias = ""
                var botRegion = ""
                var lexConfig : JSONObject? = null

                try {
                    lexConfig = AWSMobileClient.getInstance().configuration.optJsonObject("Lex")
                    lexConfig = lexConfig.getJSONObject(lexConfig.keys().next())

                    botName = lexConfig.getString("Name")
                    botAlias = lexConfig.getString("Alias")
                    botRegion = lexConfig.getString("Region")
                    Log.i("name", botName)
                    Log.i("alias", botAlias)
                    Log.i("Region", botRegion)
                } catch (e: JSONException) {
                    Log.i("AWSClient", "parse JSOn error")
                }

                val lexInterConfig = InteractionConfig(
                    botName,
                    botAlias,
                    id
                )

                lexClient = InteractionClient(
                    context,
                    AWSMobileClient.getInstance(),
                    Regions.fromName(botRegion),
                    lexInterConfig
                )

                //lexClient.setAudioPlaybackListener()
                lexClient!!.setInteractionListener(interactionListener)

            }

            override fun onError(e: Exception?) {
                Log.i("AWSClient", "error initializing")
            }

        })
    }

    val interactionListener = object : InteractionListener {
        override fun onReadyForFulfillment(response: Response?) {
            Log.i("lex listener", "transaction completed")
            val responseText = response?.textResponse
            responseText?.let {
                //messageList.add(responseText)
                _messagesState.value = responseText
            }
            Log.i("lex listener", "response (ready): $responseText")
        }

        override fun promptUserToRespond(response: Response?, continuation: LexServiceContinuation?) {
            val responseText = response?.textResponse
            responseText?.let {
                _messagesState.value = responseText
            }
            Log.i("lex listener", "response (prompt): $responseText")
        }

        override fun onInteractionError(response: Response?, e: Exception?) {
            if (response != null) {
                if (DialogState.Failed.toString().equals(response.getDialogState())) {
                    Log.i("lex listener", "error detected")
                } else {
                    Log.i("lex listener", "please try again")
                }
            } else {
                Log.i("lex listener", "interaction error detected")
            }
        }

    }

    fun sendMessage(message: String) {
        lexClient!!.textInForTextOut(message, null)
    }
}