package com.example.temp

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private val notes: MutableList<Note>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTextView: TextView = itemView.findViewById(R.id.noteTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.noteTextView.text = notes[position].content
        holder.itemView.setOnLongClickListener { view ->
            val options = arrayOf<String>("Edit Note", "Delete Note")
            val builder = AlertDialog.Builder(view.context).apply {
                setTitle("Select Option")
                setItems(options) { dialog, item ->
                    when (options[item]) {
                        "Edit Note" -> {
                            val builder = AlertDialog.Builder(view.context)
                            val input = EditText(view.context)
                            input.setText(notes[position].content)

                            builder.setView(input)
                            builder.setTitle("Edit Note")
                            builder.setPositiveButton("OK") { dialog, which ->
                                val editedText = input.text.toString()
                                if (editedText.isNotBlank()) {
                                    notes[position].content = editedText
                                    notifyItemChanged(position)
                                    Toast.makeText(view.context, "Note Edited", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(view.context, "Note can't be empty", Toast.LENGTH_SHORT).show()
                                }
                            }
                            builder.setNegativeButton("Cancel") { dialog, which ->
                                dialog.cancel()
                            }

                            val alertDialog = builder.create()
                            alertDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                            alertDialog.show()
                        }
                        "Delete Note" -> {
                            // Remove the item from the list
                            notes.removeAt(position)

                            // Notify the adapter that the item has been removed
                            notifyItemRemoved(position)
                            // Optionally, show a Toast message to indicate that the note has been deleted
                            Toast.makeText(view.context, "Note deleted", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
             builder.show()

            true
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun addNote(note: Note) {
        notes.add(note)
        notifyItemInserted(notes.size - 1)
    }
}