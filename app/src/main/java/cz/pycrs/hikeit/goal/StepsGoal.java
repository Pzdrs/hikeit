package cz.pycrs.hikeit.goal;

import java.time.LocalDateTime;

public class StepsGoal extends Goal{
    private final int steps;
    public StepsGoal(String title, LocalDateTime deadline, int steps) {
        super(title, deadline);
        this.steps = steps;
    }
}
