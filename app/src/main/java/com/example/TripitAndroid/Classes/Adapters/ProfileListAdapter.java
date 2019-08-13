package com.example.TripitAndroid.Classes.Adapters;

import com.example.TripitAndroid.Fragments.AddPostFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.TripitAndroid.Classes.Post;
import com.example.TripitAndroid.Classes.UserInfo;
import com.example.TripitAndroid.R;
import com.example.TripitAndroid.models.Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.PostRowViewHolder> {
    ArrayList<Post> mData;
    ProfileListAdapter.OnItemClickListener mListener;
    ProfileListAdapter.OnDeleteClickListener mDeleteListener;
    ProfileListAdapter.OnEditClickListener mEditListener;

    public ProfileListAdapter(ArrayList<Post> data) {
        mData = data;
    }

    public interface OnItemClickListener {
        void onClick(int index);
    }

    public interface OnDeleteClickListener {
        void onClick(int index);
    }

    public interface OnEditClickListener {
        void onClick(View view, int index);
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

        OnEditClickListener editListener = new OnEditClickListener() {
            @Override
            public void onClick(View view, int index) {
                Post post = mData.get(index);

                if(post != null && view != null) {
                    AppCompatActivity activity = (AppCompatActivity)view.getContext();
                    final AddPostFragment myFragment = new AddPostFragment();
                    myFragment.setPost(post, post.id);
                    activity.getSupportFragmentManager().beginTransaction().add(R.id.main_navigation, myFragment).commit();
                }
            }
        };

        if(mDeleteListener == null) {
            mDeleteListener = deleteListener;
        }

        if(mEditListener == null) {
            mEditListener = editListener;
        }

        PostRowViewHolder viewHolder = new PostRowViewHolder(mData.get(i),mData.get(i).id,view, mListener, mDeleteListener, mEditListener);
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
        Post mPost;


        public PostRowViewHolder(Post p, String id,@NonNull View itemView, final OnItemClickListener listener, final OnDeleteClickListener deleteListener, final OnEditClickListener editListener) {
            super(itemView);
            mid = id;
            mPost = p;
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
                    if (editListener != null) {
                        if (index != RecyclerView.NO_POSITION) {
                            editListener.onClick(v, index);
                        }
                    }
                }
            });
        }

        public void bind(Post post) {
            // user photo
            profileImage.setTag(post.id);
            profileImage.setImageResource(R.drawable.default_profile);
            setUserInfo(post.userID, R.drawable.default_profile);

            // main photo
            mainImage.setTag(post.id);
            mainImage.setImageResource(R.drawable.empty);
            setImage(mainImage, post.getImage(), R.drawable.empty);

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

        private void setImage(final ImageView image, String path, int defaultImageIndex) {
            if(path != null && path.trim().length() != 0) {

                Model.instance.getImageBitMap(path, new Model.GetImageBitMapListener() {
                    @Override
                    public void onComplete(Bitmap bitMap) {
                        image.setImageBitmap(bitMap);
                    }

                    @Override
                    public void fail() { }
                });
            }
        }

        private void setUserInfo(final String userId, final int defaultImageIndex) {
            Model.instance.getUserInfo(userId, new Model.OnUserInfoUpdated() {
                @Override
                public void onUserInfoUpdated(UserInfo userInfo) {
                    if (userInfo != null) {
                        userName.setText(userInfo.displayName);
                        setImage(profileImage, userInfo.profileImageUrl, defaultImageIndex);
                    }
                    else {
                        userName.setText(userId);
                        setUserInfo(userId, defaultImageIndex);
                    }
                }
            });
        }
    }
}


