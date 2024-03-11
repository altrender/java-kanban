package ru.yandex.practicum.kanban;

import java.util.ArrayList;

public class Epic extends Task{
    ArrayList<Integer> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subTasks = new ArrayList<>();
    }
    public Epic(String name, String description, int id) {
        super(name, description, id);
        this.subTasks = new ArrayList<>();
    }
    public Epic(String name, String description, ArrayList<Integer> subTasks) {
        super(name, description);
        this.subTasks = subTasks;
    }

    public Epic(String name, String description, int id, ArrayList<Integer> subTasks) {
        super(name, description, id);
        this.subTasks = subTasks;
    }

    public void setStatus(Status newStatus) {
        if (newStatus != this.status) {
            this.status = newStatus;
        }
    }


    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public void disAssignSubTask(Integer subTaskId) {
        if (subTasks.contains(subTaskId) ) {
            subTasks.remove(subTaskId);
        }
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
