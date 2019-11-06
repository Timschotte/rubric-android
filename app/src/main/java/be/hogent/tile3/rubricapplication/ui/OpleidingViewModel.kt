package be.hogent.tile3.rubricapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import be.hogent.tile3.rubricapplication.dao.OpleidingDao


class OpleidingViewModel(val database: OpleidingDao,
                         application: Application): AndroidViewModel(application) {

}