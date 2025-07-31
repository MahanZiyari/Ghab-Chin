package ir.mahan.ghabchin.util.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import ir.mahan.ghabchin.R
import ir.mahan.ghabchin.util.network.NetworkChecker
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<T : ViewBinding> : Fragment() {


    @Inject
    lateinit var networkChecker: NetworkChecker

    //Other
    var isNetworkAvailable = true


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
        }
    }

}