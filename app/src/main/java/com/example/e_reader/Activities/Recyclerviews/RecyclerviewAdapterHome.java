package com.example.e_reader.Activities.Recyclerviews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_reader.Activities.Activities.DetailActivity;
import com.example.e_reader.Activities.BookTypes.BookParser;
import com.example.e_reader.Activities.Database.BookTable;
import com.example.e_reader.Activities.BookTypes.EpubParser;
import com.example.e_reader.R;
import nl.siegmann.epublib.domain.Book;

import java.util.List;

public class RecyclerviewAdapterHome extends RecyclerView.Adapter<RecyclerviewAdapterHome.ViewHolder> {
    private final List<RecyclerviewAdapterHome.Data> mData;
    private final LayoutInflater mInflater;
    private RecyclerviewAdapterHome.ItemClickListener mClickListener;


    public RecyclerviewAdapterHome(Context context, List<RecyclerviewAdapterHome.Data> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;

    }




    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public RecyclerviewAdapterHome.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_widgets_home, parent, false);

        return new RecyclerviewAdapterHome.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerviewAdapterHome.ViewHolder holder, int position) {
        RecyclerviewAdapterHome.Data data = mData.get(position);
        holder.cardText.setText(data.bookTable.getTitle());

        // The image of the book is actually going to be the first page of the .epub file
        // The URI is actually a reference to the path of the .epub file
        // So all we have to do is store the first page of that .epub file into the holder.cardImage

        BookParser epubParser = new EpubParser(); // TODO: This is hardcoded but eventually we don't want to hardcode the book type, instead we want to check for the type of the book and make a parser based on that

        Bitmap coverBitmap = epubParser.getCoverImage(data.bookTable.getUri(), data.context);
        holder.cardImage.setImageBitmap(coverBitmap);

        holder.card.setOnClickListener(view -> {
            String bookUri = data.bookTable.getUri(); // We send this to the next activity, so we can know which book to query from the database

            Intent intent = new Intent(data.context, DetailActivity.class);
            intent.putExtra("bookUri", bookUri);
            data.context.startActivity(intent);

        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class Data {
        Context context;
        BookTable bookTable;

        public Data(Context context, BookTable bookTable) {
            this.context = context;
            this.bookTable = bookTable;
        }

    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView card;
        TextView cardText;
        ImageView cardImage;

        ViewHolder(View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card_home);
            cardText = itemView.findViewById(R.id.card_home_textview);
            cardImage = itemView.findViewById(R.id.card_home_imageview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


}
