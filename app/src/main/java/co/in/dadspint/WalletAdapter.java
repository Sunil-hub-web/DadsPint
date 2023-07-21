package co.in.dadspint;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {

    Context context;
    ArrayList<WalletModel> wallet_Models;
    public WalletAdapter(ArrayList<WalletModel> walletModels, FragmentActivity activity) {

        this.context = activity;
        this.wallet_Models = walletModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WalletModel wallet = wallet_Models.get(position);

        holder.date.setText(Html.fromHtml("<font color='#FF3700B3'>Date/Time :<br></font>"+wallet.getCreated_date()));
        holder.wallet_amount.setText(Html.fromHtml("<font color='#FF3700B3'>Wallet Amount :<br></font>"+wallet.getWallet_amount()));
    }

    @Override
    public int getItemCount() {
        return wallet_Models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date,wallet_amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            wallet_amount = itemView.findViewById(R.id.wallet_amount);
        }
    }
}
