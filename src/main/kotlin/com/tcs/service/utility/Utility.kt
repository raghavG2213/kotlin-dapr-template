package com.tcs.service.utility

import com.tcs.service.constant.URLPath.GET_ALL_URI
import com.tcs.service.constant.URLPath.INVOKE_DATA_POINT
import com.tcs.service.constant.URLPath.SERVICE_APP_ID
import com.tcs.service.model.Model
import io.dapr.client.DaprClient
import io.dapr.client.DaprClientBuilder
import io.dapr.client.DaprHttp
import io.dapr.client.domain.HttpExtension


object Utility {

    // Using dapr to get secret from azure key vault
    fun getUtilitySecret(secretStore: String, secretKey: String): String? {

        val client : DaprClient = DaprClientBuilder().build()

        val mapParams: MutableMap<String, String> = mutableMapOf()
        mapParams["metadata.namespace"] = ""

        val secret = client.getSecret(secretStore,secretKey, mapParams).block()

        return secret?.get(secretKey.toString())
    }


    //Using dapr to invoke get service
    fun invokeFromDapr(params: MutableMap<String, String>): MutableList<Model>? {


        val client : DaprClient = DaprClientBuilder().build()
        val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, params)

        val res1 = client.invokeService(SERVICE_APP_ID, INVOKE_DATA_POINT + GET_ALL_URI,
                httpExtension, mapOf(Pair("Content-Type", "application/json")), Array<Model>::class.java).block()?.toMutableList()

        return res1

    }


}