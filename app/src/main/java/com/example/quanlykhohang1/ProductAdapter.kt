package com.example.quanlykhohang1
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private var products: List<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvItemName)
        val tvQuantity: TextView = view.findViewById(R.id.tvItemQuantity)
        val tvPrice: TextView = view.findViewById(R.id.tvItemPrice)
        val tvCategory: TextView = view.findViewById(R.id.tvItemCategory)
        val tvLowStock: TextView = view.findViewById(R.id.tvItemLowStock)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvName.text = product.name
        holder.tvPrice.text = "Giá bán: ${FormatUtils.formatVnd(product.price)}"
        holder.tvCategory.text = product.category
        holder.tvQuantity.text = "SL: ${product.quantity}"

        if (product.quantity < 5) {
            holder.tvQuantity.setTextColor(Color.parseColor("#D32F2F"))
            holder.tvLowStock.visibility = View.VISIBLE
        } else {
            holder.tvQuantity.setTextColor(Color.parseColor("#212121"))
            holder.tvLowStock.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newList: List<Product>) {
        products = newList
        notifyDataSetChanged()
    }
}