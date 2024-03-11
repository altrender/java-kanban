package ru.yandex.practicum.kanban;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;
    private static int sequenceTaskId = 0;
    public static int getSequenceTaskId() {
        return sequenceTaskId;
    }
    private static int nextSequenceId() {
        sequenceTaskId++;
        return sequenceTaskId;
    }

    public TaskManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> listTasks = new ArrayList<>();
        for (Integer i : tasks.keySet()) {
            listTasks.add(tasks.get(i));
        }
        return listTasks;
    }

    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> listSubTasks = new ArrayList<>();
        for (Integer i : subTasks.keySet()) {
            listSubTasks.add(subTasks.get(i));
        }
        return listSubTasks;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> listEpics = new ArrayList<>();
        for (Integer i : epics.keySet()) {
            listEpics.add(epics.get(i));
        }
        return listEpics;
    }

    public void clearAllTasks() {
        tasks.clear();
    }

    public void clearAllSubTasks() {
        // сперва удалим сабтаски из эпиков
        for (Integer subTaskId : subTasks.keySet()) {
            if (epics.containsKey(subTasks.get(subTaskId).getEpicId())) {
                epics.get(subTasks.get(subTaskId).getEpicId()).disAssignSubTask(subTaskId);
            }
        }
        // теперь очищаем все сабтаски
        subTasks.clear();
    }

    public void clearAllEpics() {
        // сперва у всех связанных сабтасков удалим связь с эпиком
        for (Integer epicId : epics.keySet()) {
            for (Integer subTaskId : epics.get(epicId).subTasks) {
                if (subTasks.containsKey(subTaskId)) {
                    subTasks.remove(subTaskId);
                }
            }
        }
        // теперь удаляем все эпики
        epics.clear();
    }

    public int createTask(Task task) {
        task.setId(nextSequenceId());
        tasks.put(task.getId(), task);
        return task.getId();
    }
    public int createSubTask(SubTask subTask) {
        subTask.setId(nextSequenceId());
        subTasks.put(subTask.getId(), subTask);
        // при создании подзадачи, проверим, не стоит ли поменять статус эпика
        // к примеру, если Эпик был в DONE, то теперь он перейдет в IN_PROGRESS
        checkUpdateStatus(subTask.epicId);
        return subTask.getId();
    }
    public int createEpic(Epic epic) {
        epic.setId(nextSequenceId());
        epics.put(epic.getId(), epic);
        // при создании эпика нет смысла в проверке на смену его статуса
        // эпик всегда создается в начальном статусе NEW
        return epic.getId();
    }

    public void modifyTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void modifySubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        // обязательно проверим статус эпика
        checkUpdateStatus(subTask.epicId);
    }

    public void modifyEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        // обязательно проверим статус эпика, ведь к нему моги приписать другие подзадачи
        checkUpdateStatus(epic.getId());
    }

    public void deleteTask(Integer taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        }
    }

    public void deleteSubTask(Integer subTaskId) {
        if (subTasks.containsKey(subTaskId)) {
            // удаляем сначала сабтаск из эпика
            if (epics.containsKey(subTasks.get(subTaskId).getEpicId())) {
                epics.get(subTasks.get(subTaskId).getEpicId()).disAssignSubTask(subTaskId);
            }
            // проверим статус эпика
            checkUpdateStatus(subTasks.get(subTaskId).getEpicId());
            // теперь удаляем сабтаск из списка
            subTasks.remove(subTaskId);
        }
    }

    public void deleteEpic(Integer epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            epics.remove(epicId);
            // удалим сабтаски из списка
            for (Integer subTaskId : epic.subTasks) {
                if (subTasks.containsKey(subTaskId)) {
                    subTasks.remove(subTaskId);
                }
            }
        }
    }

    public ArrayList<Integer> getSubTasksByEpic(Integer epicId) {
        if (epics.containsKey(epicId)) {
            return epics.get(epicId).getSubTasks();
        }
        return new ArrayList<Integer>();
    }
    public void checkUpdateStatus(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            int countDone = 0;
            int countNew = 0;
            for (int i = 0; i < epic.subTasks.size(); i++) {
                if (subTasks.containsKey(epic.subTasks.get(i))) {
                    switch (subTasks.get(epic.subTasks.get(i)).getStatus()) {
                        case NEW:
                            countNew++;
                            break;
                        case DONE:
                            countDone++;
                            break;
                    }
                    if (epic.subTasks.size() == 0 || epic.subTasks.size() == countNew) {
                        epic.setStatus(Status.NEW);
                    } else if (epic.subTasks.size() == countDone) {
                        epic.setStatus(Status.DONE);
                    } else {
                        epic.setStatus(Status.IN_PROGRESS);
                    }
                }
            }
        }
    }

}
