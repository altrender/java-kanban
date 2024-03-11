package ru.yandex.practicum.kanban;

public class SubTask extends Task{
    protected int epicId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int newEpicId) {
        this.epicId = newEpicId;
    }

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, int id, int epicId) {
        super(name, description, id);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, int id, int epicId, Status status) {
        super(name, description, id);
        this.epicId = epicId;
        this.status = status;
    }


    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epicId=" + epicId +
                '}';
    }
}
