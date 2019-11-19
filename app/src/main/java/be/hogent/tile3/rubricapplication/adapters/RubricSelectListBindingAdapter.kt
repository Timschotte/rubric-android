package be.hogent.tile3.rubricapplication.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import be.hogent.tile3.rubricapplication.model.Rubric

@BindingAdapter("rubricNaam")
fun TextView.setRubricNaam(item: Rubric?){
    item?.let{
        text = item.onderwerp
    }
}