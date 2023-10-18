package com.example.e_reader.BookTypes;

import android.graphics.Bitmap;
import android.net.Uri;

public interface BookParser {
    Bitmap getCoverImage();

    String getTitle();

    String getAuthor();

    String getPublisher();

    String getPublicationDate();

    String getDescription();

    String getIdentifier();

    String getType();

    void setUri(Uri uri);

}