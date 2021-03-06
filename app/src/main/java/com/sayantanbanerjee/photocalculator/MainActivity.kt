package com.sayantanbanerjee.photocalculator

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.sayantanbanerjee.photocalculator.room.Information
import com.sayantanbanerjee.photocalculator.room.InformationDatabase
import com.sayantanbanerjee.photocalculator.room.InformationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var expressionView: TextView
    private lateinit var resultView: TextView
    private lateinit var chooseImage: Button
    private lateinit var showHistory: Button
    private val IMAGE_CAMERA_CODE = 101
    private lateinit var bitmap: Bitmap
    private lateinit var repository: InformationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository =
            InformationRepository(InformationDatabase.getInstance(application).informationDAO)

        imageView = findViewById(R.id.imageView)
        showHistory = findViewById(R.id.showHistory)
        expressionView = findViewById(R.id.expressionView)
        resultView = findViewById(R.id.resultView)
        chooseImage = findViewById(R.id.chooseImage)

        chooseImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startCamera()
            } else {
                val permissionArrays = arrayOf<String>(Manifest.permission.CAMERA)
                ActivityCompat.requestPermissions(
                    this,
                    permissionArrays,
                    1
                );
            }
        }

        showHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startCamera()
            }else{
                Toast.makeText(this,getString(R.string.denied),Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, IMAGE_CAMERA_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAMERA_CODE && resultCode == RESULT_OK) {
            val extras: Bundle? = data?.extras
            bitmap = extras?.get("data") as Bitmap
            imageView.setImageBitmap(bitmap)
            detectText()
        }
    }

    private fun detectText() {
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val textRecognizer = FirebaseVision.getInstance()
            .onDeviceTextRecognizer

        textRecognizer.processImage(image)
            .addOnSuccessListener {
                val initialText: String = it.text
                var expressionString: String = ""

                if (initialText != "") {
                    expressionString = getString(R.string.expression) + " " + initialText
                } else {
                    expressionString = getString(R.string.expressionNull)
                }
                expressionView.text = expressionString

                val resultText: String = initialText

                var stringOne: String = ""
                var stringTwo: String = ""
                var flag: Boolean = true
                var flagOperand: Boolean = false
                var operand: Int = 0
                var flagResult: Boolean = true
                var finalResult: String = ""

                try {
                    for (char: Char in resultText) {
                        if (char == ' ' || char == '\n') {
                            continue
                        } else if (char in '0'..'9') {
                            if (flag) {
                                stringOne += char
                            } else {
                                stringTwo += char
                            }
                        } else if (char == '+' || char == '-' || char == '_' || char == 'x' || char == 'X' || char == '/') {
                            if (!flagOperand) {
                                flagOperand = true;
                                flag = false;
                                if (char == '+') {
                                    operand = 1
                                } else if (char == '-' || char == '_') {
                                    operand = 2
                                } else if (char == 'x' || char == 'X') {
                                    operand = 3
                                } else {
                                    operand = 4
                                }
                            } else {
                                flagResult = false
                                break
                            }
                        } else {
                            flagResult = false
                            break
                        }
                    }

                    if (!flagResult) {
                        finalResult = getString(R.string.invalid)
                    } else {
                        val first = stringOne.toInt()
                        val second = stringTwo.toInt()
                        var result: Int = 0
                        if (operand == 1) {
                            result = first + second
                        } else if (operand == 2) {
                            result = first - second
                        } else if (operand == 3) {
                            result = first * second
                        } else {
                            if (second == 0) {
                                finalResult = getString(R.string.noZeroDivide)
                            } else {
                                result = first / second
                                finalResult = "Final Result : $result"
                            }
                        }

                        if (operand != 4) {
                            finalResult = "Final Result : $result"
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            val newInformation: Information =
                                Information(0, expressionString, finalResult)
                            repository.insert(newInformation)
                        }
                    }
                    resultView.text = finalResult
                    resultView.visibility = View.VISIBLE
                } catch (exception: Exception) {
                    resultView.text = getString(R.string.cantRecognize)
                    resultView.visibility = View.VISIBLE
                }

            }
            .addOnFailureListener {
                resultView.text = getString(R.string.cantRecognize)
                resultView.visibility = View.VISIBLE
            }

    }
}
