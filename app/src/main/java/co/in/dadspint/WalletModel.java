package co.in.dadspint;

public class WalletModel {

    String wallet_id,wallet_amount,wallet_status,user_id,created_date;

    public WalletModel(String wallet_id, String wallet_amount, String wallet_status, String user_id,String created_date) {
        this.wallet_id = wallet_id;
        this.wallet_amount = wallet_amount;
        this.wallet_status = wallet_status;
        this.user_id = user_id;
        this.created_date = created_date;
    }

    public String getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(String wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getWallet_amount() {
        return wallet_amount;
    }

    public void setWallet_amount(String wallet_amount) {
        this.wallet_amount = wallet_amount;
    }

    public String getWallet_status() {
        return wallet_status;
    }

    public void setWallet_status(String wallet_status) {
        this.wallet_status = wallet_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
