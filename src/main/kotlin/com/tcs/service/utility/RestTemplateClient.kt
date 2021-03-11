package com.tcs.service.utility


import com.tcs.service.constant.URLPath.GET_ALL_URI
import com.tcs.service.constant.URLPath.GET_REF_DATA
import com.tcs.service.constant.URLPath.INVOKE_DATA_POINT
import com.tcs.service.constant.URLPath.SERVICE_APP_ID
import com.tcs.service.constant.URLPath.SERVICE_APP_ID1
import com.tcs.service.model.*
import io.dapr.client.DaprClient
import io.dapr.client.DaprClientBuilder
import io.dapr.client.DaprHttp
import io.dapr.client.domain.HttpExtension
import org.json.JSONArray
import org.springframework.http.*
import org.springframework.stereotype.Component


@Component
class RestTemplateClient {

    val client : DaprClient = DaprClientBuilder().build()
    val mapPair = mapOf(Pair("Content-Type", "application/json"))


    fun getDeliveryMomentAll(storeNumber: Long?, streamNumber: Int?, deliveryDateTime: String?)
            : MutableList<Model>? {

        val mapParams: MutableMap<String, String> = mutableMapOf()

            mapParams["storeNumber"] = storeNumber.toString();
            mapParams["streamNumber"] = streamNumber.toString();
            mapParams["deliveryDateTime"] = deliveryDateTime.toString();

        return Utility.invokeFromDapr(mapParams)
    }

    //Using dapr to invoke post service
    fun postForm(params: Model) : ResponseEntity<ServiceResponse>? {


        val mapParams: MutableMap<String, String> = mutableMapOf()

            mapParams["storeNumber"] = params.data.toString();

        val response = client.invokeService(SERVICE_APP_ID, INVOKE_DATA_POINT + GET_ALL_URI, params,
                HttpExtension.POST, mapPair, JSONArray::class.java).block()
        
        return ResponseEntity.ok(ServiceResponse("200",
              "Success", response))

    }


    //Using dapr to invoke put service
    fun putForm(params: Model) : ResponseEntity<ServiceResponse>? {

        val mapParams: MutableMap<String, String> = mutableMapOf()

            mapParams["storeNumber"] = params.data.toString();

        val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, mapParams)

        val delivererList = client.invokeService(SERVICE_APP_ID1, GET_REF_DATA + GET_ALL_URI,
                    httpExtension, mapPair, Array<BaseModel>::class.java).block()?.toMutableList()

            if(!delivererList.isNullOrEmpty())
            {

                params.delivererNumber = delivererList[0].delivererNumber

                params.id = params.storeNumber.toString() + params.deliveryDateTime + params.streamNumber


               } else {
                return null
            }

        val response = client.invokeService(SERVICE_APP_ID, INVOKE_DATA_POINT + GET_ALL_URI, params,
                    HttpExtension.POST, mapPair, JSONArray::class.java).block()


        return ResponseEntity.ok(ServiceResponse("200",
                    "Success", response))
    }

    //Using dapr to invoke delete service
    fun delForm(id:String) {

        client.invokeService(SERVICE_APP_ID, "$INVOKE_DATA_POINT$GET_ALL_URI/$id",
                HttpExtension.DELETE, mapPair, JSONArray::class.java).block()

    }

}

