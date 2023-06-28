package co.in.dadspint;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import co.in.dadspint.R;

import java.util.ArrayList;

public class SliderAdpter extends RecyclerView.Adapter<SliderAdpter.ViewHOlder> {

    Context context;
    ArrayList<SingleProductGallery> show_Image;

    Dialog dialog;
    RecyclerView recyclerImageSlider;
    SliderAdpter sliderAdpter;
    ViewPager2 showImageViewPager2;
    TextView [] dots;
    LinearLayout dots_container;
    ImageView backimage;

    public SliderAdpter(Context singleProductDesc, ArrayList<SingleProductGallery> singleProductGalleries) {

        this.context = singleProductDesc;
        this.show_Image = singleProductGalleries;
    }

    @NonNull
    @Override
    public SliderAdpter.ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_items,parent,false);
        return new ViewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdpter.ViewHOlder holder, int position) {

        SingleProductGallery slideImage = show_Image.get(position);

        String image = "https://dadspint.com/uploads/"+slideImage.getImage();
        Glide.with(context).load(image).into(holder.img_showImage);

        holder.img_showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(context);
                dialog.setContentView(R.layout.imagesliderdialog);
                showImageViewPager2 = dialog.findViewById(R.id.showImageViewPager2);
                dots_container = dialog.findViewById(R.id.dots_container);
                backimage = dialog.findViewById(R.id.backimage);
                sliderAdpter = new SliderAdpter(context,show_Image);
                showImageViewPager2.setAdapter(sliderAdpter);
                int arraysize = show_Image.size();
                dots = new TextView[arraysize];
                dotsIndicator();

                backimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });


                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //window.setBackgroundDrawableResource(R.drawable.dialogback);
            }
        });

    }

    @Override
    public int getItemCount() {
        return show_Image.size();
    }

    public class ViewHOlder extends RecyclerView.ViewHolder {
        ImageView img_showImage;
        public ViewHOlder(@NonNull View itemView) {
            super(itemView);

            img_showImage = itemView.findViewById(R.id.img_showImage);
        }
    }

    private void selectedIndicatorPosition(int position) {


        for(int i=0;i<dots.length;i++){


            if(i==position){

                dots[i].setTextColor(context.getResources().getColor(R.color.blue600));

            }else{

                dots[i].setTextColor(context.getResources().getColor(R.color.primary));
            }
        }

    }
    private void dotsIndicator() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);

        for(int i=0;i<dots.length;i++){

            dots[i] = new TextView(context);
            dots[i].setText(Html.fromHtml("&#9679;"));
            dots[i].setTextSize(20);
            dots[i].setPadding(5, 0, 5, 0);
            dots[i].setLayoutParams(params);
            dots_container.addView(dots[i]);
        }
    }
}
