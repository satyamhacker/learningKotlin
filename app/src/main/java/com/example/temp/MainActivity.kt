package com.example.temp

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val addNotesIconImageViewId = findViewById<ImageView>(R.id.addNotesIconImageViewId)
        val recyclerViewId = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerViewId)

        addNotesIconImageViewId.setOnClickListener {
            addNotes()
        }


    }

    fun addNotes(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Note")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val note = input.text.toString()
            Toast.makeText(applicationContext, note, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}
