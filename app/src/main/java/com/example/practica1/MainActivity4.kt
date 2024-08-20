package com.example.practica1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity4 : AppCompatActivity() {

    private lateinit var editTextSearch: EditText
    private lateinit var buttonSearch: Button
    private lateinit var listViewResults: ListView



    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        editTextSearch = findViewById(R.id.editTextSearch)
        buttonSearch = findViewById(R.id.buttonSearch)
        listViewResults = findViewById(R.id.listViewResults)

        buttonSearch.setOnClickListener {
            val searchText = editTextSearch.text.toString().trim()

            if (searchText.isNotEmpty()) {
                searchInFirestore(searchText)
            } else {
                Toast.makeText(this, "Por favor, ingrese un término de búsqueda", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun searchInFirestore(queryText: String) {
        val resultsList = ArrayList<String>()
        val collections = db.collection("products")

        // Buscar por nombre
        collections.whereEqualTo("name", queryText).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val resultInfo = extractProductInfo(document)
                    if (!resultsList.contains(resultInfo)) {
                        resultsList.add(resultInfo)
                    }
                }
                // Buscar por marca
                collections.whereEqualTo("brand", queryText).get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val resultInfo = extractProductInfo(document)
                            if (!resultsList.contains(resultInfo)) {
                                resultsList.add(resultInfo)
                            }
                        }
                        // Buscar por modelo
                        collections.whereEqualTo("model", queryText).get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    val resultInfo = extractProductInfo(document)
                                    if (!resultsList.contains(resultInfo)) {
                                        resultsList.add(resultInfo)
                                    }
                                }
                                // Buscar por categoría
                                collections.whereEqualTo("category", queryText).get()
                                    .addOnSuccessListener { documents ->
                                        for (document in documents) {
                                            val resultInfo = extractProductInfo(document)
                                            if (!resultsList.contains(resultInfo)) {
                                                resultsList.add(resultInfo)
                                            }
                                        }
                                        // Buscar por nota
                                        collections.whereEqualTo("note", queryText).get()
                                            .addOnSuccessListener { documents ->
                                                for (document in documents) {
                                                    val resultInfo = extractProductInfo(document)
                                                    if (!resultsList.contains(resultInfo)) {
                                                        resultsList.add(resultInfo)
                                                    }
                                                }
                                                // Mostrar resultados
                                                if (resultsList.isEmpty()) {
                                                    Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resultsList)
                                                    listViewResults.adapter = adapter
                                                }
                                            }
                                    }
                            }
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al buscar los datos: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun extractProductInfo(document: DocumentSnapshot): String {
        val name = document.getString("name") ?: "Sin nombre"
        val brand = document.getString("brand") ?: "Sin marca"
        val model = document.getString("model") ?: "Sin modelo"
        val note = document.getString("note") ?: "Sin nota"
        val category = document.getString("category") ?: "Sin categoría"

        return "Nombre: $name\nMarca: $brand\nModelo: $model\nNota: $note\nCategoría: $category"
    }


}
