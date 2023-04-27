package com.example.cringecoindisplayer

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.withStyledAttributes
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class CoinActivity: LinearLayout {
    private var coinName:String = "<COIN>"
    private var badgeURL:String = "https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880"


    constructor(context: Context) : super(context){
        LayoutInflater.from(context).inflate(R.layout.fragment_coin_entry, this, true)

        var assetName:TextView = findViewById<TextView>(R.id.CoinName)
        var coinBadge:ImageView = findViewById<ImageView>(R.id.CoinBadge)
        assetName.text = coinName
        Picasso.get().load(badgeURL).into(coinBadge)

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context) {
        LayoutInflater.from(context).inflate(R.layout.fragment_coin_entry, this, true)


        context.withStyledAttributes(attrs, R.styleable.CoinActivity){
                coinName = getString(R.styleable.CoinActivity_coinName) as String
                badgeURL = getString(R.styleable.CoinActivity_badgeURL) as String
                var assetName:TextView = findViewById(R.id.CoinName)
                 var coinBadge:ImageView = findViewById(R.id.CoinBadge)
             assetName.text = getString(R.styleable.CoinActivity_coinName)
                Picasso.get().load(getString(R.styleable.CoinActivity_badgeURL)).into(coinBadge)

        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var assetName:TextView = findViewById(R.id.CoinName)
        var coinBadge:ImageView = findViewById(R.id.CoinBadge)

        assetName.text = coinName
        Picasso.get().load(badgeURL).into(coinBadge)


    }

    fun setAssetName( inAsset:String){
        var coinNameView:TextView = findViewById<TextView>(R.id.CoinName)
        coinNameView.text = inAsset
        coinName = inAsset
        invalidate()
        //requestLayout()
    }

    fun setAssetBadgeURL(url:String){
        badgeURL = url
        var coinBadge:ImageView = findViewById(R.id.CoinBadge)

        Picasso.get().load(badgeURL).into(coinBadge)
        invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setAssetData(data:AssetData){
        var lastPrice = findViewById<TextView>(R.id.LastPrice)
        var lastUpdate = findViewById<TextView>(R.id.LastUpdate)

        lastPrice.text = "$${data.lastPrice.toString()}"
        lastUpdate.text = "${data.lastUpdated.toString()}".uppercase()

    }

    fun callUpdate(){}

}