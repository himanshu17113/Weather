package com.example.weather

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

const val base_url = "https://api.openweathermap.org/"
val api_key = "d9e8bee7a30ace93639a9366ee292141"



interface GetJsonDat {

    @GET(  "data/2.5/weather" )
    fun getweather(@Query("lat") lat:String, @Query("lon") lon:String, @Query("appid") appid:String,): Call<data>

}
object  weatherservice{
  val  weatherInstance : GetJsonDat
  init {
      val retrofit = Retrofit.Builder()
          .baseUrl(base_url)
          .addConverterFactory(GsonConverterFactory.create())
          .build()
      weatherInstance = retrofit.create(GetJsonDat::class.java)
  }
}
