package com.example.e_reader.BookTypes;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Html;
import nl.siegmann.epublib.domain.*;
import nl.siegmann.epublib.epub.EpubReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EpubParser implements BookParser {

    private Context context;
    private Uri uri;
    private Book book;

    public EpubParser(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
        try {
            ContentResolver resolver = this.context.getContentResolver();
            resolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            InputStream epubInputStream = resolver.openInputStream(this.uri);
            this.book = new EpubReader().readEpub(epubInputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Bitmap getCoverImage() {
        Resource coverImage = book.getCoverImage();
        if (coverImage != null) {
            try {
                InputStream data = coverImage.getInputStream();
                return BitmapFactory.decodeStream(data);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getTitle() {
        return book.getTitle();
    }

    @Override
    public String getAuthor() {
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

    @Override
    public String getPublisher() {
        List<String> publishers = book.getMetadata().getPublishers();
        return !publishers.isEmpty() ? publishers.get(0) : "[Unknown Publisher]";
    }

    @Override
    public String getPublicationDate() {
        List<Date> dates = book.getMetadata().getDates();
        return !dates.isEmpty() ? dates.get(0).toString() : "[Unknown Date]";
    }

    @Override
    public String getDescription() {
        List<String> descriptions = book.getMetadata().getDescriptions();
        if (!descriptions.isEmpty()) {
            String descriptionWithHtml = descriptions.get(0); // Get the first description with HTML
            // Convert to plain text
            return Html.fromHtml(descriptionWithHtml, Html.FROM_HTML_MODE_LEGACY).toString();
        }
        return "[Unknown Description]";
    }

    @Override
    public String getIdentifier() {
        List<Identifier> identifiers = book.getMetadata().getIdentifiers();
        return !identifiers.isEmpty() ? identifiers.get(0).getValue() : "[Unknown Identifier]";
    }

    @Override
    public String getType() {
        return book.getMetadata().getTypes().isEmpty() ? "[Unknown Type]" : book.getMetadata().getTypes().get(0);
    }

    @Override
    public void setUri(Uri uri) {
        this.uri = uri;
    }


}
