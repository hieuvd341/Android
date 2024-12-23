package com.example.fragment

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fragment.R
import com.example.fragment.AddStudentFragment
import com.example.fragment.EditStudentFragment
import com.example.fragment.StudentModel

class MainActivity : AppCompatActivity() {

    // Danh sách sinh viên
    val students = mutableListOf<StudentModel>()

    // Khởi tạo Adapter cho ListView
    lateinit var studentAdapter: StudentAdapter

    // Database Helper
    lateinit var dbHelper: StudentDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo Database Helper
        dbHelper = StudentDatabaseHelper(this)

        // Đọc dữ liệu từ SQLite
        loadStudentsFromDatabase()

        // Khởi tạo Adapter và truyền callback xử lý
        studentAdapter = StudentAdapter(
            this,
            students,
            onEditClick = { student ->
                // Mở Fragment chỉnh sửa sinh viên
                openEditStudentFragment(student)
            },
            onRemoveClick = { student ->
                // Xóa sinh viên khỏi danh sách và SQLite
                removeStudentFromDatabase(student)
                students.remove(student)
                studentAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Removed ${student.studentName}", Toast.LENGTH_SHORT).show()
            }
        )

        val listView = findViewById<ListView>(R.id.list_view_students)
        listView.adapter = studentAdapter

        // Đăng ký context menu cho ListView
        registerForContextMenu(listView)

        // Set OnItemClickListener cho ListView
        listView.setOnItemClickListener { _, _, position, _ ->
            val student = students[position]
            openEditStudentFragment(student)
        }

        // Add "Add new" button click listener
        findViewById<Button>(R.id.btn_add_new).setOnClickListener {
            openAddStudentFragment()
        }
    }

    // Mở Fragment thêm sinh viên
    private fun openAddStudentFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, AddStudentFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    // Mở Fragment để chỉnh sửa thông tin sinh viên
    private fun openEditStudentFragment(student: StudentModel) {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = EditStudentFragment.newInstance(student)
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    // Thêm sinh viên vào danh sách và SQLite
    fun addStudent(newStudent: StudentModel) {
        students.add(newStudent)
        saveStudentToDatabase(newStudent)
        studentAdapter.notifyDataSetChanged()
    }

    // Lưu sinh viên vào SQLite
    private fun saveStudentToDatabase(student: StudentModel) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(StudentDatabaseHelper.COLUMN_NAME, student.studentName)
            put(StudentDatabaseHelper.COLUMN_ID, student.studentId)
        }
        db.insert(StudentDatabaseHelper.TABLE_NAME, null, values)
    }

    // Xóa sinh viên khỏi SQLite
    private fun removeStudentFromDatabase(student: StudentModel) {
        val db = dbHelper.writableDatabase
        db.delete(
            StudentDatabaseHelper.TABLE_NAME,
            "${StudentDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(student.studentId)
        )
    }

    // Đọc dữ liệu từ SQLite và thêm vào danh sách
    private fun loadStudentsFromDatabase() {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            StudentDatabaseHelper.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(StudentDatabaseHelper.COLUMN_NAME))
                val id = getString(getColumnIndexOrThrow(StudentDatabaseHelper.COLUMN_ID))
                students.add(StudentModel(name, id))
            }
        }
        cursor.close()
    }
}

// Database Helper
class StudentDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "students.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "students"
        const val COLUMN_ID = "studentId"
        const val COLUMN_NAME = "studentName"

        private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID TEXT PRIMARY KEY, " +
                "$COLUMN_NAME TEXT NOT NULL)"
    }

}
