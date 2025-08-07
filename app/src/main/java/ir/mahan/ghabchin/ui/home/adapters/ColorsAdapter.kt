package ir.mahan.ghabchin.ui.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.mahan.ghabchin.data.model.home.ColorTone
import ir.mahan.ghabchin.databinding.ItemColorsBinding
import ir.mahan.ghabchin.util.base.BaseDiffUtils
import javax.inject.Inject

class ColorsAdapter @Inject constructor() : RecyclerView.Adapter<ColorsAdapter.ViewHolder>() {

    private var items = emptyList<ColorTone>()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ItemColorsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemColorsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ColorTone) {
            binding.apply {
                //Color
                color.setBackgroundColor(ContextCompat.getColor(context, item.color))
                //Click
                root.setOnClickListener {
                    onItemClickListener?.let {
                        val colorName = context.getString(item.colorName)
                        it(colorName)
                    }
                }
            }
        }
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<ColorTone>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}