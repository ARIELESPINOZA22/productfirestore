package com.example.practica1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

class MainActivity2 : AppCompatActivity() {

    private lateinit var etProductName: EditText
    private lateinit var etBrand: EditText
    private lateinit var etModel: EditText
    private lateinit var etNote: EditText
    private lateinit var btnSave: Button
    private lateinit var tvStatus: TextView
    private lateinit var spinnerCategory: Spinner

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        etProductName = findViewById(R.id.etProductName)
        etBrand = findViewById(R.id.etBrand)
        etModel = findViewById(R.id.etModel)
        etNote = findViewById(R.id.etNote)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnSave = findViewById(R.id.btnSave)
        tvStatus = findViewById(R.id.tvStatus)

        btnSave.setOnClickListener {
            val productName = etProductName.text.toString()
            val brand = etBrand.text.toString()
            val model = etModel.text.toString()
            val note = etNote.text.toString()
            val category = spinnerCategory.selectedItem.toString()

            if (productName.isEmpty() || brand.isEmpty() || model.isEmpty() || note.isEmpty()) {
                // Mostrar mensaje si algún campo está vacío
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Obtener el correo electrónico del usuario autenticado
                val currentUser = auth.currentUser
                val userEmail = currentUser?.email

                if (userEmail != null) {
                // Crear el registro si todos los campos están completos
                 val product = hashMapOf(
                "name" to etProductName.text.toString(),
                "brand" to etBrand.text.toString(),
                "model" to etModel.text.toString(),
                "note" to etNote.text.toString(),
                "category" to category, // Guardar la categoría seleccionada
                "timestamp" to Timestamp.now(), // Agregar la fecha y hora actual
                     "userEmail" to userEmail // Agregar el correo electrónico del usuario
            )

            db.collection("products")
                .add(product)
                .addOnSuccessListener {
                    tvStatus.text = "Producto guardado con éxito"

                    // Limpiar los campos de EditText
                    etProductName.text.clear()
                    etBrand.text.clear()
                    etModel.text.clear()
                    etNote.text.clear()
                    spinnerCategory.setSelection(0) // Seleccionar la primera opción ("Producto")

                }
                .addOnFailureListener {
                    tvStatus.text = "Error al guardar el producto"
                }
                } else {
                    Toast.makeText(this, "No se pudo obtener el correo electrónico del usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}