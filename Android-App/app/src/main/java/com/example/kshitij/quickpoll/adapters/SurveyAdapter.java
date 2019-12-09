package com.example.kshitij.quickpoll.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kshitij.quickpoll.R;
import com.example.kshitij.quickpoll.data.model.Survey;

import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {
    Context context;
    List<Survey> surveyList;

    public SurveyAdapter(Context context, List<Survey> surveyList) {
        this.context = context;
        this.surveyList = surveyList;
    }

    @NonNull
    @Override
    public SurveyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_item, parent, false);
        ViewHolder viewHolder = new SurveyAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SurveyAdapter.ViewHolder holder, int position) {
        holder.surveyName.setText(surveyList.get(position).surveyName);
    }

    @Override
    public int getItemCount() {
        return surveyList.size();
    }

    public interface SurveyFragmentInterface{
        void onSurveyClick(Survey survey);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView surveyName;
        ViewHolder(View itemView){
            super(itemView);
//            super(itemView);
            surveyName = itemView.findViewById(R.id.survey_name);
        }
    }
}
