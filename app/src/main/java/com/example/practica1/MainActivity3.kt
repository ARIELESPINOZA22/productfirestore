package com.example.practica1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import android.content.Intent
import android.view.View

class MainActivity3 : AppCompatActivity() {

    private lateinit var editTextStartDate: EditText
    private lateinit var editTextEndDate: EditText
    private lateinit var btnFetch: Button
    private lateinit var listViewProducts: ListView

    private val db = FirebaseFirestore.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        editTextStartDate = findViewById(R.id.editTextStartDate)
        editTextEndDate = findViewById(R.id.editTextEndDate)
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

            if (startDate != null && endDate != null) {
                fetchProducts(startDate, endDate)
            } else {
                Toast.makeText(this, "Selecciona un rango de fechas válido", Toast.LENGTH_SHORT).show()
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

    private fun fetchProducts(startDate: Date, endDate: Date) {
        val startTimestamp = Timestamp(startDate)
        val endTimestamp = Timestamp(endDate)

        db.collection("products")
            .whereGreaterThanOrEqualTo("timestamp", startTimestamp)
            .whereLessThanOrEqualTo("timestamp", endTimestamp)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val productsList = ArrayList<String>()
                for (document in documents) {
                    val name = document.getString("name") ?: "Sin nombre"
                    val brand = document.getString("brand") ?: "Sin marca"
                    val model = document.getString("model") ?: "Sin modelo"
                    val note = document.getString("note") ?: "Sin nota"
                    val timestamp = document.getTimestamp("timestamp")?.toDate()

                    val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(timestamp)
                    val productInfo = "$name - $brand - $model - $note - $formattedDate"
                    productsList.add(productInfo)
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, productsList)
                listViewProducts.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al recuperar los productos", Toast.LENGTH_SHORT).show()
            }
    }

    fun menu3(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }
}