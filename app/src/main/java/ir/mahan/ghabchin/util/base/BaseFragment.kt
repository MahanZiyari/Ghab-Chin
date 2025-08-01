package ir.mahan.ghabchin.util.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import ir.mahan.ghabchin.R
import ir.mahan.ghabchin.databinding.FragmentSplashBinding
import ir.mahan.ghabchin.util.network.NetworkChecker
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////

    protected abstract val bindingInflater: (layoutInflater: LayoutInflater) -> T
    private var _binding: T? = null
    protected val binding get() = requireNotNull(_binding)


    @Inject
    lateinit var networkChecker: NetworkChecker

    //Other
    var isNetworkAvailable = true

    ///////////////////////////////////////////////////////////////////////////
    // Abstract  Functions
    ///////////////////////////////////////////////////////////////////////////

    abstract fun T.setupUI()

    abstract fun setupObservers()


    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle Methods
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Check network
        lifecycleScope.launch {
            networkChecker.checkNetwork().collect {
                isNetworkAvailable = it
                if (!it) {
                    Toast.makeText(requireContext(), R.string.checkYourNetwork, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }// Template Method
        binding.setupUI()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}