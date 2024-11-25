package com.example.optionmenu_contextmenu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import android.content.Intent
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var lv: ListView
    private val REQUEST_CODE_ADD = 100
    private val REQUEST_CODE_EDIT = 101

    private val name = listOf(
        "Nguyễn Anh Tuấn",
        "Lê Thị Hoa",
        "Trần Văn Hải",
        "Phạm Minh Phương",
        "Vũ Thùy Linh",
        "Hoàng Đức Dũng",
        "Đặng Thu Hà",
        "Bùi Quang Huy",
        "Đinh Ngọc Trâm",
        "Trịnh Công Danh",
        "Nguyễn Thị Hồng",
        "Lê Thanh Sơn",
        "Phạm Hồng Ngọc",
        "Vũ Đức Minh",
        "Hoàng Thị Mai",
        "Trần Thanh Tùng",
        "Nguyễn Văn Khánh",
        "Đặng Minh Trí",
        "Bùi Thị Hương",
        "Đinh Văn Nam"
    )

    private val mssv = listOf(
        "SV1001",
        "SV1002",
        "SV1003",
        "SV1004",
        "SV1005",
        "SV1006",
        "SV1007",
        "SV1008",
        "SV1009",
        "SV1010",
        "SV1011",
        "SV1012",
        "SV1013",
        "SV1014",
        "SV1015",
        "SV1016",
        "SV1017",
        "SV1018",
        "SV1019",
        "SV1020"
    )
    private val myList: ArrayList<Student> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        lv = findViewById(R.id.lv)

        for (i in name.indices) {
            myList.add(Student( name[i], mssv[i] ))
        }

        val adapter = StudentAdapter(this, R.layout.layout_item, myList)
        lv.adapter = adapter

        registerForContextMenu(lv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                val intent = Intent(this, AddStudentActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_ADD)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            val name = data?.getStringExtra("name")
            val mssv = data?.getStringExtra("mssv")

            if (name != null && mssv != null) {
                myList.add(Student(name, mssv))
                (lv.adapter as StudentAdapter).notifyDataSetChanged()
            }
        }

        // Xử lý dữ liệu trả về từ EditStudentActivity
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            val updatedName = data?.getStringExtra("name")
            val updatedMssv = data?.getStringExtra("mssv")
            val position = data?.getIntExtra("position", -1)

            if (updatedName != null && updatedMssv != null && position != null && position >= 0) {
                // Cập nhật đối tượng trong myList tại vị trí đã chỉnh sửa
                val student = myList[position]
                student.name = updatedName
                student.mssv = updatedMssv

                // Thông báo cập nhật cho Adapter
                (lv.adapter as StudentAdapter).notifyDataSetChanged()
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position

        return when (item.itemId) {
            R.id.menu_edit -> {
                val student = myList[position]
                val intent = Intent(this, EditStudentActivity::class.java)
                intent.putExtra("name", student.name)
                intent.putExtra("mssv", student.mssv)
                intent.putExtra("position", position)
                startActivityForResult(intent, REQUEST_CODE_EDIT)
                true
            }
            R.id.menu_remove -> {
                myList.removeAt(position)
                (lv.adapter as StudentAdapter).notifyDataSetChanged()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

}