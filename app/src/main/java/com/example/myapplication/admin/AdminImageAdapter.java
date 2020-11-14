package com.example.myapplication.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adminActivity.ActivityViewBicycle;
import com.example.myapplication.adminActivity.EditCustomer;
import com.example.myapplication.adminActivity.EditItem;
import com.example.myapplication.config.Config;
import com.example.myapplication.R;
import com.example.myapplication.user.HomeModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminImageAdapter extends RecyclerView.Adapter<AdminImageAdapter.ItemViewHolder> {
    private Context context;
    private List<HomeModel> mList;
    private boolean mBusy = false;
    private ActivityViewBicycle mAdminUserActivity;

    public AdminImageAdapter(Context context, List<HomeModel> mList, Activity ActivityViewBicycle) {
        this.context = context;
        this.mList = mList;
        this.mAdminUserActivity = (ActivityViewBicycle) ActivityViewBicycle;
    }

    @NonNull
    @Override
    public AdminImageAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_admin_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminImageAdapter.ItemViewHolder holder, int i) {
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
        private Button btn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            ml_harga = itemView.findViewById(R.id.pricee);
            ml_merk = itemView.findViewById(R.id.txtvw);
            imageView = itemView.findViewById(R.id.imgvw);
            btn = itemView.findViewById(R.id.ubahitem);
        }

        public void bind(final HomeModel Amodel) {
            ml_merk.setText(Amodel.getMerk());
            ml_harga.setText(Amodel.getHarga());
            Picasso.get()
                    .load(Config.BASE_URL+"image/"+Amodel.getGambar())
                    .into(imageView);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EditItem.class);
                    intent.putExtra("id",Integer.valueOf(Amodel.getIditem()));
                    intent.putExtra("merk",String.valueOf(Amodel.getMerk()));
                    intent.putExtra("harga",String.valueOf(Amodel.getHarga()));
                    intent.putExtra("kodesepeda",String.valueOf(Amodel.getKodesepeda()));
                    intent.putExtra("warna",String.valueOf(Amodel.getWarna()));
                    intent.putExtra("gambar",String.valueOf(Amodel.getGambar()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
