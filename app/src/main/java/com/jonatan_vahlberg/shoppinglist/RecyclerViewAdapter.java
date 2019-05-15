package com.jonatan_vahlberg.shoppinglist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        final ShoppingItem item = mList.get(i);
        if(mList.get(i).isToBeDeleted()){
            viewHolder.itemView.setVisibility(View.GONE);
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            return;
        }
        else{
            viewHolder.itemView.setVisibility(View.VISIBLE);
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        viewHolder.name.setText(mList.get(i).getName());
        setLayout(viewHolder,i);
        final int index = i;
        //setLayout(viewHolder,item);

        //final ViewHolder holder = viewHolder;
        viewHolder.foreground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ARGH", "onClick: ARGH");
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                mList.get(index).setChecked(!(mList.get(index).isChecked()));
                setLayout(viewHolder,index);
                realm.commitTransaction();


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

    public void setLayout(ViewHolder holder, int  index){
        if((!mList.get(index).isChecked())){
            holder.topForeground.setBackgroundColor(Color.WHITE);
        }
        else{
            holder.topForeground.setBackgroundColor(mContext.getResources().getColor(R.color.lightGray));

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout parentLayout;
        TextView name;
        RelativeLayout foreground, background, topForeground;
        TextView amount;

        public ViewHolder(View itemView){
            super(itemView);
            this.parentLayout = itemView.findViewById(R.id.parent_layout);
            this.name = itemView.findViewById(R.id.name);
            this.foreground = itemView.findViewById(R.id.foreground);
            this.topForeground = itemView.findViewById(R.id.top_foreground);
            this.background = itemView.findViewById(R.id.background);
            this.amount = itemView.findViewById(R.id.amount);
        }
    }
}
