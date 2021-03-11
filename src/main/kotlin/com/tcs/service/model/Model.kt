package com.tcs.service.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id

data class Model (

        @Id
        var id: String?="",

        var data: BaseModel = BaseModel(),
        var storeNumber: Int=0,
        var streamNumber: Long? =0,
        var deliveryStreamName: String? ="",
        var schemaName: String? ="",
        var deliveryDateTime: String?="",
        var orderDateTime: String? ="",
        var orderStatus: String?="",
        var totalOrderQuantity: Long?=0,
        var delivererNumber:Int?=0
    )