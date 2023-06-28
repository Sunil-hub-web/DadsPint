package com.example.schooluniformapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    Context context;
    ArrayList<CategoryModelClass> categorylistarray;
    public CategoryListAdapter(ArrayList<CategoryModelClass> allcategorymode, FragmentActivity activity) {

        this.context = activity;
        this.categorylistarray = allcategorymode;
    }

    @NonNull
    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.categorylistpage, parent, false);
        return new CategoryListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.ViewHolder holder, int position) {

        CategoryModelClass category = categorylistarray.get(position);

        String imageUrl = "https://dadspint.com/uploads/" + category.getCat_img();
        Glide.with(context).load(imageUrl).into(holder.categoryimage);

        holder.categoryname.setText(category.cat_name);

        holder.lincategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SubCategoryPage subCategoryPage = new SubCategoryPage();
                Bundle args = new Bundle();
                args.putString("categoryID", category.getCat_id());
                args.putString("categoryName", category.getCat_name());
                subCategoryPage.setArguments(args);
                FragmentTransaction transaction =((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framLayout, subCategoryPage); // Add your fragment class
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorylistarray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryimage;
        TextView categoryname;
        LinearLayout lincategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryimage = itemView.findViewById(R.id.categoryimage);
            categoryname = itemView.findViewById(R.id.categoryname);
            lincategory = itemView.findViewById(R.id.lincategory);
        }
    }
}
