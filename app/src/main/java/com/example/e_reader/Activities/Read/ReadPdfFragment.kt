package com.example.e_reader.Activities.Read

import BitmapAdapter
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.e_reader.BookTypes.PdfParser
import com.example.e_reader.Database.BookTable
import com.example.e_reader.Database.BookViewModel
import com.example.e_reader.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReadPdfFragment : Fragment() {
    private lateinit var viewModel: BookViewModel
    private var canContinue: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_read_pdf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uri = requireActivity().intent.getStringExtra("uri")

        this.viewModel = BookViewModel(requireActivity().application)
        viewModel.allBooks.observe(viewLifecycleOwner) { books ->
            if (!canContinue) {
                return@observe
            }
            canContinue = false

            var bookTable: BookTable? = null
            books.forEach { book ->
                if (book.uri == uri) {
                    bookTable = book
                }
            }

            val viewPager: ViewPager2 = requireActivity().findViewById(R.id.pdfViewPager)
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // Update lastPage here
                    bookTable?.lastPage = position.toString()
                    viewModel.update(bookTable!!)
                }
            })

            lifecycleScope.launch {
                val images = withContext(Dispatchers.IO) {
                    val bookParser = PdfParser(requireContext(), Uri.parse(uri))
                    bookParser.getAllPages()
                }

                val startingPage: Int = (bookTable?.lastPage?.toInt()) ?: 0
                viewPager.adapter = bookTable?.let {
                    BitmapAdapter(images)
                }
                viewPager.setCurrentItem(startingPage, true)
            }
        }




    }
}