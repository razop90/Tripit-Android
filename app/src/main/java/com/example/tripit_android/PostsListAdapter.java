package com.example.tripit_android;

//import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

public class PostsListAdapter  extends RecyclerView.Adapter<PostsListAdapter.StudentRowViewHolder> {
    Vector<String> mData;
    OnItemClickListener mListener;

    public PostsListAdapter(Vector<String> data) {
        mData = data;
    }
    public interface OnItemClickListener{
        void onClick(int index);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public StudentRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.post_row, viewGroup,false);
        StudentRowViewHolder viewHolder = new StudentRowViewHolder(view,mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentRowViewHolder studentRowViewHolder, int i) {
        String str = mData.elementAt(i);
        studentRowViewHolder.bind(str);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class StudentRowViewHolder extends RecyclerView.ViewHolder {
        ImageView mAvatar;
        TextView mName;
        TextView mId;
        CheckBox mCb;

        public StudentRowViewHolder(@NonNull View itemView,
                                    final OnItemClickListener listener) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.strow_avatar_img);
            mName = itemView.findViewById(R.id.strow_name_tv);
            mId = itemView.findViewById(R.id.strow_id_tv);
            mCb = itemView.findViewById(R.id.strow_checkbox);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    if (listener != null){
                        if (index != RecyclerView.NO_POSITION) {
                            listener.onClick(index);
                        }
                    }
                }
            });

            mCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int index = getAdapterPosition();
                    Log.d("TAG","chewckbox value change to: " + isChecked + " on item " + index);
                }
            });
        }

        public void bind(String str){
            mName.setText(str);
            mId.setText("id " + str);
            mCb.setChecked(false);
        }
    }
}
