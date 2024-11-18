package com.example.myapplication

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(private val students: MutableList<StudentModel>) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
        val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
        val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
        val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun getItemCount(): Int = students.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]

        holder.textStudentName.text = student.studentName
        holder.textStudentId.text = student.studentId

        // Edit button functionality
        holder.imageEdit.setOnClickListener {
            showEditStudentDialog(holder.itemView, position)
        }

        // Delete button functionality
        holder.imageRemove.setOnClickListener {
            deleteStudent(holder.itemView, position)
        }
    }

    private fun showEditStudentDialog(view: View, position: Int) {
        val student = students[position]
        val dialogView = LayoutInflater.from(view.context).inflate(R.layout.dialog_add_edit_student, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextStudentName)
        val editTextId = dialogView.findViewById<EditText>(R.id.editTextStudentId)

        editTextName.setText(student.studentName)
        editTextId.setText(student.studentId)

        AlertDialog.Builder(view.context)
            .setTitle("Edit Student")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                student.studentName = editTextName.text.toString()
                student.studentId = editTextId.text.toString()
                notifyItemChanged(position)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteStudent(view: View, position: Int) {
        val student = students[position]
        students.removeAt(position)
        notifyItemRemoved(position)

        Snackbar.make(view, "Student deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                students.add(position, student)
                notifyItemInserted(position)
            }
            .show()
    }
}
