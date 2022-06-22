package com.test.henripotier.data.model

import com.google.gson.annotations.SerializedName


/**
 * This class defines the model Offer received by the server
 */
data class CommercialOffers(
    @SerializedName("offers")
    val offers: List<Offer>
)


