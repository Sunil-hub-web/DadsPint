package com.example.schooluniformapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {

    Context context;
    ArrayList<OrderSummary_ModelClass> ordersummary;

    public OrderSummaryAdapter(Context context, ArrayList<OrderSummary_ModelClass> ordersummary) {

        this.context = context;
        this.ordersummary = ordersummary;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkoutorderdet,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {

        OrderSummary_ModelClass order_summary = ordersummary.get(position);

        String image = "https://dadspint.com/uploads/"+order_summary.getProduct_img();
        Picasso.with(context).load(image).into(holder.productImage);
        holder.productName.setText(order_summary.getProduct_name());

        String quantity = order_summary.getQuantity();
        String salesPrice = order_summary.getSale_price();

        Double d_quantity = Double.valueOf(quantity);
        Double d_salesPrice = Double.valueOf(salesPrice);
        Double amount = d_quantity * d_salesPrice;
        String str_amount = String.valueOf(amount);

        holder.total.setText(str_amount);

    }

    @Override
    public int getItemCount() {
        return ordersummary.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName,total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            total = itemView.findViewById(R.id.total);

        }
    }
}
