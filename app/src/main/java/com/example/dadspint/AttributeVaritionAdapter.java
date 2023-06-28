package com.example.dadspint;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttributeVaritionAdapter extends RecyclerView.Adapter<AttributeVaritionAdapter.ViewHolder> {

    ArrayList<SingleProducuAttribute> singleProducuAttributes;
    ArrayList<SingleProductVariations> singleProductVariations = new ArrayList<>();
    ;
    Context context;
    ArrayList<String> varitionnameId = new ArrayList<>();
    ArrayList<Integer> varitionnameId2 = new ArrayList<>();

    ArrayList<VaritionNameDet> varitionnameId1 = new ArrayList<>();

    public static String varitionprice = "", varitionName = "", strnamedetails = "NotSelect",
            varition_id,strPrice,services_Id;
    ArrayList<String> str_singleProductVariations = new ArrayList<>();
    HashMap<String, String> hash_singleProductVariations = new HashMap<>();

    public AttributeVaritionAdapter(Context singleProductDesc, ArrayList<SingleProducuAttribute>
            singleProducuAttributes) {

        this.context = singleProductDesc;
        this.singleProducuAttributes = singleProducuAttributes;

    }

    @NonNull
    @Override
    public AttributeVaritionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attributevarition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttributeVaritionAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position1) {

        SingleProducuAttribute singlattribut = singleProducuAttributes.get(position1);
        holder.attributeName.setText(singlattribut.getAttribute_name());

        singleProductVariations = singlattribut.getSingleProductVariations();
        str_singleProductVariations = singlattribut.getStr_singleProductVariations();
        hash_singleProductVariations = singlattribut.getHash_singleProductVariations();

        varitionprice = singlattribut.getSalesprice();

//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context.getApplicationContext(),
//                R.layout.spiner_text, str_singleProductVariations);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        holder.tv_variationName.setThreshold(1);//will start working from first character
//        holder.tv_variationName.setAdapter(adapter2);
        //holder.tv_variationName.setTextColor(Color.RED);

        VeritionNameAdapter adapter = new VeritionNameAdapter(context, R.layout.spiner_text
                , singleProductVariations);
        holder.tv_variationName.setAdapter(adapter);

     /*   ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(context,
                R.layout.spiner_text, str_singleProductVariations);
        dataAdapterVehicle.setDropDownViewResource(R.layout.spiner_text);
        holder.tv_variationName.setAdapter(dataAdapterVehicle);
*/

        Log.d("varitionName", str_singleProductVariations.toString());

        holder.tv_variationName.setSelection(-1, true);


       // holder.tv_variationName.setSelection(0,false);

        holder.tv_variationName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            /*    try {

                    varitionName = holder.tv_variationName.getItemAtPosition(holder.tv_variationName.getSelectedItemPosition()).toString();

                    if (varitionName.equalsIgnoreCase("Select VerName")) {

                        varition_id = "";

                    } else {

                        varition_id = hash_singleProductVariations.get(varitionName);

                        varitionnameId.add(varition_id);

                        Log.d("varitionArray",varitionnameId.toString());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/


            /*        VaritionNameDet varitionNameDet = new VaritionNameDet(
                            srtposition,varition_id
                    );*/

                try {

                    SingleProductVariations mystate = (SingleProductVariations) parent.getSelectedItem();

                    varition_id = mystate.getVariation_id();
                    varitionName = mystate.getVariation_name();

                    Log.d("R_Pincode", varition_id);

                //    Toast.makeText(context, ""+position1, Toast.LENGTH_SHORT).show();

                    if (varitionnameId2.size() !=0 ){

                        if (varitionnameId2.contains(position1)){

                            varitionnameId.set(position1, varition_id);

                        }else{

                            varitionnameId.add(varition_id);
                        }

                    }else{

                        varitionnameId2.add(position1);
                        varitionnameId.add(varition_id);

                    }

                    int size = singleProducuAttributes.size();
                    int size1 = varitionnameId.size();

                    Log.d("varitionArray1", String.valueOf(size));
                    Log.d("variationArray2", String.valueOf(size1));
                    Log.d("varitionArray3", varitionnameId.toString());
                    Log.d("varitionArray4", varitionnameId2.toString());

                    if (size == size1) {

                     //   Toast.makeText(context, "Show Price", Toast.LENGTH_SHORT).show();

                        StringBuffer sb = new StringBuffer();

                        if (varitionnameId.size() != 0) {

                            for (String s : varitionnameId) {

                                sb.append(s);
                                sb.append(", ");
                            }

                            services_Id = sb.toString();

                            // remove last character (,)
                            services_Id = services_Id.substring(0, services_Id.length() - 2);

                            getvaritionPrice(singlattribut.getProduct_id(), services_Id);

                            Log.d("purposeofvisitdata", services_Id);
                            Log.d("purposeofvisi", singlattribut.getProduct_id() + "," + services_Id);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
            //varitionnameId.clear();

    }

    public String getvaritionPrice(){

        return services_Id;
    }

    @Override
    public int getItemCount() {
        return singleProducuAttributes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView attributeName;
        Spinner tv_variationName;
        LinearLayout rootContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            attributeName = itemView.findViewById(R.id.attributeName);
            tv_variationName = itemView.findViewById(R.id.tv_variationName);

            rootContainer = itemView.findViewById(R.id.rootContainer);
        }
    }
    public void getvaritionPrice(String product_id,String variation){

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Get price Details....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrl.variation_price, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String error = jsonObject.getString("error");
                    String messages = jsonObject.getString("messages");

                    Log.d("responsedetails",response.toString());

                    if (status.equals("200")){

                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String status_message = jsonObject_message.getString("status");

                        JSONArray jsonArray_price = new JSONArray(status_message);

                        if (jsonArray_price.length() != 0){

                            for (int i=0;i<jsonArray_price.length();i++){

                                JSONObject jsonObject1_price = jsonArray_price.getJSONObject(0);
                                strPrice = jsonObject1_price.getString("price");

                                SingleProductFragment.sales_price.setText("Rs  "+strPrice);
                                SingleProductFragment.salesprice = strPrice;
                            }
                        }else{

                            Toast.makeText(context, "Price not Found1", Toast.LENGTH_SHORT).show();

                            SingleProductFragment.sales_price.setText("Rs  "+varitionprice);
                            SingleProductFragment.salesprice = varitionprice;
                        }

                    }else{

                        JSONObject jsonObject_message = new JSONObject(messages);
                        String responsecode = jsonObject_message.getString("responsecode");
                        String status_message = jsonObject_message.getString("status");

                        Toast.makeText(context, "Price not Found", Toast.LENGTH_SHORT).show();

                        SingleProductFragment.sales_price.setText("Rs  "+varitionprice);
                        SingleProductFragment.salesprice = varitionprice;
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();

                SingleProductFragment.sales_price.setText("Rs  "+varitionprice);
                SingleProductFragment.salesprice = varitionprice;
            }
        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("variation",variation);
                params.put("product_id",product_id);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
