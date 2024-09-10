package com.example.ecommercemobileapp2hand.Views.Utils;

import androidx.recyclerview.widget.DiffUtil;

import com.example.ecommercemobileapp2hand.Models.Product;

import java.util.ArrayList;

public class ProductDiffCallBack extends DiffUtil.Callback {
    private final ArrayList<Product> oldList;
    private final ArrayList<Product> newList;

    public ProductDiffCallBack(ArrayList<Product> oldList, ArrayList<Product> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getProduct_id() == newList.get(newItemPosition).getProduct_id();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
