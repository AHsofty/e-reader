package com.example.e_reader.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.e_reader.Activities.Read.ReadActivity;
import com.example.e_reader.BookTypes.BookParser;
import com.example.e_reader.BookTypes.ParserPicker;
import com.example.e_reader.Database.BookTable;
import com.example.e_reader.Database.BookViewModel;
import com.example.e_reader.R;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    private BookViewModel viewModel;
    private BookTable book;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Objects.requireNonNull(this.getSupportActionBar()).hide();

        this.viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        ImageView deleteBtn = this.findViewById(R.id.detail_delete);


        String bookUri = getIntent().getStringExtra("uri");
        BookParser bookParser = ParserPicker.getBookParser(bookUri, this);

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
            this.book = book;

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

                // The cover image is also responsible for going to the Read activity
                coverIV.setOnClickListener(view -> {
                    Intent intent = new Intent(this, ReadActivity.class);
                    intent.putExtra("uri", bookUri);
                    startActivity(intent);
                });

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

        deleteBtn.setOnClickListener(view -> {
            viewModel.delete(this.book);
            finish();
        });


    }
}