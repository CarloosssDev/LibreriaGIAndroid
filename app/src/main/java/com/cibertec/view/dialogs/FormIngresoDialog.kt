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

class FormIngresoDialog (
    private val context: Context,
    private val productos: List<ProductoResponse>,
    private val ingresoToEdit: IngresoResponse? = null,
    private val onIngresoEdit: (IngresoResponse) -> Unit = {},
    private val onIngresoSaved: (IngresoRequest) -> Unit = {}
) {
    private val isEditMode = ingresoToEdit != null
    private val dialog = Dialog(context)

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_form_ingreso)
        dialog.setCancelable(true)

        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val etCantidad = dialog.findViewById<TextView>(R.id.etCantidad)
        val etComentario = dialog.findViewById<TextView>(R.id.etComentario)
        val spinnerProductIngreso = dialog.findViewById<AutoCompleteTextView>(R.id.spinnerProductIngreso)
        val btnSave = dialog.findViewById<TextView>(R.id.btnSave)
        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)

        val productosName = productos.map { it.nombre }
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, productosName)
        spinnerProductIngreso.setAdapter(adapter)

        if (isEditMode) {
            tvTitle.setText("Editar Ingreso")
            btnSave.setText("Actualizar")
            etCantidad.setText(ingresoToEdit?.cantidad.toString())
            etComentario.setText(ingresoToEdit?.comentario)
            val productToSelect = productos.find { it.nombre == ingresoToEdit?.producto_nombre }
            spinnerProductIngreso.setText(productToSelect?.nombre, false)
        } else {
            tvTitle.setText("Nuevo Ingreso")
            btnSave.setText("Guardar")
        }
        btnSave.setOnClickListener {
            val cantidad = etCantidad.text.toString().toIntOrNull()
            val comentario = etComentario.text.toString()
            val producto = spinnerProductIngreso.text.toString()

            if (cantidad == null || cantidad <= 0) {
                Toast.makeText(context, "Ingrese una cantidad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (comentario.isEmpty()) {
                Toast.makeText(context, "Ingrese un comentario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val productToSelect = productos.find { it.nombre == producto }
            if (productToSelect == null) {
                Toast.makeText(context, "Producto no encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (isEditMode) {
                val updatedIngreso = IngresoResponse(
                    id = ingresoToEdit!!.id,
                    fecha = ingresoToEdit.fecha,
                    cantidad = cantidad,
                    comentario = comentario,
                    producto_id = productToSelect.id,
                    producto_nombre = productToSelect.nombre,
                )
                onIngresoEdit(updatedIngreso)
                dialog.dismiss()
            } else {
                val newIngreso = IngresoRequest(
                    cantidad = cantidad,
                    comentario = comentario,
                    producto_id = productToSelect.id,
                )
                onIngresoSaved(newIngreso)
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