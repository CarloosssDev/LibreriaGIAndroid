package com.cibertec.view.adapters

import android.view.*
import android.widget.ImageButton

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.R
import com.cibertec.model.*

class ProductAdapter(
    private var products: List<ProductoResponse>,
    private val onDeleteClick: (ProductoResponse) -> Unit,
    private val onEditClick: (ProductoResponse) -> Unit
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

        holder.txtProductName.text = product.nombre
        holder.txtProductDesc.text = product.descripcion
        holder.txtProductPrice.text = "S/ ${"%.2f".format(product.precio_unitario)}"
        holder.txtProductStock.text = "Stock: ${product.stock_actual}"
        holder.txtProductCategory.text = product.categoria_nombre
        holder.btnDelete.setOnClickListener { onDeleteClick(product) }
        holder.btnEdit.setOnClickListener { onEditClick(product) }
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newProducts: List<ProductoResponse>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
