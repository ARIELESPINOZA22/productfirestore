package com.example.practica1


import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*
import android.widget.ListView

import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.print.pdf.PrintedPdfDocument
import android.os.ParcelFileDescriptor
import java.io.FileOutputStream
import java.io.IOException

class MainActivity3 : AppCompatActivity() {

    private lateinit var editTextStartDate: EditText
    private lateinit var editTextEndDate: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnFetch: Button
    private lateinit var listViewProducts: ListView

    private val db = FirebaseFirestore.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        val listViewData: ListView = findViewById(R.id.listViewProducts)

        editTextStartDate = findViewById(R.id.editTextStartDate)
        editTextEndDate = findViewById(R.id.editTextEndDate)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnFetch = findViewById(R.id.btnFetch)
        listViewProducts = findViewById(R.id.listViewProducts)

        // Configurar los EditText para mostrar DatePickerDialog
        editTextStartDate.setOnClickListener {
            showDatePickerDialog { date ->
                editTextStartDate.setText(dateFormat.format(date))
            }
        }

        editTextEndDate.setOnClickListener {
            showDatePickerDialog { date ->
                editTextEndDate.setText(dateFormat.format(date))
            }
        }

        btnFetch.setOnClickListener {
            val startDate = dateFormat.parse(editTextStartDate.text.toString())
            val endDate = dateFormat.parse(editTextEndDate.text.toString())
            val category = spinnerCategory.selectedItem.toString()

            if (startDate != null && endDate != null) {
                fetchProducts(startDate, endDate, category)
            } else {
                Toast.makeText(this, "Selecciona un rango de fechas válido", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun showDatePickerDialog(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                onDateSelected(selectedCalendar.time)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun fetchProducts(startDate: Date, endDate: Date, category: String) {
        val startTimestamp = Timestamp(startDate)
        val endTimestamp = Timestamp(endDate)

        db.collection("products")
            .whereEqualTo("category", category)
            .whereGreaterThanOrEqualTo("timestamp", startTimestamp)
            .whereLessThanOrEqualTo("timestamp", endTimestamp)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(
                        this,
                        "No se encontraron productos para la categoría seleccionada",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val productsList = ArrayList<String>()
                    for (document in documents) {
                        val name = document.getString("name") ?: "Sin nombre"
                        val brand = document.getString("brand") ?: "Sin marca"
                        val model = document.getString("model") ?: "Sin modelo"
                        val note = document.getString("note") ?: "Sin nota"
                        val timestamp = document.getTimestamp("timestamp")?.toDate()

                        val formattedDate =
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                                timestamp
                            )
                        val productInfo = "Nombre: $name\n" +
                                "Marca: $brand\n" +
                                "Modelo: $model\n" +
                                "Nota: $note\n" +
                                "Categoría: $category\n" +
                                "FechaRegistro: $formattedDate"
                        productsList.add(productInfo)
                    }

                    val adapter =
                        ArrayAdapter(this, android.R.layout.simple_list_item_1, productsList)
                    listViewProducts.adapter = adapter
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Error al recuperar los productos: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

}
