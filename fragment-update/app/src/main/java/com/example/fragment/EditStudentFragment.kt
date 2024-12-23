package com.example.fragment

import com.example.fragment.MainActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.fragment.R
import com.example.fragment.StudentModel

class EditStudentFragment : Fragment(R.layout.fragment_edit_student) {

    private lateinit var nameEditText: EditText
    private lateinit var idEditText: EditText
    private lateinit var updateButton: Button

    companion object {
        private const val ARG_STUDENT = "student"

        fun newInstance(student: StudentModel): EditStudentFragment {
            val fragment = EditStudentFragment()
            val args = Bundle()
            args.putSerializable(ARG_STUDENT, student) // Truyền đối tượng sinh viên
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements
        nameEditText = view.findViewById(R.id.edit_student_name)
        idEditText = view.findViewById(R.id.edit_student_id)
        updateButton = view.findViewById(R.id.btn_update_student)

        // Retrieve student data from arguments
        val student = arguments?.getSerializable(ARG_STUDENT) as StudentModel
        nameEditText.setText(student.studentName)
        idEditText.setText(student.studentId)

        // Update student information
        updateButton.setOnClickListener {
            student.studentName = nameEditText.text.toString()
            student.studentId = idEditText.text.toString()

            // Notify MainActivity to update the student list
            (activity as MainActivity).studentAdapter.notifyDataSetChanged()

            // Pop the fragment from back stack to return to the previous screen
            fragmentManager?.popBackStack()
        }
    }
}

private fun Any.notifyDataSetChanged() {
    TODO("Not yet implemented")
}
