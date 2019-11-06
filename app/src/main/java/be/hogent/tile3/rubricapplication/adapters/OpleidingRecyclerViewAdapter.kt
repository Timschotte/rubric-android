package be.hogent.tile3.rubricapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.model.Opleiding

class OpleidingRecyclerViewAdapter: RecyclerView.Adapter<TextItemViewHolder>(){
    var data = listOf<Opleiding>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.naam
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.opleiding_text_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }
}