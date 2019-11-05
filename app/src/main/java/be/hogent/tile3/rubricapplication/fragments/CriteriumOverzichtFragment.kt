package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.adapters.CriteriaListListener
import be.hogent.tile3.rubricapplication.adapters.CriteriumOverzichtListAdapter
import be.hogent.tile3.rubricapplication.databinding.FragmentCriteriumOverzichtBinding
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.animation.ValueAnimator


/**
 * A simple [Fragment] subclass.
 */
class CriteriumOverzichtFragment : Fragment() {

    private var criteriumOverzichtViewModel: CriteriumOverzichtViewModel? = null

    private var overzichtPaneelIngeklapt = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCriteriumOverzichtBinding>(
            inflater,
            R.layout.fragment_criterium_overzicht,
            container,
            false
        )

        criteriumOverzichtViewModel =
            ViewModelProviders.of(this).get(CriteriumOverzichtViewModel::class.java)

        val adapter =
            CriteriumOverzichtListAdapter(CriteriaListListener { criteriumId, positie ->
                Log.i("CriteriumOverzichtFrag","Geklikt op criterium met id " + criteriumId + "en positie " + positie)
                criteriumOverzichtViewModel?.onCriteriumClicked(criteriumId, positie)
            })

        binding.rubricCriteriaListRecycler.adapter = adapter

        criteriumOverzichtViewModel?.rubricCriteria?.observe(viewLifecycleOwner, Observer{
            Log.i("CriteriumOverzichtFrag", "New rubricCriteria list received, size: " + it?.size)
            it?.let{
                adapter.submitList(it)
            }
        })

        criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.observe(viewLifecycleOwner, Observer{
            it?.let{
                adapter.stelPositieGeselecteerdCriteriumIn(it)
                adapter.notifyDataSetChanged()
            }
        })

        binding.klapInKlapUitButton.setOnClickListener {
            criteriumOverzichtViewModel?.onKlapInKlapUitButtonClicked()
        }

        criteriumOverzichtViewModel?.overzichtPaneelUitgeklapt?.observe(viewLifecycleOwner,
            Observer{ overzichtPaneelUitgeklapt: Boolean ->
            val animOverzichtBalk = ObjectAnimator.ofFloat(
                binding.criteriumEvaluatieOverzichtBalk,
                "translationX",
                binding.criteriumEvaluatieOverzichtBalk.translationX,
                if(!overzichtPaneelUitgeklapt)
                binding.criteriumEvaluatieOverzichtBalk.translationX + resources
                    .getDimensionPixelOffset(R.dimen.criteria_overzicht_translationX)
                    .toFloat()
                else
                    0.0F
            )

            val animCriteriumEvaluatieFramePositie = ObjectAnimator.ofFloat(
                binding.criteriumEvaluatieFragmentContainer,
                "translationX",
                binding.criteriumEvaluatieFragmentContainer.translationX,
                if(!overzichtPaneelUitgeklapt)
                    binding.criteriumEvaluatieFragmentContainer.translationX + resources
                        .getDimensionPixelOffset(R.dimen.criteria_overzicht_translationX)
                        .toFloat()
                else
                    0.0F
            )

            val animCriteriumEvaluatieFrameBreedte = ValueAnimator.ofInt(
                binding.criteriumEvaluatieFragmentContainer.measuredWidth,
                if(!overzichtPaneelUitgeklapt)
                    binding.criteriumOverzichtFragmentWrapper.width - resources
                        .getDimensionPixelOffset(R.dimen.criteria_overzicht_ingeklapt_breedte)
                else
                    binding.criteriumOverzichtFragmentWrapper.width + resources
                    .getDimensionPixelOffset(R.dimen.criteria_overzicht_translationX) - resources
                        .getDimensionPixelOffset(R.dimen.criteria_overzicht_ingeklapt_breedte)
            )

            animCriteriumEvaluatieFrameBreedte.addUpdateListener { valueAnimator ->
                val animWaarde = valueAnimator.animatedValue as Int
                val layoutParams = binding.criteriumEvaluatieFragmentContainer.layoutParams
                layoutParams.width = animWaarde
                binding.criteriumEvaluatieFragmentContainer.layoutParams = layoutParams
            }

            val set = AnimatorSet()
            set.duration = 500L
            set.playTogether(animOverzichtBalk,
                animCriteriumEvaluatieFramePositie,
                animCriteriumEvaluatieFrameBreedte)
            set.start()

            if(overzichtPaneelUitgeklapt)
                (binding.klapInKlapUitButton as ImageButton).setImageResource(android.R.drawable.ic_media_previous)
            else
                (binding.klapInKlapUitButton as ImageButton).setImageResource(android.R.drawable.ic_media_next)

            binding.criteriumEvaluatieFragmentContainer.requestLayout()
        })

        binding.setLifecycleOwner(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val criteriumEvaluatieFragment = CriteriumEvaluatieFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.criterium_evaluatie_fragment_container, criteriumEvaluatieFragment).commit()
    }





}
