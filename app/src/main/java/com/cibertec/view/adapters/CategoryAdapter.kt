package com.cibertec.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.R
import com.cibertec.model.CategoryWithProductCount

class CategoryAdapter (
    private var categoriesWithCount: List<CategoryWithProductCount>,
    private val onDeleteClick: (CategoryWithProductCount) -> Unit,
    private val onEditClick: (CategoryWithProductCount) -> Unit
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
        val item = categoriesWithCount[position]
        val category = item.category

        holder.tvCategoryName.text = category.name
        holder.tvCategoryDescription.text = category.description
        holder.txtProductCount.text = "${item.productCount} productos"

        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
        holder.btnEdit.setOnClickListener { onEditClick(item) }
    }

    override fun getItemCount(): Int = categoriesWithCount.size

    fun updateData(newList: List<CategoryWithProductCount>) {
        categoriesWithCount = newList
        notifyDataSetChanged()
    }
}
