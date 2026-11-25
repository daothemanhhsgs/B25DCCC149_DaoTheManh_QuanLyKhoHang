package com.example.quanlykhohang1
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class AddProductActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        dbHelper = DatabaseHelper(this)

        val btnHeaderBack = findViewById<ImageButton>(R.id.btnHeaderBack)
        val etName = findViewById<EditText>(R.id.etName)
        val etPrice = findViewById<EditText>(R.id.etPrice)
        val etQuantity = findViewById<EditText>(R.id.etQuantity)
        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)
        val switchActive = findViewById<SwitchCompat>(R.id.switchActive)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val btnSaveProduct = findViewById<Button>(R.id.btnSaveProduct)

        val categories = arrayOf("Điện tử", "Thực phẩm", "Văn phòng phẩm", "Gia dụng", "Khác")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerCategory.adapter = spinnerAdapter

        btnHeaderBack.setOnClickListener { finish() }

        btnSaveProduct.setOnClickListener {
            val name = etName.text.toString().trim()
            val priceStr = etPrice.text.toString().trim()
            val qtyStr = etQuantity.text.toString().trim()
            val category = spinnerCategory.selectedItem.toString()
            val active = if (switchActive.isChecked) 1 else 0
            val description = etDescription.text.toString().trim()

            if (name.isEmpty()) {
                etName.error = "Tên sản phẩm không được để trống"
                etName.requestFocus()
                return@setOnClickListener
            }

            val price = priceStr.toLongOrNull()
            if (price == null || price <= 0) {
                etPrice.error = "Giá bán phải là số lớn hơn 0"
                etPrice.requestFocus()
                return@setOnClickListener
            }

            val quantity = qtyStr.toIntOrNull()
            if (quantity == null || quantity < 0) {
                etQuantity.error = "Số lượng phải là số nguyên không âm"
                etQuantity.requestFocus()
                return@setOnClickListener
            }

            val newProduct = Product(
                name = name,
                price = price,
                quantity = quantity,
                category = category,
                active = active,
                description = description
            )

            val insertedId = dbHelper.insertProduct(newProduct)
            if (insertedId != -1L) {
                Toast.makeText(this, "Đã thêm sản phẩm thành công", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Lỗi khi lưu sản phẩm", Toast.LENGTH_SHORT).show()
            }
        }
    }
}