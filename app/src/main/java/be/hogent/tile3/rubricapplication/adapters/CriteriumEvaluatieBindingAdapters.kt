package be.hogent.tile3.rubricapplication.adapters

import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.BindingAdapter
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau

@BindingAdapter("numberPickerValue")
fun NumberPicker.setInitialValue(geselecteerdNiveau: Niveau?){
    geselecteerdNiveau?.let{
        value = ((geselecteerdNiveau.bovengrens  + geselecteerdNiveau.ondergrens) / 2)
    }
}

@BindingAdapter("criteriumNaamAlsTitel")
fun TextView.setCriteriumNaamAlsTitel(item: Criterium?){
    item?.let{
        text = item.naam
    }
}