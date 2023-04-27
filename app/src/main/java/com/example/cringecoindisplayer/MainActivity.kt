package com.example.cringecoindisplayer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cringecoindisplayer.ui.theme.CringeCoinDisplayerTheme
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : ComponentActivity() {

    var assets:HashMap<String, AssetData> = HashMap<String, AssetData>()
    var coinElements:HashMap<String, CoinActivity> = HashMap<String, CoinActivity>()
    var trackedSymbols:ArrayList<String> = ArrayList<String>()
    var progressVal:Int = 0
    val refreshTimeSec = 15

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.basedlayout)


        if( getIntent().getExtras() != null)
        {
            var inAssets:ArrayList<String> =
                (intent.getSerializableExtra("currentAssets") as ArrayList<String>)
            trackedSymbols = inAssets
        }else{
            trackedSymbols.add("bitcoin")
            trackedSymbols.add("ethereum")
        }

        getCoins()
        drawTokens()

        var addButton:Button = findViewById<Button>(R.id.AddAsset)
        addButton.setOnClickListener{
            it->
            addAssetButton()
        }


        val checkTimer = object: CountDownTimer(15*1000, 15*1000/100){
            override fun onTick(millisLeft: Long){
                var progress:ProgressBar = findViewById<ProgressBar>(R.id.UpdateCountdown)
                progressVal += 1
                progress.progress = progressVal
            }
            override fun onFinish() {
                getCoins()
                progressVal = 0
                this.start()

            }
        }

        val listUpdater = object: CountDownTimer(250, 100){
            override fun onTick(millisLeft: Long){

            }
            override fun onFinish() {
                drawTokens()

                this.start()

            }
        }

        listUpdater.start()
        checkTimer.start()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun drawTokens(){
        var controlLayout = findViewById<LinearLayout>(R.id.TokenHost)
       // println("BEEE")

        assets.keys.forEachIndexed{ index, asset->
            if(!coinElements.containsKey(asset)){
                val coinComp:CoinActivity = CoinActivity(applicationContext)
                coinComp.id = index

                coinComp.setAssetBadgeURL(assets[asset]!!.urlBadge)
                coinComp.setAssetName(assets[asset]!!.symbol)
                coinComp.setAssetData(assets[asset]!!)

                controlLayout.addView(coinComp)
                coinElements[asset] = coinComp
            }else{

                var coinAct:CoinActivity = coinElements[asset] as CoinActivity
                coinAct.setAssetData(assets[asset]!!)
            }


        }

    }

    fun getCoins(){

        var client = OkHttpClient()
        var apiUrl = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=${trackedSymbols.joinToString(separator = ",")}"
        val request = Request.Builder()
            .header("Accept", "application/json")
            .addHeader("X-CMC_PRO_API_KEY", "04d540cd-0439-4819-8a31-e2a2d0f6f2b3")
            .url(apiUrl).build()


        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed"+e?.toString())
            }
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call?, response: Response?) {

                println("RESP CODE " + response?.code())
                if(response?.code() == 200){
                    val body = response?.body()?.string()
                    //println("Body :"+body)
                    var rawResponse = JSONArray(body)
                    println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                    for(index in 0 until rawResponse.length()){
                        val assetRaw:JSONObject = rawResponse[index] as JSONObject
                        var priceStr = assetRaw.getDouble("current_price")

                        if(assets.containsKey(assetRaw["symbol"])){

                            var priceStr:String = assetRaw["current_price"].toString()
                            assets[assetRaw["symbol"]]?.lastPrice = priceStr.toFloat()
                            assets[assetRaw["symbol"]]?.lastUpdated = LocalDateTime.parse(assetRaw["last_updated"] as String, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))


                        }else{
                            var assetData = AssetData(symbol = assetRaw["symbol"] as String,
                                urlBadge = assetRaw["image"] as String,
                                lastPrice = priceStr.toFloat(),lastUpdated = LocalDateTime.parse(assetRaw["last_updated"] as String, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")))
                            assets.put(assetRaw["symbol"] as String, assetData)


                        }


                    }

                }else{
                    println("ERROR! Resp code " + response?.code())
                    println(response?.body()?.string())
                }
            }

        })

    }

    fun addAssetButton(){
        var intent = Intent(this@MainActivity, NewAsset::class.java)
        intent.putExtra("currentAssets", trackedSymbols)
        startActivity(intent)
    }
}




