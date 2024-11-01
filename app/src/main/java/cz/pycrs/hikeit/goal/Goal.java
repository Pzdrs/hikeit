package cz.pycrs.hikeit.goal;


import java.time.LocalDateTime;

public abstract class Goal {
    public enum Type {
        DISTANCE, DURATION, STEPS
    }
    public Goal(String title, LocalDateTime deadline) {
        this.title = title;
        this.deadline = deadline;
        this.created = LocalDateTime.now();
    }

    private LocalDateTime created;

    private String title;
    private LocalDateTime deadline;

    public LocalDateTime getCreated() {
        return created;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }
}
