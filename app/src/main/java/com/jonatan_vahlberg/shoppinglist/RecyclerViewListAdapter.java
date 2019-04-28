package com.jonatan_vahlberg.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
        listViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm = Realm.getDefaultInstance();

                realm.beginTransaction();
                mList.deleteFromRealm(index);
                realm.commitTransaction();
            }
        });
        listViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
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


    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView title, date;
        Button edit,delete;
        LinearLayout relativeLayout;

        public ListViewHolder(View itemView){
            super(itemView);

            this.title = itemView.findViewById(R.id.list_title);
            this.date = itemView.findViewById(R.id.list_date);
            this.edit = itemView.findViewById(R.id.list_edit);
            this.delete = itemView.findViewById(R.id.list_delete);
            this.relativeLayout = itemView.findViewById(R.id.list_parent_layout);

        }
    }
    }
