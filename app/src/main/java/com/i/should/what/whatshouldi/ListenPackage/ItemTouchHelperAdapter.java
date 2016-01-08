package com.i.should.what.whatshouldi.ListenPackage;

/**
 * Created by ryan on 8/1/2015.
 */
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
