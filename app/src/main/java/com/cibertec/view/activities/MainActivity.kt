package com.cibertec.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cibertec.R
import com.cibertec.view.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_products -> {
                    replaceFragment(ProductsFragment())
                    true
                }
                R.id.navigation_categories -> {
                    replaceFragment(CategoriesFragment())
                    true
                }
                R.id.navigation_income -> {
                    replaceFragment(IngresosFragment())
                    true
                }
                R.id.navigation_outcome -> {
                    replaceFragment(SalidasFragment())
                    true
                }
                else -> false
            }
        }
        if (savedInstanceState == null) {
            bottomNavigation.selectedItemId = R.id.navigation_products
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}