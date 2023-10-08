package com.example.e_reader.Activities.BookTypes;

import android.content.Context;
import android.net.Uri;

public class ParserPicker {
    public static BookParser getBookParser(String uri, Context context) {
        String mimeType = context.getContentResolver().getType(Uri.parse(uri));
        if (mimeType.equals("application/epub+zip")) {
            return new EpubParser(context, Uri.parse(uri));
        }

        return null;
    }
}
