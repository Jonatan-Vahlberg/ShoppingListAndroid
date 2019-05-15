package com.jonatan_vahlberg.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;

public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerViewListAdapter.ListViewHolder> {
    private Context mContext;
    private RealmResults<ShoppingList> mList;
    private Realm realm;

    public RecyclerViewListAdapter(Context context, RealmResults<ShoppingList> list){
        mContext = context;
        this.mList = list;
        realm = Realm.getDefaultInstance();
        this.mList = realm.where(ShoppingList.class).findAll();
    }

    @NonNull
    @Override
    public RecyclerViewListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_view_list,viewGroup,false);
        ListViewHolder viewHolder = new ListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, int i) {
        if(mList.get(i).isToBeDeleted()){
            listViewHolder.itemView.setVisibility(View.GONE);
            listViewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            return;
        }
        else{
            listViewHolder.itemView.setVisibility(View.VISIBLE);
            listViewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        listViewHolder.title.setText(mList.get(i).getName());
        listViewHolder.date.setText(mList.get(i).getDate());
        final int index = i;
        final long id = mList.get(i).getId();
        listViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AddListActivity.class);
                intent.putExtra("Update",id);

                v.getContext().startActivity(intent);

            }
        });
        listViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                intent.putExtra("id",id);

                v.getContext().startActivity(intent);
            }
        });
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
        if(!(mList.get(position).isToBeDeleted())) return;
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        mList.deleteFromRealm(position);
        realm.commitTransaction();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView title, date;
        ImageButton edit;
        LinearLayout foreground;
        RelativeLayout background;

        public ListViewHolder(View itemView){
            super(itemView);

            this.title = itemView.findViewById(R.id.list_title);
            this.date = itemView.findViewById(R.id.list_date);
            this.edit = itemView.findViewById(R.id.list_edit);
            this.foreground = itemView.findViewById(R.id.foreground);
            this.background = itemView.findViewById(R.id.background);

        }
    }
    }
