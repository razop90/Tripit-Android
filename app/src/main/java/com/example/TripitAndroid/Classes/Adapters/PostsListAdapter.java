package com.example.TripitAndroid.Classes.Adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.example.TripitAndroid.Classes.Post;
import com.example.TripitAndroid.Classes.UserInfo;
import com.example.TripitAndroid.R;
import com.example.TripitAndroid.models.Model;
import java.util.ArrayList;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostRowViewHolder> {
    ArrayList<Post> mData;
    OnItemClickListener mListener;

    public PostsListAdapter(ArrayList<Post> data) {
        mData = data;
    }

    public interface OnItemClickListener {
        void onClick(int index);
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

        public void bind(final Post post) {

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
