package com.example.practica1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)
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