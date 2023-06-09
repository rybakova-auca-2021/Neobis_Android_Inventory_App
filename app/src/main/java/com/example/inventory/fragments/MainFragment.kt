package com.example.inventory.fragments

import RecyclerViewAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.inventory.model.Product
import com.example.inventory.R
import com.example.inventory.databinding.FragmentMainBinding
import com.example.inventory.presenter.Presenter
import com.example.inventory.presenter.PresenterClass

class MainFragment : Fragment(), Presenter.ProductView {
    private lateinit var binding: FragmentMainBinding
    private val adapter by lazy { RecyclerViewAdapter() }
    private lateinit var presenter: PresenterClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupAddButton()
        getProduct()
    }

    private fun getProduct() {
        presenter = PresenterClass(requireContext())
        presenter.attachView(this)
        presenter.getAllProducts()
    }

    private fun setupRecyclerView() {
        binding.rvMain.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvMain.adapter = adapter
        adapter.setOnItemClick(object : RecyclerViewAdapter.ListClickListener<Product> {
            override fun onClick(data: Product, position: Int) {
                val fragment = DetailFragment()
                fragment.arguments = Bundle().apply { putParcelable("products", data) }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
    }
    private fun setupAddButton() {
        binding.addButtonMain.setOnClickListener {
            val fragment = AddProductFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
    override fun onResume() {
        super.onResume()
        presenter.getAllProducts()
    }
    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun showAllProducts(products: List<Product>) {
        Log.e("Test", "showAllProductsFragment")
        adapter.updateProduct(products)
    }
}
