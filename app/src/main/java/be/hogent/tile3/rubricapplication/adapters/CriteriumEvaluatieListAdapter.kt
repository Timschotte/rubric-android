package be.hogent.tile3.rubricapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.databinding.ListItemNormaalCriteriumEvaluatieBinding
import be.hogent.tile3.rubricapplication.model.Niveau
import kotlinx.android.synthetic.main.list_item_normaal_criterium_evaluatie.view.*

/**
 * [RecyclerView] adapter for a CriteriumEvaluatie [Niveau]-list
 * @param clickListener [CriteriumEvaluatieListListener] for handling user clicks in [RecyclerView]
 * @property positieGeselecteerdNiveau Initial value is -1 indicating the starting situation and therefor voldoendeNiveau ([Niveau]) must be marked
 * @see ListAdapter
 * @see CriteriumNiveauListDiffCallback
 */
class CriteriumEvaluatieListAdapter(val clickListener: CriteriumEvaluatieListListener):
    ListAdapter<Niveau, CriteriumEvaluatieListAdapter.ViewHolder>(CriteriumNiveauListDiffCallback()){

    private var positieGeselecteerdNiveau: Int = -1

    /**
     * Function for setting [positieGeselecteerdNiveau]
     * @param positieGeselecteerdNiveau [Int] New value for [positieGeselecteerdNiveau]
     */
    fun stelPositieGeselecteerdNiveauIn(positieGeselecteerdNiveau: Int){
        this.positieGeselecteerdNiveau = positieGeselecteerdNiveau
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
     * Arranges styling for the [ViewHolder] depending if the [Niveau] is selected or not.
     * Shows icon indicating voldoendeNiveau
     * @param holder [ViewHolder]
     * @param position [Int]
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(getItem(position)!!, position, clickListener)
        if(position == positieGeselecteerdNiveau)
            holder.pasOpmaakGeselecteerdToe()
        else
            holder.verwijderOpmaakGeselecteerd()
        if(holder.binding.niveau?.volgnummer == 0)
            holder.binding.voldoendeNiveauIcoonImageView.visibility = View.VISIBLE
        else
            holder.binding.voldoendeNiveauIcoonImageView.visibility = View.GONE
    }

    /**
     * Custom [ViewHolder] class
     * @constructor creates the ViewHolder
     * @property binding [ListItemNormaalCriteriumEvaluatieBinding] Binding object
     * @see RecyclerView.ViewHolder
     */
    class ViewHolder private constructor (val binding: ListItemNormaalCriteriumEvaluatieBinding)
        : RecyclerView.ViewHolder(binding.root){
        companion object {
            /**
             * Inflates the ItemView layout into the [ViewHolder]
             * @param parent [ViewGroup]
             * @return [ViewHolder]
             */
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemNormaalCriteriumEvaluatieBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
        /**
         * Binds the [item] ([Niveau]), including [clickListener] ([CriteriumEvaluatieListListener]) to the [ViewHolder] on a given [position]
         * @param item [Niveau]
         * @param position [Int]
         * @param clickListener [CriteriumEvaluatieListListener]
         */
        fun bind(item: Niveau, position: Int, clickListener: CriteriumEvaluatieListListener) {
            binding.niveau = item
            binding.positie = position
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        /**
         * Function that assigns a style to the selected [Niveau].
         * Changes width, height, background and translationZ.
         */
        fun pasOpmaakGeselecteerdToe(){
            this.itemView.criteriumNiveauLayout.layoutParams.width =
                this.itemView.context.resources.getDimension(R.dimen.criterium_evaluatie_list_item_geselecteerd_breedte).toInt()
            this.itemView.criteriumNiveauLayout.layoutParams.height =
                this.itemView.context.resources.getDimension(R.dimen.criterium_evaluatie_list_item_geselecteerd_hoogte).toInt()
            this.itemView.criteriumNiveauLayout.background=
                ContextCompat.getDrawable(this.itemView.context,
                    R.drawable.list_item_geselecteerd_background)
            this.itemView.criteriumNiveauLayout.translationZ = this.itemView.context.resources.getDimension(R.dimen.list_item_geselecteerd_z)
        }
        /**
         * Function that resets the style of a non-selected [Niveau]
         * Resets width, height, background and translationZ
         */
        fun verwijderOpmaakGeselecteerd(){
            this.itemView.criteriumNiveauLayout.layoutParams.width =
                this.itemView.context.resources.getDimension(R.dimen.criterium_evaluatie_list_item_normaal_breedte).toInt()
            this.itemView.criteriumNiveauLayout.layoutParams.height =
                this.itemView.context.resources.getDimension(R.dimen.criterium_evaluatie_list_item_normaal_hoogte).toInt()
            this.itemView.criteriumNiveauLayout.background=
                ContextCompat.getDrawable(this.itemView.context,
                    R.drawable.list_item_normaal_background)
            this.itemView.criteriumNiveauLayout.translationZ = 0.0F
        }
    }
}

/**
 * The RelationsListDiffCallback provides 2 functions to verify, when the list of Relations in the
 * RelationsList is updated, which Relations are new and which Relations' contents have changed.
 * This allows the RecyclerView to be more efficient in handling updated Relationslists.
 * @see DiffUtil.ItemCallback
 */
class CriteriumNiveauListDiffCallback: DiffUtil.ItemCallback<Niveau>(){
    /**
     * Function to verify if two [Niveau] objects are the same by comparing id's
     * @param oldItem [Niveau]
     * @param newItem [Niveau]
     * @return [Boolean]
     */
    override fun areItemsTheSame(oldItem: Niveau, newItem: Niveau): Boolean {
        return oldItem.niveauId == newItem.niveauId
    }
    /**
     * Function to verify if contents of two [Niveau] objects are the same. This is used to verify if an item has changed.
     * @param oldItem [Niveau]
     * @param newItem [Niveau]
     * @return [Boolean]
     */
    override fun areContentsTheSame(oldItem: Niveau, newItem: Niveau): Boolean {
        return oldItem == newItem
    }
}

/**
 * ClickListener class for handling user onClick event on a [Niveau]-item in the [RecyclerView]
 * @property clickListener clickListener that returns niveauId for a clicked [Niveau]
 */
class CriteriumEvaluatieListListener(val clickListener: (niveauId: Long, position: Int) -> Unit){
    fun onClick(niveau: Niveau, position: Int) = clickListener(niveau.niveauId, position)
}