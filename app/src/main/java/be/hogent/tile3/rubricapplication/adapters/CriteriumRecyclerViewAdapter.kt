package be.hogent.tile3.rubricapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.fragments.RubricFragment
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.ui.NiveauViewModel
import be.hogent.tile3.rubricapplication.utils.RubricUtils
import be.hogent.tile3.rubricapplication.views.NiveauView
import kotlinx.android.synthetic.main.criterium_list_content.view.*
import kotlinx.android.synthetic.main.niveau_cell.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

class CriteriumRecyclerViewAdapter(private val parentFragment: RubricFragment) :
    ListAdapter<Criterium, CriteriumRecyclerViewAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var niveauViewModel: NiveauViewModel
    //ListAdapter helps you to work with RecyclerViews that change the content over time.
    // All you need to do is submit a new list. It runs the DiffUtil tool behind the scenes for you on the background thread.
    // Then it runs the animations based on how the list has changed.
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Criterium>() {
            override fun areItemsTheSame(oldItem: Criterium, newItem: Criterium): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: Criterium, newItem: Criterium): Boolean {
                return false
            }
        }
    }

    //This will create the contentView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        niveauViewModel = ViewModelProviders.of(parentFragment.activity!!).get(NiveauViewModel::class.java)

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.criterium_list_content, parent, false)
        return ViewHolder(view)
    }


    //This will fill the items with data and set a listener
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //get the memory
        val criterium: Criterium = getItem(position)

        //fill the layout with data
        var criteriumNiveaus: List<Niveau>

        var rubricNiveaus: List<Niveau>
        doAsync {
            criteriumNiveaus = niveauViewModel.getNiveausForCriterium(criterium.criteriumId )
            rubricNiveaus = niveauViewModel.getNiveausForRubric(criterium.rubricId )
            holder.description.text = criterium.naam

            onComplete {
                var hoogsteNiveau = RubricUtils.hoogsteNiveau(rubricNiveaus)
                var laagsteNiveau = RubricUtils.laagsteNiveau(rubricNiveaus)

                for(x in laagsteNiveau until hoogsteNiveau + 1){
                    if(criteriumNiveaus.any { y -> y.volgnummer == x }){
                        val huidigNiveau = criteriumNiveaus.filter { z -> z.volgnummer == x }.get(0)
                        val niveauView = NiveauView(parentFragment.activity!!)
                        val layoutParam = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f)
                        layoutParam.setMargins(5, 5, 5, 5)
                        niveauView.layoutParams = layoutParam
                        niveauView.titel.text = huidigNiveau.titel
                        niveauView.omschrijving.text = huidigNiveau.omschrijving
                        if(x == 0){
                            val resources = parentFragment.activity!!.applicationContext.resources
                            val color: Int = resources.getColor(R.color.colorSecundaryDark)
                            niveauView.NiveauCellContainer.setCardBackgroundColor(color)
                        }
                        holder.tableRow.addView(niveauView)
                    } else{
                        val whitespace = LinearLayout(parentFragment.activity!!)
                        val layoutParam = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f)
                        layoutParam.setMargins(5, 5, 5, 5)
                        whitespace.layoutParams = layoutParam
                        holder.tableRow.addView(whitespace)
                    }
                }
            }

        }

        with(holder.itemView) {
            tag = criterium // Save the memory represented by this view
            //setOnClickListener(onClickListener)
        }
    }

    //we create this function so we can pass the memory to the viewmodel to delete it
    fun getCriterium(position: Int): Criterium{
        return getItem(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = view.criteriumDescription
        val tableRow: TableRow = view.criterium_tablerow
    }
}