package com.example.weather

import android.content.SharedPreferences
import java.util.Date
import java.text.SimpleDateFormat
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar

import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.databinding.ActivityMainBinding

import retrofit2.Call
import retrofit2.Callback
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val lat=intent.getStringExtra("lat")
        val long=intent.getStringExtra("long")

        window.statusBarColor= Color.parseColor("#1383C3")


 val WeatherData: SharedPreferences = getSharedPreferences("weather", MODE_PRIVATE)
        val editor = WeatherData.edit()

         binding.address.text = WeatherData.getString("Address",null)
        binding.updatedAt.text = WeatherData.getString("UpdatedAtText",null)
        binding.status.text = WeatherData.getString("WeatherDescription",null)
        binding.temp.text = WeatherData.getString("Temp",null)
        binding.tempMin.text = WeatherData.getString("TempMin",null)
        binding.tempMax.text = WeatherData.getString("TempMax",null)
        binding.sunrise.text =
        SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(WeatherData.getLong("Sunrise",0)*1000))
        binding.sunset.text =
            SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(WeatherData.getLong("Sunset",0)*1000))
        binding.wind.text = WeatherData.getString("WindSpeed",null)
        binding.pressure.text = WeatherData.getString("Pressure",null)
        binding.humidity.text = WeatherData.getString("Humidity",null)
        binding.feellike.text = WeatherData.getString("Feellike",null)

        if (lat != null) {
            if (long != null) {
                getWeather(lat, long,editor)

            }
        }

     //   setContentView(R.layout.activity_main)

      //  weatherTask().execute()

    }


    private fun getWeather(lat: String,long: String,editor: SharedPreferences.Editor) {


        val weather = weatherservice.weatherInstance.getweather(lat,long, "d9e8bee7a30ace93639a9366ee292141")

        weather.enqueue(
            object : Callback<data> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<data>,
                    response: retrofit2.Response<data>
                ) {
                    val jsonObj = response.body()
                    if (jsonObj != null)
                    { try {

                        val main = jsonObj.main
                        val sys = jsonObj.sys
                        val wind = jsonObj.wind
                        val weather = jsonObj.weather

                        val updatedAt: Int = jsonObj.dt

                        val current = LocalDateTime.now()

                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        val formatted = current.format(formatter)
                        val updatedAtText =
                            "Updated at: "+ formatted
                               // .format(Date(updatedAt*1000))
//                            "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)
//                                .format(Date(updatedAt*1000))
                        val df = DecimalFormat("#.#")
                        df.roundingMode = RoundingMode.CEILING
                        val tem = df.format(jsonObj.main.temp.minus( 273.15))
                        val temp = "$tem°C"
                        val tempMin = "Min Temp: " + jsonObj.main.temp_min.minus( 273.15).toInt().toString()+"°C"
                        val tempMax = "Max Temp: " + jsonObj.main.temp_max.minus(273.15).toInt().toString()+"°C"
                        val pressure = main.pressure.toString()
                        val humidity = main.humidity.toString()
                        val feellike = "Feel Like: " +  jsonObj.main.feels_like.minus( 273.15).roundToInt().toString() +"°C"
                        val sunrise:Long = sys.sunrise.toLong()
                        val sunset:Long = sys.sunset.toLong()
                        val windSpeed = wind.speed.toString()
                        val weatherDescription = jsonObj.weather[0].description

                        val address = jsonObj.name +", "+ sys.country

                        editor.apply {
                            putString("Temp",temp)
                            putString("TempMin",tempMin)
                            putString("TempMax",tempMax)
                            putString("Pressure",pressure)
                            putString("Humidity",humidity)
                            putString("Feellike",feellike)
                            putLong("Sunrise",sunrise)
                            putLong("Sunset",sunset)
                            putString("WindSpeed",windSpeed)
                            putString("WeatherDescription",weatherDescription)
                            putString("Address",address)
                            putString("UpdatedAtText",updatedAtText)
                            apply()
                        }

                        /* Populating extracted data into our views */
                        findViewById<TextView>(R.id.address).text = address
                        findViewById<TextView>(R.id.updated_at).text =  updatedAtText
                        findViewById<TextView>(R.id.status).text = weatherDescription
                        findViewById<TextView>(R.id.temp).text = temp
                        findViewById<TextView>(R.id.temp_min).text = tempMin
                        findViewById<TextView>(R.id.temp_max).text = tempMax
                        findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                        findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                        findViewById<TextView>(R.id.wind).text = windSpeed
                        findViewById<TextView>(R.id.pressure).text = pressure
                        findViewById<TextView>(R.id.humidity).text = humidity
                        binding.feellike.text = feellike

                    } catch (e: Exception) {
                        findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                        findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
                    }}


//                  {
//
//
//                        binding.city.text = weathers.name
//
//                        var lat = weathers.coord.lat
//                        var long = weathers.coord.lon
//                        binding.coordinates.text = "${lat} , ${long}"
//                        binding.weather.text = weathers.weather[0].main
//                        var tempr = weathers.main.temp
//
//                        binding.temp.text = "${tempr}°F"
//
//
//                        var mintemp = weathers.main.temp_min
//
//                        min_temp.text = "${mintemp}°F"
//                        var maxtemp = weathers.main.temp_max
//
//                        max_temp.text = "${maxtemp}°F"
//
//                        binding.pressure.text = weathers.main.pressure.toString()
//                        binding.humidity.text = weathers.main.humidity.toString()
//                        binding.wind.text = weathers.wind.speed.toString()
//
//                        degree.text = "Degree : " + weathers.wind?.deg.toString() + "°"
//
////        gust.text="Gust : "+response.getJSONObject("wind").getString("gust")
//                    }
//                                   else{
//                                       loadData(
//
//                                       )
//                                   }

                }

                override fun onFailure(call: Call<data>, t: Throwable) {

                    Toast.makeText(this@MainActivity,"open net", Toast.LENGTH_LONG).show()
                }



            } )



    }



