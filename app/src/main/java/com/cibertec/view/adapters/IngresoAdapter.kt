package com.cibertec.view.adapters

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.*
import com.cibertec.R
import com.cibertec.model.*
import java.time.LocalDateTime

class IngresoAdapter (
    private var ingresos: List<IngresoResponse>,
    private var onEditClick: (IngresoResponse) -> Unit,
    private var onDeleteClick: (IngresoResponse) -> Unit
): RecyclerView.Adapter<IngresoAdapter.IngresoViewHolder>() {
    class IngresoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtProductName: TextView = itemView.findViewById(R.id.txtProductName)
        val txtIngresoDate: TextView = itemView.findViewById(R.id.txtIncomeDate)
        val txtIngresoQuantity: TextView = itemView.findViewById(R.id.txtIncomeQuantity)
        val txtComentario: TextView = itemView.findViewById(R.id.etComentario)
        val btnEditIngreso: ImageButton = itemView.findViewById(R.id.btnEditIngreso)
        val btnDeleteIngreso: ImageButton = itemView.findViewById(R.id.btnDeleteIngreso)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngresoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ingreso_card, parent, false)
        return IngresoViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngresoViewHolder, position: Int) {
        val ingreso = ingresos[position]
        val fecha = LocalDateTime.parse(ingreso.fecha)
        val formattedDate = "${fecha.dayOfMonth}/${fecha.monthValue}/${fecha.year} ${fecha.hour}:${fecha.minute}"
        holder.txtProductName.text = ingreso.producto_nombre
        holder.txtIngresoDate.text = formattedDate
        holder.txtComentario.text = ingreso.comentario
        holder.txtIngresoQuantity.text = ingreso.cantidad.toString()
        holder.btnEditIngreso.setOnClickListener { onEditClick(ingreso) }
        holder.btnDeleteIngreso.setOnClickListener { onDeleteClick(ingreso) }
    }

    override fun getItemCount(): Int = ingresos.size

    fun updateData(newIngresos: List<IngresoResponse>) {
        ingresos = newIngresos
        notifyDataSetChanged()
    }
}