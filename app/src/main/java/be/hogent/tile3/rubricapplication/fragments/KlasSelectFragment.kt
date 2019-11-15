package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil

import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.databinding.FragmentKlasSelectBinding

/**
 * A simple [Fragment] subclass.
 */
class KlasSelectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentKlasSelectBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_klas_select, container, false)

        return binding.root


    }


}
