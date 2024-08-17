package com.example.practica1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun registrosprod(view: View) {
        startActivity(Intent(this,MainActivity2::class.java))
    }

    fun searchprod(view: View) {
        startActivity(Intent(this,MainActivity3::class.java))
    }

    fun searchproddate(view: View) {
        startActivity(Intent(this,MainActivity4::class.java))
    }
}
