package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String Tag = "RecyclerView";
    private Context iContext;
    private ArrayList<InfoFromDatabase> infoList;
    public static final String SHARED_PREFS= "sharedPrefs";
    public static final String Text = "Text";

    public CustomAdapter(Context iContext, ArrayList<InfoFromDatabase> infoList) {
        this.iContext = iContext;
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_layout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(infoList.get(position).getName());

        Glide.with(iContext).load(infoList.get(position).getUrl()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(iContext, recipiesInfo.class);
                String s  = holder.textView.getText().toString();
                i.putExtra("Name", s);

                SharedPreferences sharedPreferences = iContext.getSharedPreferences(Text, Context.MODE_PRIVATE);
                String meal = sharedPreferences.getString("Meal", "");
                i.putExtra("Meal", meal);




                iContext.startActivity(i);


            }
        });



    }





    @Override
    public int getItemCount() {
        return infoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.name);

        }
    }

}