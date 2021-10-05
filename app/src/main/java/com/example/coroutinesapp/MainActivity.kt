package com.example.coroutinesapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var tvadvice: TextView
    private lateinit var bnewadvice: Button
    val adviceUrl = "https://api.adviceslip.com/advice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            tvadvice = findViewById(R.id.tvadvice)
            bnewadvice = findViewById(R.id.bnewadvice)

            bnewadvice.setOnClickListener(){ requestApi() }
    }

     private fun requestApi()
     { CoroutineScope(IO).launch {
         val data = withContext(Default) { fetchAdvice() }

         if (data.isNotEmpty())
             updateAdvice(data)
         }
     }

     private fun fetchAdvice():String{
        var advice = ""
        try { advice = URL(adviceUrl).readText(Charsets.UTF_8) }
        catch (e:Exception) { println("Error $e") }
        return advice
     }

     private suspend fun updateAdvice(data:String)
     { withContext(Main)
        { val jsonObject = JSONObject(data)
          val slip = jsonObject.getJSONObject("slip")
          val advice = slip.getString("advice")

          tvadvice.text = advice
        }
     }

}