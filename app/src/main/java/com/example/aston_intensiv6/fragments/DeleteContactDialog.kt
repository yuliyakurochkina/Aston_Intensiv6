package com.example.aston_intensiv6.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.aston_intensiv6.DataSetting
import com.example.aston_intensiv6.R
import java.lang.IllegalStateException

class DeleteContactDialog : DialogFragment() {
    companion object {
        private const val ARGS_ID = "ARGS_id1"
        fun newDeleteContactDialog(pos: Int): DeleteContactDialog {
            val fragment = DeleteContactDialog()
            val bundle = Bundle()
            bundle.putInt(ARGS_ID, pos)
            fragment.arguments = bundle
            return fragment
        }

        fun getContactPosFromArgs(bundle: Bundle): Int = bundle.getInt(ARGS_ID)
    }

    private var dataSource: DataSetting? = null
    private var contactId = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DataSetting) {
            dataSource = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            contactId = getContactPosFromArgs(it)
        }
        var message = ""
        if (contactId >= 0) {
            dataSource?.let {
                val contact = it.getContactById(contactId)
                if (contact != null) {
                    message = "${contact.name} ${contact.surname}"
                }
            }
        }
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.delete_con_dialog)).setMessage(message)
                .setPositiveButton(
                    getString(R.string.delete_con)
                ) { _, _ ->
                    dataSource?.deleteContactById(contactId)
                    setFragmentResult(RecyclerViewFragment.REQUEST_KEY, Bundle())
                    dialog?.cancel()
                }.setNegativeButton(
                    getString(R.string.cancel)
                ) { _, _ -> dialog?.cancel() }
            builder.create()
        } ?: throw IllegalStateException("Activity is null")
    }

    override fun onDetach() {
        super.onDetach()
        dataSource = null
    }
}