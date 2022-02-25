package com.tchs.demotask.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CarResponse {
    @SerializedName("cars")
    @Expose
    private var result: List<Result?>? = null

    fun getResult(): List<Result?>? {
        return result
    }

    class Result {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("car")
        @Expose
        var car: String? = null

        @SerializedName("car_model")
        @Expose
        var carModel: String? = null

        @SerializedName("car_color")
        @Expose
        var carColor: String? = null

        @SerializedName("car_model_year")
        @Expose
        var carModelYear: Int? = null

        @SerializedName("car_vin")
        @Expose
        var carVin: String? = null

        @SerializedName("price")
        @Expose
        var price: String? = null


        @SerializedName("availability")
        @Expose
        var availability: String? = null

    }
}