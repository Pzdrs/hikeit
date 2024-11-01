package cz.pycrs.hikeit.ui.goals;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.pycrs.hikeit.goal.DistanceGoal;
import cz.pycrs.hikeit.goal.Goal;
import cz.pycrs.hikeit.goal.StepsGoal;

public class GoalsViewModel extends ViewModel {

    private MutableLiveData<List<Goal>> goals;
    public LiveData<List<Goal>> getGoals() {
        if (goals == null) {
            goals = new MutableLiveData<>();
            loadGoals();
        }
        return goals;
    }

    private void loadGoals() {
        List<Goal> goals = Arrays.asList(
                new DistanceGoal("Hike 1000 km", LocalDateTime.now().plusYears(1),1000),
                new StepsGoal("Walk 1 million steps", LocalDateTime.now().plusYears(1),1000000)
        );

        // Once the goals are loaded, update the LiveData
        this.goals.setValue(goals);
    }

    public void createGoal(String label, LocalDateTime deadline, String type, float value) {
        Goal goal;
        switch (type) {
            case "Vzdálenost":
            case "Distance":
                goal = new DistanceGoal(label, deadline, value);
                break;
            case "Počet kroků":
            case "Steps":
                goal = new StepsGoal(label, deadline, (int) value);
                break;
            default:
                throw new IllegalArgumentException("Unknown goal type: " + label);
        }
        // POST to api and if 200, do the following
        List<Goal> currentGoals = new ArrayList<>(this.goals.getValue());
        currentGoals.add(goal);
        this.goals.setValue(currentGoals);
    }
}