package be.hogent.tile3.rubricapplication.adapters

import android.util.Log
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

class CriteriumOverzichtListAdapter(val clickListener: CriteriaListListener):
    ListAdapter<Criterium, CriteriumOverzichtListAdapter.ViewHolder>(CriteriaListDiffCallback()){

    private var positieGeselecteerdCriterium: Int = -1

    fun stelPositieGeselecteerdCriteriumIn(positieGeselecteerdNiveau: Int){
        this.positieGeselecteerdCriterium = positieGeselecteerdNiveau
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(getItem(position)!!, position, clickListener)
        if(position == positieGeselecteerdCriterium)
            holder.pasOpmaakGeselecteerdToe()
        else
            holder.verwijderOpmaakGeselecteerd()
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

        fun bind(item: Criterium, position: Int, clickListener: CriteriaListListener) {
            binding.criterium = item
            binding.positie = position
            binding.clickListener = clickListener
            // zorgen dat bij het opnieuw binden van de viewholders, bv. als de gebruiker een
            // ander criterium in de lijst selecteert, dat de tekst terug inklapt.
            binding.criteriumOmschrijvingTextView.maxLines = OMSCHRIJVING_MAX_LIJNEN
            binding.criteriumOmschrijvingTextView.setOnLongClickListener {
                var numLines = it.criteriumOmschrijvingTextView.maxLines
                if(numLines == OMSCHRIJVING_MAX_LIJNEN)
                    it.criteriumOmschrijvingTextView.maxLines = Int.MAX_VALUE
                else
                    it.criteriumOmschrijvingTextView.maxLines = OMSCHRIJVING_MAX_LIJNEN
                // setOnLongClickListener vereist een Boolean als return om aan te geven dat het
                // event consumed werd
                true
            }
            binding.executePendingBindings()
        }

        fun pasOpmaakGeselecteerdToe(){
            this.itemView.criteriumLayout.background = ContextCompat
                .getDrawable(this.itemView.context, R.drawable.list_item_geselecteerd_background)
            this.itemView.divider.visibility = View.INVISIBLE
        }

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
 */
class CriteriaListDiffCallback: DiffUtil.ItemCallback<Criterium>(){
    override fun areItemsTheSame(oldItem: Criterium, newItem: Criterium): Boolean {
        return oldItem.criteriumId == newItem.criteriumId
    }

    override fun areContentsTheSame(oldItem: Criterium, newItem: Criterium): Boolean {
        return oldItem == newItem
    }
}

class CriteriaListListener(val clickListener: (criteriumId: Long, position: Int) -> Unit){
    fun onClick(criterium: Criterium, position: Int) = clickListener(criterium.criteriumId, position)
}