package com.cibertec.view.fragments

import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.*
import com.cibertec.R
import com.cibertec.controller.*
import com.cibertec.model.*
import com.cibertec.view.adapters.ProductAdapter
import com.cibertec.view.dialogs.FormProductDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.journeyapps.barcodescanner.*

class ProductsFragment : Fragment(R.layout.fragment_products) {

    private var productController: ProductController = ProductController()
    private var categoryController: CategoryController = CategoryController()
    private lateinit var adapter: ProductAdapter
    private lateinit var rvProducts: RecyclerView
    private lateinit var pbProducts: ProgressBar

    private var scannedQRCallback: ((String) -> Unit)? = null

    private val qrScanLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            // Aquí actualizamos la descripción si hay un listener activo
            scannedQRCallback?.invoke(result.contents)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI(view)
        setupFabListener(view)
        loadData()
    }

    private fun startQRScan(onScanned: (String) -> Unit) {
        scannedQRCallback = onScanned

        val options = ScanOptions()
        options.setPrompt("Escanea un código QR")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.setBarcodeImageEnabled(true)
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)

        qrScanLauncher.launch(options)
    }

    private fun setupFabListener(view: View) {
        val fabAddProduct = view.findViewById<FloatingActionButton>(R.id.fabAddProduct)
        fabAddProduct.setOnClickListener {
            categoryController.getCategorias(
                onSuccess = {
                    FormProductDialog(
                        context = requireContext(),
                        categorias = it,
                        onProductSaved = { product ->
                            insertAndRefreshProducts(product)
                        },
                        onScanRequested = { updateDescription ->
                            startQRScan { scannedText ->
                                updateDescription(scannedText)
                            }
                        }
                    ).show()
                },
                onError = {
                    Log.e("ProductsFragment", "Error al cargar categorías", it)
                    Toast.makeText(
                        requireContext(),
                        "Error al cargar categorías",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun setupUI(view: View) {
        rvProducts = view.findViewById(R.id.rvProducts)
        pbProducts = view.findViewById(R.id.pbProducts)

        rvProducts.layoutManager = LinearLayoutManager(requireContext())

        adapter = ProductAdapter(
            products = emptyList(),
            onEditClick = { product ->
                showEditDialog(product)
            },
            onDeleteClick = { product ->
                showDeleteConfirmation(product)
            }
        )
        rvProducts.adapter = adapter
    }

    private fun loadData() {
        productController.loadProductos(
            onStart = {
                pbProducts.visibility = View.VISIBLE
                rvProducts.visibility = View.GONE
            },
            onFinish = { products ->
                pbProducts.visibility = View.GONE
                rvProducts.visibility = View.VISIBLE

                adapter.updateData(products)
            },
            onError = { error ->
                pbProducts.visibility = View.GONE
                Log.e("ProductsFragment", "Error al cargar datos", error)
                Toast.makeText(requireContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
        )
    }
    private fun insertAndRefreshProducts(product: ProductoRequest) {
        productController.insertProducto(
            producto = product,
            onInsert = {
                Toast.makeText(requireContext(), "Producto guardado ${it.nombre}", Toast.LENGTH_SHORT).show()
                loadData()
            },
            onError = { error ->
                Log.e("ProductsFragment", "Error al insertar producto", error)
                Toast.makeText(requireContext(), "Error al guardar el producto", Toast.LENGTH_SHORT).show()
            }
        )
    }
    private fun updateAndRefreshProducts(product: ProductoResponse) {
        productController.updateProducto(
            producto = product,
            onUpdate = {
                Toast.makeText(requireContext(), "Producto actualizado", Toast.LENGTH_SHORT).show()
                loadData()
            },
            onError = { error ->
                Log.e("ProductsFragment", "Error al actualizar producto", error)
                Toast.makeText(requireContext(), "Error al actualizar el producto", Toast.LENGTH_SHORT).show()
            }
        )
    }
    private fun showDeleteConfirmation(product: ProductoResponse) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar el producto '${product.nombre}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                productController.deleteProducto(
                    id = product.id,
                    onDelete = {
                        Toast.makeText(requireContext(), "Producto eliminado", Toast.LENGTH_SHORT).show()
                        loadData()
                    },
                    onError = { error ->
                        Log.e("ProductsFragment", "Error al eliminar producto", error)
                        Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }
    private fun showEditDialog(product: ProductoResponse) {
        categoryController.getCategorias(
            onSuccess = {
                FormProductDialog(
                    context = requireContext(),
                    categorias = it,
                    productToEdit = product,
                    onProductEdit = { it ->
                        updateAndRefreshProducts(it)
                    },
                    onScanRequested = { updateDescription ->
                        startQRScan { scannedText ->
                            updateDescription(scannedText)
                        }
                    }
                ).show()
            },
            onError = {
                Log.e("ProductsFragment", "Error al cargar categorías", it)
                Toast.makeText(
                    requireContext(),
                    "Error al cargar categorías",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}
