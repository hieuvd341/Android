package com.example.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.fragment.R

class AddStudentFragment : Fragment(R.layout.fragment_add_student) {

    private lateinit var nameEditText: EditText
    private lateinit var idEditText: EditText
    private lateinit var addButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements
        nameEditText = view.findViewById(R.id.edit_student_name)
        idEditText = view.findViewById(R.id.edit_student_id)
        addButton = view.findViewById(R.id.btn_add_student)

        // Thêm sinh viên
        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val id = idEditText.text.toString()
            if (name.isNotBlank() && id.isNotBlank()) {
                // Thêm sinh viên vào danh sách trong MainActivity
                val newStudent = StudentModel(name, id)
                (activity as MainActivity).addStudent(newStudent)
                fragmentManager?.popBackStack()  // Quay lại màn hình trước
            }
        }
    }
}