package com.cibertec.view.adapters

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.*
import com.cibertec.R
import com.cibertec.model.*
import java.time.LocalDateTime

class SalidaAdapter (
    private var salidas: List<SalidaResponse>,
    private var onEditClick: (SalidaResponse) -> Unit,
    private var onDeleteClick: (SalidaResponse) -> Unit
): RecyclerView.Adapter<SalidaAdapter.SalidaViewHolder>() {
    class SalidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtProductName: TextView = itemView.findViewById(R.id.txtProductName)
        val txtSalidaDate: TextView = itemView.findViewById(R.id.txtIngresoDate)
        val txtSalidaQuantity: TextView = itemView.findViewById(R.id.txtSalidaQuantity)
        val txtComentario: TextView = itemView.findViewById(R.id.txtMotivo)
        val btnEditSalida: ImageButton = itemView.findViewById(R.id.btnEditSalida)
        val btnDeleteSalida: ImageButton = itemView.findViewById(R.id.btnDeleteSalida)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalidaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_salida_card, parent, false)
        return SalidaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalidaViewHolder, position: Int) {
        val salida = salidas[position]
        val fecha = LocalDateTime.parse(salida.fecha)
        val formattedDate = "${fecha.dayOfMonth}/${fecha.monthValue}/${fecha.year} ${fecha.hour}:${fecha.minute}"
        holder.txtProductName.text = salida.producto_nombre
        holder.txtSalidaDate.text = formattedDate
        holder.txtComentario.text = salida.motivo
        holder.txtSalidaQuantity.text = "-${salida.cantidad}"
        holder.btnEditSalida.setOnClickListener { onEditClick(salida) }
        holder.btnDeleteSalida.setOnClickListener { onDeleteClick(salida) }
    }

    override fun getItemCount(): Int = salidas.size

    fun updateData(newSalidas: List<SalidaResponse>) {
        salidas = newSalidas
        notifyDataSetChanged()
    }
}