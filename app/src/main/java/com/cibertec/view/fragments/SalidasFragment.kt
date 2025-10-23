package com.cibertec.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.cibertec.R
import com.cibertec.controller.*
import com.cibertec.model.*
import com.cibertec.view.adapters.SalidaAdapter
import com.cibertec.view.dialogs.FormSalidaDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SalidasFragment : Fragment(R.layout.fragment_salidas) {

    private val LOW_STOCK_THRESHOLD = 5
    private var salidaController = SalidaController()
    private var productoController = ProductController()
    private lateinit var adapter: SalidaAdapter
    private lateinit var rvSalidas: RecyclerView
    private lateinit var pbSalidas: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupUI(view)
        setupFabListener(view)
        loadSalidas()
    }

    private fun setupFabListener(view: View) {
        val fabAddProduct = view.findViewById<FloatingActionButton>(R.id.fabAddSalida)
        fabAddProduct.setOnClickListener {
            productoController.getProductos(
                onSuccess = {
                    FormSalidaDialog(
                        context = requireContext(),
                        productos = it,
                        onSalidaSaved = {
                            insertAndRefreh(it)
                        }
                    ).show()
                },
                onError = {
                    Log.e("Error", it.message.toString())
                    Toast.makeText(
                        requireContext(),
                        "Error al cargar los productos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun setupUI(view: View) {
        rvSalidas = view.findViewById(R.id.rvSalidas)
        pbSalidas = view.findViewById(R.id.pbSalidas)

        rvSalidas.layoutManager = LinearLayoutManager(requireContext())
        adapter = SalidaAdapter(
            emptyList(),
            {
                showEditDialog(it)
            },
            {
                showDeleteConfirmation(it)
            })
        rvSalidas.adapter = adapter
    }

    private fun loadSalidas() {
        salidaController.loadSalidas(
            onStart = {
                pbSalidas.visibility = View.VISIBLE
                rvSalidas.visibility = View.GONE
            },
            onFinish = { list ->
                pbSalidas.visibility = View.GONE
                rvSalidas.visibility = View.VISIBLE
                adapter.updateData(list)
            },
            onError = {
                Log.e("Error", it.message.toString())
                Toast.makeText(requireContext(), "Error al cargar las salidas", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }

    private fun insertAndRefreh(salida: SalidaRequest) {
        salidaController.insertSalida(
            salida = salida,
            onSuccess = {
                loadSalidas()
                checkStock(it.producto_id)
            },
            onError = {
                Log.e("Error", it.message.toString())
                Toast.makeText(requireContext(), "Error al insertar la salida", Toast.LENGTH_SHORT)
                    .show()
            })
    }

    private fun updateAndRefreh(salida: SalidaResponse) {
        salidaController.updateSalida(
            salida = salida,
            onUpdated = {
                Toast.makeText(requireContext(), "Salida actualizada", Toast.LENGTH_SHORT).show()
                loadSalidas()
                checkStock(it.producto_id)
            },
            onError = {
                Log.e("Error", it.message.toString())
                Toast.makeText(requireContext(), "Error al actualizar la salida", Toast.LENGTH_SHORT)
                    .show()
            })
    }

    private fun showDeleteConfirmation(salida: SalidaResponse) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar la salida del producto '${salida.producto_nombre}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                salidaController.deleteSalida(
                    id = salida.id,
                    onDeleted = {
                        Toast.makeText(requireContext(), "Salida eliminada", Toast.LENGTH_SHORT)
                            .show()
                        loadSalidas()
                    },
                    onError = { error ->
                        Log.e("ProductsFragment", "Error al eliminar el Salida", error)
                        Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }

    private fun showEditDialog(salida: SalidaResponse) {
        productoController.getProductos(
            onSuccess = {
                FormSalidaDialog(
                    context = requireContext(),
                    productos = it,
                    salidaToEdit = salida,
                    onSalidaEdit = { item ->
                        updateAndRefreh(item)
                    }
                ).show()
            },
            onError = {
                Log.e("Error", it.message.toString())
                Toast.makeText(
                    requireContext(),
                    "Error al cargar los productos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    private fun checkStock(productId: Int) {
        productoController.getProducto(
            id = productId,
            onSuccess = {
                if (it.stock_actual < LOW_STOCK_THRESHOLD) {
                    showLowStockAlert(it)
                }
            },
            onError = {
                Log.e("Error", it.message.toString())
            }
        )
    }
    private fun showLowStockAlert(producto: ProductoResponse) {
        AlertDialog.Builder(requireContext())
            .setTitle("⚠️ Alerta de Bajo Stock")
            .setMessage("El stock del producto '${producto.nombre}' ha bajado a ${producto.stock_actual} unidades. Considera reponerlo.")
            .setPositiveButton("Entendido", null)
            .create()
            .show()
    }

}