package cz.pycrs.hikeit.goal;


import android.content.res.Resources;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

public abstract class Goal {
    public Goal(int id, String title, LocalDateTime deadline, float value) {
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.created = LocalDateTime.now();
        this.value = value;
    }

    public Goal(String title, LocalDateTime deadline, float value) {
        this.title = title;
        this.deadline = deadline;
        this.created = LocalDateTime.now();
        this.value = value;
    }

    private int id;
    private LocalDateTime created;

    private String title;
    private LocalDateTime deadline;
    protected float value;
    public LocalDateTime getCreated() {
        return created;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public float getValue() {
        return value;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public abstract String getProgressText(Resources res, float progress);

    public static Goal of(JSONObject json) throws JSONException {
        int id = json.getInt("id");
        String type = json.getString("type");
        String label = json.getString("label");
        float value = (float) json.getDouble("value");
        LocalDateTime deadline = LocalDateTime.parse(json.getString("deadline"), DateTimeFormatter.RFC_1123_DATE_TIME);
        switch (type) {
            case "steps":
                return new StepsGoal(id, label, deadline, (int) value);
            case "distance":
                return new DistanceGoal(id, label, deadline, value);
            default:
                throw new IllegalArgumentException("Unknown goal type: " + type);
        }
    }

    public static String getType(String typeLabel) {
        switch (typeLabel) {
            case "Steps":
            case "Počet kroků":
                return "steps";
            case "Distance":
            case "Vzdálenost":
                return "distance";
            default:
                throw new IllegalArgumentException("Unknown goal type: " + typeLabel);
        }
    }
}
