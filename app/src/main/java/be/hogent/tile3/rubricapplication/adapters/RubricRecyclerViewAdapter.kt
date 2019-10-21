package be.hogent.tile3.rubricapplication.adapters

import android.os.Build
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.model.Rubric
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class RubricRecyclerViewAdapter() : RecyclerView.Adapter<RubricRecyclerViewAdapter.ViewHolder>(){
    var data = listOf<Rubric>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rubric_list_content,parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.onderwerp.text = item.onderwerp
        holder.omschrijving.text = item.omschrijving
        holder.laatstewijziging.text = LocalDate.parse(item.datumTijdLaatsteWijziging, DateTimeFormatter.ofPattern("ddMMyyyy")).toString()
        holder.userimage.setImageResource(R.drawable.ic_user)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val onderwerp: TextView = itemView.findViewById(R.id.rubric_onderwerp)
        val omschrijving: TextView = itemView.findViewById(R.id.rubric_omschrijving)
        val laatstewijziging: TextView = itemView.findViewById(R.id.rubric_laatstewijziging)
        val userimage: ImageView = itemView.findViewById(R.id.rubric_userimage)
    }

}