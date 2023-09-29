package com.example.e_reader.Activities.Recyclerviews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.e_reader.Activities.Database.BookTable;
import com.example.e_reader.Activities.EpubParser;
import com.example.e_reader.R;

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
        EpubParser epubParser = new EpubParser();
        Bitmap coverBitmap = epubParser.getEpubCoverImage(data.bookTable.getUri(), data.context);
        holder.cardImage.setImageBitmap(coverBitmap);


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
