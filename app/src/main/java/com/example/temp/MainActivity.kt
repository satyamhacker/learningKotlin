package com.example.temp

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
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
        val searchViewId = findViewById<SearchView>(R.id.searchViewId)

        // Initialize adapter with an empty list initially
        notesAdapter = NotesAdapter(mutableListOf())
        recyclerViewId.layoutManager = LinearLayoutManager(this)
        recyclerViewId.adapter = notesAdapter

        // Load notes and update UI after data is ready
        loadNotes()

        // Set up SearchView listener
        searchViewId.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = mutableListOf<Note>()
                if (newText.isNullOrEmpty()) {
                    filteredList.addAll(notesList)
                    Log.d("MainActivity", "SearchView is empty, filteredList: $filteredList")
                    Log.d("MainActivity", "notesList content: $notesList")
                } else {
                    for (note in notesList) {
                        if (note.content.contains(newText, ignoreCase = true)) {
                            filteredList.add(note)
                        }
                    }
                    Log.d("MainActivity", "Filtered for '$newText': $filteredList")
                }
                notesAdapter.updateNotes(filteredList)
                return true
            }
        })

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
                val note = Note(content = noteContent)
                lifecycleScope.launch {
                    noteDatabase.noteDao().insert(note)
                    notesList.add(note) // Update notesList
                    notesAdapter.updateNotes(notesList) // Update adapter with full list
                    Log.d("MainActivity", "Note added, notesList: $notesList")
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
            notesList.clear() // Clear old data
            notesList.addAll(notes) // Add new data
            Log.d("MainActivity", "Loaded notesList: $notesList")
            notesAdapter.updateNotes(notesList) // Update adapter with full list
        }
    }
}