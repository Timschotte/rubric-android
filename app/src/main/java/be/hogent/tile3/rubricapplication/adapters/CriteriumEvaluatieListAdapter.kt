package be.hogent.tile3.rubricapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.databinding.ListItemNormaalCriteriumEvaluatieBinding
import be.hogent.tile3.rubricapplication.model.Niveau

// TODO: dependency injection
class CriteriumEvaluatieListAdapter(val clickListener: CriteriumEvaluatieListListener):
    ListAdapter<Niveau, CriteriumEvaluatieListAdapter.ViewHolder>(CriteriumNiveauListDiffCallback()){

    // Create the contentView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // Fill the item with content and set a click listener.
    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor (val binding: ListItemNormaalCriteriumEvaluatieBinding)
        : RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemNormaalCriteriumEvaluatieBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Niveau, clickListener: CriteriumEvaluatieListListener) {
            binding.niveau = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }
}

class CriteriumNiveauListDiffCallback: DiffUtil.ItemCallback<Niveau>(){
    override fun areItemsTheSame(oldItem: Niveau, newItem: Niveau): Boolean {
        return oldItem.niveauId == newItem.niveauId
    }

    override fun areContentsTheSame(oldItem: Niveau, newItem: Niveau): Boolean {
        return oldItem == newItem
    }
}

class CriteriumEvaluatieListListener(val clickListener: (niveauId: String) -> Unit){
    fun onClick(niveau: Niveau) = clickListener(niveau.niveauId)
}