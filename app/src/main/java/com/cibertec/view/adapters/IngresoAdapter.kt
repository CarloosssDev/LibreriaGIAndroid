package com.cibertec.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.*
import com.cibertec.R
import com.cibertec.model.*

class IngresoAdapter (
    private var ingresos: List<Ingreso>,
    private var products: List<Product>,
    private var onEditClick: (Ingreso) -> Unit,
    private var onDeleteClick: (Ingreso) -> Unit
): RecyclerView.Adapter<IngresoAdapter.IngresoViewHolder>() {
    class IngresoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtProductName: TextView = itemView.findViewById(R.id.txtProductName)
        val txtIngresoDate: TextView = itemView.findViewById(R.id.txtIncomeDate)
        val txtIngresoQuantity: TextView = itemView.findViewById(R.id.txtIncomeQuantity)
        val txtComentario: TextView = itemView.findViewById(R.id.txtComentario)
        val btnEditIngreso: ImageButton = itemView.findViewById(R.id.btnEditIngreso)
        val btnDeleteIngreso: ImageButton = itemView.findViewById(R.id.btnDeleteIngreso)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngresoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingreso_card, parent, false)
        return IngresoViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngresoViewHolder, position: Int) {
        val ingreso = ingresos[position]
        val product = products.find { it.id == ingreso.product_id }
        holder.txtProductName.text = product?.name ?: ""
        holder.txtIngresoDate.text = ingreso.fecha
        holder.txtComentario.text = ingreso.comentario
        holder.txtIngresoQuantity.text = ingreso.cantidad.toString()
        holder.btnEditIngreso.setOnClickListener { onEditClick(ingreso) }
        holder.btnDeleteIngreso.setOnClickListener { onDeleteClick(ingreso) }
    }

    override fun getItemCount(): Int = ingresos.size

    fun updateData(newIngresos: List<Ingreso>, newProducts: List<Product>) {
        ingresos = newIngresos
        products = newProducts
        notifyDataSetChanged()
    }
}