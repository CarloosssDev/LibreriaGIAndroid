package com.cibertec.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.R
import com.cibertec.controller.CategoryController
import com.cibertec.view.adapters.CategoryAdapter

class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private lateinit var controller: CategoryController
    private lateinit var adapter: CategoryAdapter
    private lateinit var rvCategories: RecyclerView
    private lateinit var pbCategories: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller = CategoryController(requireContext())

        setupUI(view)
        loadCategories()
    }

    private fun setupUI(view: View) {
        rvCategories = view.findViewById(R.id.rvCategories)
        pbCategories = view.findViewById(R.id.pbCategories)

        rvCategories.layoutManager = LinearLayoutManager(requireContext())


        adapter = CategoryAdapter(
            emptyList(),
            onEditClick = {
                Toast.makeText(requireContext(), "Editar: ${it.nombre}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = {
                Toast.makeText(requireContext(), "Eliminar: ${it.nombre}", Toast.LENGTH_SHORT).show()
            }
        )
        rvCategories.adapter = adapter
    }

    private fun loadCategories() {
        controller.loadCategoriasAPI(
            onStartLoading = {
                pbCategories.visibility = View.VISIBLE
                rvCategories.visibility = View.GONE
            },
            onFinishLoading = { list ->
                pbCategories.visibility = View.GONE
                rvCategories.visibility = View.VISIBLE

                adapter.updateData(list)
            },
            onError = { error ->
                pbCategories.visibility = View.GONE
                Log.e("CategoriesFragment", "Error al cargar las categorías", error)
                Toast.makeText(requireContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
