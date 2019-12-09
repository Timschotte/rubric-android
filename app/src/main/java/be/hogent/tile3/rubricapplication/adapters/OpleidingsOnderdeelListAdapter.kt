package be.hogent.tile3.rubricapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.databinding.OpleidingTextViewBinding
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel

/**
 * [RecyclerView] adapter for an [OpleidingsOnderdeel]-list
 * @param clickListener [OpleidingsOnderdeelListener] for handling user clicks in [RecyclerView]
 * @see OpleidingsOnderdeelListAdapter.OpleidingViewHolder
 * @see CriteriaListDiffCallback
 */
class OpleidingsOnderdeelListAdapter(val clickListener: OpleidingsOnderdeelListener): ListAdapter<OpleidingsOnderdeel, OpleidingsOnderdeelListAdapter.OpleidingViewHolder>(OpleidingsOnderdeelDiffCallback()){
    /**
     * Function for creating a custom [OpleidingViewHolder]
     * @param parent [ViewGroup]
     * @param viewType [Int]
     * @return [OpleidingViewHolder] object
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpleidingViewHolder {
        return OpleidingViewHolder.from(parent)
    }
    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the itemView to reflect the item at the given position.
     * @param holder [OpleidingViewHolder]
     * @param position [Int]
     */
    override fun onBindViewHolder(holder: OpleidingViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }
    /**
     * Custom [OpleidingViewHolder] class
     * @constructor creates the ViewHolder
     * @property binding [OpleidingViewHolder] Binding object
     * @see RecyclerView.ViewHolder
     */
    class OpleidingViewHolder private constructor(val binding: OpleidingTextViewBinding) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the [item] ([OpleidingsOnderdeel]), including [clickListener] ([OpleidingsOnderdeelListener]), to the [OpleidingViewHolder]
         * @param item [OpleidingsOnderdeel]
         * @param clickListener [OpleidingsOnderdeelListener]
         */
        fun bind(item: OpleidingsOnderdeel, clickListener: OpleidingsOnderdeelListener) {
            binding.opleidingsOnderdeel = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object {
            /**
             * Inflates the ItemView layout into the [OpleidingViewHolder]
             * @param parent [ViewGroup]
             * @return [OpleidingViewHolder]
             */
            fun from(parent: ViewGroup): OpleidingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OpleidingTextViewBinding.inflate(layoutInflater, parent, false)
                return OpleidingViewHolder(binding)
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
class OpleidingsOnderdeelDiffCallback: DiffUtil.ItemCallback<OpleidingsOnderdeel>() {
    /**
     * Function to verify if two [OpleidingsOnderdeel] objects are the same by comparing id's
     * @param oldItem [OpleidingsOnderdeel]
     * @param newItem [OpleidingsOnderdeel]
     * @return [Boolean]
     */
    override fun areItemsTheSame(oldItem: OpleidingsOnderdeel, newItem: OpleidingsOnderdeel): Boolean {
        return oldItem.id == newItem.id
    }
    /**
     * Function to verify if contents of two [OpleidingsOnderdeel] objects are the same. This is used to verify if an item has changed.
     * @param oldItem [OpleidingsOnderdeel]
     * @param newItem [OpleidingsOnderdeel]
     * @return [Boolean]
     */
    override fun areContentsTheSame(oldItem: OpleidingsOnderdeel, newItem: OpleidingsOnderdeel): Boolean {
        return oldItem == newItem
    }
}
/**
 * ClickListener class for handling user onClick event on a [OpleidingsOnderdeel]-item in the [RecyclerView]
 * @property clickListener clickListener that returns opleidingsOnderdeelId for a clicked [OpleidingsOnderdeel]
 */
class OpleidingsOnderdeelListener(val clickListener: (opleidingsOnderdeelId: Long) -> Unit){
    fun onClick(opleidingsOnderdeel: OpleidingsOnderdeel) = clickListener(opleidingsOnderdeel.id)
}

