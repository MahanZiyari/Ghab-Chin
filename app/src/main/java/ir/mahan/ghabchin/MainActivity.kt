package ir.mahan.ghabchin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.ghabchin.databinding.ActivityMainBinding
import ir.mahan.ghabchin.util.base.BaseActivity

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}