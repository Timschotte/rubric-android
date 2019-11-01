package be.hogent.tile3.rubricapplication.adapters

import android.widget.NumberPicker
import androidx.databinding.BindingAdapter
import be.hogent.tile3.rubricapplication.model.Niveau

@BindingAdapter("numberPickerValue")
fun NumberPicker.setInitialValue(geselecteerdNiveau: Niveau?){
    geselecteerdNiveau?.let{
        value = ((geselecteerdNiveau.bovengrens  + geselecteerdNiveau.ondergrens) / 2)
    }
}