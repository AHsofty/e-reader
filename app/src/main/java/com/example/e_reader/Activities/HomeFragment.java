package com.example.e_reader.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.e_reader.Activities.Database.Book;
import com.example.e_reader.Activities.Database.BookDao;
import com.example.e_reader.Activities.Database.BookDatabase;
import com.example.e_reader.Activities.Database.BookViewModel;
import com.example.e_reader.R;

import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private Uri theBookFileUri;
    private BookViewModel viewModel;

    public HomeFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);
        this.viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        ImageView importView = rootview.findViewById(R.id.importBtn);
        importView.setOnClickListener(this::openFileDialog);
        return rootview;
    }

    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    this.theBookFileUri = data.getData();
                    Log.d("HomeFragment", "onActivityResult: " + this.theBookFileUri);
                }
            }
    );

    public void openFileDialog(View view) {
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.setType("application/epub+zip");
        data = Intent.createChooser(data, "Choose a file");
        sActivityResultLauncher.launch(data);
    }


}