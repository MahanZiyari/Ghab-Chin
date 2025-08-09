package ir.mahan.ghabchin.ui.detail.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ir.mahan.ghabchin.R
import ir.mahan.ghabchin.databinding.FragmentDetaailInfoBinding
import ir.mahan.ghabchin.util.loadImage
import ir.mahan.ghabchin.util.separating

@AndroidEntryPoint
class DetailInfoFragment : BottomSheetDialogFragment() {
    ///////////////////////////////////////////////////////////////////////////
    // properties
    ///////////////////////////////////////////////////////////////////////////
    //Binding
    private var _binding: FragmentDetaailInfoBinding? = null
    private val binding get() = _binding!!


    // Args
    private val args: DetailInfoFragmentArgs by navArgs()


    ///////////////////////////////////////////////////////////////////////////
    // UI Functions
    ///////////////////////////////////////////////////////////////////////////

    fun setupUI() {
        args.data.let { data ->
            //InitViews
            binding.apply {
                titleTxt.text = data.altDescription
                //Description
                if (data.description.isNullOrEmpty().not())
                    descriptionTxt.text = data.description
                else
                    descriptionTxt.isVisible = false
                //User
                data.user?.let { user ->
                    userNameTxt.text = user.name
                    userPhotosCountTxt.text = "${user.totalPhotos} ${getString(R.string.photos)}"
                    user.profileImage?.large?.let { avatar ->
                        userAvatarImg.loadImage(avatar)
                    }
                }
                //Counts
                likesTxt.text = data.likes?.separating()
                viewsTxt.text = data.views?.separating()
                downloadsTxt.text = data.downloads?.separating()
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle Methods
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetaailInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}