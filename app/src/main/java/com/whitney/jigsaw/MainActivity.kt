package com.whitney.jigsaw

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var resultImageView: ImageView? = null
    var buttonTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultImageView = findViewById(R.id.result_imageview)
        buttonTextView = findViewById(R.id.button_textview);
        buttonTextView?.setOnClickListener({
            buttonTextView?.setOnClickListener(null)

            val bitmap = EdgeDetector.sobel(resources, R.drawable.jigsaw_puzzle_01, buttonTextView, this)
            resultImageView?.setImageBitmap(bitmap)
        })
    }
}
