package co.in.dadspint;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import co.in.dadspint.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private final Context context;
    private List<BannerModelClass> mSliderItems = new ArrayList<>();

    public SliderAdapterExample(Context context, List<BannerModelClass> sliderItemList) {
        this.context = context;
        this.mSliderItems = sliderItemList;
    }

    /*public void renewItems(List<SliderItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }*/

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        BannerModelClass sliderItem = mSliderItems.get(position);

        //viewHolder.textViewDescription.setText(sliderItem.getDescription());
        //viewHolder.textViewDescription.setTextSize(16);
       // viewHolder.textViewDescription.setTextColor(Color.WHITE);

        String imageUrl = "https://dadspint.com/uploads/"+sliderItem.getImage();

        Glide.with(context).load(imageUrl).into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ProductDetailsFragment();
                Bundle args = new Bundle();
                args.putString("YourKey", "SchoolUniform");
                fragment.setArguments(args);
                FragmentTransaction transaction =((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.framLayout, fragment); // Add your fragment class
                transaction.addToBackStack(null);
                transaction.commit();

                DeshBoardActivity.menu.setVisibility(View.GONE);
                DeshBoardActivity.backimage.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            //imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
           // textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;


        }
    }

}