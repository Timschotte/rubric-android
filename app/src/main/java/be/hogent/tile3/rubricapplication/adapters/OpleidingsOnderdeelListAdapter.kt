package be.hogent.tile3.rubricapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.databinding.OpleidingTextViewBinding

class OpleidingsOnderdeelListAdapter(val clickListener: OpleidingsOnderdeelListener): ListAdapter<OpleidingsOnderdeel, OpleidingsOnderdeelListAdapter.OpleidingViewHolder>(OpleidingsOnderdeelDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpleidingViewHolder {
        Log.i("OpleidingsOnderdeelLA", "Creating viewholder...")
        return OpleidingViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: OpleidingViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class OpleidingViewHolder private constructor(val binding: OpleidingTextViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OpleidingsOnderdeel, clickListener: OpleidingsOnderdeelListener) {
            binding.opleidingsOnderdeel = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): OpleidingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OpleidingTextViewBinding.inflate(layoutInflater, parent, false)
                return OpleidingViewHolder(binding)
            }
        }
    }
}


class OpleidingsOnderdeelDiffCallback: DiffUtil.ItemCallback<OpleidingsOnderdeel>() {
    override fun areItemsTheSame(oldItem: OpleidingsOnderdeel, newItem: OpleidingsOnderdeel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OpleidingsOnderdeel, newItem: OpleidingsOnderdeel): Boolean {
        return oldItem == newItem
    }
}

class OpleidingsOnderdeelListener(val clickListener: (opleidingsOnderdeelId: Long) -> Unit){
    fun onClick(opleidingsOnderdeel: OpleidingsOnderdeel) = clickListener(opleidingsOnderdeel.id)
}

