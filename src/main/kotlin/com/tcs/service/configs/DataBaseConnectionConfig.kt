package com.tcs.service.configs

import com.mongodb.ConnectionString
import org.springframework.data.mongodb.core.MongoTemplate
import com.mongodb.client.MongoClients
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.tcs.service.constant.URLPath
import com.tcs.service.utility.Utility
import io.dapr.client.DaprClient
import io.dapr.client.DaprClientBuilder
import io.dapr.client.DaprHttp
import io.dapr.client.domain.HttpExtension
import khttp.responses.Response
import org.apache.logging.log4j.kotlin.logger
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.core.CrudMethods
import java.lang.Exception
import javax.annotation.PostConstruct



/*
The below code is for connecting to utilities
 through secret from azure key vault
*/

class DataBaseConnectionConfig {

    @Bean
    fun mongo(): MongoClient? {

        val logger = logger()

        val connectionString = ConnectionString(Utility.getUtilitySecret("", "").toString())

        val mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).build()

        return MongoClients.create(mongoClientSettings)
    }


    @Bean
    fun mongoTemplate(): MongoTemplate {

        return MongoTemplate(mongo()!!, Utility.getUtilitySecret("", "").toString())
    }

    fun invokeFromDapr(params: MutableMap<String, String>) {


        val client : DaprClient = DaprClientBuilder().build()
        val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, params)

        val res1 = client.invokeService("", "URLPath",
                httpExtension, mapOf(Pair("Content-Type", "application/json")), JSONArray::class.java).block()?.toMutableList()

//        return res1

    }
}