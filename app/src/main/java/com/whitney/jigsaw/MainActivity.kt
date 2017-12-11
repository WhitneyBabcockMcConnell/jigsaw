package com.whitney.jigsaw

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var sourceImageView: ImageView? = null
    var resultImageView: ImageView? = null
    var buttonTextView: TextView? = null
    var onOffButtonTextView: TextView? = null
    val handler = Handler()
    val sobelRunnable = Runnable {
        buttonTextView?.isEnabled = false
        sourceImageView?.isEnabled = false
        val bitmap = EdgeDetector.sobel(sourceImageView?.drawable as BitmapDrawable, buttonTextView, this)
        resultImageView?.setImageBitmap(bitmap)
        sourceImageView?.isEnabled = true
        buttonTextView?.isEnabled = true
    }
    var currentImageResource: Int = 0
    val imageResources: IntArray = intArrayOf(
            R.drawable.jigsaw_puzzle_01,
            R.drawable.jigsaw_puzzle_02,
            R.drawable.jigsaw_puzzle_03,
            R.drawable.jigsaw_puzzle_04)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sourceImageView = findViewById(R.id.source_imageview)
        sourceImageView?.setImageResource(imageResources[currentImageResource])
        sourceImageView?.setOnClickListener({
            currentImageResource++
            if (currentImageResource >= imageResources.size) {
                currentImageResource = 0
            }
            sourceImageView?.setImageResource(imageResources[currentImageResource])
            resultImageView?.setImageResource(0)
            buttonTextView?.setText(R.string.generate_edges)
        })

        resultImageView = findViewById(R.id.result_imageview)
        buttonTextView = findViewById(R.id.button_textview);
        buttonTextView?.setOnClickListener({
            //            val thread = Thread(sobelRunnable);
//            thread.priority = Thread.MAX_PRIORITY
//            thread.start()

            handler.post(sobelRunnable)
        })

        onOffButtonTextView = findViewById(R.id.on_off_textview);
        onOffButtonTextView?.setOnClickListener({
            onOffButtonTextView?.setText(if ("On".equals(onOffButtonTextView?.text)) R.string.Off else R.string.On)
        })
    }
}
