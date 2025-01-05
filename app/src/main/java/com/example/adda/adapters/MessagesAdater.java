package com.example.adda.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.adda.R;
import com.example.adda.models.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdater extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECIVE = 2;
    private String sImage;
    private String rImage;

    public MessagesAdater(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout_item, parent, false);
            return new ReciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages messages = messagesArrayList.get(position);

        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            if (!messages.getMessage().isEmpty()){
                viewHolder.txtmessage.setText(messages.getMessage());
                viewHolder.txtmessage.setVisibility(View.VISIBLE);
                viewHolder.imgCard.setVisibility(View.GONE);
            }else {
                Glide.with(context) //1
                        .load(messages.getImageUrl())
                        .placeholder(R.drawable.no_img)
                        .error(R.drawable.no_img)
                        .transform(new RoundedCorners(10))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewHolder.imgMessages);
                viewHolder.imgCard.setVisibility(View.VISIBLE);
                viewHolder.txtmessage.setVisibility(View.GONE);
            }
            Picasso.get().load(sImage).placeholder(R.drawable.round_perm_identity_24).error(R.drawable.round_perm_identity_24).into(viewHolder.circleImageView);

        } else {
            ReciverViewHolder viewHolder = (ReciverViewHolder) holder;
            if (!messages.getMessage().isEmpty()){
                viewHolder.txtmessage.setText(messages.getMessage());
                viewHolder.txtmessage.setVisibility(View.VISIBLE);
                viewHolder.imgCard.setVisibility(View.GONE);
            }else {
                Glide.with(context) //1
                        .load(messages.getImageUrl())
                        .placeholder(R.drawable.no_img)
                        .error(R.drawable.no_img)
                        .transform(new RoundedCorners(10))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewHolder.imgMessages);
                viewHolder.imgCard.setVisibility(View.VISIBLE);
                viewHolder.txtmessage.setVisibility(View.GONE);
            }
            Picasso.get().load(rImage).placeholder(R.drawable.round_perm_identity_24).error(R.drawable.round_perm_identity_24).into(viewHolder.circleImageView);
        }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECIVE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txtmessage;
        ImageView imgMessages;
        CardView imgCard;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile_image);
            txtmessage = itemView.findViewById(R.id.txtMessages);
            imgMessages = itemView.findViewById(R.id.imgMessages);
            imgCard = itemView.findViewById(R.id.imgCard);

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setImgHead(String rImage, String sImage) {
        this.sImage = sImage;
        this.rImage = rImage;
        notifyDataSetChanged();
    }

    class ReciverViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView txtmessage;
        ImageView imgMessages;
        CardView imgCard;

        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profile_image);
            txtmessage = itemView.findViewById(R.id.txtMessages);
            imgMessages = itemView.findViewById(R.id.imgMessages);
            imgCard = itemView.findViewById(R.id.imgCard);

        }
    }
}
