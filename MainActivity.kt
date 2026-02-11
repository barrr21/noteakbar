package com.example.noteakbar

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "notes_db"
        ).build()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)
        btnSave = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().insert(
                    Note(
                        title = etTitle.text.toString(),
                        content = etContent.text.toString()
                    )
                )
                loadData()
            }
        }

        loadData()
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getAll()
            runOnUiThread {
                recyclerView.adapter = NoteAdapter(notes) {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.noteDao().delete(it)
                        loadData()
                    }
                }
            }
        }
    }
}
