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

class IngresosFragment : Fragment(R.layout.fragment_ingresos) {
    private lateinit var ingresoController: IngresoController
    private lateinit var productController: ProductController
    private lateinit var adapter: IngresoAdapter
    private lateinit var rvIngresos: RecyclerView
    private lateinit var pbIngresos: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ingresoController = IngresoController(requireContext())
        productController = ProductController(requireContext())

        setupUI(view)
        loadData()
    }


    private fun setupUI(view: View) {
        rvIngresos = view.findViewById(R.id.rvIngresos)
        pbIngresos = view.findViewById(R.id.pbIngresos)

        rvIngresos.layoutManager = LinearLayoutManager(requireContext())
        adapter = IngresoAdapter(
            emptyList(),
            emptyList(),
            {},
            {})
        rvIngresos.adapter = adapter
    }

    fun loadData() {
        pbIngresos.visibility = View.VISIBLE
        rvIngresos.visibility = View.GONE

        ingresoController.loadIngresosAndProducts(
            onStart = {},
            onFinish = { ingresos, products ->
                pbIngresos.visibility = View.GONE
                rvIngresos.visibility = View.VISIBLE
                adapter.updateData(ingresos, products)
            },
            onError = { error ->
                pbIngresos.visibility = View.GONE
                Log.e("IngresosFragment", "Error al cargar datos", error)
                Toast.makeText(requireContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show()
            })
    }
}