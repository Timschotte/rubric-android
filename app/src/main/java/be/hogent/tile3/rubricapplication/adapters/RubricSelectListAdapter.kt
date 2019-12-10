package be.hogent.tile3.rubricapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.databinding.RubricSelectTextViewBinding
import be.hogent.tile3.rubricapplication.model.Rubric

/**
 * [RecyclerView] adapter for a [Rubric]-list
 * @param clickListener [RubricListener] for handling user clicks in [RecyclerView]
 * @see RubricSelectListAdapter.RubricViewHolder
 * @see RubricDiffCallback
 */
class RubricSelectListAdapter(val clickListener: RubricListener): ListAdapter<Rubric, RubricSelectListAdapter.RubricViewHolder>(RubricDiffCallback()){
    /**
     * Function for creating a custom [RubricViewHolder]
     * @param parent [ViewGroup]
     * @param viewType [Int]
     * @return [RubricViewHolder] object
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RubricViewHolder {
        return RubricViewHolder.from(parent)
    }
    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the itemView to reflect the item at the given position.
     * @param holder [RubricViewHolder]
     * @param position [Int]
     */
    override fun onBindViewHolder(holder: RubricViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }
    /**
     * Custom [RubricViewHolder] class
     * @constructor creates the ViewHolder
     * @property binding [RubricSelectTextViewBinding] Binding object
     * @see RecyclerView.ViewHolder
     */
    class RubricViewHolder private constructor(val binding: RubricSelectTextViewBinding) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the [item] ([Rubric]), including [clickListener] ([RubricListener]), to the [RubricViewHolder]
         * @param item [Rubric]
         * @param clickListener [RubricListener]
         */
        fun bind(item: Rubric, clickListener: RubricListener) {
            binding.rubric = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            /**
             * Inflates the ItemView layout into the [RubricViewHolder]
             * @param parent [ViewGroup]
             * @return [RubricViewHolder]
             */
            fun from(parent: ViewGroup): RubricViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RubricSelectTextViewBinding.inflate(layoutInflater, parent, false)
                return RubricViewHolder(binding)
            }
        }
    }
}

/**
 * The RelationsListDiffCallback provides 2 functions to verify, when the list of Relations in the
 * RelationsList is updated, which Relations are new and which Relations' contents have changed.
 * This allows the RecyclerView to be more efficient in handling updated Relationslists.
 * @see DiffUtil.ItemCallback
 */
class RubricDiffCallback: DiffUtil.ItemCallback<Rubric>() {
    /**
     * Function to verify if two [Rubric] objects are the same by comparing id's
     * @param oldItem [Rubric]
     * @param newItem [Rubric]
     * @return [Boolean]
     */
    override fun areItemsTheSame(oldItem: Rubric, newItem: Rubric): Boolean {
        return oldItem.rubricId == newItem.rubricId
    }
    /**
     * Function to verify if contents of two [Rubric] objects are the same. This is used to verify if an item has changed.
     * @param oldItem [Rubric]
     * @param newItem [Rubric]
     * @return [Boolean]
     */
    override fun areContentsTheSame(oldItem: Rubric, newItem: Rubric): Boolean {
        return oldItem == newItem
    }
}
/**
 * ClickListener class for handling user onClick event on a [Rubric]-item in the [RecyclerView]
 * @property clickListener clickListener that returns rubricId for a clicked [Rubric]
 */
class RubricListener(val clickListener: (rubricId: Long) -> Unit){
    fun onClick(rubric: Rubric) = clickListener(rubric.rubricId)
}
