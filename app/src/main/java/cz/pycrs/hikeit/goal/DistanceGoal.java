package cz.pycrs.hikeit.goal;

import java.time.LocalDateTime;

public class DistanceGoal extends Goal{
    private final float distance;
    public DistanceGoal(String title, LocalDateTime deadline, float distance) {
        super(title, deadline);
        this.distance = distance;
    }
}
