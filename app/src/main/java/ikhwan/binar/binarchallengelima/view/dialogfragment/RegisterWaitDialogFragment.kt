package ikhwan.binar.binarchallengelima.view.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import ikhwan.binar.binarchallengelima.R
import ikhwan.binar.binarchallengelima.viewmodel.UserApiViewModel


class RegisterWaitDialogFragment : DialogFragment()  {

    private val viewModel: UserApiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_wait_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.registerCheck.postValue(false)

        viewModel.registerCheck.observe(viewLifecycleOwner){
            dialog?.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}