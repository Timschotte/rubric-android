package be.hogent.tile3.rubricapplication.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.model.Student

@BindingAdapter("criteriumNiveauNaam")
fun TextView.setCriteriumNiveauNaam(item: Niveau?){
    item?.let{
        text = item.titel
    }
}

@BindingAdapter("criteriumNiveauOmschrijving")
fun TextView.setCriteriumNiveauOmschrijving(item: Niveau?){
    item?.let{
        text = item.omschrijving
    }
}