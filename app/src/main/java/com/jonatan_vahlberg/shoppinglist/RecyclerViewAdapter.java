package com.jonatan_vahlberg.shoppinglist;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private RealmList<ShoppingItem> mList;
    private Realm realm;

    public RecyclerViewAdapter(Context context, RealmList<ShoppingItem> list){
        mContext = context;
        this.mList = list;
        realm = Realm.getDefaultInstance();
        //this.mList = realm.where(ShoppingItem.class).findAll();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_view_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        if(mList.get(i).isToBeDeleted()){
            viewHolder.itemView.setVisibility(View.GONE);
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            return;
        }
        else{
            viewHolder.itemView.setVisibility(View.VISIBLE);
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        Glide.with(mContext)
                .asBitmap()
                .load(mList.get(i).getImage())
                .into(viewHolder.image);
        viewHolder.name.setText(mList.get(i).getName());
        if (mList.get(i).isChecked()){

            viewHolder.check.setText("✔️");
            viewHolder.check.setBackgroundColor(Color.parseColor("#9fd7fb"));
        }
        else{
            viewHolder.check.setText("️❌");
            viewHolder.check.setBackgroundColor(Color.WHITE);
        }


        final int index = i;
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                realm.beginTransaction();
                if(mList.get(index).isChecked()){

                    mList.get(index).setChecked(false);
                    button.setText("️❌");
                    button.setBackgroundColor(Color.WHITE);
                }
                else{
                    mList.get(index).setChecked(true);
                    button.setText("✔️");
                    button.setBackgroundColor(Color.parseColor("#9fd7fb"));
                }
                realm.commitTransaction();
            }
        });
        viewHolder.check.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                realm = Realm.getDefaultInstance();

                realm.beginTransaction();
                mList.deleteFromRealm(index);
                realm.commitTransaction();
                return true;
            }
        });
        viewHolder.amount.setText(mList.get(i).getAmount()+mList.get(i).getAmountType());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void itemToBeDeleted (int position) {

        realm.beginTransaction();
        mList.get(position).setToBeDeleted(true);
        realm.commitTransaction();
        notifyDataSetChanged();
        //notifyItemRemoved(position);
    }

    public void restoreItem(int position) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        mList.get(position).setToBeDeleted(false);
        realm.commitTransaction();
        notifyDataSetChanged();
    }

    public void willDeleteItem(int position){
        if(!(mList.get(position).isToBeDeleted())) return;;
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        mList.deleteFromRealm(position);
        realm.commitTransaction();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout parentLayout;
        CircleImageView image;
        TextView name;
        Button check;
        RelativeLayout foreground, background;
        TextView amount;

        public ViewHolder(View itemView){
            super(itemView);
            this.parentLayout = itemView.findViewById(R.id.parent_layout);
            this.image = itemView.findViewById(R.id.image);
            this.name = itemView.findViewById(R.id.name);
            this.check = itemView.findViewById(R.id.check);
            this.foreground = itemView.findViewById(R.id.foreground);
            this.background = itemView.findViewById(R.id.background);
            this.amount = itemView.findViewById(R.id.amount);

        }
    }
}
