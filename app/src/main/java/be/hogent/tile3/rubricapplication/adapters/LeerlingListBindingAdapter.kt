package be.hogent.tile3.rubricapplication.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.model.Student
import java.text.DateFormat
import java.text.SimpleDateFormat

@BindingAdapter("studentNaam")
fun TextView.setStudentNaam(item: Student?){
    item?.let{
        if(item.studentAchternaam != null && item.studentVoornaam != null) {
            text = item.studentAchternaam + " " + item.studentAchternaam
        }else{
            text = item.studentNaam
        }
    }
}
@BindingAdapter("evaluatie_studentNaam")
fun TextView.setEvaluatieStudentNaam(item: Student?){
    item?.let{
        if(item.studentAchternaam != null && item.studentVoornaam != null) {
            text = resources.getString(R.string.evaluatie)+" "+item.studentAchternaam + " " + item.studentAchternaam
        }else{
            text = resources.getString(R.string.evaluatie)+" "+item.studentNaam
        }
    }
}
@BindingAdapter("studentGeboortedatum")
fun TextView.setStudentGeboortedatum(item: Student?){
    item?.let{
        if(it.studentGeboortedatum != null){
            text = "("+item.studentGeboortedatum+")"
        }else{
            text = "(Geboortedatum niet gekend)"
        }
    }
}
@BindingAdapter("studentNummer")
fun TextView.setStudentNummer(item: Student?){
    item?.let{
        text = item.studentenNr
    }
}