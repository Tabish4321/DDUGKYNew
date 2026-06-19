package com.deendayalproject.base

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.deendayalproject.R
import com.deendayalproject.databinding.DialogImagePreviewBinding

class ImagePreviewDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_COUNT = "count"
        private const val ARG_BITMAP = "bitmap"

        fun newInstance(title: String, bitmap: Bitmap?,count:String=""): ImagePreviewDialogFragment {
            return ImagePreviewDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_COUNT, count)
                    putParcelable(ARG_BITMAP, bitmap)
                }
            }
        }
    }

    private var _binding: DialogImagePreviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setStyle(STYLE_NORMAL, R.style.ThemeOverlay_App_RoundedDialog)
        _binding = DialogImagePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(ARG_TITLE) ?: ""
        val count = arguments?.getString(ARG_COUNT) ?: ""

        val bitmap = arguments?.getParcelable<Bitmap>(ARG_BITMAP)
        binding.textViewTitle.text = title
        if (bitmap != null) {
            binding.textNoImage.visibility = View.GONE
            binding.imageViewPreview.setImageBitmap(bitmap)
        } else {
            binding.textNoImage.visibility = View.VISIBLE
            binding.imageViewPreview.setImageResource(R.drawable.no_data)
        }

        if (count.isNotBlank()) {
            binding.tvCount.visibility = View.VISIBLE
            binding.tvCount.text = count
        } else {
            binding.tvCount.visibility = View.GONE
        }

        binding.buttonClose.setOnClickListener {
            dismiss()
        }
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 1).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {


        super.onDestroyView()
        _binding = null
    }
}