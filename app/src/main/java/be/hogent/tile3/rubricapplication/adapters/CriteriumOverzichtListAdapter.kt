package be.hogent.tile3.rubricapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.databinding.ListItemCriteriumVanRubricBinding
import be.hogent.tile3.rubricapplication.model.Criterium
import kotlinx.android.synthetic.main.list_item_criterium_van_rubric.view.*

const val OMSCHRIJVING_MAX_LIJNEN: Int = 3
/**
 * [RecyclerView] adapter for a [Criterium]-list
 * @param clickListener [CriteriaListListener] for handling user clicks in [RecyclerView]
 * @property positieGeselecteerdCriterium Initial value is -1 indicating the starting situation
 * @see CriteriumOverzichtListAdapter.ViewHolder
 * @see CriteriaListDiffCallback
 */
class CriteriumOverzichtListAdapter(val clickListener: CriteriaListListener):
    ListAdapter<Criterium, CriteriumOverzichtListAdapter.ViewHolder>(CriteriaListDiffCallback()){

    private var positieGeselecteerdCriterium: Int = -1

    /**
     * Function for setting [positieGeselecteerdCriterium]
     * @param positieGeselecteerdCriterium [Int] New value for [positieGeselecteerdCriterium]
     */
    fun stelPositieGeselecteerdCriteriumIn(positieGeselecteerdCriterium: Int){
        this.positieGeselecteerdCriterium = positieGeselecteerdCriterium
    }
    /**
     * Function for creating a custom [ViewHolder]
     * @param parent [ViewGroup]
     * @param viewType [Int]
     * @return [ViewHolder] object
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }
    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the itemView to reflect the item at the given position.
     * Arranges styling for the [ViewHolder] depending if the [Criterium] is selected or not.
     * @param holder [ViewHolder]
     * @param position [Int]
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(getItem(position)!!, position, clickListener)
        if(position == positieGeselecteerdCriterium)
            holder.pasOpmaakGeselecteerdToe()
        else
            holder.verwijderOpmaakGeselecteerd()
    }
    /**
     * Custom [ViewHolder] class
     * @constructor creates the ViewHolder
     * @property binding [ListItemCriteriumVanRubricBinding] Binding object
     * @see RecyclerView.ViewHolder
     */
    class ViewHolder private constructor (val binding: ListItemCriteriumVanRubricBinding)
        : RecyclerView.ViewHolder(binding.root){
        companion object {
            /**
             * Inflates the ItemView layout into the [ViewHolder]
             * @param parent [ViewGroup]
             * @return [ViewHolder]
             */
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCriteriumVanRubricBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
        /**
         * Binds the [item] ([Criterium]), including [clickListener] ([CriteriaListListener]) to the [ViewHolder] on a given [position]
         * When [ViewHolder] is recycled, the 'criteriumOmschrijving' TextView maxLines is resetted to [OMSCHRIJVING_MAX_LIJNEN]
         * @param item [Criterium]
         * @param position [Int]
         * @param clickListener [CriteriaListListener]
         */
        fun bind(item: Criterium, position: Int, clickListener: CriteriaListListener) {
            binding.criterium = item
            binding.positie = position
            binding.clickListener = clickListener
            binding.criteriumOmschrijvingTextView.maxLines = OMSCHRIJVING_MAX_LIJNEN
            binding.criteriumOmschrijvingTextView.setOnLongClickListener {
                val numLines = it.criteriumOmschrijvingTextView.maxLines
                if(numLines == OMSCHRIJVING_MAX_LIJNEN)
                    it.criteriumOmschrijvingTextView.maxLines = Int.MAX_VALUE
                else
                    it.criteriumOmschrijvingTextView.maxLines = OMSCHRIJVING_MAX_LIJNEN
                true
            }
            binding.executePendingBindings()
        }
        /**
         * Function that assigns a style to the selected [Criterium].
         * Changes background and divider visibility.
         */
        fun pasOpmaakGeselecteerdToe(){
            this.itemView.criteriumLayout.background = ContextCompat
                .getDrawable(this.itemView.context, R.drawable.list_item_geselecteerd_background)
            this.itemView.divider.visibility = View.INVISIBLE
        }
        /**
         * Function that resets the style of a non-selected [Criterium]
         * Resets background and divider visibility
         */
        fun verwijderOpmaakGeselecteerd(){
            this.itemView.background = null
            this.itemView.divider.visibility = View.VISIBLE
        }
    }
}
/**
 * The RelationsListDiffCallback provides 2 functions to verify, when the list of Relations in the
 * RelationsList is updated, which Relations are new and which Relations' contents have changed.
 * This allows the RecyclerView to be more efficient in handling updated Relationslists.
 * @see DiffUtil.ItemCallback
 */
class CriteriaListDiffCallback: DiffUtil.ItemCallback<Criterium>(){
    /**
     * Function to verify if two [Criterium] objects are the same by comparing id's
     * @param oldItem [Criterium]
     * @param newItem [Criterium]
     * @return [Boolean]
     */
    override fun areItemsTheSame(oldItem: Criterium, newItem: Criterium): Boolean {
        return oldItem.criteriumId == newItem.criteriumId
    }
    /**
     * Function to verify if contents of two [Criterium] objects are the same. This is used to verify if an item has changed.
     * @param oldItem [Criterium]
     * @param newItem [Criterium]
     * @return [Boolean]
     */
    override fun areContentsTheSame(oldItem: Criterium, newItem: Criterium): Boolean {
        return oldItem == newItem
    }
}
/**
 * ClickListener class for handling user onClick event on a [Criterium]-item in the [RecyclerView]
 * @property clickListener clickListener that returns criteriumId for a clicked [Criterium]
 */
class CriteriaListListener(val clickListener: (criteriumId: Long, position: Int) -> Unit){
    fun onClick(criterium: Criterium, position: Int) = clickListener(criterium.criteriumId, position)
}