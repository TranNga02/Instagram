package com.example.instagram.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.ui.model.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private Context context;
    private ArrayList<Message> messages;

    private FirebaseAuth firebaseAuth;

    public MessageAdapter(Context context) {
        this.context = context;
        this.messages = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void Add(Message message) {
        messages.add(message);
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2) {
                return message1.getTimestamp().compareTo(message2.getTimestamp());
            }
        });
        notifyDataSetChanged();
    }

    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_send_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(firebaseAuth.getUid())) {
            holder.mainLayout.removeAllViews();

            View newView = LayoutInflater.from(context).inflate(R.layout.item_container_send_message, holder.mainLayout, false);

            holder.mainLayout.addView(newView);

            TextView messageTextView = newView.findViewById(R.id.text_message);
            messageTextView.setText(message.getMessage());
            TextView datetime = newView.findViewById(R.id.text_datetime);

            if (position < messages.size()-1  && isSameTime(messages.get(position + 1), message) && message.getSenderId().equals(messages.get(position).getSenderId())) {
                datetime.setVisibility(View.GONE); // Ẩn thời gian
            } else {
                datetime.setVisibility(View.VISIBLE);
                datetime.setText(format(message.getTimestamp()));
            }

        } else {
            holder.mainLayout.removeAllViews();

            View newView = LayoutInflater.from(context).inflate(R.layout.item_container_receive_message, holder.mainLayout, false);

            holder.mainLayout.addView(newView);

            TextView messageTextView = newView.findViewById(R.id.text_message);
            messageTextView.setText(message.getMessage());

            TextView datetime = newView.findViewById(R.id.text_datetime);
            if (position < messages.size()-1  && isSameTime(messages.get(position + 1), message) && message.getSenderId().equals(messages.get(position).getSenderId())) {
                datetime.setVisibility(View.GONE); // Ẩn thời gian
            } else {
                datetime.setVisibility(View.VISIBLE); // Hiển thị thời gian
                datetime.setText(format(message.getTimestamp()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    private boolean isSameTime(Message message1, Message message2) {

        return format(message1.getTimestamp()).equals(format(message2.getTimestamp()));
    }
    private String format(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateTime = dateFormat.format(date);
        return dateTime;
    }
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView datetime;
        public LinearLayout mainLayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_message);
            mainLayout = itemView.findViewById(R.id.mainMessageLayout);
            datetime = itemView.findViewById(R.id.text_datetime);
        }
    }


}

