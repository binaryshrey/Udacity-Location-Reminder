package com.udacity.project4.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

/**
 * Base Fragment to observe on the common LiveData objects
 */
abstract class BaseFragment : Fragment() {
    /**
     * Every fragment has to have an instance of a view model that extends from the BaseViewModel
     */
    abstract val baseViewModel: BaseViewModel

    override fun onStart() {
        super.onStart()

        //shoe showSnackBar
        baseViewModel.showSnackBar.observe(this, Observer {
            Snackbar.make(this.requireView(), it, Snackbar.LENGTH_LONG).show()
        })

        //shoe showSnackBar
        baseViewModel.showSnackBarInt.observe(this, Observer {
            Snackbar.make(this.requireView(), getString(it), Snackbar.LENGTH_LONG).show()
        })

        //show error toast
        baseViewModel.showErrorMessage.observe(this, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })

        //show toast
        baseViewModel.showToast.observe(this, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        })


        //nav handler
        baseViewModel.navigationCommand.observe(this, Observer { command ->
            when (command) {
                is NavigationCommand.BackTo -> findNavController().popBackStack(command.destinationId, false)
                is NavigationCommand.Back -> findNavController().popBackStack()
                is NavigationCommand.To -> requireView().findNavController().navigate(command.directions)
            }
        })
    }
}