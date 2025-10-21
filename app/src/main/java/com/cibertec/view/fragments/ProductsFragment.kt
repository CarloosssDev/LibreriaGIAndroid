package com.cibertec.view.fragments

import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.R
import com.cibertec.controller.CategoryController
import com.cibertec.controller.ProductController
import com.cibertec.model.Product
import com.cibertec.model.ProductoRequest
import com.cibertec.model.ProductoResponse
import com.cibertec.view.adapters.ProductAdapter
import com.cibertec.view.dialogs.FormProductDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsFragment : Fragment(R.layout.fragment_products) {

    private lateinit var productController: ProductController
    private lateinit var categoryController: CategoryController
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

        productController = ProductController(requireContext())
        categoryController = CategoryController(requireContext())

        setupUI(view)
        setupFabListener(view)
        loadData()
    }

    fun startQRScan(onScanned: (String) -> Unit) {
        scannedQRCallback = onScanned

        val options = ScanOptions()
        options.setPrompt("Escanea un código QR")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.setBarcodeImageEnabled(true)
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)

        qrScanLauncher.launch(options)
    }

    fun setupFabListener(view: View) {
        val fabAddProduct = view.findViewById<FloatingActionButton>(R.id.fabAddProduct)
        fabAddProduct.setOnClickListener {
            lifecycleScope.launch {
                val categories = withContext(Dispatchers.IO) {
                    categoryController.getCategoriasAPI()
                }

                FormProductDialog(
                    context = requireContext(),
                    categorias = categories,
                    onProductSaved = { product ->
                        insertAndRefreshProducts(product)
                    },
                    onProductEdit = {},
                    onScanRequested = { updateDescription ->
                        startQRScan { scannedText ->
                            updateDescription(scannedText)
                        }
                    }
                ).show()
            }
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
        pbProducts.visibility = View.VISIBLE
        rvProducts.visibility = View.GONE

        productController.loadProductosAPI(
            onStart = {
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
        productController.insertProductoAPI(
            producto = product,
            onInserted = {
                Toast.makeText(requireContext(), "Producto guardado  ${it.nombre}", Toast.LENGTH_SHORT).show()
                loadData()
            },
            onError = { error ->
                Log.e("ProductsFragment", "Error al insertar producto", error)
                Toast.makeText(requireContext(), "Error al guardar el producto", Toast.LENGTH_SHORT).show()
            }
        )
    }
    private fun updateAndRefreshProducts(product: ProductoResponse) {
        productController.updateProductAPI(
            producto = product,
            onUpdated = {
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
                productController.deleteProductAPI(
                    id = product.id,
                    onDeleted = {
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
        lifecycleScope.launch {
            val categories = withContext(Dispatchers.IO) {
                categoryController.getCategoriasAPI()
            }

            FormProductDialog(
                context = requireContext(),
                categorias = categories,
                productToEdit = product,
                onProductSaved = {},
                onProductEdit = { updatedProduct ->
                    updateAndRefreshProducts(updatedProduct)
                },
                onScanRequested = { updateDescription ->
                    startQRScan { scannedText ->
                        updateDescription(scannedText)
                    }
                }
            ).show()
        }
    }
}
