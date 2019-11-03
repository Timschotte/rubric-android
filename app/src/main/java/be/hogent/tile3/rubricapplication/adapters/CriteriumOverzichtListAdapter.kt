package be.hogent.tile3.rubricapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.databinding.ListItemCriteriumVanRubricBinding
import be.hogent.tile3.rubricapplication.model.Criterium

class CriteriumOverzichtListAdapter(val clickListener: CriteriaListListener):
    ListAdapter<Criterium, CriteriumOverzichtListAdapter.ViewHolder>(CriteriaListDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("CriteriumOverzichtLA", "Creating viewholder...")
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        Log.i("CriteriumOverzichtLA", "binding viewholder for position " + position)
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor (val binding: ListItemCriteriumVanRubricBinding)
        : RecyclerView.ViewHolder(binding.root){


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCriteriumVanRubricBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Criterium, clickListener: CriteriaListListener) {
            Log.i("CriteriumOverzichtLA", "Binding criterium " + item.naam)
            binding.criterium = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }
}

/**
 * The RelationsListDiffCallback provides 2 functions to verify, when the list of Relations in the
 * RelationsList is updated, which Relations are new and which Relations' contents have changed.
 * This allows the RecyclerView to be more efficient in handling updated Relationslists.
 */
class CriteriaListDiffCallback: DiffUtil.ItemCallback<Criterium>(){
    override fun areItemsTheSame(oldItem: Criterium, newItem: Criterium): Boolean {
        return oldItem.criteriumId == newItem.criteriumId
    }

    override fun areContentsTheSame(oldItem: Criterium, newItem: Criterium): Boolean {
        return oldItem == newItem
    }
}

class CriteriaListListener(val clickListener: (criteriumId: String) -> Unit){
    fun onClick(criterium: Criterium) = clickListener(criterium.criteriumId)
}