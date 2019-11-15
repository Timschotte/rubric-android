package be.hogent.tile3.rubricapplication.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import be.hogent.tile3.rubricapplication.model.Student

@BindingAdapter("studentNaam")
fun TextView.setStudentNaam(item: Student?){
    item?.let{
        text = item.studentNaam
    }
}

@BindingAdapter("studentNummer")
fun TextView.setStudentNummer(item: Student?){
    item?.let{
        text = item.studentenNr
    }
}