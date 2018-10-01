package com.example.lvo.alertnotifications;

import android.content.Intent;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<NotificationModel> mDataset;
    public static String notificationStatus;
//    OnItemClickListener mItemClickListener;

//    public interface OnItemClickListener {
//        public void onItemClick(View view , int position);
//    }
//    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
//        this.mItemClickListener = mItemClickListener;
//    }


    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
//        public TextView mTextText;
        private TextView mTextTitle;
        private TextView textViewId;
        private Switch mTextSwitch;
        private ViewHolder(View v) {
            super(v);
            mTextSwitch = v.findViewById(R.id.switch1);
            mTextSwitch.setClickable(false);

//            mTextText = v.findViewById(R.id.row_text);
            mTextTitle = v.findViewById(R.id.row_title);
            textViewId = v.findViewById(R.id.textViewId);
        }
        public void bind(NotificationModel message) {
//            mTextText.setText(message.getDescription());
            mTextTitle.setText(message.getTitle());
            textViewId.setText(message.getId());

//            Log.d("!!!!!!!!!!!!!!!!!!!!!!!!!!!", message.getId().concat(" - ").concat(message.getTitle()).concat(" - ").concat(message.getComplete()) );
            if (Integer.parseInt( message.getComplete()) == 1) {
                mTextSwitch.setChecked(true);
            } else {
                mTextSwitch.setChecked(false);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<NotificationModel> myDataset) {
        mDataset = myDataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_text_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            final Switch sw = view.findViewById(R.id.switch1);
            final TextView tx = view.findViewById(R.id.textViewId);
            if (!sw.isChecked()) {
                App.getApi().checkNotification(FirebaseInstanceId.getInstance().getToken(), tx.getText().toString(), "checked").enqueue(new Callback<simpleResponceModel>() {
                    @Override
                    public void onResponse(Call<simpleResponceModel> call, Response<simpleResponceModel> response) {
                        //Данные успешно пришли, но надо проверить response.body() на null
                        Log.d("REQUEST", response.body().getStatus() );
                        sw.setChecked(true);
                    }
                    @Override
                    public void onFailure(Call<simpleResponceModel> call, Throwable t) {
                        Log.d("ERROR", t.toString() );

                    }
                });
            } else {
                Intent intent = new Intent(view.getContext(), IncidentActivity.class);
                intent.putExtra("notification_id",tx.getText().toString());
                view.getContext().startActivity(intent);
            }
            }
        });
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
// - get element from your dataset at this position
// - replace the contents of the view with that element
        holder.bind(mDataset.get(position));
//        holder.mTextView.setText(mDataset.get(position).getTitle());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}