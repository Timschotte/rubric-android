package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.databinding.FragmentRubricSelectBinding

/**
 * A simple [Fragment] subclass.
 */
class RubricSelectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRubricSelectBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_rubric_select, container, false)

        val args = RubricSelectFragmentArgs.fromBundle(arguments!!)
        Toast.makeText(context, "${args.opleidingsOnderdeelId}", Toast.LENGTH_LONG).show()

        return binding.root
    }



}
