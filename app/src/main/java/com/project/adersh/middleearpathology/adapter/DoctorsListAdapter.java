package com.project.adersh.middleearpathology.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.adersh.middleearpathology.DelClickListner;
import com.project.adersh.middleearpathology.R;
import com.project.adersh.middleearpathology.model.Root;


public class DoctorsListAdapter extends RecyclerView.Adapter<DoctorsListAdapter.MyViewHolder> {

    Context context;
    private Root data;

    DelClickListner clickListn;

    public DoctorsListAdapter(Context context, Root data, DelClickListner clickListn) {
        this.context = context;
        this.data = data;
        this.clickListn = clickListn;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.doc_rv_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.docName.setText(data.doctorDetails.get(position).name);
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListn.delClick(data.doctorDetails.get(position).id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.doctorDetails.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView docName;
        Button delBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            docName = itemView.findViewById(R.id.doc_name);
            delBtn = itemView.findViewById(R.id.deleteBtn);
        }


    }
}
