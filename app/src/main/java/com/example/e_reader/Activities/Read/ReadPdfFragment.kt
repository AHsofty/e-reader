package com.example.e_reader.Activities.Read

import BitmapAdapter
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.e_reader.BookTypes.PdfParser
import com.example.e_reader.R


class ReadPdfFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_read_pdf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Thread {
            val uri = requireActivity().intent.getStringExtra("uri")
            val bookParser = PdfParser(requireContext(), Uri.parse(uri))
            val images: MutableList<Bitmap> = bookParser.getAllPages()

            activity?.runOnUiThread {
                val viewPager: ViewPager2 = requireActivity().findViewById(R.id.pdfViewPager)
                viewPager.adapter = BitmapAdapter(images)
            }
        }.start()
    }
}