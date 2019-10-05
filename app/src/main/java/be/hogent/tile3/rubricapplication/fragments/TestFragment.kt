package be.hogent.tile3.rubricapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.model.TestResource
import be.hogent.tile3.rubricapplication.ui.TestViewModel
import kotlinx.android.synthetic.main.fragment_test.view.*


/**
 * This displays the test-message from the backend
 */
class TestFragment : Fragment() {

    /**
     * The viewmodel used to retrieve the test from the backend
     */
    private lateinit var viewModel: TestViewModel

    /**
     * We create the view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_test, container, false)

        viewModel = ViewModelProviders.of(activity!!).get(TestViewModel::class.java)

        rootView.testTxt.setText("Trying to connect to backend ...")

        //Observes the livedate from the viewmodel and displays it when it changes
        val testObjectObserver = Observer<TestResource> { newTest ->
            rootView.testTxt.setText(newTest.message)
        }

        //attach the observer to the livedata
        viewModel.getTestObject().observe(this, testObjectObserver)
        return rootView
    }
}