package com.example.e_reader.Activities.Activities.Read

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

class ReadEpubFragment : Fragment() {

    private lateinit var streamer: Streamer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_read_epub, container, false)
    }

    @OptIn(ExperimentalReadiumApi::class, DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val uri = requireActivity().intent.getStringExtra("uri")
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

        GlobalScope.launch {
            val pub = streamer
                .open(asset, allowUserInteraction = true, sender = this@ReadEpubFragment)
                .getOrThrow()

            val navigatorFactory = EpubNavigatorFactory(pub)

            requireActivity().runOnUiThread {
                val fragmentFactory = navigatorFactory.createFragmentFactory(initialLocator = null)

                childFragmentManager.fragmentFactory = fragmentFactory
                childFragmentManager.beginTransaction().replace(
                    R.id.fragment_reader_container,
                    fragmentFactory.instantiate(requireActivity().classLoader, EpubNavigatorFragment::class.java.name)
                ).commit()
            }

            super.onViewCreated(view, savedInstanceState)
        }
    }
}