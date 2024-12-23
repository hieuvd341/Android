package com.example.studentmanagement

import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentmanagement.controller.StudentAdapter
import com.example.studentmanagement.database.StudentData
import com.example.studentmanagement.model.Student

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val students = StudentData.students
        val studentAdapter = StudentAdapter(students)

        findViewById<Button>(R.id.btn_add_new).setOnClickListener{
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.layout_dialog_add)

            dialog.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val btnSave = dialog.findViewById<Button>(R.id.btn_add)
            val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)

            btnSave.setOnClickListener{
                val name = dialog.findViewById<EditText>(R.id.edit_name).text.toString()
                val id = dialog.findViewById<EditText>(R.id.edit_id).text.toString()

                students.add(Student(name, id))
                studentAdapter.notifyItemInserted(students.size - 1)
                dialog.dismiss()
            }

            btnCancel.setOnClickListener{
                dialog.dismiss()
            }
        }

        findViewById<RecyclerView>(R.id.recycler_view_students).run {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
}