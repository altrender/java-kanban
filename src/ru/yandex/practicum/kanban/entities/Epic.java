package ru.yandex.practicum.kanban.entities;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subTasks = new ArrayList<>();
    }

    public void setStatus(Status newStatus) {
        if (newStatus != this.status) {
            this.status = newStatus;
        }
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public void removeSubTask(Integer subTaskId) {
        subTasks.remove(subTaskId);
    }

    public void removeAllSubTasks() {
        subTasks.clear();
    }

    public void AddSubTask(Integer subTaskId) {
        subTasks.add(subTaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subTasks=" + subTasks +
                '}';
    }
}
