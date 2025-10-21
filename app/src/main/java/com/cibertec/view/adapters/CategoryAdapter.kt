package com.cibertec.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.R
import com.cibertec.model.*

class CategoryAdapter (
    private var categorias: List<CategoriaResponse>,
    private val onDeleteClick: (CategoriaResponse) -> Unit,
    private val onEditClick: (CategoriaResponse) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategoryName: TextView = itemView.findViewById(R.id.txtCategoryName)
        val tvCategoryDescription: TextView = itemView.findViewById(R.id.txtCategoryDescription)
        val txtProductCount: TextView = itemView.findViewById(R.id.txtProductCount)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteCategory)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEditCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_card, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categorias[position]
        val products = category.productos.size
        val productCount = if(products == 1) {
            "${products} producto"
        } else {
            "${products} productos"
        }

        holder.tvCategoryName.text = category.nombre
        holder.tvCategoryDescription.text = category.descripcion
        holder.txtProductCount.text = productCount

        holder.btnDelete.setOnClickListener { onDeleteClick(category) }
        holder.btnEdit.setOnClickListener { onEditClick(category) }
    }

    override fun getItemCount(): Int = categorias.size

    fun updateData(newList: List<CategoriaResponse>) {
        categorias = newList
        notifyDataSetChanged()
    }
}
