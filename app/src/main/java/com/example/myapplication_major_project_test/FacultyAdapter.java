package com.example.myapplication_major_project_test;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.Facultyholder>{
    Context context;
    List<Faculty> flist;

    public FacultyAdapter(Context context, List<Faculty> flist) {
        this.context = context;
        this.flist = flist;
    }

    @NonNull
    @Override
    public Facultyholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Facultyholder(LayoutInflater.from(context).inflate(R.layout.cell_faculty, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Facultyholder holder, int position) {
        Faculty faculty = flist.get(position);
        String status = faculty.getStatus();
        if(status.equals("Leave")){
            holder.mTvfname.setText(faculty.getName()+" - On Leave");
            holder.mTvfclass.setVisibility(View.GONE);
            holder.mTvftime.setVisibility(View.GONE);
            holder.mllcell.setBackgroundColor(Color.rgb(213,104,104));
        }
        else if(status.equals("Default")){
            holder.mTvfname.setText(faculty.getName()+" - Entry Not Available");
            holder.mTvfclass.setVisibility(View.GONE);
            holder.mTvftime.setVisibility(View.GONE);
            holder.mllcell.setBackgroundColor(Color.rgb(213,104,104));
        }
        else{
            holder.mTvfname.setText(faculty.getName());
            holder.mTvfclass.setText(faculty.getClass_n());
            holder.mTvftime.setText(faculty.getDuration());
            holder.mllcell.setBackgroundColor(Color.rgb(123,236,128));
        }
    }

    @Override
    public int getItemCount() {
        return flist.size();
    }

    public class Facultyholder extends RecyclerView.ViewHolder {
        TextView mTvfname;
        TextView mTvfclass;
        TextView mTvftime;
        LinearLayout mllcell;

        public Facultyholder(@NonNull View itemView) {
            super(itemView);
            mTvfname = itemView.findViewById(R.id.tv_cell_facultyname);
            mTvfclass = itemView.findViewById(R.id.tv_cell_class);
            mTvftime = itemView.findViewById(R.id.tv_cell_timing);
            mllcell = itemView.findViewById(R.id.ll_cell);
        }
    }
}
