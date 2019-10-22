package be.hogent.tile3.rubricapplication.adapters

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.databinding.RubricListContentBinding
import be.hogent.tile3.rubricapplication.model.Rubric

class RubricRecyclerViewAdapter(val clickListener: MainRubricListener) : ListAdapter<Rubric,
        RubricRecyclerViewAdapter.ViewHolder>(MainRubricDiffCallback()){

    var data = listOf<Rubric>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        if (position %2 == 1){
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"))
        }

        holder.bind(clickListener,item)
    }

    class ViewHolder private constructor(val binding: RubricListContentBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: MainRubricListener, item: Rubric){
            binding.rubric = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RubricListContentBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

}

class MainRubricDiffCallback: DiffUtil.ItemCallback<Rubric>(){
    override fun areItemsTheSame(oldItem: Rubric, newItem: Rubric): Boolean {
        return oldItem.rubricId == newItem.rubricId
    }

    override fun areContentsTheSame(oldItem: Rubric, newItem: Rubric): Boolean {
        return oldItem == newItem
    }
}

class MainRubricListener(val clickListener: (Rubric) -> Unit){
    fun onClick(rubric: Rubric) = clickListener(rubric)
}