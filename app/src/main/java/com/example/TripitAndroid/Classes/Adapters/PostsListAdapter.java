package com.example.TripitAndroid.Classes.Adapters;

//import android.media.Image;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.TripitAndroid.R;

import java.util.Vector;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostRowViewHolder> {
    Vector<String> mData;
    OnItemClickListener mListener;

    public PostsListAdapter(Vector<String> data) {
        mData = data;
    }

    public interface OnItemClickListener {
        void onClick(int index);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public PostRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_row, viewGroup, false);
        PostRowViewHolder viewHolder = new PostRowViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostRowViewHolder postRowViewHolder, int i) {
        String str = mData.elementAt(i);
        postRowViewHolder.bind(str);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class PostRowViewHolder extends RecyclerView.ViewHolder {
        Button likeButton;
        Button commentButton;
        ImageView profileImage;
        ImageView mainImage;
        TextView userName;
        TextView location;
        TextView description;
        TextView creationDate;

        public PostRowViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            likeButton = itemView.findViewById(R.id.row_like_button);
            commentButton = itemView.findViewById(R.id.row_comment_button);
            profileImage = itemView.findViewById(R.id.row_profile_image);
            mainImage = itemView.findViewById(R.id.row_main_image);
            userName = itemView.findViewById(R.id.row_username);
            location = itemView.findViewById(R.id.row_location);
            description = itemView.findViewById(R.id.row_description);
            creationDate = itemView.findViewById(R.id.row_creation_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    if (listener != null) {
                        if (index != RecyclerView.NO_POSITION) {
                            listener.onClick(index);
                        }
                    }
                }
            });

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bind(String str) {
            userName.setText(str);
            //mCb.setChecked(false);
        }
    }
}
