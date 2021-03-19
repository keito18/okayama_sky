package com.example.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    /**
     * お天気情報のURL。
     */
    val WEATHERINFO_URL = "https://api.openweathermap.org/data/2.5/weather"
    /**
     * お天気APIにアクセスすするためのAPI Key。
     * ※※※※※この値は各自のものに書き換える!!※※※※※
     */
    val APP_ID = "be644885bbf639d920fcbf0f7cdc12f6"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var biseiButton: Button = findViewById(R.id.button_bisei)
        var ryutenButton: Button = findViewById(R.id.button_ryuten)
        var satukiButton: Button = findViewById(R.id.button_satuki)


        var name = ""
        biseiButton.setOnClickListener {
            name = "714-1411"
            val url = "$WEATHERINFO_URL?zip=$name,JP&appid=$APP_ID&lang=ja&units=metric"
            asyncExecute(url)
        }
        ryutenButton.setOnClickListener {
            name = "701-2437"
            val url = "$WEATHERINFO_URL?zip=$name,JP&appid=$APP_ID&lang=ja&units=metric"
            asyncExecute(url)
        }
        satukiButton.setOnClickListener {
            name = "708-1515"
            val url = "$WEATHERINFO_URL?zip=$name,JP&appid=$APP_ID&lang=ja&units=metric"
            asyncExecute(url)

        }
    }




        fun postExecutorRunner(result: String) {
            val rootJSON = JSONObject(result)
            val tempJSON = rootJSON.getJSONObject("main")
            val tempmax = tempJSON.getString("temp_max")
            val tempmin = tempJSON.getString("temp_min")
            val weatherJSONArray = rootJSON.getJSONArray("weather")
            val weatherJSON = weatherJSONArray.getJSONObject(0)
            val weather =  weatherJSON.getString("description")
            var todayweather: TextView = findViewById(R.id.today_weather_text)
            var todaytempmax: TextView = findViewById(R.id.today_temp_max)
            var todaytempmin: TextView = findViewById(R.id.today_temp_min)
            todayweather.text = weather
            todaytempmax.text = tempmax
            todaytempmin.text = tempmin

        }

        private suspend fun backgroundTaskRunner(url: String): String {
            val returnVal = withContext(Dispatchers.IO) {
                var result = ""
                val url = URL(url)
                val con = url.openConnection() as? HttpURLConnection
                con?.run {
                    requestMethod = "GET"
                    connect()
                    result = is2String(inputStream)
                    disconnect()
                    inputStream.close()
                }
                result
            }
            return returnVal
        }




        private fun asyncExecute(url: String) {
            lifecycleScope.launch {
                val result = backgroundTaskRunner(url)  // (1)
                postExecutorRunner(result)  // (2)
            }
        }
        private fun is2String(stream: InputStream): String {
            val sb = StringBuilder()
            val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
            var line = reader.readLine()
            while(line != null) {
                sb.append(line)
                line = reader.readLine()
            }
            reader.close()
            return sb.toString()
        }


}