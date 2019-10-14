package be.hogent.tile3.rubricapplication.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import be.hogent.tile3.rubricapplication.R
import kotlinx.android.synthetic.main.niveau_cell.view.*

class NiveauView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : LinearLayout(context, attrs, defStyleAttr) {


    fun setTitel(titel: String){
        this.titel.text = titel
    }

    fun setOmschrijving(omschrijving: String){
        this.omschrijving.text = omschrijving
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.niveau_cell, this, true)
        orientation = VERTICAL
    }
}