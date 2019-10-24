package be.hogent.tile3.rubricapplication.utils

import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import be.hogent.tile3.rubricapplication.model.Rubric
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.R



@BindingAdapter("rubric_onderwerp")
fun TextView.setOnderwerp(item: Rubric){
    item?.let {
        text = item.onderwerp
    }
}

@BindingAdapter("rubric_omschrijving")
fun TextView.setOmschrijving(item: Rubric){
    item?.let {
        text = item.omschrijving
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("rubric_laatstewijziging")
fun TextView.setLaatsteWijziging(item: Rubric){
    item?.let {
        val l = LocalDate.parse(item.datumTijdLaatsteWijziging, DateTimeFormatter.ofPattern("ddMMyyyy"))
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        text = ("Laatste wijziging op:  " + formatter.format(l))
    }
}

@BindingAdapter("rubric_userimage")
fun ImageView.setImageResource(item: Rubric){
    item?.let {
        setImageResource(be.hogent.tile3.rubricapplication.R.drawable.ic_user)
    }
}