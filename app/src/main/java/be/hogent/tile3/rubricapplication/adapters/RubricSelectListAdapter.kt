package be.hogent.tile3.rubricapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.databinding.OpleidingTextViewBinding
import be.hogent.tile3.rubricapplication.databinding.RubricSelectTextViewBinding
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.model.Rubric

class RubricSelectListAdapter(val clickListener: RubricListener): ListAdapter<Rubric, RubricSelectListAdapter.RubricViewHolder>(RubricDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RubricViewHolder {
        Log.i("RubricLA", "Creating viewholder...")
        return RubricViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RubricViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class RubricViewHolder private constructor(val binding: RubricSelectTextViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Rubric, clickListener: RubricListener) {
            binding.rubric = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RubricViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RubricSelectTextViewBinding.inflate(layoutInflater, parent, false)
                return RubricViewHolder(binding)
            }
        }
    }
}


class RubricDiffCallback: DiffUtil.ItemCallback<Rubric>() {
    override fun areItemsTheSame(oldItem: Rubric, newItem: Rubric): Boolean {
        return oldItem.rubricId == newItem.rubricId
    }

    override fun areContentsTheSame(oldItem: Rubric, newItem: Rubric): Boolean {
        return oldItem == newItem
    }
}

class RubricListener(val clickListener: (rubricId: Long) -> Unit){
    fun onClick(rubric: Rubric) = clickListener(rubric.rubricId)
}
