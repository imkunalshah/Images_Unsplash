package com.kunal.sunbase_task.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.kunal.sunbase_task.R
import com.kunal.sunbase_task.data.network.models.Photo
import com.kunal.sunbase_task.databinding.FragmentDialogFullscreenBinding
import com.kunal.sunbase_task.ui.base.BaseDialogFragment
import com.kunal.sunbase_task.utils.*

class FullScreenDialogFragment :
    BaseDialogFragment<FragmentDialogFullscreenBinding>(FragmentDialogFullscreenBinding::inflate) {


    companion object {
        const val TAG = "FullScreenDialogFragment"
        const val PHOTO = "photo"

        fun newInstance(photo: Photo?): FullScreenDialogFragment {
            val bundle = Bundle().apply {
                putParcelable(PHOTO, photo)
            }
            val showDetailsDialogFragment = FullScreenDialogFragment().apply {
                arguments = bundle
            }
            return showDetailsDialogFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDimens(view)
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

    override fun initializeViews() {
        binding.progressBar.visible()
        val photo: Photo? = arguments?.getParcelable(PHOTO)
        binding.apply {
            photoFullScreen.loadImage(
                photo?.urls?.full,
                photoFullScreen,
                onFailed = {
                    context?.showToast("Loading Failed")
                    binding.progressBar.gone()
                    dismiss()
                },
                onSuccess = {
                    binding.closeButton.visible()
                    binding.progressBar.gone()
                }
            )
            closeButton.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun initializeObservers() {}
}