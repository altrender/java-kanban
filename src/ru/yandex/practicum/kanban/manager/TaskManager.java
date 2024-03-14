package ru.yandex.practicum.kanban.manager;

import ru.yandex.practicum.kanban.entities.Epic;
import ru.yandex.practicum.kanban.entities.Status;
import ru.yandex.practicum.kanban.entities.SubTask;
import ru.yandex.practicum.kanban.entities.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;
    private int sequenceTaskId = 0;

    public TaskManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void clearAllTasks() {
        tasks.clear();
    }

    public void clearAllSubTasks() {
        // очищаем все подзадачи
        subTasks.clear();
        // обходим все эпики и чистим у них подзадачи
        for (Epic epic : epics.values()) {
            epic.removeAllSubTasks();
        }
    }

    public void clearAllEpics() {
        // удаляем все эпики
        epics.clear();
        // удаляем все сабтаски, так как они не могут существовать без эпиков (в задаче про это не сказано)
        subTasks.clear();
    }

    public int createTask(Task task) {
        task.setId(nextSequenceId());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public int createSubTask(SubTask subTask) {
        int subTaskId = nextSequenceId();
        int epicId = subTask.getEpicId();
        subTask.setId(subTaskId);
        subTasks.put(subTaskId, subTask);
        epics.get(epicId).addSubTask(subTaskId);
        // при создании подзадачи, проверим, не стоит ли поменять статус эпика
        // к примеру, если Эпик был в DONE, то теперь он перейдет в IN_PROGRESS
        checkUpdateStatus(epicId);
        return subTaskId;
    }

    public int createEpic(Epic epic) {
        epic.setId(nextSequenceId());
        epics.put(epic.getId(), epic);
        // при создании эпика нет смысла в проверке на смену его статуса
        // эпик всегда создается в начальном статусе NEW
        return epic.getId();
    }

    public void modifyTask(Task task) {
        // проверим на всякий случай, что задача с таким id была ранее в хэшмапе
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
        // если не было, то по идее мы должны выдавать исключение
        // но эту тему еще не проходили :)
    }

    public void modifySubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
        }
        // обязательно проверим статус эпика
        checkUpdateStatus(subTask.getEpicId());
    }

    public void modifyEpic(Epic epic) {
        int epicId = epic.getId();
        if (epics.containsKey(epicId)) {
            // проверка статуса эпика не требуется, поэтому напрямую обновим имя и описание, этого достаточно
            Epic currentEpic = epics.get(epicId);
            currentEpic.setName(epic.getName());
            currentEpic.setDescription(epic.getDescription());
        }
    }

    public void deleteTask(Integer taskId) {
        tasks.remove(taskId);
    }

    public void deleteSubTask(Integer subTaskId) {
        // удаляем сабтаск
        SubTask subTask = subTasks.remove(subTaskId);
        // если по переданному id сабтаск нашли в хэшмапе, удалим его из соответствующего эпика
        if (subTask != null) {
            int epicId = subTask.getEpicId();
            epics.get(epicId).removeSubTask(subTaskId);
            // проверим статус эпика
            checkUpdateStatus(epicId);
        }
    }

    public void deleteEpic(Integer epicId) {
        // удаляем эпик
        Epic epic = epics.remove(epicId);
        // если по переданному id эпика нашли в хэшмапе, удалим все его сабтаски из соответствующей хэшмапы
        if (epic != null) {
            for (Integer subTaskId : epic.getSubTasks()) {
                subTasks.remove(subTaskId);
            }
        }
    }

    public ArrayList<Task> getSubTasksByEpic(Integer epicId) {
        ArrayList<Task> subTasksByEpic = new ArrayList<>();
        if (epics.containsKey(epicId)) {
            for (Integer subTaskId : epics.get(epicId).getSubTasks()) {
                subTasksByEpic.add(subTasks.get(subTaskId));
            }
        }
        return subTasksByEpic;
    }

    private void checkUpdateStatus(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            ArrayList<Integer> epicSubTaskList = epic.getSubTasks();
            int countDone = 0;
            int countNew = 0;
            for (Integer subTaskId : epicSubTaskList) {
                SubTask subtask = subTasks.get(subTaskId);
                switch (subtask.getStatus()) {
                    case NEW:
                        countNew++;
                        break;
                    case DONE:
                        countDone++;
                        break;
                }
            }
            if (epicSubTaskList.isEmpty() || epicSubTaskList.size() == countNew) {
                epic.setStatus(Status.NEW);
            } else if (epicSubTaskList.size() == countDone) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    private int nextSequenceId() {
        return ++sequenceTaskId;
    }

}
