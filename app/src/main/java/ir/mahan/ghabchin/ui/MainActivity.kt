package ir.mahan.ghabchin.ui

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.ghabchin.databinding.ActivityMainBinding
import ir.mahan.ghabchin.util.base.BaseActivity
import ir.mahan.ghabchin.util.setStatusBarIconsColor

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////
    //Binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle Methods
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // StatusBar
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
            setStatusBarIconsColor(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}