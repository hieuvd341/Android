package com.example.a2912

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val studentViewModel: StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etName: EditText = findViewById(R.id.etName)
        val btnAdd: Button = findViewById(R.id.btnAdd)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val adapter = StudentAdapter { student -> studentViewModel.delete(student) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        studentViewModel.allStudents.observe(this, Observer { students ->
            students?.let { adapter.submitList(it) }
        })

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            if (name.isNotEmpty()) {
                val student = Student(name = name)
                studentViewModel.insert(student)
                etName.text.clear()
            }
        }
    }
}
