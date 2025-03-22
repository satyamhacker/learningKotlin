package com.example.temp

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var notesAdapter: NotesAdapter
    private val notesList = mutableListOf<Note>()
    private lateinit var noteDatabase: NoteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        noteDatabase = NoteDatabase.getDatabase(this)

        val addNotesIconImageViewId = findViewById<ImageView>(R.id.addNotesIconImageViewId)
        val recyclerViewId = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerViewId)

        notesAdapter = NotesAdapter(notesList)
        recyclerViewId.layoutManager = LinearLayoutManager(this)
        recyclerViewId.adapter = notesAdapter

        addNotesIconImageViewId.setOnClickListener {
            addNotes()
        }

        loadNotes()
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
                val note = Note(content = noteContent)
                lifecycleScope.launch {
                    noteDatabase.noteDao().insert(note)
                    notesAdapter.addNote(note)
                }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun loadNotes() {
        lifecycleScope.launch {
            val notes = noteDatabase.noteDao().getAllNotes()
            notesList.addAll(notes)
            notesAdapter.notifyDataSetChanged()
        }
    }
}