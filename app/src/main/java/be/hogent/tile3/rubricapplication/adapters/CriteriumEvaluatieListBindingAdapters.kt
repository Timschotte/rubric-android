package be.hogent.tile3.rubricapplication.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import be.hogent.tile3.rubricapplication.model.Niveau

/**
 * BindingAdapter for [TextView] for a [Niveau].
 * Sets the 'titel' ([Niveau]) in the [TextView].
 * @param item [Niveau]
 * @receiver [TextView]
 */
@BindingAdapter("criteriumNiveauNaam")
fun TextView.setCriteriumNiveauNaam(item: Niveau?){
    item?.let{
        text = item.titel
    }
}
/**
 * BindingAdapter for [TextView] for a [Niveau].
 * Sets the 'omschrijving' ([Niveau]) in the [TextView].
 * @param item [Niveau]
 * @receiver [TextView]
 */
@BindingAdapter("criteriumNiveauOmschrijving")
fun TextView.setCriteriumNiveauOmschrijving(item: Niveau?){
    item?.let{
        text = item.omschrijving
    }
}