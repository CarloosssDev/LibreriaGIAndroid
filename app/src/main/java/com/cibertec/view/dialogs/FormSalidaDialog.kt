package com.cibertec.view.dialogs

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import com.cibertec.R
import com.cibertec.model.*

class FormSalidaDialog (
    private val context: Context,
    private val productos: List<ProductoResponse>,
    private val salidaToEdit: SalidaResponse? = null,
    private val onSalidaEdit: (SalidaResponse) -> Unit = {},
    private val onSalidaSaved: (SalidaRequest) -> Unit = {}
) {
    private val isEditMode = salidaToEdit != null
    private val dialog = Dialog(context)

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_form_salida)
        dialog.setCancelable(true)

        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val etCantidad = dialog.findViewById<TextView>(R.id.etCantidad)
        val etMotivo = dialog.findViewById<TextView>(R.id.etMotivo)
        val spinnerProductSalida = dialog.findViewById<AutoCompleteTextView>(R.id.spinnerProductoSalida)
        val btnSave = dialog.findViewById<TextView>(R.id.btnSave)
        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)

        val productosName = productos.map { it.nombre }
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, productosName)
        spinnerProductSalida.setAdapter(adapter)

        if (isEditMode) {
            tvTitle.setText("Editar Salida")
            btnSave.setText("Actualizar")
            etCantidad.setText("${salidaToEdit?.cantidad}")
            etMotivo.setText(salidaToEdit?.motivo)
            val productToSelect = productos.find { it.nombre == salidaToEdit?.producto_nombre }
            spinnerProductSalida.setText(productToSelect?.nombre, false)
        } else {
            tvTitle.setText("Nueva Salida")
            btnSave.setText("Guardar")
        }
        btnSave.setOnClickListener {
            val cantidad = etCantidad.text.toString().toIntOrNull()
            val motivo = etMotivo.text.toString()
            val producto = spinnerProductSalida.text.toString()

            if (cantidad == null || cantidad <= 0) {
                Toast.makeText(context, "Ingrese una cantidad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (motivo.isEmpty()) {
                Toast.makeText(context, "Ingrese un comentario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val productToSelect = productos.find { it.nombre == producto }
            if (productToSelect == null) {
                Toast.makeText(context, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val stockDisponibleReal: Int
            if (isEditMode) {
                val cantidadOriginal = salidaToEdit?.cantidad ?: 0
                stockDisponibleReal = productToSelect.stock_actual + cantidadOriginal
            } else {
                stockDisponibleReal = productToSelect.stock_actual
            }

            if (cantidad > stockDisponibleReal) {
                Toast.makeText(context, "No hay suficiente stock. Disponible: $stockDisponibleReal unidades.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (isEditMode) {
                val updatedSalida = SalidaResponse(
                    id = salidaToEdit!!.id,
                    fecha = salidaToEdit.fecha,
                    cantidad = cantidad,
                    motivo = motivo,
                    producto_id = productToSelect.id,
                    producto_nombre = productToSelect.nombre,
                )
                onSalidaEdit(updatedSalida)
                dialog.dismiss()
            } else {
                val newSalida = SalidaRequest(
                    cantidad = cantidad,
                    motivo = motivo,
                    producto_id = productToSelect.id,
                )
                onSalidaSaved(newSalida)
                dialog.dismiss()
            }
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
    fun show() {
        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}