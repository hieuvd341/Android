package com.example.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.fragment.R
import com.example.fragment.StudentModel

class StudentAdapter(
    private val context: Context,
    private val students: List<StudentModel>,
    private val onEditClick: (StudentModel) -> Unit,
    private val onRemoveClick: (StudentModel) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int {
        return students.size
    }

    override fun getItem(position: Int): Any {
        return students[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.layout_student_item, parent, false)

            holder = ViewHolder()
            holder.nameTextView = view.findViewById(R.id.text_student_name)
            holder.idTextView = view.findViewById(R.id.text_student_id)
            holder.removeImageView = view.findViewById(R.id.image_remove)
            holder.editImageView = view.findViewById(R.id.image_edit)

            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val student = students[position]

        // Set dữ liệu cho các phần tử trong layout
        holder.nameTextView.text = student.studentName
        holder.idTextView.text = student.studentId

        // Xử lý sự kiện khi nhấn nút xóa
        holder.removeImageView.setOnClickListener {
            onRemoveClick(student)
        }

        // Xử lý sự kiện khi nhấn nút chỉnh sửa
        holder.editImageView.setOnClickListener {
            onEditClick(student)
        }

        return view
    }

    // Lớp ViewHolder để tối ưu hóa việc truy cập các thành phần UI
    private class ViewHolder {
        lateinit var nameTextView: TextView
        lateinit var idTextView: TextView
        lateinit var removeImageView: ImageView
        lateinit var editImageView: ImageView
    }
}