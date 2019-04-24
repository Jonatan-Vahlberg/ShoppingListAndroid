package com.jonatan_vahlberg.shoppinglist;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private RealmResults<Product> mList;
    private Realm realm;

    public RecyclerViewAdapter(Context context, RealmResults<Product> list){
        mContext = context;
        this.mList = list;
        realm = Realm.getDefaultInstance();
        this.mList = realm.where(Product.class).findAll();
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
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView name;
        Button check;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView){
            super(itemView);
            this.image = itemView.findViewById(R.id.image);
            this.name = itemView.findViewById(R.id.name);
            this.check = itemView.findViewById(R.id.check);
            this.relativeLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
