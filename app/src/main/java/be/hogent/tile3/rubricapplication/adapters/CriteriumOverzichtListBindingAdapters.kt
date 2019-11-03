package be.hogent.tile3.rubricapplication.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import be.hogent.tile3.rubricapplication.model.Criterium

@BindingAdapter("criteriumNaam")
fun TextView.setCriteriumNaam(item: Criterium?){
    item?.let{
        text = item.naam
    }
}

@BindingAdapter("criteriumOmschrijving")
fun TextView.setCriteriumOmschrijving(item: Criterium?){
    item?.let{
        text = item.omschrijving
    }
}

@BindingAdapter("criteriumGewicht")
fun TextView.setCriteriumGewicht(item: Criterium?){
    item?.let{
        text = item.gewicht.toString()
    }
}