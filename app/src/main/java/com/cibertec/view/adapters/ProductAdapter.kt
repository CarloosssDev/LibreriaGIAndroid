package com.cibertec.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.R
import com.cibertec.model.Category
import com.cibertec.model.Product

class ProductAdapter(
    private var products: List<Product>,
    private var categories: List<Category>,
    private val onDeleteClick: (Product) -> Unit,
    private val onEditClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtProductName: TextView = itemView.findViewById(R.id.txtProductName)
        val txtProductDesc: TextView = itemView.findViewById(R.id.txtProductDesc)
        val txtProductPrice: TextView = itemView.findViewById(R.id.txtProductPrice)
        val txtProductStock: TextView = itemView.findViewById(R.id.txtProductStock)
        val txtProductCategory: TextView = itemView.findViewById(R.id.txtProductCategory)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_card, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        val category = categories.find { it.id == product.categoryId }

        holder.txtProductName.text = product.name
        holder.txtProductDesc.text = product.description
        holder.txtProductPrice.text = "S/ ${"%.2f".format(product.price)}"
        holder.txtProductStock.text = "Stock: ${product.stock}"

        holder.txtProductCategory.text = category?.name ?: "Sin categoría"

        holder.btnDelete.setOnClickListener { onDeleteClick(product) }
        holder.btnEdit.setOnClickListener { onEditClick(product) }
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newProducts: List<Product>, newCategories: List<Category>) {
        products = newProducts
        categories = newCategories
        notifyDataSetChanged()
    }
}
