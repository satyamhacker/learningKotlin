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
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : ComponentActivity() {
    private lateinit var notesAdapter: NotesAdapter
    private val notesList = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val addNotesIconImageViewId = findViewById<ImageView>(R.id.addNotesIconImageViewId)
        val recyclerViewId = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerViewId)

        notesAdapter = NotesAdapter(notesList)
        recyclerViewId.layoutManager = LinearLayoutManager(this)
        recyclerViewId.adapter = notesAdapter

        addNotesIconImageViewId.setOnClickListener {
            addNotes()
        }
    }

    private fun addNotes() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Note")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val noteContent = input.text.toString()
            if (noteContent.isNotEmpty()) {
                val note = Note(noteContent)
                notesAdapter.addNote(note)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}
