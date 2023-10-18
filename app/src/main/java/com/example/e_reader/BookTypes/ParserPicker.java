package com.example.e_reader.BookTypes;

import android.content.Context;
import android.net.Uri;

public class ParserPicker {
    public static BookParser getBookParser(String uri, Context context) {
        if (getFileType(uri, context).equals("application/epub+zip")) {
            return new EpubParser(context, Uri.parse(uri));
        }

        if (getFileType(uri, context).equals("application/pdf")) {
            return new PdfParser(context, Uri.parse(uri));
        }

        return null;
    }

    public static String getFileType(String uri, Context context) {
        return context.getContentResolver().getType(Uri.parse(uri));
    }
}
