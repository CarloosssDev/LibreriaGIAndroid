package com.cibertec.view.fragments

import android.app.AlertDialog
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
import com.cibertec.model.CategoriaRequest
import com.cibertec.model.CategoriaResponse
import com.cibertec.view.adapters.CategoryAdapter
import com.cibertec.view.dialogs.FormCategoryDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private var controller: CategoryController = CategoryController()
    private lateinit var adapter: CategoryAdapter
    private lateinit var rvCategories: RecyclerView
    private lateinit var pbCategories: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI(view)
        setupFabListener(view)
        loadCategories()
    }

    fun setupFabListener(view: View) {
        val fabAddProduct = view.findViewById<FloatingActionButton>(R.id.fabAddCategory)
        fabAddProduct.setOnClickListener {
            FormCategoryDialog(
                context = requireContext(),
                onCategorySaved = {
                    insertAndRefresh(it)
                }
            ).show()
        }
    }
    private fun setupUI(view: View) {
        rvCategories = view.findViewById(R.id.rvCategories)
        pbCategories = view.findViewById(R.id.pbCategories)

        rvCategories.layoutManager = LinearLayoutManager(requireContext())


        adapter = CategoryAdapter(
            emptyList(),
            onEditClick = {
                showEditDialog(it)
            },
            onDeleteClick = {
                showDeleteConfirmation(it)
            }
        )
        rvCategories.adapter = adapter
    }

    private fun loadCategories() {
        controller.loadCategorias(
            onStart = {
                pbCategories.visibility = View.VISIBLE
                rvCategories.visibility = View.GONE
            },
            onFinish = { list ->
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

    private fun insertAndRefresh(categoria: CategoriaRequest) {
        controller.insertCategoria(
            categoria = categoria,
            onInsert = {
                Toast.makeText(requireContext(), "Categoría creada ${it.nombre}", Toast.LENGTH_SHORT).show()
                loadCategories()
            },
            onError = {
                Log.e("CategoriesFragment", "Error al crear categoría", it)
                Toast.makeText(requireContext(), "Error al crear categoría", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun updateAndRefresh(categoria: CategoriaResponse) {
        controller.updateCategoria(
            categoria = categoria,
            onUpdate = {
                Toast.makeText(requireContext(), "Categoría actualizada ${it.nombre}", Toast.LENGTH_SHORT).show()
                loadCategories()
            },
            onError = {
                Log.e("CategoriesFragment", "Error al actualizar categoría", it)
                Toast.makeText(requireContext(), "Error al actualizar categoría", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun showDeleteConfirmation(categoria: CategoriaResponse) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Categoría")
            .setMessage("¿Estás seguro de que deseas eliminar la categoria '${categoria.nombre}' ?")
            .setPositiveButton("Eliminar") { _, _ ->
                controller.deleteCategoria(
                    id = categoria.id,
                    onDelete = {
                        Toast.makeText(requireContext(), "Categoría eliminada", Toast.LENGTH_SHORT)
                            .show()
                        loadCategories()
                    },
                    onError = {
                        Log.e("CategoriesFragment", "Error al eliminar categoría ${categoria.nombre}", it)
                        Toast.makeText(requireContext(), "Error al eliminar categoría", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }

    private fun showEditDialog(categoria: CategoriaResponse) {
        FormCategoryDialog(
            context = requireContext(),
            categoryToEdit = categoria,
            onCategoryUpdated = {
                updateAndRefresh(it)
            }
        ).show()
    }
}
