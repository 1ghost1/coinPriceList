package com.example.cringecoindisplayer

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity


class NewAsset : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addasset)
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var newAssetInput:EditText = findViewById<EditText>(R.id.NewSymbol)
        newAssetInput.requestFocus()
        imm.showSoftInput(newAssetInput, InputMethodManager.SHOW_IMPLICIT)

        var addButton = findViewById<Button>(R.id.AddButton)
        addButton.setOnClickListener {



            var inAssets:ArrayList<String> =
                (intent.getSerializableExtra("currentAssets") as ArrayList<String>)
            inAssets.add(newAssetInput.text as String)

            var intent = Intent(this@NewAsset, MainActivity::class.java)
            intent.putExtra("currentAssets", inAssets)
            startActivity(intent)
        }
    }
}