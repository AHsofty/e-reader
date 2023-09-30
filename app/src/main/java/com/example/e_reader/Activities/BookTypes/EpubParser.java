package com.example.e_reader.Activities.BookTypes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EpubParser implements BookParser {
    @Override
    public Bitmap getCoverImage(String epubUri, Context context) {
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

    @Override
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

    @Override
    public String getAuthor(Context context, Uri uri) {
        try {
            InputStream epubInputStream = context.getContentResolver().openInputStream(uri);
            if (epubInputStream != null) {
                Book book = (new EpubReader()).readEpub(epubInputStream);
                epubInputStream.close();

                List<Author> authors = book.getMetadata().getAuthors();
                StringBuilder authorNames = new StringBuilder();
                for (int i = 0; i < authors.size(); i++) {
                    authorNames.append(authors.get(i).getFirstname()).append(" ").append(authors.get(i).getLastname());
                    if (i < authors.size() - 1) {
                        authorNames.append(", ");
                    }
                }
                return authorNames.toString();
            }
            else {
                return "[Unknown Author]";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "[Unknown Author]";
        }
    }

    @Override
    public String getPublisher(Context context, Uri uri) {
        try {
            InputStream epubInputStream = context.getContentResolver().openInputStream(uri);
            if (epubInputStream != null) {
                Book book = (new EpubReader()).readEpub(epubInputStream);
                epubInputStream.close();
                List<String> publishers = book.getMetadata().getPublishers();
                return !publishers.isEmpty() ? publishers.get(0) : "[Unknown Publisher]";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[Unknown Publisher]";
    }

    @Override
    public String getPublicationDate(Context context, Uri uri) {
        try {
            InputStream epubInputStream = context.getContentResolver().openInputStream(uri);
            if (epubInputStream != null) {
                Book book = (new EpubReader()).readEpub(epubInputStream);
                epubInputStream.close();
                List<Date> dates = book.getMetadata().getDates();
                return !dates.isEmpty() ? dates.get(0).toString() : "[Unknown Date]";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[Unknown Date]";
    }

    @Override
    public String getDescription(Context context, Uri uri) {
        try {
            InputStream epubInputStream = context.getContentResolver().openInputStream(uri);
            if (epubInputStream != null) {
                Book book = (new EpubReader()).readEpub(epubInputStream);
                epubInputStream.close();

                List<String> descriptions = book.getMetadata().getDescriptions();
                if (!descriptions.isEmpty()) {
                    String descriptionWithHtml = descriptions.get(0); // Get the first description with HTML

                    // Convert to plain text
                    return Html.fromHtml(descriptionWithHtml, Html.FROM_HTML_MODE_LEGACY).toString();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "[Unknown Description]";
    }

    @Override
    public String getIdentifier(Context context, Uri uri) {
        try {
            InputStream epubInputStream = context.getContentResolver().openInputStream(uri);
            if (epubInputStream != null) {
                Book book = (new EpubReader()).readEpub(epubInputStream);
                epubInputStream.close();
                List<Identifier> identifiers = book.getMetadata().getIdentifiers();
                return !identifiers.isEmpty() ? identifiers.get(0).getValue() : "[Unknown Identifier]";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[Unknown Identifier]";
    }

    @Override
    public String getType(Context context, Uri uri) {
        try {
            InputStream epubInputStream = context.getContentResolver().openInputStream(uri);
            if (epubInputStream != null) {
                Book book = (new EpubReader()).readEpub(epubInputStream);
                epubInputStream.close();
                return book.getMetadata().getTypes().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "[Unknown Type]";
    }
}
