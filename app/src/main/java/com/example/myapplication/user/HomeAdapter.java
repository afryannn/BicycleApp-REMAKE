package com.example.myapplication.user;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.config.Config;
import com.example.myapplication.userActivity.HomeActivity;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ItemViewHolder> {
    private Context context;
    private List<HomeModel> mList;
    private boolean mBusy = false;
    private HomeActivity mAdminUserActivity;

    public HomeAdapter(Context context, List<HomeModel> mList, Activity HomeActivity) {
        this.context = context;
        this.mList = mList;
        this.mAdminUserActivity = (HomeActivity) HomeActivity;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_grid_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int i) {
        final HomeModel Amodel = mList.get(i);
        holder.bind(Amodel);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clearData() {
        int size = this.mList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mList.remove(0);
            }
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView ml_harga,ml_merk;
        private ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            ml_harga = itemView.findViewById(R.id.pricee);
            ml_merk = itemView.findViewById(R.id.txtvw);
            imageView = itemView.findViewById(R.id.imgvw);

        }

        private void bind(final HomeModel Amodel) {
            ml_merk.setText(Amodel.getMerk());
            ml_harga.setText(Amodel.getHarga());
            Picasso.get()
                    .load(Config.BASE_URL+"image/"+Amodel.getGambar())
                    .into(imageView);
        }

    }

}