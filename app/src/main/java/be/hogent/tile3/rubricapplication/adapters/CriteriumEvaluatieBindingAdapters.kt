package be.hogent.tile3.rubricapplication.adapters

import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.BindingAdapter
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.model.Student
import kotlinx.android.synthetic.main.leerling_text_view.*

@BindingAdapter("numberPickerValue")
fun NumberPicker.setInitialValue(geselecteerdNiveau: Niveau?){
    geselecteerdNiveau?.let{
        value = ((geselecteerdNiveau.bovengrens  + geselecteerdNiveau.ondergrens) / 2)
    }
}

@BindingAdapter("criterium", "student")
fun TextView.setCriteriumNaamAlsTitel(criterium: Criterium?, student: Student?){
    text = criterium?.naam + " - " + student?.studentNaam
}