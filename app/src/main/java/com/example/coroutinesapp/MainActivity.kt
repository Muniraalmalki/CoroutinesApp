package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {
    private  lateinit var tvAdvice:TextView
    private lateinit var adviceButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvAdvice = findViewById(R.id.tvAdvice)
        adviceButton = findViewById(R.id.adviceButtton)

        adviceButton.setOnClickListener {
         //   CoroutineScope(IO).launch {
//                requestAPI()
             //   withContext(Main){
              //      tvAdvice.text = advice
             //   }
          //  }
            requestAPI()
        }

    }
    private fun requestAPI(){
        // we use Coroutines to fetch the data, then update the Recycler View if the data is valid
        CoroutineScope(IO).launch {
            // we fetch the data
            val data = async { fetchData() }.await()
            // once the data comes back, we populate our Recycler View
            if(data.isNotEmpty()){
                adviceText(data)
            }else{
                Log.d("MAIN", "Unable to get data")
            }
        }
    }
    private fun fetchData(): String{

        var response = ""
        try{
            response = URL("https://api.adviceslip.com/advice").readText()
        }catch(e: Exception){
            Log.d("MAIN", "ISSUE: $e")
        }
        // our response is saved as a string and returned
        return response
    }

    private suspend fun adviceText(result: String){
        withContext(Dispatchers.Main){

           val jsonObject = JSONObject(result)
            val slip = jsonObject.getJSONObject("slip")
            val id = slip.getInt("id")
            val advice = slip.getString("advice")
            tvAdvice.text = advice

        }
    }

}