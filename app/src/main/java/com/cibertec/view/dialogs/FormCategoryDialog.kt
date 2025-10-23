package com.cibertec.view.dialogs

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.cibertec.R
import com.cibertec.model.*
import com.google.android.material.button.MaterialButton

class FormCategoryDialog (
    private val context: Context,
    private val categoryToEdit: CategoriaResponse? = null,
    private val onCategoryUpdated: (CategoriaResponse) -> Unit = {} ,
    private val onCategorySaved: (CategoriaRequest) -> Unit = {}
) {
    private val isEditMode = categoryToEdit != null
    private val dialog: Dialog = Dialog(context)

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_form_category)
        dialog.setCancelable(true)

        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val txtCategoryName = dialog.findViewById<EditText>(R.id.txtCategoryName)
        val txtCategoryDescription = dialog.findViewById<EditText>(R.id.txtCategoryDescription)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)
        val btnCancel = dialog.findViewById<MaterialButton>(R.id.btnCancel)

        if (isEditMode) {
            tvTitle.text = "Editar Categoría"
            btnSave.text = "Actualizar"
            txtCategoryName.setText(categoryToEdit?.nombre)
            txtCategoryDescription.setText(categoryToEdit?.descripcion)
        } else {
            tvTitle.text = "Nueva Categoría"
            btnSave.text = "Guardar"
        }

        btnSave.setOnClickListener {
            val name = txtCategoryName.text.toString()
            val description = txtCategoryDescription.text.toString()

            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (isEditMode) {
                val updatedCategory = CategoriaResponse(
                    id = categoryToEdit!!.id,
                    nombre = name,
                    descripcion = description,
                    productos = categoryToEdit.productos
                )
                onCategoryUpdated(updatedCategory)
            }
            val newCategory = CategoriaRequest(
                nombre = name,
                descripcion = description
            )
            onCategorySaved(newCategory)
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