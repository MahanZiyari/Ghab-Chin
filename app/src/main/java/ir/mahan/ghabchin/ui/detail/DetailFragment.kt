package ir.mahan.ghabchin.ui.detail

import academy.nouri.rotateview.RotateView
import android.Manifest
import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.innfinity.permissionflow.lib.requestPermissions
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.ghabchin.R
import ir.mahan.ghabchin.databinding.FragmentDetailBinding
import ir.mahan.ghabchin.util.IMAGE_MIME_TYPE
import ir.mahan.ghabchin.util.JPG
import ir.mahan.ghabchin.util.base.BaseFragment
import ir.mahan.ghabchin.util.isVisible
import ir.mahan.ghabchin.util.network.Wrapper
import ir.mahan.ghabchin.util.setStatusBarIconsColor
import ir.mahan.ghabchin.util.showSnackBar
import ir.mahan.ghabchin.viewmodel.DetailViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    ///////////////////////////////////////////////////////////////////////////
    // properties
    ///////////////////////////////////////////////////////////////////////////
    override val bindingInflater: (LayoutInflater) -> FragmentDetailBinding
        get() = FragmentDetailBinding::inflate

    // Viewmodel
    private val viewModel: DetailViewModel by viewModels()

    // Args
    private val args: DetailFragmentArgs by navArgs()

    // Other
    @Inject
    lateinit var wallpaperManager: WallpaperManager

    @Inject
    lateinit var downloadManager: DownloadManager
    private lateinit var imageBitmap: Bitmap
    private lateinit var rotateView: RotateView
    private var isEnabledRotateView = false
    private var downloadID = 0L


    ///////////////////////////////////////////////////////////////////////////
    // UI Functions
    ///////////////////////////////////////////////////////////////////////////

    override fun FragmentDetailBinding.setupUI() {
        //Set color for status bar icons
        requireActivity().setStatusBarIconsColor(false)
        setWallpaperImg.setOnClickListener {
            if (::imageBitmap.isInitialized.not()) return@setOnClickListener
            wallpaperManager.setBitmap(imageBitmap)
            setWallpaperImg.setImageResource(R.drawable.check)
        }
    }

    override fun setupObservers() {
        observePhoto()
    }

    private fun observePhoto() = binding.apply {
        viewModel.photoDetails.observe(viewLifecycleOwner) {
            when (it) {
                is Wrapper.Loading -> {
                    loading.isVisible(true, container)
                }

                is Wrapper.Success -> {
                    it.data?.let {
                        setupRotateView(it.urls!!.regular!!)
                        activeRotateView()
                        // Handling Download
                        downloadImg.setOnClickListener { view ->
                            requestPermission()
                            it.urls.full?.let { url ->
                                downloadImage(url, it.slug!!)
                            }
                        }
                        // Info
                        infoImg.setOnClickListener { _ ->
                            val direction = DetailFragmentDirections.actionDetailToInfo(it)
                            findNavController().navigate(direction)
                        }
                    }

                }

                is Wrapper.Error -> {
                    loading.isVisible(false, container)
                    root.showSnackBar(it.message!!)
                }
            }
        }
    }

    private fun setupRotateView(imageUrl: String) = lifecycleScope.launch {
        binding.apply {
            val loader = ImageLoader(requireContext())
            val request = ImageRequest.Builder(requireContext())
                .data(imageUrl)
                .allowHardware(true)
                .build()

            val imageDrawable = (loader.execute(request) as SuccessResult).drawable
            imageBitmap = (imageDrawable as BitmapDrawable).bitmap
            //Delay
            delay(200)
            //Rotate view
            rotateView = RotateView.getInstance(requireContext())!!
            rotateView.apply {
                setImageWithBitmap(coverImg, imageBitmap)
                center()
            }
            //Hide loading
            loading.isVisible(false, container)
        }
    }

    private fun activeRotateView() = binding.apply {
        rotateViewImg.apply {
            setOnClickListener {
                if (rotateView.isDeviceSupported()) {
                    isEnabledRotateView = if (isEnabledRotateView.not()) {
                        rotateView.registerListener()
                        setBackgroundResource(R.drawable.bg_circle_azure_alpha_selected)
                        true
                    } else {
                        rotateView.unRegisterListener()
                        setBackgroundResource(R.drawable.bg_circle_azure_alpha)
                        false
                    }
                } else {
                    root.showSnackBar(getString(R.string.notSupportRotateView))
                }
            }
        }

    }

    private fun requestPermission() {
        lifecycleScope.launch {
            requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).collect {}
        }
    }

    private fun downloadImage(imageURL: String, fileName: String) {
        val downloadUri = imageURL.toUri()
        val downlodRequest = DownloadManager.Request(downloadUri)
        downlodRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setTitle(fileName)
            .setMimeType(IMAGE_MIME_TYPE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                File.separator + fileName + JPG
            )

        downloadID = downloadManager.enqueue(downlodRequest)
        //Show progress bar
        binding.downloadLoading.apply {
            isVisible = true
            isIndeterminate = true
        }
    }

    private fun setReceiverForDownloadCompletion(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadID) {
                    binding.apply {
                        downloadLoading.isVisible = false
                        downloadImg.setImageResource(R.drawable.check)
                    }
                }
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle  Methods
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.let {
            viewModel.getPhotoDetailsBy(it.id)
        }
        ContextCompat.registerReceiver(
            requireContext(), setReceiverForDownloadCompletion(), IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE
            ), ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onStop() {
        super.onStop()
        if (this::rotateView.isInitialized)
            rotateView.unRegisterListener()
    }
}