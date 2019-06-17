package com.example.TripitAndroid.Classes.Adapters;

import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.TripitAndroid.Classes.Post;
import com.example.TripitAndroid.Classes.UserInfo;
import com.example.TripitAndroid.Fragments.AddPostFragment;
import com.example.TripitAndroid.R;
import com.example.TripitAndroid.models.FirebaseModel;
import com.example.TripitAndroid.models.Model;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.PostRowViewHolder> {
    ArrayList<Post> mData;
    ProfileListAdapter.OnItemClickListener mListener;

    public ProfileListAdapter(ArrayList<Post> data) {
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_edit_row, viewGroup, false);
        PostRowViewHolder viewHolder = new PostRowViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostRowViewHolder postRowViewHolder, int i) {
        Post post = mData.get(i);
        postRowViewHolder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class PostRowViewHolder extends RecyclerView.ViewHolder {

        Button likeButton;
        Button commentButton;
        Button editButton;


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
            editButton = itemView.findViewById(R.id.row_edit_button);

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

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editPost();
                }
            });
        }

        private void editPost() {

        }


        public void bind(Post post) {
            final String userID = post.userID;

            Model.instance.getUserInfo(userID, new Model.OnUserInfoUpdated() {
                @Override
                public void onUserInfoUpdated(UserInfo userInfo) {
                    if (userInfo != null) {
                        userName.setText(userInfo.displayName);
                    }
                    else userName.setText(userID);
                }
            });

            //Fields:
            location.setText(post.location);
            description.setText(post.description);
            creationDate.setText(post.creationDateStringFormat);
            //mainImage.setImageBitmap(Model.instance.getImage(Model.instance.getImage();););

            //            postImageView.setImageBitmap(imageBitmap);

            //Like button image:
            @DrawableRes int drawable = R.drawable.like_unpressed;
            if (post.likes.containsKey(post.userID))
                drawable = R.drawable.like_pressed;
            likeButton.setBackgroundResource(drawable);
        }
    }

}


