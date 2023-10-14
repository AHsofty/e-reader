// Special thanks to IPSVN for helping me with this file

package com.example.e_reader.Activities.Activities.Read

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.lifecycleScope
import com.example.e_reader.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.readium.r2.navigator.epub.EpubNavigatorFactory
import org.readium.r2.navigator.epub.EpubNavigatorFragment
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.asset.FileAsset
import org.readium.r2.streamer.Streamer
import java.io.File
import java.io.FileOutputStream
import androidx.fragment.app.commitNow
import com.example.e_reader.Activities.Database.BookTable
import com.example.e_reader.Activities.Database.BookViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import nl.siegmann.epublib.domain.Book
import org.json.JSONObject
import org.readium.r2.shared.publication.Locator
import java.util.Locale


class ReadEpubFragment : Fragment() {

    private lateinit var streamer: Streamer
    private lateinit var navigator: EpubNavigatorFragment
    private lateinit var viewModel: BookViewModel
    private lateinit var myBook: BookTable


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_read_epub, container, false)
    }

    @OptIn(ExperimentalReadiumApi::class, DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        this.viewModel = BookViewModel(requireActivity().application)

        val uri = requireActivity().intent.getStringExtra("uri")

        viewModel.allBooks.observe(viewLifecycleOwner) { books ->
            books.forEach { book ->
                if (book.uri == uri) {
                    this.myBook = book
                }
            }
        }

        streamer = Streamer(context = requireContext())
        val contentResolver = context?.contentResolver
        val inputStream = contentResolver?.openInputStream(Uri.parse(uri))

        val tempFile = File.createTempFile("tempEpub", ".epub", requireContext().cacheDir)

        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }


        val asset = FileAsset(tempFile)

        lifecycleScope.launch {
            val pub = streamer
                .open(asset, allowUserInteraction = true, sender = this@ReadEpubFragment)
                .getOrThrow()

            val navigatorFactory = EpubNavigatorFactory(pub)

            requireActivity().runOnUiThread {

                val fragmentFactory: FragmentFactory
                if (myBook.lastPage != "-1") {
                    fragmentFactory = navigatorFactory.createFragmentFactory(initialLocator = Locator.fromJSON(JSONObject(myBook.lastPage)))
                }
                else {
                    fragmentFactory = navigatorFactory.createFragmentFactory(initialLocator = null)
                }


                childFragmentManager.fragmentFactory = fragmentFactory
                childFragmentManager.commitNow {
                    add(R.id.fragment_reader_container, EpubNavigatorFragment::class.java, bundleOf(), "read")
                }

                navigator = childFragmentManager.findFragmentByTag("read") as EpubNavigatorFragment
                navigator.currentLocator
                    .onEach {
                        myBook.lastPage = it.toJSON().toString()
                        viewModel.update(myBook)
                    }
                    .launchIn(this)
            }

            super.onViewCreated(view, savedInstanceState)
        }
    }
}