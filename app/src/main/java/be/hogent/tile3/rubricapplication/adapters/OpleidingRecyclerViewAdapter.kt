package be.hogent.tile3.rubricapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.model.Opleiding
import be.hogent.tile3.rubricapplication.R

class OpleidingRecyclerViewAdapter: RecyclerView.Adapter<OpleidingRecyclerViewAdapter.ViewHolder>(){
    var data = listOf<Opleiding>()
        set(value) {
            field = value
            System.out.println(value)
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.opleiding.setText(item.naam)
        System.out.println(holder.opleiding.text.toString())
        System.out.println(item.naam)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.opleiding_text_view, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val opleiding: TextView = itemView.findViewById(R.id.opleiding_naam)
    }

}

