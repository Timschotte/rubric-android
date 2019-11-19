package be.hogent.tile3.rubricapplication.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.adapters.CriteriumEvaluatieListAdapter
import be.hogent.tile3.rubricapplication.adapters.CriteriumEvaluatieListListener
import be.hogent.tile3.rubricapplication.databinding.FragmentCriteriumEvaluatieBinding
import be.hogent.tile3.rubricapplication.ui.CriteriumEvaluatieViewModel
import android.text.InputType
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CriteriumEvaluatieFragment
        : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCriteriumEvaluatieBinding>(
            inflater,
            R.layout.fragment_criterium_evaluatie,
            container,
            false
        )

        this.parentFragment?.let{
            val criteriumOverzichtViewModel
                    = ViewModelProviders.of(it)
                        .get(CriteriumOverzichtViewModel::class.java)


            val criteriumEvaluatieViewModel
                    = ViewModelProviders.of(this, viewModelFactory { CriteriumEvaluatieViewModel() })
                        .get(CriteriumEvaluatieViewModel::class.java)

            binding.criteriumEvaluatieViewModel = criteriumEvaluatieViewModel
            binding.criterium = criteriumOverzichtViewModel.geselecteerdCriterium.value

            criteriumOverzichtViewModel?.geselecteerdCriterium?.observe(viewLifecycleOwner, Observer{
                it?.let{
                    criteriumEvaluatieViewModel.onGeselecteerdCriteriumChanged(
                        it.criteriumId)
                }
            })

            criteriumEvaluatieViewModel.criteriumNiveaus.observe(viewLifecycleOwner, Observer {
                criteriumEvaluatieViewModel.setGeselecteerdCriteriumNiveau()
            })


            criteriumEvaluatieViewModel.geselecteerdCriteriumNiveau.observe(viewLifecycleOwner, Observer{
                    geselecteerdNiveau ->
                // NumberPicker minValue en maxValue niet mogelijk via databinding
                geselecteerdNiveau?.let {
                    binding.scoreNumberPicker.minValue = geselecteerdNiveau.ondergrens
                    binding.scoreNumberPicker.maxValue = geselecteerdNiveau.bovengrens
                }

            })

            val adapter =
                CriteriumEvaluatieListAdapter(CriteriumEvaluatieListListener { niveauId, position ->
                    criteriumEvaluatieViewModel.onNiveauClicked(niveauId, position)
                })

            binding.criteriumNiveausRecycler.isNestedScrollingEnabled = false

            binding.criteriumNiveausRecycler.adapter = adapter

            criteriumEvaluatieViewModel.criteriumNiveaus.observe(viewLifecycleOwner, Observer {
                if(!it.isNullOrEmpty()){
                    it.let {
                        adapter.submitList(it)
                    }
                }
            })

            criteriumEvaluatieViewModel.positieGeselecteerdCriteriumNiveau?.observe(viewLifecycleOwner, Observer{
                it?.let{
                    adapter.stelPositieGeselecteerdNiveauIn(it)
                    adapter.notifyDataSetChanged()
                }
            })

            binding.voegCommentaarToeFloatingActionButton.setOnClickListener {
                var oudeCommentaar =
                    criteriumEvaluatieViewModel.criteriumEvaluatie.value?.commentaar ?: ""

                val builder = AlertDialog.Builder(this.context!!)
                builder.setTitle(R.string.criterium_evaluatie_commentaar_dialog_titel)

                val input = EditText(this.context!!)
                input.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
                input.setSingleLine(false)
                input.setText(oudeCommentaar)
                input.setTextColor(ContextCompat.getColor(context!!, R.color.secondaryTextColor))
                builder.setView(input)

                builder.setPositiveButton(R.string.criterium_evaluatie_commentaar_dialog_bevestig)
                { _, _ -> criteriumEvaluatieViewModel.onCommentaarChanged(
                    oudeCommentaar,
                    input.text.toString()) }
                builder.setNegativeButton(R.string.criterium_evaluatie_commentaar_dialog_annuleer)
                { dialog, _ -> dialog.cancel() }

                builder.show()
            }

            binding.toonCriteriumOmschrijvingImageButton.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle(criteriumOverzichtViewModel?.geselecteerdCriterium?.value?.naam
                        ?: getString(R.string.criterium_evaluatie_omschrijving_dialog_titel_default))
                    .setMessage(criteriumOverzichtViewModel?.geselecteerdCriterium?.value?.omschrijving
                        ?: getString(R.string.criterium_evaluatie_omschrijving_dialog_omschrijving_default))
                    .setPositiveButton(R.string.criterium_evaluatie_omschrijving_dialog_bevestig, null)
                    .show()
            }

            criteriumOverzichtViewModel?.positieGeselecteerdCriterium?.observe(viewLifecycleOwner, Observer{
                binding.upEdgeButton.visibility = if(it == 0) View.GONE else View.VISIBLE
                binding.downEdgeButton.visibility = if(it == criteriumOverzichtViewModel?.positieLaatsteCriterium?.value ?: 0) View.GONE else View.VISIBLE
            })

            binding.upEdgeButton.setOnClickListener{
                criteriumOverzichtViewModel?.onUpEdgeButtonClicked()
            }

            binding.downEdgeButton.setOnClickListener{
                criteriumOverzichtViewModel?.onDownEdgeButtonClicked()
            }
        }

        return binding.root
    }

    protected inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>):T = f() as T
        }

}
