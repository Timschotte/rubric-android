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
import android.annotation.SuppressLint
import java.sql.Time
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


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
        //val l = LocalDateTime.parse(item.datumTijdLaatsteWijziging, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
        val df1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val df2 = SimpleDateFormat("yyyy-MM-dd")
        val result = df1.parse(item.datumTijdLaatsteWijziging)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        text = ("Laatste wijziging op:  " + df2.format(result))
    }
}

@BindingAdapter("rubric_userimage")
fun ImageView.setImageResource(item: Rubric){
    item?.let {
        setImageResource(be.hogent.tile3.rubricapplication.R.drawable.ic_user)
    }
}

@SuppressLint("SimpleDateFormat")
fun convertDateToLong(date : LocalDateTime): Long {
    val df = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
    val toParse = date.toString()
    return df.parse(toParse).time
}
@RequiresApi(Build.VERSION_CODES.O)
fun convertLongToDate(time : Long) :LocalDateTime {
    val date = Date(time)
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}