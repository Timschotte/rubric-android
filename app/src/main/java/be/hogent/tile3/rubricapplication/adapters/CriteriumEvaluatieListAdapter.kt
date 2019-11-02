package be.hogent.tile3.rubricapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.databinding.ListItemNormaalCriteriumEvaluatieBinding
import be.hogent.tile3.rubricapplication.model.Niveau
import kotlinx.android.synthetic.main.list_item_normaal_criterium_evaluatie.view.*

// TODO: dependency injection
class CriteriumEvaluatieListAdapter(val clickListener: CriteriumEvaluatieListListener):
    ListAdapter<Niveau, CriteriumEvaluatieListAdapter.ViewHolder>(CriteriumNiveauListDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.bind(getItem(position)!!, clickListener)
        if(holder.binding.niveau?.volgnummer == 0){
            holder.pasOpmaakGeselecteerdToe()
        }
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