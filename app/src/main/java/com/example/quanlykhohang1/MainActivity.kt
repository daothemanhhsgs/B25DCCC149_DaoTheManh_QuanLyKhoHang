package com.example.quanlykhohang1
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: ProductAdapter

    private lateinit var tvTotalProducts: TextView
    private lateinit var tvTotalValue: TextView
    private lateinit var tvLowStock: TextView
    private lateinit var tvEmpty: TextView
    private lateinit var rvProducts: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        tvTotalProducts = findViewById(R.id.tvTotalProducts)
        tvTotalValue = findViewById(R.id.tvTotalValue)
        tvLowStock = findViewById(R.id.tvLowStock)
        tvEmpty = findViewById(R.id.tvEmpty)
        rvProducts = findViewById(R.id.rvProducts)
        val btnAddProduct = findViewById<Button>(R.id.btnAddProduct)

        adapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            startActivity(intent)
        }

        rvProducts.layoutManager = LinearLayoutManager(this)
        rvProducts.adapter = adapter

        btnAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val productList = dbHelper.getAllProducts()
        adapter.updateData(productList)

        if (productList.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            rvProducts.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            rvProducts.visibility = View.VISIBLE
        }

        tvTotalProducts.text = productList.size.toString()
        tvTotalValue.text = FormatUtils.formatVnd(dbHelper.getTotalValue())
        tvLowStock.text = dbHelper.countLowStock().toString()
    }
}