package ru.velkonost.robofest.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import ru.velkonost.robofest.FullScreenPhotoActivity;
import ru.velkonost.robofest.R;
import ru.velkonost.robofest.RecyclerView_Activity;
import ru.velkonost.robofest.model.Data_Model;

/**
 * Created by Andrey on 09.02.2017.
 */

public class RecyclerView_Adapter extends
        RecyclerView.Adapter<RecyclerViewHolder> {// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<String> arrayList;
    private Context context;

    public RecyclerView_Adapter(Context context,
                                ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
       // final Data_Model model = arrayList.get(position);

        final RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder
       // mainHolder.imageview.getLayoutParams().height = mainHolder.imageview.getWidth();
        String url = arrayList.get(position);
        mainHolder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlToOpen = "http://robofestomsk.ru/images/robofest2017_photo/bor"+(RecyclerView_Activity.page+position)+".jpg";

                Intent intent = new Intent(context, FullScreenPhotoActivity.class);
                intent.putExtra("Photo", 5);
                intent.putExtra("url", urlToOpen);
                context.startActivity(intent);
            }
        });


        Picasso
                .with(context)
                .load(url)
                .into(mainHolder.imageview, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        RecyclerView_Activity.checkImg = true;
                        RecyclerView_Activity.btnNextPage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        RecyclerView_Activity.checkImg = false;
                        mainHolder.cardView.setVisibility(View.INVISIBLE);
                        RecyclerView_Activity.btnNextPage.setVisibility(View.INVISIBLE);
                    }
                });


        // bitmap

        // setting title

      /*  mainHolder.title.setText(model.getTitle());

        mainHolder.imageview.setImageBitmap(image);*/



    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_gallery, viewGroup, false);
        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup);
        return listHolder;

    }


}