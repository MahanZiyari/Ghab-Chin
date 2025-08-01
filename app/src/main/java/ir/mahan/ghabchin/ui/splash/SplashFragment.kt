package ir.mahan.ghabchin.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.ghabchin.databinding.FragmentSplashBinding
import ir.mahan.ghabchin.util.base.BaseFragment
import ir.mahan.ghabchin.util.loadImage
import ir.mahan.ghabchin.util.network.Wrapper
import ir.mahan.ghabchin.util.setStatusBarIconsColor
import ir.mahan.ghabchin.util.showSnackBar
import ir.mahan.ghabchin.viewmodel.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////
    // binding
    override val bindingInflater: (LayoutInflater) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate

    // ViewModel
    val viewModel by viewModels<SplashViewModel>()

    ///////////////////////////////////////////////////////////////////////////
    // Ui Functions
    ///////////////////////////////////////////////////////////////////////////

    override fun FragmentSplashBinding.setupUI() {
        //Set color for status bar icons
        requireActivity().setStatusBarIconsColor(false)
    }

    override fun setupObservers() {
        observeRandomPhoto()
    }

    private fun observeRandomPhoto() = binding.apply {
        viewModel.randomPhoto.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Wrapper.Loading -> {
                    loading.isVisible = true
                }

                is Wrapper.Success -> {
                    response.data?.let {
                        // Load Photo
                        it.urls?.regular?.let { url ->
                            animateBgImg.loadImage(url)
                        }
                        // Navigate with delay
                        lifecycleScope.launch {
                            delay(3500)
                            loading.isVisible = false
                            Toast.makeText(requireContext(), "BOOOOOO", Toast.LENGTH_SHORT).show()
                            // TODO: Navigate to HomeScreen
                        }
                    }
                }

                is Wrapper.Error -> {
                    root.showSnackBar(response.message!!)
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle Methods
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Getting Screen Required Data
        viewModel.getRandomPhoto()
    }

}