package cz.pycrs.hikeit.goal;

import android.content.Context;
import android.content.res.Resources;

import java.time.LocalDateTime;

import cz.pycrs.hikeit.R;

public class DistanceGoal extends Goal{
    public DistanceGoal(String title, LocalDateTime deadline, float distance) {
        super(title, deadline, distance);
    }

    @Override
    public String getProgressText(Resources res, float progress) {
        return res.getString(R.string.distance_progress, progress, value);
    }
}
