package ir.mahan.ghabchin.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.mahan.ghabchin.data.model.home.ResponsePhotos.ResponsePhotosItem
import ir.mahan.ghabchin.data.model.search.ResponseSearch
import ir.mahan.ghabchin.databinding.ItemImagesCategoriesBinding
import ir.mahan.ghabchin.util.loadImage
import javax.inject.Inject

class searchAdapter @Inject constructor() : PagingDataAdapter<ResponseSearch.Result,
        searchAdapter.ViewHolder>(differCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemImagesCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: searchAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.setIsRecyclable(false)
    }

    inner class ViewHolder(private val binding: ItemImagesCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseSearch.Result) {
            binding.apply {
                //Image
                item.urls?.regular?.let {
                    image.loadImage(it)
                }
                //Click
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item.id!!)
                    }
                }
            }
        }
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<ResponseSearch.Result>() {
            override fun areItemsTheSame(
                oldItem: ResponseSearch.Result,
                newItem: ResponseSearch.Result
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ResponseSearch.Result,
                newItem: ResponseSearch.Result
            ): Boolean {
                return oldItem == newItem

            }
        }
    }
}