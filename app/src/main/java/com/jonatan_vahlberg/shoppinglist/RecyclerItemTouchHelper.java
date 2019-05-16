package com.jonatan_vahlberg.shoppinglist;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/*
* Extends Simple Callback class which lets the Recycleview handle complex touches on static views
* */
public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    //Constructor
    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener){
        super(dragDirs,swipeDirs);
        this.listener = listener;
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {

        //super.onSelectedChanged(viewHolder, actionState);
        //Check if viewholder exsists
        if(viewHolder != null){
            //Is viewholder one of two accepted types
            if(viewHolder instanceof  RecyclerViewAdapter.ViewHolder){
                final View foregroundView = ((RecyclerViewAdapter.ViewHolder) viewHolder).foreground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
            else if(viewHolder instanceof  RecyclerViewListAdapter.ListViewHolder){
                final View foregroundVIew = ((RecyclerViewListAdapter.ListViewHolder)viewHolder).foreground;
                getDefaultUIUtil().onSelected(foregroundVIew);
            }

        }
    }

    //This is necessary code for swipe-able cells to draw over background layer
    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            //super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if(viewHolder instanceof  RecyclerViewAdapter.ViewHolder){
            final View foregroundView = ((RecyclerViewAdapter.ViewHolder) viewHolder).foreground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }
        else if(viewHolder instanceof  RecyclerViewListAdapter.ListViewHolder){
            final View foregroundView = ((RecyclerViewListAdapter.ListViewHolder)viewHolder).foreground;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if(viewHolder instanceof  RecyclerViewAdapter.ViewHolder){
            final View foregroundView = ((RecyclerViewAdapter.ViewHolder) viewHolder).foreground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }
        else if(viewHolder instanceof  RecyclerViewListAdapter.ListViewHolder){
            final View foregroundView = ((RecyclerViewListAdapter.ListViewHolder)viewHolder).foreground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }

    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof  RecyclerViewAdapter.ViewHolder){
            final View foregroundView = ((RecyclerViewAdapter.ViewHolder) viewHolder).foreground;
            getDefaultUIUtil().clearView(foregroundView);
        }
        else if(viewHolder instanceof  RecyclerViewListAdapter.ListViewHolder){
            final View foregroundView = ((RecyclerViewListAdapter.ListViewHolder)viewHolder).foreground;
            getDefaultUIUtil().clearView(foregroundView);
        }

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //Call for implemented Interface to check swipe-status
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
