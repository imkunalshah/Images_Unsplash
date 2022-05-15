package com.kunal.sunbase_task.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kunal.sunbase_task.R
import com.kunal.sunbase_task.databinding.FragmentQuitAppDialogBinding
import com.kunal.sunbase_task.ui.base.BaseDialogFragment
import com.kunal.sunbase_task.utils.getDialogWidth

class QuitAppDialogFragment :
    BaseDialogFragment<FragmentQuitAppDialogBinding>(FragmentQuitAppDialogBinding::inflate) {


    var onQuitClicked: (() -> Unit)? = null

    companion object {
        const val TAG = "QuitAppDialogFragment"
        fun newInstance(): QuitAppDialogFragment {
            return QuitAppDialogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuitAppDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDimens(view)
        initializeViews()
    }

    override fun initializeViews() {
        binding.quitButton.setOnClickListener {
            onQuitClicked?.invoke()
            dismiss()
        }
        binding.noButton.setOnClickListener {
            dismiss()
        }
    }

    private fun setDimens(view: View) {
        view.layoutParams.width = context?.getDialogWidth() ?: 200
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded)
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
                true
            } else false
        }
        return dialog
    }

    override fun initializeObservers() {}
}