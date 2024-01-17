package ru.netology.maps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.maps.R
import ru.netology.maps.databinding.CardPlaceMarkBinding
import ru.netology.maps.dto.PlaceMark

interface OnInteractionListener {
    fun remove(placeMark: PlaceMark)
    fun edit(placeMark: PlaceMark)
    fun openCardPost(placeMark: PlaceMark)
}

class PlaceMarkAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<PlaceMark, PlaceMarkVH>(PlaceMarkCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceMarkVH {
        val binding =
            CardPlaceMarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceMarkVH(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PlaceMarkVH, position: Int) {
        val placeMark = getItem(position)
        holder.bind(placeMark)
    }
}

class PlaceMarkVH(
    private val binding: CardPlaceMarkBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(placeMark: PlaceMark) {
        binding.apply {
            name.text = placeMark.name
            longitude.text = placeMark.point.longitude.toString()
            latitude.text = placeMark.point.latitude.toString()

            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.placemark_options)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.remove(placeMark)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.edit(placeMark)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}


class PlaceMarkCallBack : DiffUtil.ItemCallback<PlaceMark>() {
    override fun areItemsTheSame(oldItem: PlaceMark, newItem: PlaceMark): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PlaceMark, newItem: PlaceMark): Boolean {
        return oldItem.name == newItem.name
    }

}
