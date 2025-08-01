package ir.mahan.ghabchin.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.ghabchin.R
import ir.mahan.ghabchin.data.model.home.ResponsePhotos
import ir.mahan.ghabchin.databinding.FragmentHomeBinding
import ir.mahan.ghabchin.ui.home.adapters.LatestPhotosAdapter
import ir.mahan.ghabchin.util.SPLASH_DELAY
import ir.mahan.ghabchin.util.base.BaseFragment
import ir.mahan.ghabchin.util.loadImage
import ir.mahan.ghabchin.util.network.Wrapper
import ir.mahan.ghabchin.util.setStatusBarIconsColor
import ir.mahan.ghabchin.util.setupRecyclerview
import ir.mahan.ghabchin.util.showSnackBar
import ir.mahan.ghabchin.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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


    ///////////////////////////////////////////////////////////////////////////
    // UI Functions
    ///////////////////////////////////////////////////////////////////////////
    override fun FragmentHomeBinding.setupUI() {
        requireActivity().setStatusBarIconsColor(false)
    }

    override fun setupObservers() {
        observeLatestPhotos()
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
            Toast.makeText(requireContext(), "ID: $it", Toast.LENGTH_SHORT).show()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle  Methods
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLatestPhotos()
    }

}