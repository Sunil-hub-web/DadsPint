package co.in.dadspint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import co.in.dadspint.R;

import java.util.ArrayList;

public class ViewAddressAdapter extends RecyclerView.Adapter<ViewAddressAdapter.ViewHolder> {

    ArrayList<ViewAddressModel> viewAddress_Model = new ArrayList<ViewAddressModel>();
    Context context;
    public static String addressId,all_values,checkOut1,shippingcharge;
    int index;
    public ViewAddressAdapter(ArrayList<ViewAddressModel> viewAddress_model, Context context, String checkOut) {

        this.viewAddress_Model = viewAddress_model;
        this.context = context;
        this.checkOut1 = checkOut;

    }

    @NonNull
    @Override
    public ViewAddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addressdetailview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAddressAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ViewAddressModel addressView = viewAddress_Model.get(position);

        holder.addressdetails.setText(addressView.getFirst_name()+", "+addressView.getLast_name()+", "+addressView.getNumber() +"\n"+
                addressView.getEmail()+", "+addressView.getSchool_name()+"\n"+
                addressView.getState()+", "+addressView.getCity_name()+addressView.getPincode()+"\n"+
                addressView.getAddress1()+", "+addressView.getAdress2());

        holder.shoppingCharges.setText("Shopping Charges : "+addressView.getShipping_price());

        /*if (checkOut1.equals("")){

            holder.btn_SelectAddress.setVisibility(View.GONE);

        }else{

            holder.btn_SelectAddress.setVisibility(View.VISIBLE);
        }*/

        holder.rel_Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addressId = addressView.getAddress_id();

                index = position;
                notifyDataSetChanged();
                all_values = holder.addressdetails.getText().toString().trim();
                shippingcharge = addressView.getShipping_price();


               // holder.rel_Click.setBackgroundResource(R.drawable.selectaddressback);
              //  holder.rel_Click.setElevation(15);

                //Toast.makeText(context, all_values, Toast.LENGTH_SHORT).show();

            }
        });

        if(index == position){

            holder.rel_Click.setBackgroundResource(R.drawable.selectaddressback);
            holder.rel_Click.setElevation(15);

        }
        else {

            holder.rel_Click.setBackgroundResource(R.drawable.homecard_back1);
            holder.rel_Click.setElevation(5);
        }

    }

    public String addressvalue(){

        return all_values;
    }

    public String shipping(){

        return shippingcharge;
    }

    @Override
    public int getItemCount() {
        return viewAddress_Model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView addressdetails,shoppingCharges;
        ImageView btn_Delete;
       // MaterialButton btn_SelectAddress;
        RelativeLayout rel_Click;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_Delete = itemView.findViewById(R.id.btn_Delete);
            addressdetails = itemView.findViewById(R.id.addressdetails);
            shoppingCharges = itemView.findViewById(R.id.shoppingCharges);
           // btn_SelectAddress = itemView.findViewById(R.id.btn_SelectAddress);
            rel_Click = itemView.findViewById(R.id.rel_Click);
        }
    }
}
