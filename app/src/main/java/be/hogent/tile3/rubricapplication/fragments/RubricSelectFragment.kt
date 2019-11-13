package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.databinding.FragmentRubricSelectBinding
import be.hogent.tile3.rubricapplication.ui.RubricSelectViewModel
import be.hogent.tile3.rubricapplication.ui.factories.RubricSelectViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class RubricSelectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRubricSelectBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_rubric_select, container, false)
        val opleidingsOnderdeel = RubricSelectFragmentArgs.fromBundle(arguments!!)

        val viewModelFactory = RubricSelectViewModelFactory(opleidingsOnderdeel.opleidingsOnderdeelId)
        val rubricSelectViewModel = ViewModelProviders.of(this, viewModelFactory).get(RubricSelectViewModel::class.java)

        binding.rubricSelectViewModel = rubricSelectViewModel
        binding.setLifecycleOwner(this)



        return binding.root
    }



}
