package com.example.quanlykhohang1
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var productId: Int = -1
    private var currentProduct: Product? = null

    private lateinit var tvName: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvQuantity: TextView
    private lateinit var tvLowStock: TextView
    private lateinit var tvStockValue: TextView
    private lateinit var tvDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        dbHelper = DatabaseHelper(this)
        productId = intent.getIntExtra("PRODUCT_ID", -1)

        if (productId == -1) {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tvName = findViewById(R.id.tvDetailName)
        tvCategory = findViewById(R.id.tvDetailCategory)
        tvStatus = findViewById(R.id.tvDetailStatus)
        tvPrice = findViewById(R.id.tvDetailPrice)
        tvQuantity = findViewById(R.id.tvDetailQuantity)
        tvLowStock = findViewById(R.id.tvDetailLowStock)
        tvStockValue = findViewById(R.id.tvDetailStockValue)
        tvDescription = findViewById(R.id.tvDetailDescription)

        val btnHeaderBack = findViewById<ImageButton>(R.id.btnHeaderBack)
        val btnImport = findViewById<Button>(R.id.btnImport)
        val btnSell = findViewById<Button>(R.id.btnSell)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        loadProductDetail()

        btnImport.setOnClickListener {
            currentProduct?.let { prod ->
                val newQty = prod.quantity + 1
                dbHelper.updateQuantity(prod.id, newQty)
                loadProductDetail()
            }
        }

        btnSell.setOnClickListener {
            currentProduct?.let { prod ->
                if (prod.quantity <= 0) {
                    Toast.makeText(this, "Hết hàng", Toast.LENGTH_SHORT).show()
                } else {
                    val newQty = prod.quantity - 1
                    dbHelper.updateQuantity(prod.id, newQty)
                    loadProductDetail()
                }
            }
        }

        btnHeaderBack.setOnClickListener { finish() }
        btnBack.setOnClickListener { finish() }

        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa sản phẩm này khỏi kho?")
                .setPositiveButton("Xóa") { _, _ ->
                    dbHelper.deleteProduct(productId)
                    Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("Hủy", null)
                .show()
        }
    }

    private fun loadProductDetail() {
        val product = dbHelper.getProductById(productId)
        if (product == null) {
            Toast.makeText(this, "Sản phẩm không còn tồn tại", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        currentProduct = product

        tvName.text = product.name
        tvCategory.text = product.category
        tvStatus.text = if (product.active == 1) "Đang kinh doanh" else "Ngừng kinh doanh"
        tvPrice.text = FormatUtils.formatVnd(product.price)
        tvQuantity.text = product.quantity.toString()
        tvLowStock.visibility = if (product.quantity < 5) View.VISIBLE else View.GONE

        val stockValue = product.price * product.quantity
        tvStockValue.text = FormatUtils.formatVnd(stockValue)
        tvDescription.text = product.description.ifEmpty { "Chưa có mô tả" }
    }
}