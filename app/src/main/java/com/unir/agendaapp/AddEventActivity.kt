package com.unir.agendaapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class AddEventActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var eventTypeSpinner: Spinner
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private lateinit var allDayCheckBox: CheckBox
    private lateinit var descriptionInput: EditText
    private lateinit var confirmButton: Button
    private lateinit var cancelButton: Button

    private val calendar: Calendar = Calendar.getInstance()
    private var startDateSet: Boolean = false
    private var endDateSet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_add_event)

        titleInput = findViewById(R.id.titleInput)
        eventTypeSpinner = findViewById(R.id.eventTypeSpinner)
        startDateButton = findViewById(R.id.startDateButton)
        endDateButton = findViewById(R.id.endDateButton)
        allDayCheckBox = findViewById(R.id.allDayCheckBox)
        descriptionInput = findViewById(R.id.descriptionInput)
        confirmButton = findViewById(R.id.confirmButton)
        cancelButton = findViewById(R.id.cancelButton)

        // Configuración del Spinner
        val eventTypes = arrayOf("Cita", "Aniversario", "Cuenta atrás")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, eventTypes)
        eventTypeSpinner.adapter = adapter

        // Eventos de los botones de fecha
        startDateButton.setOnClickListener { showDateTimePicker(true) }
        endDateButton.setOnClickListener { showDateTimePicker(false) }

        // Botón Confirmar
        confirmButton.setOnClickListener { confirmEvent() }

        // Botón Cancelar
        cancelButton.setOnClickListener {
            finish() // Vuelve a la pantalla de inicio
        }
    }

    private fun showDateTimePicker(isStartDate: Boolean) {
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
                calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                if (isStartDate) {
                    startDateSet = true
                    startDateButton.text = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(calendar.time)
                } else {
                    endDateSet = true
                    endDateButton.text = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(calendar.time)
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private fun confirmEvent() {
        val title = titleInput.text.toString().trim()
        val eventType = eventTypeSpinner.selectedItem.toString()
        val description = descriptionInput.text.toString().trim()

        // Validaciones
        if (title.isEmpty()) {
            titleInput.error = "El título no puede estar vacío"
            Toast.makeText(
                this, "El título no puede estar vacío",Toast.LENGTH_SHORT).show()
            return
        }
        if (eventType.isEmpty()) {
            Toast.makeText(this, "El tipo de evento no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }
        if (!startDateSet) {
            Toast.makeText(this, "La fecha de inicio no puede estar vacía", Toast.LENGTH_SHORT).show()
            return
        }
        if (!allDayCheckBox.isChecked && !endDateSet) {
            Toast.makeText(this, "La fecha de fin no puede estar vacía", Toast.LENGTH_SHORT).show()
            return
        }

        // Muestra el resumen de los datos
        val summaryIntent = Intent(this, EventSummaryActivity::class.java).apply {
            putExtra("title", title)
            putExtra("eventType", eventType)
            putExtra("startDate", startDateButton.text.toString())
            putExtra("endDate", if (allDayCheckBox.isChecked) "Todo el día" else endDateButton.text.toString())
            putExtra("description", description)
        }
        startActivity(summaryIntent)
        finish()
    }
}
