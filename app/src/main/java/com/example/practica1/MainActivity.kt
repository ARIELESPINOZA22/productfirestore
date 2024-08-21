package com.example.practica1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

     /* esto es para el boton registrar ususario antes poner visible xml
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            // Validar si los campos no están vacíos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
            createUser(email, password)
            }
        }*/

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            // Validar si los campos no están vacíos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
            signIn(email, password)
            }
        }

        logoutButton.setOnClickListener {
            signOut()
        }
    }

    /* esto es para el boton registrar ususario antes poner visible xml
    private fun createUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Registro fallido", Toast.LENGTH_SHORT).show()
                }
            }
    }*/


    private fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    // Navegar a al menu principal
                    val intent = Intent(this,MainActivity5::class.java)
                    startActivity(intent)
                    finish() // Opcional: finalizar MainActivity si ya no la necesitas
                } else {
                    Toast.makeText(this, "Inicio de sesión fallido. Registrando nuevo usuario...", Toast.LENGTH_SHORT).show()
                    // Si el inicio de sesión falla, se intentará registrar al usuario
                    //createUser(email, password)
                }
            }
    }

    private fun signOut() {
        mAuth.signOut()
        findViewById<Button>(R.id.logoutButton).visibility = Button.GONE
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }
}




