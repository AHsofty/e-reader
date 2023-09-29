package com.example.e_reader.Activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EpubParser {

    public Bitmap getEpubCoverImage(String epubUri, Context context) {
        try {
            InputStream epubInputStream = context.getContentResolver().openInputStream(Uri.parse(epubUri));
            Book book = (new EpubReader()).readEpub(epubInputStream);
            Resource coverImage = book.getCoverImage();

            if (coverImage != null) {
                InputStream data = coverImage.getInputStream();
                return BitmapFactory.decodeStream(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTitle(Uri uri, Context context) {
        try {
            InputStream epubInputStream = context.getContentResolver().openInputStream(Uri.parse(String.valueOf(uri)));
            Book book = (new EpubReader()).readEpub(epubInputStream);
            return book.getTitle();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "[Unknown Title]";
        }
    }
}
