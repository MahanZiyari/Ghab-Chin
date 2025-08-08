package ir.mahan.ghabchin.ui.search

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
import ir.mahan.ghabchin.databinding.FragmentSearchBinding
import ir.mahan.ghabchin.ui.category.CategoryFragmentArgs
import ir.mahan.ghabchin.util.LoadMoreAdapter
import ir.mahan.ghabchin.util.base.BaseFragment
import ir.mahan.ghabchin.util.setStatusBarIconsColor
import ir.mahan.ghabchin.viewmodel.SearchViewModel
import javax.inject.Inject
import kotlin.compareTo
import kotlin.getValue
import kotlin.text.append

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    ///////////////////////////////////////////////////////////////////////////
    // properties
    ///////////////////////////////////////////////////////////////////////////
    override val bindingInflater: (LayoutInflater) -> FragmentSearchBinding
        get() = FragmentSearchBinding::inflate

    // Args
    private val args: SearchFragmentArgs by navArgs()

    // ViewModel
    private val viewModel: SearchViewModel by viewModels()

    // Adapters
    @Inject
    lateinit var searchAdapter: searchAdapter


    ///////////////////////////////////////////////////////////////////////////
    // UI  Functions
    ///////////////////////////////////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    override fun FragmentSearchBinding.setupUI() {
        //Set color for status bar icons
        requireActivity().setStatusBarIconsColor(true)
        //Back
        backImg.setOnClickListener { findNavController().popBackStack() }
        //Title
        args.let {
            searchTitle.text = "${it.query} ${getString(R.string.photos)}"
        }
        setupAdapter()
        initRecycler()
    }

    private fun setupAdapter() {
        searchAdapter.apply {
            setOnItemClickListener {
                Toast.makeText(requireContext(), "ID: $it", Toast.LENGTH_SHORT).show()
            }
            addLoadStateListener { state ->
                binding.loading.isVisible = state.source.refresh is LoadState.Loading
                binding.searchList.isVisible = state.source.refresh is LoadState.NotLoading
                //Empty
                if (state.source.refresh is LoadState.NotLoading &&
                    state.append.endOfPaginationReached && searchAdapter.itemCount < 1
                ) {
                    binding.emptyLay.isVisible = true
                    binding.searchList.isVisible = false
                } else {
                    binding.emptyLay.isVisible = false
                    binding.searchList.isVisible = true
                }
            }
        }
    }

    private fun initRecycler() = binding.searchList.apply {
        layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        adapter = searchAdapter.withLoadStateFooter(LoadMoreAdapter { searchAdapter.retry() })
    }

    override fun setupObservers() {
        observeSearchPhotos()
    }

    private fun observeSearchPhotos() {
        viewModel.searchPhotos.observe(viewLifecycleOwner) {
            searchAdapter.submitData(lifecycle, it)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle  Methods
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Update Category Id
        args.let {
            viewModel.updateSearchQuery(it.query)
        }
    }

}