package com.example.e_reader.Activities.BookTypes;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

public interface BookParser {
    Bitmap getCoverImage(String url, Context context);

    String getTitle(Uri uri, Context context);

    String getAuthor(Context context, Uri uri);

    String getPublisher(Context context, Uri uri);

    String getPublicationDate(Context context, Uri uri);

    String getDescription(Context context, Uri uri);

    String getIdentifier(Context context, Uri uri);

    String getType(Context context, Uri uri);
}
