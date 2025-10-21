package com.cibertec.view.dialogs

import android.app.Dialog
import android.content.Context
import android.view.*
import android.widget.*
import com.cibertec.R
import com.cibertec.model.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.*

class FormProductDialog(
    private val context: Context,
    private val categorias: List<CategoriaResponse>,
    private val productToEdit: ProductoResponse? = null,
    private val onProductEdit: (ProductoResponse) -> Unit,
    private val onProductSaved: (ProductoRequest) -> Unit,
    private val onScanRequested: (updateDescription: (String) -> Unit) -> Unit
) {
    private val isEditMode = productToEdit != null
    private val dialog: Dialog = Dialog(context)

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_form_product)
        dialog.setCancelable(false)

        val tvTitle = dialog.findViewById<android.widget.TextView>(R.id.tvTitle)
        val etProductName = dialog.findViewById<TextInputEditText>(R.id.txtProductName)
        val etDescription = dialog.findViewById<TextInputEditText>(R.id.txtDescription)
        val etPrice = dialog.findViewById<TextInputEditText>(R.id.txtProductPrice)
        val etStock = dialog.findViewById<TextInputEditText>(R.id.txtStock)

        val spinnerCategory = dialog.findViewById<AutoCompleteTextView>(R.id.spinnerProductCategory)

        val layoutDescription = dialog.findViewById<TextInputLayout>(R.id.layoutDescription)

        val btnSave = dialog.findViewById<MaterialButton>(R.id.btnSave)
        val btnCancel = dialog.findViewById<MaterialButton>(R.id.btnCancel)


        val categoryNames = categorias.map { it.nombre }
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, categoryNames)
        spinnerCategory.setAdapter(adapter)

        if (isEditMode) {
            tvTitle.text = "Editar Producto"
            btnSave.text = "Actualizar"
            productToEdit?.let { product ->
                etProductName.setText(product.nombre)
                etDescription.setText(product.descripcion)
                etPrice.setText(product.precio_unitario.toString())
                etStock.setText(product.stock_actual.toString())

                val categoryToSelect = categorias.find { it.id == product.categoria_id }
                categoryToSelect?.let {
                    spinnerCategory.setText(it.nombre, false)
                }
            }
        } else {
            tvTitle.text = "Agregar Producto"
            btnSave.text = "Guardar"
        }

        layoutDescription.setEndIconOnClickListener {
            onScanRequested { scannedText ->
                etDescription.setText(scannedText)
            }
        }

        btnSave.setOnClickListener {
            val name = etProductName.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val priceStr = etPrice.text.toString().trim()
            val stockStr = etStock.text.toString().trim()
            val categoryName = spinnerCategory.text.toString()

            if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || categoryName.isEmpty()) {
                Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedCategory = categorias.find { it.nombre == categoryName }
            if (selectedCategory == null) {
                Toast.makeText(context, "Por favor, seleccione una categoría válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = priceStr.toDoubleOrNull()
            val stock = stockStr.toIntOrNull()

            if (price == null || stock == null || price <= 0.0 || stock < 0) {
                Toast.makeText(context, "Ingrese valores numéricos válidos para precio y stock", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEditMode) {
                val productResponse = ProductoResponse(
                    id = productToEdit!!.id,
                    nombre = name,
                    descripcion = description,
                    precio_unitario = price,
                    stock_actual = stock,
                    categoria_id = selectedCategory.id,
                    categoria_nombre = selectedCategory.nombre
                )
                onProductEdit(productResponse)
            }

            val productRequest = ProductoRequest(
                nombre = name,
                descripcion = description,
                precio_unitario = price,
                stock_actual = stock,
                categoria_id = selectedCategory.id
            )

            onProductSaved(productRequest)

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
