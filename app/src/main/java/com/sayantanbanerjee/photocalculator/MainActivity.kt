package com.sayantanbanerjee.photocalculator

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage

class MainActivity : AppCompatActivity() {

    private lateinit var imageView : ImageView
    private lateinit var textView: TextView
    private lateinit var chooseImage : Button
    private lateinit var detectText: Button
    private val IMAGE_CAMERA_CODE = 101

    private lateinit var bitmap : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
        chooseImage = findViewById(R.id.chooseImage)
        detectText = findViewById(R.id.detectText)

        chooseImage.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, IMAGE_CAMERA_CODE)
            }
        }

        detectText.setOnClickListener {
            detectText()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAMERA_CODE && resultCode == RESULT_OK) {
            val extras : Bundle? = data?.extras
            bitmap = extras?.get("data") as Bitmap
            imageView.setImageBitmap(bitmap)
        }
    }

    private fun detectText(){
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val textRecognizer = FirebaseVision.getInstance()
            .onDeviceTextRecognizer

        textRecognizer.processImage(image)
            .addOnSuccessListener {
                val resultText: String = it.text
                textView.text = "INITIAL EXPRESSION : \n\n" + resultText
            }
            .addOnFailureListener {
                Toast.makeText(this, "NO TEXT DETECTED / FOUND", Toast.LENGTH_LONG).show()
            }

    }
}
