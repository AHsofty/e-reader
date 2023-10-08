// TODO: right now the UI looks pretty awful, this is because we're just loading HTML
// TODO: The next step is actually making the book content look pretty inside of the webview

package com.example.e_reader.Activities.Activities.Read;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.e_reader.Activities.BookTypes.BookParser;
import com.example.e_reader.Activities.BookTypes.EpubParser;
import com.example.e_reader.R;

import java.util.List;


public class ReadEpubFragment extends Fragment {

    private WebView epubWebView;
    private BookParser epubParser;
    private int currentPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_read_epub, container, false);

        this.epubWebView = view.findViewById(R.id.epubWebView);
        Button prevButton = view.findViewById(R.id.prevButton);
        Button nextButton = view.findViewById(R.id.nextButton);

        // Gets the book uri from the intent
        String uri = requireActivity().getIntent().getStringExtra("uri");
        this.epubParser = new EpubParser(requireContext(), Uri.parse(uri));


        loadPage(this.currentPage);

        prevButton.setOnClickListener(v -> {
            if (this.currentPage > 0) {
                this.currentPage--;
                loadPage(currentPage);
            }
        });

        nextButton.setOnClickListener(v -> {
            this.currentPage++;
            loadPage(currentPage);
        });

        return view;
    }

    private void loadPage(int pageIndex) {
        String htmlContent = epubParser.getContentOfPage(pageIndex);
        Log.d("lol", htmlContent);
        epubWebView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null); // We load with base url because otherwise the text might look distorted
    }
}