package com.example.e_reader.Activities.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.RecyclerView;
import com.example.e_reader.Activities.BookTypes.BookParser;
import com.example.e_reader.Activities.Database.BookTable;
import com.example.e_reader.Activities.Database.BookViewModel;
import com.example.e_reader.Activities.BookTypes.EpubParser;
import com.example.e_reader.Activities.Recyclerviews.RecyclerviewAdapterHome;
import com.example.e_reader.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private Uri theBookFileUri;
    private BookViewModel viewModel;
    private ActivityResultLauncher<Intent> sActivityResultLauncher;
    private ArrayList<RecyclerviewAdapterHome.Data> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerviewAdapterHome adapter;



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
                                BookParser epubParser = new EpubParser(this.getActivity(), this.theBookFileUri); // TODO: This is hardcoded but eventually we don't want to hardcode the book type, instead we want to check for the type of the book and make a parser based on that
                                String title = epubParser.getTitle();
                                BookTable bookTable = new BookTable();
                                bookTable.setUri(this.theBookFileUri.toString());
                                bookTable.setTitle(title);
                                this.viewModel.insert(bookTable);

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

        this.viewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        viewModel.getAllBooks().observe(getViewLifecycleOwner(), books -> {
            data.clear();
            for (BookTable bookTable : books) {
                data.add(new RecyclerviewAdapterHome.Data(this.getContext(), bookTable));
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        });

        recyclerView = rootview.findViewById(R.id.rviewHome);
        adapter = new RecyclerviewAdapterHome(this.getContext(), data);
        recyclerView.setAdapter(adapter);

        return rootview;
    }



    public void openFileDialog(View view) {
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.setType("application/epub+zip");
        data = Intent.createChooser(data, "Choose a file");
        sActivityResultLauncher.launch(data);
    }
}