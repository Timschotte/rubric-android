package be.hogent.tile3.rubricapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.model.RubricResource
import be.hogent.tile3.rubricapplication.model.TestResource
import be.hogent.tile3.rubricapplication.ui.RubricViewModel
import be.hogent.tile3.rubricapplication.ui.TestViewModel
import kotlinx.android.synthetic.main.fragment_test.view.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


/**
 * This displays the test-message from the backend
 */
class TestFragment : Fragment() {

    /**
     * The viewmodel used to retrieve the test from the backend
     */
    private lateinit var viewModel: TestViewModel
    private lateinit var viewModel2: RubricViewModel

    /**
     * We create the view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_test, container, false)

        viewModel = ViewModelProviders.of(activity!!).get(TestViewModel::class.java)
        viewModel2 = ViewModelProviders.of(activity!!).get(RubricViewModel::class.java)
        rootView.testTxt.setText("Trying to connect to backend ...")

        //Observes the livedate from the viewmodel and displays it when it changes
        val testObjectObserver = Observer<TestResource> { newTest ->
            rootView.testTxt.setText(newTest.message)
        }

        //attach the observer to the livedata
        viewModel.getTestObject().observe(this, testObjectObserver)

        //Observes the livedate from the viewmodel and displays it when it changes
        val testObject2Observer = Observer<RubricResource> { newTest ->

        }

        //attach the observer to the livedata
        viewModel2.getTestObject().observe(this, testObject2Observer)
        return rootView


    }
}