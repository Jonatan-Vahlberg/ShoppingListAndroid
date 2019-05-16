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

    //constructor
    public RecyclerViewAdapter(Context context, RealmList<ShoppingItem> list){
        //Get realm Object from realm
        mContext = context;
        this.mList = list;
        realm = Realm.getDefaultInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Create Recylerview cell
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_view_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        //Bind data to recycler view cell
        final ShoppingItem item = mList.get(i);
        //While in the process of deletion make row invisible and removed
        if(mList.get(i).isToBeDeleted()){
            viewHolder.itemView.setVisibility(View.GONE);
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            return;
        }
        //If item has been reverted from deletion show it again
        else{
            viewHolder.itemView.setVisibility(View.VISIBLE);
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }


        //BINDING DATA
        viewHolder.name.setText(mList.get(i).getName());
        setLayout(viewHolder,i);
        final int index = i;

        //On click / check for item
        viewHolder.foreground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Realm sync transaction update value
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
        //Realm transaction for updating toBeDeleted
        realm.beginTransaction();
        mList.get(position).setToBeDeleted(true);
        realm.commitTransaction();
        notifyDataSetChanged();
    }

    public void restoreItem(int position) {
        //Realm sync transaction for updating item to be restored
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        mList.get(position).setToBeDeleted(false);
        realm.commitTransaction();
        notifyDataSetChanged();
    }

    public void willDeleteItem(int position){
        //if item is not still to be deleted
        if(!(mList.get(position).isToBeDeleted())) return;;
        //else Permanently remove item from realm NOT REVERSIBLE
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        mList.deleteFromRealm(position);
        realm.commitTransaction();
    }

    public void setLayout(ViewHolder holder, int  index){
        //Set layout based on item checked status
        if((!mList.get(index).isChecked())){
            holder.topForeground.setBackgroundColor(Color.WHITE);
        }
        else{
            holder.topForeground.setBackgroundColor(mContext.getResources().getColor(R.color.lightGray));

        }
    }


    //Viewholder used for Adapter Creation
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
