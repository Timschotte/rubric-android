package be.hogent.tile3.rubricapplication.adapters

import android.widget.NumberPicker
import androidx.databinding.BindingAdapter
import be.hogent.tile3.rubricapplication.model.Niveau

/**
 * BindingAdapter for [NumberPicker] for a selected [Niveau].
 * Sets the initial value of the [NumberPicker]
 * @param geselecteerdNiveau Selected [Niveau]
 * @receiver [NumberPicker]
 */
@BindingAdapter("numberPickerValue")
fun NumberPicker.setInitialValue(geselecteerdNiveau: Niveau?){
    geselecteerdNiveau?.let{
        value = ((geselecteerdNiveau.bovengrens  + geselecteerdNiveau.ondergrens) / 2)
    }
}
