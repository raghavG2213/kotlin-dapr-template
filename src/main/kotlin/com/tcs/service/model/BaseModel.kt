package com.tcs.service.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * This is an example for Model class
 */
@Document(collection = "template-container")
data class BaseModel(
        @Id
        var id: String = "1",
        var modId: String = "1",
        var modDesc: String = "Basic",
        val deliveryStream: Int?=0,
        val storeNumber:Long?=0,
        val startDate: String?="",
        val endDate: String?="",
        val delivererNumber: Int=0
)