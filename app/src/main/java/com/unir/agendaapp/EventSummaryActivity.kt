package com.unir.agendaapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

private const val ARG_TITLE = "title"
private const val ARG_TYPE = "eventType"
private const val ARG_START= "startDate"
private const val ARG_END = "endDate"
private const val ARG_DESC = "description"

class EventSummaryActivity : AppCompatActivity() {
    private var jsonEvent: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_summary)

        //Genera el json con los datos del evento
        jsonEvent = """
            {
            "title": "${intent.getStringExtra(ARG_TITLE)}",
            "eventType": "${intent.getStringExtra(ARG_TYPE)}",
            "startDate": "${intent.getStringExtra(ARG_START)}",
            "endDate": "${intent.getStringExtra(ARG_END)}",
            "description": "${intent.getStringExtra(ARG_DESC)}"
            }
            """.trimIndent()

        //Muestra el json en el textview
        findViewById<TextView>(R.id.summaryTextView).text = jsonEvent

        //Fucncionalidad para finalizar la actividad (Regresa a la actividad principal)
        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish()
        }

        //Funcionalidad para copiar el json
        findViewById<TextView>(R.id.summaryTextView).setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", jsonEvent)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(getApplicationContext(), "Texto copiado al portapapeles", Toast.LENGTH_SHORT).show()
        }
    }
}
