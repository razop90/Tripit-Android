package com.example.TripitAndroid.Classes.Adapters;

import com.example.TripitAndroid.Fragments.AddPostFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.TripitAndroid.Classes.Post;
import com.example.TripitAndroid.Classes.UserInfo;
import com.example.TripitAndroid.Fragments.AddPostFragment;
import com.example.TripitAndroid.R;
import com.example.TripitAndroid.models.FirebaseModel;
import com.example.TripitAndroid.models.Model;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.PostRowViewHolder> {
    ArrayList<Post> mData;
    ProfileListAdapter.OnItemClickListener mListener;
    ProfileListAdapter.OnDeleteClickListener mDeleteListener;

    public ProfileListAdapter(ArrayList<Post> data) {
        mData = data;
    }

    public interface OnItemClickListener {
        void onClick(int index);
    }

    public interface OnDeleteClickListener {
        void onClick(int index);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public PostRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_edit_row, viewGroup, false);

        OnDeleteClickListener deleteListener = new OnDeleteClickListener() {
            @Override
            public void onClick(int index) {
                Model.instance.setPostAsDeleted(mData.get(index).id);
            }
        };

        if(mDeleteListener == null) {
            mDeleteListener = deleteListener;
        }

        PostRowViewHolder viewHolder = new PostRowViewHolder(mData.get(i).id,view, mListener, mDeleteListener);
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
        Button trashButton;
        Button commentButton;
        Button editButton;
        ImageView profileImage;
        ImageView mainImage;
        TextView userName;
        TextView location;
        TextView description;
        TextView creationDate;
        String mid;


        public PostRowViewHolder(String id,@NonNull View itemView, final OnItemClickListener listener, final OnDeleteClickListener deleteListener) {
            super(itemView);
            mid = id;
            likeButton = itemView.findViewById(R.id.row_like_button);
            commentButton = itemView.findViewById(R.id.row_comment_button);
            profileImage = itemView.findViewById(R.id.row_profile_image);
            mainImage = itemView.findViewById(R.id.row_main_image);
            userName = itemView.findViewById(R.id.row_username);
            location = itemView.findViewById(R.id.row_location);
            description = itemView.findViewById(R.id.row_description);
            creationDate = itemView.findViewById(R.id.row_creation_date);
            editButton = itemView.findViewById(R.id.row_edit_button);
            trashButton = itemView.findViewById(R.id.row_trash_button);

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

            trashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    if (deleteListener != null) {
                        if (index != RecyclerView.NO_POSITION) {
                            deleteListener.onClick(index);
                        }
                    }
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();



                    if (deleteListener != null) {
//                        if (index != RecyclerView.NO_POSITION) {
//                            deleteListener.onClick(index);
//                        }
                    }
                }
            });
        }

        private void deletePost(String id) {
/*
Fragment newFragment = new ExampleFragment();
FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
transaction.replace(R.id.fragment_container, newFragment);
transaction.addToBackStack(null);

// Commit the transaction
transaction.commit();
 */
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

            //Photo
            mainImage.setTag(post.id);
            mainImage.setImageResource(R.drawable.empty);

            if(post.getImage() != null) {
                Picasso.get().setIndicatorsEnabled(true);
                final String postID = post.id;

                Target target = new Target(){
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        if (mainImage.getTag() == postID) {
                            mainImage.setImageBitmap(bitmap);
                            mainImage.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        mainImage.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        mainImage.setVisibility(View.VISIBLE);
                    }
                };
                String a = post.getImage();
                Picasso.get().load(post.getImage())
                        .placeholder(R.drawable.empty)
                        .into(mainImage);

            }else{
                mainImage.setVisibility(View.INVISIBLE);
            }


            //Fields:
            location.setText(post.location);
            description.setText(post.description);
            creationDate.setText(post.creationDateStringFormat);


            //Like button image:
            @DrawableRes int drawable = R.drawable.like_unpressed;
            if (post.likes.containsKey(post.userID))
                drawable = R.drawable.like_pressed;
            likeButton.setBackgroundResource(drawable);
        }
    }

}


