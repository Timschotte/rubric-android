package be.hogent.tile3.rubricapplication.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.model.Opleiding
import be.hogent.tile3.rubricapplication.databinding.OpleidingTextViewBinding

class OpleidingRecyclerViewAdapter: ListAdapter<Opleiding, OpleidingRecyclerViewAdapter.OpleidingViewHolder>(DiffCallback){

    class OpleidingViewHolder(private var binding: OpleidingTextViewBinding) : RecyclerView.ViewHolder {

    }

    companion object DiffCallback : DiffUtil.ItemCallback<Opleiding>{
        override fun areItemsTheSame(oldItem: Opleiding, newItem: Opleiding): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Opleiding, newItem: Opleiding): Boolean {
            return oldItem.opleidingId == newItem.opleidingId
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OpleidingRecyclerViewAdapter.OpleidingViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(
        holder: OpleidingRecyclerViewAdapter.OpleidingViewHolder,
        position: Int
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
//    var data = listOf<Opleiding>()
//        set(value) {
//            field = value
//            System.out.println(value)
//            notifyDataSetChanged()
//        }
//
//    override fun getItemCount() = data.size
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = data[position]
//        holder.opleiding.setText(item.naam)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        val view = layoutInflater.inflate(R.layout.opleiding_text_view, parent, false)
//        return ViewHolder(view)
//    }
//
//    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        val opleiding: TextView = itemView.findViewById(R.id.opleiding_naam)
//    }

}

