package be.hogent.tile3.rubricapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.databinding.OpleidingTextViewBinding

//TODO: Recyclerview voor opleidingen fixen

class OpleidingsOnderdeelListAdapter: ListAdapter<OpleidingsOnderdeel, OpleidingViewHolder>(OpleidingsOnderdeelDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpleidingViewHolder {
        Log.i("OpleidingsOnderdeelLA", "Creating viewholder...")
        return OpleidingViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: OpleidingViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class OpleidingViewHolder private constructor(val binding: OpleidingTextViewBinding) : RecyclerView.ViewHolder(binding.root) {
    val opleidingsOnderdeel: TextView = itemView.findViewById(R.id.opleiding_naam)

    fun bind(item: OpleidingsOnderdeel) {
        Log.i("OpleidingsOnderdeelLA", "Binding text..." + item.naam)
        opleidingsOnderdeel.setText(item.naam)
    }

    companion object {
        fun from(parent: ViewGroup): OpleidingViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = OpleidingTextViewBinding.inflate(layoutInflater, parent, false)
            return OpleidingViewHolder(binding)
        }
    }
}

class OpleidingsOnderdeelDiffCallback: DiffUtil.ItemCallback<OpleidingsOnderdeel>() {
    override fun areItemsTheSame(oldItem: OpleidingsOnderdeel, newItem: OpleidingsOnderdeel): Boolean {
        return oldItem.opleidingsOnderdeelId == newItem.opleidingsOnderdeelId
    }

    override fun areContentsTheSame(oldItem: OpleidingsOnderdeel, newItem: OpleidingsOnderdeel): Boolean {
        return oldItem == newItem
    }

}

