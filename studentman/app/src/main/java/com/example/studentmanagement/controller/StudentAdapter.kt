package com.example.studentmanagement.controller

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.studentmanagement.R
import com.example.studentmanagement.database.StudentData
import com.example.studentmanagement.model.Student
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(private var students: MutableList<Student>): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.student_name)
        val studentID: TextView = itemView.findViewById(R.id.student_id)
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

        holder.studentName.text = student.studentName
        holder.studentID.text= student.studentId

        holder.imageEdit.setOnClickListener {
            showUpdateDialog(holder.itemView.context, position)
        }
        holder.imageRemove.setOnClickListener {
            showRemoveDialog(holder.itemView.context, position)
        }
    }

    private fun showUpdateDialog(context: Context, position: Int) {
        val student = students[position]
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_dialog_edit)

        dialog.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val buttonUpdate = dialog.findViewById<Button>(R.id.btn_update)
        val buttonCancel = dialog.findViewById<Button>(R.id.btn_cancel_update)

        val updateName = dialog.findViewById<EditText>(R.id.update_name)
        val updateID = dialog.findViewById<EditText>(R.id.update_id)

        updateName.setText(student.studentName)
        updateID.setText(student.studentId)

        buttonUpdate.setOnClickListener {
            val updatedName = updateName.text.toString()
            val updatedID = updateID.text.toString()

            if (updatedName.isNotEmpty() && updatedID.isNotEmpty()) {
                val updatedStudent = Student(updatedName, updatedID)
                students[position] = updatedStudent
                notifyItemChanged(position)

                Toast.makeText(context, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showRemoveDialog(context: Context, position: Int) {
        val student = students[position]
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_dialog_remove)

        dialog.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val buttonRemove = dialog.findViewById<Button>(R.id.btn_remove)
        val buttonCancel = dialog.findViewById<Button>(R.id.btn_cancel_remove)

        buttonRemove.setOnClickListener{
            StudentData.students.remove(student)
            notifyItemRemoved(position)

            val snackbar = Snackbar.make((context as AppCompatActivity)
                .findViewById(R.id.recycler_view_students),
                "Xóa thông tin sinh viên thành công",
                Snackbar.LENGTH_SHORT)

            snackbar.setAction("Hoàn tác") {
                StudentData.students.add(position, student)
                notifyItemInserted(position)
            }

            snackbar.show()
            dialog.dismiss()
        }

        buttonCancel.setOnClickListener{
            dialog.dismiss()
        }
    }
}