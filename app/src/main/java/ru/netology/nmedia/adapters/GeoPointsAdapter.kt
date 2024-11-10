package ru.netology.nmedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ListItemBinding
import ru.netology.nmedia.dto.GeoPoint

typealias OnClickListener = (geoPoint: GeoPoint) -> Unit

class GeoPointsAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<GeoPointViewHolder>() {
    var list = emptyList<GeoPoint>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeoPointViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GeoPointViewHolder(binding = binding, onClickListener = onClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GeoPointViewHolder, position: Int) {
        holder.bind(list[position])
    }
}