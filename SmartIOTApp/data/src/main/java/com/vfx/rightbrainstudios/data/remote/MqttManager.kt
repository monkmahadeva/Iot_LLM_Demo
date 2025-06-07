package com.vfx.rightbrainstudios.data.remote

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.util.*
import javax.inject.Inject

open class MqttManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val serverUri = "tcp://192.168.1.5:1883" // Change to your LAN IP
    private val clientId = "iot-client-" + UUID.randomUUID().toString()
    private val mqttClient = MqttAndroidClient(context.applicationContext, serverUri, clientId)

    open fun connect(topic: String, onMessage: (String) -> Unit) {
        val options = MqttConnectOptions().apply {
            isCleanSession = true
            isAutomaticReconnect = true
            keepAliveInterval = 30 // seconds
            connectionTimeout = 10 // seconds
        }

        mqttClient.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                val payload = message?.toString() ?: return
                Log.d("MQTT", "📩 Message received: $payload")
                onMessage(payload)
            }

            override fun connectionLost(cause: Throwable?) {
                Log.w("MQTT", "⚠️ Connection lost", cause)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d("MQTT", "✅ Delivery complete")
            }
        })

        mqttClient.connect(options, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MQTT", "✅ Connected to broker")
                subscribe(topic)
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.e("MQTT", "❌ Failed to connect", exception)
            }
        })
    }

    private fun subscribe(topic: String) {
        mqttClient.subscribe(topic, 1, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d("MQTT", "✅ Subscribed to $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.e("MQTT", "❌ Failed to subscribe to $topic", exception)
            }
        })
    }

    fun disconnect() {
        if (mqttClient.isConnected) {
            mqttClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MQTT", "🔌 Disconnected")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e("MQTT", "❌ Disconnect failed", exception)
                }
            })
        } else {
            Log.w("MQTT", "⚠️ Not connected, skipping disconnect")
        }
    }
}
