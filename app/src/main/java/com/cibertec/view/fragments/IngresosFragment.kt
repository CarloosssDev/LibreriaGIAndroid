package com.cibertec.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.R
import com.cibertec.controller.IngresoController
import com.cibertec.controller.ProductController
import com.cibertec.view.adapters.IngresoAdapter
import com.cibertec.view.dialogs.FormCategoryDialog
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

    fun setupFabListener(view: View) {
        val fabAddProduct = view.findViewById<FloatingActionButton>(R.id.fabAddIngreso)
        fabAddProduct.setOnClickListener {
            productoController.getProductos(
                onSuccess = {
                    FormIngresoDialog(
                        context = requireContext(),
                        productos = it,
                        onIngresoSaved = {}
                    ).show()
                },
                onError = {
                    Log.e("Error", it.message.toString())
                    Toast.makeText(requireContext(), "Error al cargar los productos", Toast.LENGTH_SHORT).show()
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
            {},
            {})
        rvIngresos.adapter = adapter
    }

    fun loadIngresos() {
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
                Toast.makeText(requireContext(), "Error al cargar los ingresos", Toast.LENGTH_SHORT).show()
            }
        )
    }
}