//
//    inner class weatherTask() : AsyncTask<String, Void, String>() {
//        override fun onPreExecute() {
//            super.onPreExecute()
//            /* Showing the ProgressBar, Making the main design GONE */
//            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
//            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
//            findViewById<TextView>(R.id.errorText).visibility = View.GONE
//        }
//
//        override fun doInBackground(vararg params: String?): String? {
//            var response:String?
//            try{
//                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
//                    Charsets.UTF_8
//                )
//            }catch (e: Exception){
//                response = null
//            }
//            return response
//        }
//
//        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
////            try {
////                /* Extracting JSON returns from the API */
////                val jsonObj = JSONObject(result)
////                val main = jsonObj.getJSONObject("main")
////                val sys = jsonObj.getJSONObject("sys")
////                val wind = jsonObj.getJSONObject("wind")
////                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
////
////                val updatedAt:Long = jsonObj.getLong("dt")
////                val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
////                val temp = main.getString("temp")+"°C"
////                val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
////                val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"
////                val pressure = main.getString("pressure")
////                val humidity = main.getString("humidity")
////
////                val sunrise:Long = sys.getLong("sunrise")
////                val sunset:Long = sys.getLong("sunset")
////                val windSpeed = wind.getString("speed")
////                val weatherDescription = weather.getString("description")
////
////                val address = jsonObj.getString("name")+", "+sys.getString("country")
////
////                /* Populating extracted data into our views */
////                findViewById<TextView>(R.id.address).text = address
////                findViewById<TextView>(R.id.updated_at).text =  updatedAtText
////                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
////                findViewById<TextView>(R.id.temp).text = temp
////                findViewById<TextView>(R.id.temp_min).text = tempMin
////                findViewById<TextView>(R.id.temp_max).text = tempMax
////                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
////                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
////                findViewById<TextView>(R.id.wind).text = windSpeed
////                findViewById<TextView>(R.id.pressure).text = pressure
////                findViewById<TextView>(R.id.humidity).text = humidity
////
////                /* Views populated, Hiding the loader, Showing the main design */
////                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
////                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
////
////            } catch (e: Exception) {
////                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
////                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
////            }
//
//        }
//    }
}
