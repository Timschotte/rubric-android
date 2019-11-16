package be.hogent.tile3.rubricapplication.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel

@BindingAdapter("opleidingsOnderdeelNaam")
fun TextView.setOpleidingsOnderdeelNaam(item: OpleidingsOnderdeel?){
    item?.let{
        text = item.naam
    }
}