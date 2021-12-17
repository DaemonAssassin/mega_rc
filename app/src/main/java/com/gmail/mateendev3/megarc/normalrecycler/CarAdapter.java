package com.gmail.mateendev3.megarc.normalrecycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.mateendev3.megarc.R;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private final List<Car> carList;
    private static OnItemClickListener onItemClickListener;
    private static OnItemTextViewClickListener onItemTextViewClickListener;
    private static OnItemLongClickListener onItemLongClickListener;

    public CarAdapter(List<Car> carList) {
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rc_normal_row, parent, false);
        return new CarViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.getTvTitle().setText(car.getTitle());
        holder.getTvPrice().setText(car.getPrice());
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvPrice;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);

            //setting on click listener to item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(getAdapterPosition());
                }
            });

            //setting on click listener to textView
            tvPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemTextViewClickListener != null) {
                        onItemTextViewClickListener.onItemTextViewClick(tvPrice, getAdapterPosition());
                    }
                }
            });

            //setting onLongClick listener to item
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
        }

        public TextView getTvTitle() {
            return tvTitle;
        }
        public TextView getTvPrice() {
            return tvPrice;
        }
    }


    //setters for click listeners
    public void setOnItemClickListener(OnItemClickListener l) {
        onItemClickListener = l;
    }
    public void setOnItemTextViewClickListener(OnItemTextViewClickListener l) {
        onItemTextViewClickListener = l;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        onItemLongClickListener = l;
    }

    //public contracts/interfaces
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public interface OnItemTextViewClickListener {
        void onItemTextViewClick(TextView textView, int position);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
}
