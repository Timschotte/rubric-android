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
import androidx.recyclerview.widget.RecyclerView
import android.util.DisplayMetrics


class CriteriumOverzichtFragment : Fragment() {

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

        val criteriumOverzichtViewModel =
            ViewModelProviders.of(this).get(CriteriumOverzichtViewModel::class.java)

        val adapter =
            CriteriumOverzichtListAdapter(CriteriaListListener { criteriumId, positie ->
                Log.i("CriteriumOverzichtFrag","Geklikt op criterium met rubricId " + criteriumId + "en positie " + positie)
                criteriumOverzichtViewModel?.onCriteriumClicked(criteriumId, positie)
            })

        adapter.registerAdapterDataObserver( object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                var toPosition =
                    criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.value ?: 0
                Log.i(
                    "CriteriumOverzichtFrag",
                    "ItemRangeChanged... start: $positionStart, to: $toPosition"
                )
                if (itemCount > toPosition)
                    binding.rubricCriteriaListRecycler.smoothScrollToPosition(
                        toPosition
                    )
            }
            override fun onItemRangeInserted(
                positionStart: Int,
                itemCount: Int
            ) {
                var toPosition =
                    criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.value ?: 0
                Log.i("CriteriumOverzichtFrag", "onItemRangeInserted() called start: $positionStart, item num: $itemCount, toPosition: $toPosition")
                binding.rubricCriteriaListRecycler.scrollToPosition(
                    criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.value ?: 0)
            }

            override fun onItemRangeRemoved(
                positionStart: Int,
                itemCount: Int
            ) {
                var toPosition =
                    criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.value ?: 0
                Log.i("CriteriumOverzichtFrag", "onItemRangeRemoved() called start: $positionStart, item num: $itemCount, toPosition: $toPosition")
                binding.rubricCriteriaListRecycler.smoothScrollToPosition(toPosition)
            }
        })

        binding.rubricCriteriaListRecycler.adapter = adapter

        criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.observe(viewLifecycleOwner, Observer{
            it?.let{
                Log.i("CriteriumOverzichtFrag", "positie geselecteerd criterium ontvangen: $it")
                adapter.stelPositieGeselecteerdCriteriumIn(it)
                adapter.notifyDataSetChanged()
            }
        })

        criteriumOverzichtViewModel?.rubricCriteria?.observe(viewLifecycleOwner, Observer{
            Log.i("CriteriumOverzichtFrag", "New rubricCriteria list received, size: " + it?.size)
            it?.let{
                adapter.submitList(it)
//                binding.rubricCriteriaListRecycler.smoothScrollToPosition(
//                    criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.value ?: 0)
            }
        })

        binding.klapInKlapUitButton.setOnClickListener {
            criteriumOverzichtViewModel?.onKlapInKlapUitButtonClicked()
        }

        criteriumOverzichtViewModel?.overzichtPaneelUitgeklapt?.observe(viewLifecycleOwner,
            Observer{ overzichtPaneelUitgeklapt: Boolean ->

                val displaymetrics = DisplayMetrics()
                activity!!.windowManager.defaultDisplay.getMetrics(displaymetrics)
                val screenWidth = displaymetrics.widthPixels

                Log.i("CriteriumOverzichtFrag", "OverzichtpaneelUitgeklapt geobserveerd: $overzichtPaneelUitgeklapt")

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

                Log.i("CriteriumOverzichtFrag", "breedte criteriumOverzichtFragmentWrapper (1): ${binding.criteriumOverzichtFragmentWrapper.width}")

                val animCriteriumEvaluatieFrameBreedte = ValueAnimator.ofInt(
                    binding.criteriumEvaluatieFragmentContainer.measuredWidth,
                    if(!overzichtPaneelUitgeklapt)
                        screenWidth - resources.getDimensionPixelOffset(R.dimen.criteria_overzicht_ingeklapt_breedte)
                    else
                        screenWidth - resources.getDimensionPixelOffset(R.dimen.criteria_overzicht_width)
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

                Log.i("CriteriumOverzichtFrag", "breedte criteriumOverzichtFragmentWrapper (2): ${binding.criteriumOverzichtFragmentWrapper.width}")

            if(overzichtPaneelUitgeklapt)
                (binding.klapInKlapUitButton as ImageButton).setImageResource(android.R.drawable.ic_media_previous)
            else
                (binding.klapInKlapUitButton as ImageButton).setImageResource(android.R.drawable.ic_media_next)

            binding.criteriumEvaluatieFragmentContainer.requestLayout()

                Log.i("CriteriumOverzichtFrag", "breedte criteriumOverzichtFragmentWrapper (3): ${binding.criteriumOverzichtFragmentWrapper.width}")
        })

        binding.setLifecycleOwner(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.criterium_evaluatie_fragment_container, CriteriumEvaluatieFragment())
                .commitNow()
        }
    }
}
