package be.hogent.tile3.rubricapplication.utils

import be.hogent.tile3.rubricapplication.data.RubricData
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.model.Rubric
import java.lang.IllegalStateException

class RubricDataToRubricMapper {
    companion object {
        @JvmStatic
        fun getRubricModels(rubricsData: List<RubricData>): List<Rubric>{
            val result: MutableList<Rubric> = mutableListOf()
            for(rubric in rubricsData){
                result.add(Rubric(rubric.id.toString(), rubric.onderwerp, rubric.omschrijving, rubric.datumTijdCreatie, rubric.datumTijdLaatsteWijziging))
            }
            return result
        }

        @JvmStatic
        fun getCriteriumModels(rubricsData: List<RubricData>): List<Criterium>{
            val result: MutableList<Criterium> = mutableListOf()
            for(rubric in rubricsData){
                for(criteriumGroep in rubric.criteriumGroepen){
                    for(criterium in criteriumGroep.criteria){
                        result.add(Criterium(criterium.id.toString(), criterium.naam, criterium.omschrijving, criterium.gewicht.toDouble(), rubric.id.toString()))
                    }
                }
            }
            return result
        }

        @JvmStatic
        fun getNiveauModels(rubricsData: List<RubricData>): List<Niveau>{
            val result: MutableList<Niveau> = mutableListOf()
            for(rubric in rubricsData){
                for(criteriumGroep in rubric.criteriumGroepen){
                    for(criterium in criteriumGroep.criteria){
                        for(criteriumNiveau in criterium.criteriumNiveaus)
                        result.add(Niveau(criteriumNiveau.id.toString(), criteriumNiveau.niveau.naam, criteriumNiveau.omschrijving, criteriumNiveau.ondergrens, criteriumNiveau.bovengrens, criteriumNiveau.niveau.volgnummer, criterium.id.toString()))
                    }
                }
            }
            return result
        }


    }
}