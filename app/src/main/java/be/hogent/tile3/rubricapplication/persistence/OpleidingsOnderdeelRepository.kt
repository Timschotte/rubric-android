package be.hogent.tile3.rubricapplication.persistence

import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.dao.OpleidingsOnderdeelDao
import be.hogent.tile3.rubricapplication.network.RubricApi
import javax.inject.Inject

class OpleidingsOnderdeelRepository(private val opleidingsOnderdeelDao: OpleidingsOnderdeelDao) {

    @Inject lateinit var rubricApi: RubricApi

    init {
        App.component.inject(this)
    }

}