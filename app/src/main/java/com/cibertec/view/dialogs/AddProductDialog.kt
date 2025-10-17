package com.cibertec.view.dialogs

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.cibertec.R
import com.cibertec.model.Category
import com.cibertec.model.Product
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AddProductDialog(
    private val context: Context,
    private val categories: List<Category>,
    private val productToEdit: Product? = null,
    private val onProductSaved: (Product) -> Unit,
    private val onScanRequested: (updateDescription: (String) -> Unit) -> Unit
) {
    private val isEditMode = productToEdit != null
    private val dialog: Dialog = Dialog(context)

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_product)
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


        val categoryNames = categories.map { it.name }
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, categoryNames)
        spinnerCategory.setAdapter(adapter)

        if (isEditMode) {
            tvTitle.text = "Editar Producto"
            btnSave.text = "Actualizar"
            productToEdit?.let { product ->
                etProductName.setText(product.name)
                etDescription.setText(product.description)
                etPrice.setText(product.price.toString())
                etStock.setText(product.stock.toString())

                val categoryToSelect = categories.find { it.id == product.categoryId }
                categoryToSelect?.let {
                    spinnerCategory.setText(it.name, false)
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

            val selectedCategory = categories.find { it.name == categoryName }
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

            val product = Product(
                id = productToEdit?.id ?: 0,
                name = name,
                description = description,
                price = price,
                stock = stock,
                categoryId = selectedCategory.id
            )

            onProductSaved(product)
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
