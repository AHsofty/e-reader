package com.example.e_reader.Activities.Activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import com.example.e_reader.Activities.BookTypes.BookParser;
import com.example.e_reader.Activities.BookTypes.EpubParser;
import com.example.e_reader.Activities.Database.BookTable;
import com.example.e_reader.Activities.Database.BookViewModel;
import com.example.e_reader.Activities.Recyclerviews.RecyclerviewAdapterHome;
import com.example.e_reader.R;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    private BookViewModel viewModel;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        this.viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        String bookUri = getIntent().getStringExtra("bookUri");
        BookParser bookParser = new EpubParser(this, Uri.parse(bookUri)); // TODO: This is hardcoded but eventually we don't want to hardcode the book type, instead we want to check for the type of the book and make a parser based on that

        // Now we get the corresponding book object from the database
        // We can do this by querying the database for the book with the URI that we got from the intent
        viewModel.getAllBooks().observe(this, books -> {
            BookTable book = null;
            for (BookTable bookTable : books) {
                if (bookTable.getUri().equals(bookUri)) {
                    book = bookTable;
                    break;
                }
            }

            if (book != null) {
                String title = book.getTitle();
                String author = bookParser.getAuthor();

                TextView titleTV = findViewById(R.id.detail_title);
                TextView authorTV = findViewById(R.id.detail_author);
                ImageView coverIV = findViewById(R.id.detail_cover);
                TextView typeTV = findViewById(R.id.detail_type);
                TextView publisherTV = findViewById(R.id.detail_publisher);
                TextView publicationDateTV = findViewById(R.id.detail_publishDate);
                TextView descriptionTV = findViewById(R.id.detail_description);
                TextView identifierTV = findViewById(R.id.detail_identifier);


                titleTV.setText("Title: " + title);
                if ("[Unknown Title]".equals(title)) {
                    titleTV.setVisibility(View.GONE);
                }

                authorTV.setText("Author: " + author);
                if ("[Unknown Author]".equals(author)) {
                    authorTV.setVisibility(View.GONE);
                }

                Bitmap coverImage = bookParser.getCoverImage();
                if (coverImage != null) {
                    coverIV.setImageBitmap(coverImage);
                } else {
                    coverIV.setVisibility(View.GONE);
                }

                String type = bookParser.getType();
                typeTV.setText("Genre: " + type);
                if ("[Unknown Type]".equals(type)) {
                    typeTV.setVisibility(View.GONE);
                }

                String publisher = bookParser.getPublisher();
                publisherTV.setText("Publisher: " + publisher);
                if ("[Unknown Publisher]".equals(publisher)) {
                    publisherTV.setVisibility(View.GONE);
                }

                String publicationDate = bookParser.getPublicationDate();
                publicationDateTV.setText("Publication Date: " + publicationDate);
                if ("[Unknown Date]".equals(publicationDate)) {
                    publicationDateTV.setVisibility(View.GONE);
                }

                String description = bookParser.getDescription();
                descriptionTV.setText("Description: " + description);
                if ("[Unknown Description]".equals(description)) {
                    descriptionTV.setVisibility(View.GONE);
                }

                String identifier = bookParser.getIdentifier();
                identifierTV.setText("Identifier: " + identifier);
                if ("[Unknown Identifier]".equals(identifier)) {
                    identifierTV.setVisibility(View.GONE);
                }
            }
        });


    }
}