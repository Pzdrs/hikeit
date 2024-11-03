package cz.pycrs.hikeit.goal;

import android.content.res.Resources;

import java.time.LocalDateTime;

import cz.pycrs.hikeit.R;

public class StepsGoal extends Goal{
    public StepsGoal(String title, LocalDateTime deadline, int steps) {
        super(title, deadline, steps);
    }

    @Override
    public String getProgressText(Resources res, float progress) {
        return res.getString(R.string.steps_progress, (int) progress, (int) this.value);
    }
}
