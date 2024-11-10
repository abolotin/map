package ru.netology.nmedia.adapters

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ListItemBinding
import ru.netology.nmedia.dto.GeoPoint

class GeoPointViewHolder(
    private val binding: ListItemBinding,
    private val onClickListener: OnClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(geoPoint: GeoPoint) {
        with(binding) {
            titleLabel.text = geoPoint.title
            coordinatesLabel.text = "${geoPoint.lat}, ${geoPoint.lng}"
            listItem.setOnClickListener {
                onClickListener(geoPoint)
            }
        }
    }
}