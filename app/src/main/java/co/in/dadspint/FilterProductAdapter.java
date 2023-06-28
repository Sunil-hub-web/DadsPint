package co.in.dadspint;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import co.in.dadspint.R;

import java.util.ArrayList;

public class FilterProductAdapter extends RecyclerView.Adapter<FilterProductAdapter.ViewHolder> {

    ArrayList<ProductFilterModel> productFilterModels;
    Context context;

    public FilterProductAdapter(ArrayList<ProductFilterModel> productDataModel3, FragmentActivity activity) {

        this.productFilterModels = productDataModel3;
        this.context = activity;

    }

    @NonNull
    @Override
    public FilterProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.schooluniformpage,parent,false);
        return new FilterProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterProductAdapter.ViewHolder holder, int position) {


        ProductFilterModel product = productFilterModels.get(position);

        holder.uniform_name1.setText(product.product_name);
        holder.restt_price1.setText("Rs. "+product.sales_price);
        holder.restt_price2.setText("Rs. "+product.regular_price);

        holder.restt_price2.setPaintFlags(holder.restt_price2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        String imageUrl = "https://dadspint.com/uploads/"+product.getPrimary_image();
        Glide.with(context).load(imageUrl).into(holder.imag_uniform);
    }

    @Override
    public int getItemCount() {
        return productFilterModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imag_uniform;
        TextView uniform_name1,restt_price1,restt_price2,tv_minus,tv_count,tv_plus;
        LinearLayout lin_addCart,lin_add_cart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_minus = (TextView) itemView.findViewById(R.id.tv_minus);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            tv_plus = (TextView) itemView.findViewById(R.id.tv_plus);
            lin_addCart = (LinearLayout) itemView.findViewById(R.id.lin_addCart);
            lin_add_cart = (LinearLayout) itemView.findViewById(R.id.lin_add_cart);
            uniform_name1 = (TextView) itemView.findViewById(R.id.uniform_name1);
            restt_price1 = (TextView) itemView.findViewById(R.id.restt_price1);
            restt_price2 = (TextView) itemView.findViewById(R.id.restt_price2);
            imag_uniform = (ImageView) itemView.findViewById(R.id.imag_uniform);
        }
    }
}
