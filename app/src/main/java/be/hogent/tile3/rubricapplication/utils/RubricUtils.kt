package be.hogent.tile3.rubricapplication.utils

import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.model.Rubric
import java.lang.IllegalStateException

class RubricUtils{
    companion object {
        @JvmStatic
        fun laagsteNiveau(niveaus: List<Niveau>): Int{
            if(niveaus.size < 2){
                throw IllegalStateException("There should be 2 or more niveaus!!")
            }
            return niveaus.maxWith(object: Comparator<Niveau> {
                override fun compare(p1: Niveau, p2: Niveau): Int = when {
                    p1.volgnummer > p2.volgnummer -> -1
                    else -> 1
                }
            })!!.volgnummer
        }

        @JvmStatic
        fun hoogsteNiveau(niveaus: List<Niveau>): Int{
            if(niveaus.size < 2){
                throw IllegalStateException("There should be 2 or more niveaus!!")
            }
            return niveaus.minWith(object: Comparator<Niveau> {
                override fun compare(p1: Niveau, p2: Niveau): Int = when {
                    p1.volgnummer > p2.volgnummer -> -1
                    else -> 1
                }
            })!!.volgnummer
        }

    }


}