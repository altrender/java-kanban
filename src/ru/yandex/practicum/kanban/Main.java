package ru.yandex.practicum.kanban;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager tracker = new TaskManager();
        int taskId;
        int subTaskId;
        int epicId;
        ArrayList<Integer> epicSubTaskList;

        // создаем 2 отдельные задачи
        taskId = tracker.createTask(new Task("T-1", "Задача 1"));
        taskId = tracker.createTask(new Task("T-2", "Задача 2"));

        // создаем эпик с 2-мя подзадачами
        epicSubTaskList = new ArrayList<>();
        epicId = tracker.createEpic(new Epic("E-1", "Эпик 1", epicSubTaskList));
        subTaskId = tracker.createSubTask(new SubTask("ST-1", "Подзадача 1", epicId));
        epicSubTaskList.add(subTaskId);
        subTaskId = tracker.createSubTask(new SubTask("ST-2", "Подзадача 2", epicId));
        epicSubTaskList.add(subTaskId);
        tracker.modifyEpic(new Epic("E-1", "Эпик 1", epicId, epicSubTaskList));

        // создаем эпик с 1-й подзадачей
        epicSubTaskList = new ArrayList<>();
        epicId = tracker.createEpic(new Epic("E-2", "Эпик 2", epicSubTaskList));
        subTaskId = tracker.createSubTask(new SubTask("ST-3", "Подзадача 3", epicId));
        epicSubTaskList.add(subTaskId);
        tracker.modifyEpic(new Epic("E-1", "Эпик 1", epicId, epicSubTaskList));

        System.out.println(("Кейс 1. Создание задач."));
        printAll(tracker);

        // Меняем статус задач на IN_PROGRESS
        for (Task task : tracker.getAllTasks()) {
            tracker.modifyTask(new Task("T-1", "Задача 1", task.getId(), Status.IN_PROGRESS));
        }
        // Меняем статус нечетных подзадач на DONE, а четных - на IN_PROGRESS
        for (SubTask subTask : tracker.getAllSubTasks()) {
            if (subTask.getId() % 2 == 0) {
                tracker.modifySubTask(new SubTask(subTask.name, subTask.description, subTask.getId(),
                        subTask.getEpicId(), Status.IN_PROGRESS));
            } else {
                tracker.modifySubTask(new SubTask(subTask.name, subTask.description, subTask.getId(),
                        subTask.getEpicId(), Status.DONE));
            }
        }
        System.out.println(("Кейс 2. Изменение статусов."));
        printAll(tracker);

        // Удаляем задачу, подзадачу и эпик
        tracker.deleteTask(taskId);
        tracker.deleteEpic(tracker.getAllEpics().get(1).getId());
        tracker.deleteSubTask(tracker.getAllSubTasks().get(0).getId());
        System.out.println(("Кейс 3. Удаление задачи, эпика, подзадачи."));
        printAll(tracker);


    }

    public static void printAll(TaskManager tracker) {
        // Печатаем список всех задач
        System.out.println("Список всех задач: " + tracker.getAllTasks());
        System.out.println("Список всех эпиков: " + tracker.getAllEpics());
        System.out.println("Список всех подзадач: " + tracker.getAllSubTasks());
    }
}
