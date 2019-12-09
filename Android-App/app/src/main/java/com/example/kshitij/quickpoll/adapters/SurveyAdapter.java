package com.example.kshitij.quickpoll.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kshitij.quickpoll.R;
import com.example.kshitij.quickpoll.data.model.Survey;

import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {
    Context context;
    List<Survey> surveyList;
    SurveyFragmentInterface surveyFragmentInterface;
    View parentView;

    public SurveyAdapter(Context context, List<Survey> surveyList, SurveyFragmentInterface surveyFragmentInterface, View parentView) {
        this.context = context;
        this.surveyList = surveyList;
        this.surveyFragmentInterface = surveyFragmentInterface;
        this.parentView= parentView;
    }

    @NonNull
    @Override
    public SurveyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_item, parent, false);
        ViewHolder viewHolder = new SurveyAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SurveyAdapter.ViewHolder holder, final int position) {
        holder.surveyName.setText(surveyList.get(position).surveyName);
        holder.surveyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                surveyFragmentInterface.onSurveyClick(surveyList.get(position), parentView);
            }
        });

    }

    @Override
    public int getItemCount() {
        return surveyList.size();
    }

    public interface SurveyFragmentInterface{
        void onSurveyClick(Survey survey, View view);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView surveyName;
        LinearLayout surveyLayout;
        ViewHolder(View itemView){
            super(itemView);
//            super(itemView);
            surveyName = itemView.findViewById(R.id.survey_name);
            surveyLayout = itemView.findViewById(R.id.survey_item_holder);
        }
    }
}
