package cz.pycrs.hikeit;

import android.content.res.Resources;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import cz.pycrs.hikeit.goal.Goal;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalViewHolder> {
    private List<Goal> goals;
    private Resources res;

    private String getDeadlineText(LocalDateTime deadline) {
        LocalDateTime now = LocalDateTime.now();
        Duration timeLeft = Duration.between(now, deadline);
        long daysLeft = timeLeft.toDaysPart();
        long hoursLeft = timeLeft.toHoursPart();

        if(now.isAfter(deadline))
            return res.getString(R.string.deadline_expired);

        String daysText = daysLeft > 0
                ? res.getQuantityString(R.plurals.days_left, (int) daysLeft, daysLeft)
                : "";  // Empty string if no days left

        // Get the localized string for hours
        String hoursText = hoursLeft > 0
                ? res.getQuantityString(R.plurals.hours_left, (int) hoursLeft, hoursLeft)
                : "";  // Empty string if no hours left

        // If there are both days and hours left, combine them
        if (!daysText.isEmpty() && !hoursText.isEmpty()) {
            return res.getString(R.string.deadline_time_left, daysText, hoursText);
        }

        // Otherwise, return just one of them (either days or hours)
        return !daysText.isEmpty() ? daysText : hoursText;
    }
    public GoalsAdapter(List<Goal> goals, Resources resources) {
        this.goals = goals;
        this.res = resources;
    }

    @NonNull
    @Override
    public GoalsAdapter.GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalsAdapter.GoalViewHolder holder, int position) {
        float progress = 5265;
        Goal goal = goals.get(position);
        holder.titleTextView.setText(goal.getTitle());
        holder.deadlineTextView.setText(getDeadlineText(goal.getDeadline()));
        if (LocalDateTime.now().isAfter(goal.getDeadline()))
            holder.deadlineTextView.setTextColor(Color.RED);
        holder.progressTextView.setText(goal.getProgressText(res,progress));
        holder.progressBar.setProgress((int) ((progress/ goal.getValue()) * 100));
        holder.progressBar.getProgressDrawable().setColorFilter(new BlendModeColorFilter(Color.GREEN, android.graphics.BlendMode.SRC_IN));
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, deadlineTextView, progressTextView;

        ProgressBar progressBar;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTitle);
            deadlineTextView = itemView.findViewById(R.id.textViewDeadline);
            progressTextView = itemView.findViewById(R.id.textViewProgress);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
