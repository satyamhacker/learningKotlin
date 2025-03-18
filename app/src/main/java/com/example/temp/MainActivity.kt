package com.example.temp

import android.os.Bundle
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
        addNotesIconImageViewId.setOnClickListener {
            addNotes()
        }


    }

    fun addNotes(){
        Toast.makeText(this, "Add Notes", Toast.LENGTH_SHORT).show()
    }
}
