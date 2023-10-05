package com.example.e_reader.Activities.BookTypes;

import android.content.Context;
import android.net.Uri;

public class ParserPicker {
    public static BookParser getBookParser(String uri, Context context) {
        if (uri.endsWith(".epub")) {
            return new EpubParser(context, Uri.parse(uri));
        }

        return null;
    }
}
