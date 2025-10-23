package com.cibertec.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.cibertec.R
import com.cibertec.controller.*
import com.cibertec.model.*
import com.cibertec.view.adapters.IngresoAdapter
import com.cibertec.view.dialogs.FormIngresoDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class IngresosFragment : Fragment(R.layout.fragment_ingresos) {
    private var ingresoController = IngresoController()
    private var productoController = ProductController()
    private lateinit var adapter: IngresoAdapter
    private lateinit var rvIngresos: RecyclerView
    private lateinit var pbIngresos: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupUI(view)
        setupFabListener(view)
        loadIngresos()
    }

    private fun setupFabListener(view: View) {
        val fabAddProduct = view.findViewById<FloatingActionButton>(R.id.fabAddIngreso)
        fabAddProduct.setOnClickListener {
            productoController.getProductos(
                onSuccess = {
                    FormIngresoDialog(
                        context = requireContext(),
                        productos = it,
                        onIngresoSaved = {
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
        rvIngresos = view.findViewById(R.id.rvIngresos)
        pbIngresos = view.findViewById(R.id.pbIngresos)

        rvIngresos.layoutManager = LinearLayoutManager(requireContext())
        adapter = IngresoAdapter(
            emptyList(),
            { ingreso ->
                showEditDialog(ingreso)
            },
            { ingreso ->
                showDeleteConfirmation(ingreso)
            })
        rvIngresos.adapter = adapter
    }

    private fun loadIngresos() {
        ingresoController.loadIngresos(
            onStart = {
                pbIngresos.visibility = View.VISIBLE
                rvIngresos.visibility = View.GONE
            },
            onFinish = { list ->
                pbIngresos.visibility = View.GONE
                rvIngresos.visibility = View.VISIBLE
                adapter.updateData(list)
            },
            onError = {
                Log.e("Error", it.message.toString())
                Toast.makeText(requireContext(), "Error al cargar los ingresos", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }

    private fun insertAndRefreh(ingreso: IngresoRequest) {
        ingresoController.insertIngreso(
            ingreso = ingreso,
            onSuccess = {
                loadIngresos()
            },
            onError = {
                Log.e("Error", it.message.toString())
                Toast.makeText(requireContext(), "Error al insertar el ingreso", Toast.LENGTH_SHORT)
                    .show()
            })
    }

    private fun updateAndRefreh(ingreso: IngresoResponse) {
        ingresoController.updateIngreso(
            ingreso = ingreso,
            onUpdated = {
                Toast.makeText(requireContext(), "Ingreso actualizado", Toast.LENGTH_SHORT).show()
                loadIngresos()
            },
            onError = {
                Log.e("Error", it.message.toString())
                Toast.makeText(requireContext(), "Error al actualizar el ingreso", Toast.LENGTH_SHORT)
                    .show()
            })
    }


    private fun showDeleteConfirmation(ingreso: IngresoResponse) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar el ingreso del producto '${ingreso.producto_nombre}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                ingresoController.deleteIngreso(
                    id = ingreso.id,
                    onDeleted = {
                        Toast.makeText(requireContext(), "Ingreso eliminado", Toast.LENGTH_SHORT)
                            .show()
                        loadIngresos()
                    },
                    onError = { error ->
                        Log.e("ProductsFragment", "Error al eliminar el ingreso", error)
                        Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }

    private fun showEditDialog(ingreso: IngresoResponse) {
        productoController.getProductos(
            onSuccess = {
                FormIngresoDialog(
                    context = requireContext(),
                    productos = it,
                    ingresoToEdit = ingreso,
                    onIngresoEdit = {
                        updateAndRefreh(it)
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