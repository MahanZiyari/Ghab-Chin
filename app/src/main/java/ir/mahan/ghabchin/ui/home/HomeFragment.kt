package ir.mahan.ghabchin.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.ghabchin.data.model.home.ColorTone
import ir.mahan.ghabchin.data.model.home.ResponseCategories
import ir.mahan.ghabchin.data.model.home.ResponsePhotos
import ir.mahan.ghabchin.databinding.FragmentHomeBinding
import ir.mahan.ghabchin.ui.home.adapters.CategoriesAdapter
import ir.mahan.ghabchin.ui.home.adapters.ColorsAdapter
import ir.mahan.ghabchin.ui.home.adapters.LatestPhotosAdapter
import ir.mahan.ghabchin.util.base.BaseFragment
import ir.mahan.ghabchin.util.network.Wrapper
import ir.mahan.ghabchin.util.setStatusBarIconsColor
import ir.mahan.ghabchin.util.setupRecyclerview
import ir.mahan.ghabchin.util.showSnackBar
import ir.mahan.ghabchin.viewmodel.HomeViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    ///////////////////////////////////////////////////////////////////////////
    // properties
    ///////////////////////////////////////////////////////////////////////////
    // Binding
    override val bindingInflater: (LayoutInflater) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate
    // viewModel
    private val viewModel: HomeViewModel by viewModels()
    // Adapters
    @Inject
    lateinit var latestPhotosAdapter: LatestPhotosAdapter
    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter
    @Inject
    lateinit var colorsAdapter: ColorsAdapter


    ///////////////////////////////////////////////////////////////////////////
    // UI Functions
    ///////////////////////////////////////////////////////////////////////////
    override fun FragmentHomeBinding.setupUI() {
        requireActivity().setStatusBarIconsColor(false)
        initColorsRecycler()
        searchInpLay.setEndIconOnClickListener {
            val query = searchEdt.text.toString()
            val action = HomeFragmentDirections.actionToSearch(query)
            findNavController().navigate(action)
        }
    }

    override fun setupObservers() {
        observeLatestPhotos()
        observeCategories()
    }

    private fun observeLatestPhotos() = binding.apply {
        viewModel.latestPhotos.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Wrapper.Loading -> {
                    newestList.showShimmer()
                }

                is Wrapper.Success -> {
                    newestList.hideShimmer()
                    response.data?.let {
                        initLatestRecycler(it)
                    }
                }

                is Wrapper.Error -> {
                    newestList.hideShimmer()
                    root.showSnackBar(response.message!!)
                }
            }
        }
    }

    private fun initLatestRecycler(newData: List<ResponsePhotos.ResponsePhotosItem>) {
        latestPhotosAdapter.setData(newData)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.newestList.setupRecyclerview(layoutManager, latestPhotosAdapter)
        // Handling OnLick
        latestPhotosAdapter.setOnItemClickListener {
            val action = HomeFragmentDirections.actionToDetail(it)
            findNavController().navigate(action)
            Toast.makeText(requireContext(), "ID: $it", Toast.LENGTH_SHORT).show()
        }
    }

    // ColorTones
    private fun initColorsRecycler() {
        colorsAdapter.setData(ColorTone.entries.toList().shuffled())
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.colorToneList.setupRecyclerview(layoutManager, colorsAdapter)
        // Handling OnLick
        colorsAdapter.setOnItemClickListener {
            val action = HomeFragmentDirections.actionToSearch(it)
            findNavController().navigate(action)
        }
    }


    // Categories
    private fun observeCategories() = binding.apply {
        viewModel.categories.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Wrapper.Loading -> {
                    newestList.showShimmer()
                }

                is Wrapper.Success -> {
                    newestList.hideShimmer()
                    response.data?.let {
                        initCategoriesRecycler(it)
                    }
                }

                is Wrapper.Error -> {
                    newestList.hideShimmer()
                    root.showSnackBar(response.message!!)
                }
            }
        }
    }

    private fun initCategoriesRecycler(newData: List<ResponseCategories.ResponseCategoriesItem>) {
        categoriesAdapter.setData(newData)
        val layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.categoriesList.setupRecyclerview(layoutManager, categoriesAdapter)
        // Handling OnLick
        categoriesAdapter.setOnItemClickListener { id, title ->
            val action = HomeFragmentDirections.actionToCategory(id, title)
            findNavController().navigate(action)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle  Methods
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}