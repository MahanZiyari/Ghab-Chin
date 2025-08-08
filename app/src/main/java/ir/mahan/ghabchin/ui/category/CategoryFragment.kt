package ir.mahan.ghabchin.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.ghabchin.R
import ir.mahan.ghabchin.databinding.FragmentCategoryBinding
import ir.mahan.ghabchin.util.LoadMoreAdapter
import ir.mahan.ghabchin.util.base.BaseFragment
import ir.mahan.ghabchin.util.setStatusBarIconsColor
import ir.mahan.ghabchin.viewmodel.CategoryViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    ///////////////////////////////////////////////////////////////////////////
    // properties
    ///////////////////////////////////////////////////////////////////////////
    override val bindingInflater: (LayoutInflater) -> FragmentCategoryBinding
        get() = FragmentCategoryBinding::inflate

    // Args
    private val args: CategoryFragmentArgs by navArgs()

    // ViewModel
    private val viewModel: CategoryViewModel by viewModels()

    // Adapters
    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter


    ///////////////////////////////////////////////////////////////////////////
    // UI Functions
    ///////////////////////////////////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    override fun FragmentCategoryBinding.setupUI() {
        //Set color for status bar icons
        requireActivity().setStatusBarIconsColor(true)
        //Back
        backImg.setOnClickListener { findNavController().popBackStack() }
        //Title
        args.let {
            categoriesTitle.text = "${it.title} ${getString(R.string.photos)}"
        }
        setupCategoryAdapter()
        initRecycler()
    }

    private fun setupCategoryAdapter() {
        categoriesAdapter.apply {
            setOnItemClickListener {
                Toast.makeText(requireContext(), "ID: $it", Toast.LENGTH_SHORT).show()
            }
            addLoadStateListener { state ->
                binding.loading.isVisible = state.source.refresh is LoadState.Loading
                binding.categoriesList.isVisible = state.source.refresh is LoadState.NotLoading
            }
        }
    }

    private fun initRecycler() = binding.categoriesList.apply {
        layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        adapter = categoriesAdapter.withLoadStateFooter(LoadMoreAdapter { categoriesAdapter.retry() })
    }

    override fun setupObservers() {
        observeCategoryPhotos()
    }

    private fun observeCategoryPhotos() {
        viewModel.categoryPhotos.observe(viewLifecycleOwner) {
            categoriesAdapter.submitData(lifecycle, it)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle  Methods
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Update Category Id
        args.let {
            viewModel.updateCategoryId(it.id)
        }
    }

}