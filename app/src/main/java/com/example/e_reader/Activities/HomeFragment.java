package com.example.e_reader.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_reader.Activities.Database.Book;
import com.example.e_reader.Activities.Database.BookViewModel;
import com.example.e_reader.R;

import java.util.List;

public class HomeFragment extends Fragment {

    private Uri theBookFileUri;
    private BookViewModel viewModel;
    private ActivityResultLauncher<Intent> sActivityResultLauncher;


    public HomeFragment() {
        // require a empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        this.theBookFileUri = data.getData();

                        viewModel.getAllBooks().observe(getViewLifecycleOwner(), books -> {
                            // Before we add a book to the database we check if the database already contains that book
                            // We can do this by checking if the URI is already in the database or not
                            if (books.stream().noneMatch(book -> book.getUri().equals(this.theBookFileUri.toString()))) {
                                String title = this.theBookFileUri.getLastPathSegment();
                                Book book = new Book();
                                book.setUri(this.theBookFileUri.toString());
                                book.setTitle(title);
                                this.viewModel.insert(book);
                            }
                            else {
                                Toast.makeText(getContext(), "You have already added the book you selected", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);
        ImageView importView = rootview.findViewById(R.id.importBtn);
        importView.setOnClickListener(this::openFileDialog);

        // Initialize the ViewModel with the activity's lifecycle
        this.viewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        // Observe the LiveData for books
        viewModel.getAllBooks().observe(getViewLifecycleOwner(), books -> {
            for (Book book : books) {
                // TODO: Add logic for a recyclerview with a bunch of cards for each book

            }
        });

        return rootview;
    }



    public void openFileDialog(View view) {
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.setType("application/epub+zip");
        data = Intent.createChooser(data, "Choose a file");
        sActivityResultLauncher.launch(data);
    }
}