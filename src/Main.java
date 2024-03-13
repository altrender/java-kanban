import ru.yandex.practicum.kanban.entities.Epic;
import ru.yandex.practicum.kanban.entities.Status;
import ru.yandex.practicum.kanban.entities.SubTask;
import ru.yandex.practicum.kanban.entities.Task;
import ru.yandex.practicum.kanban.manager.TaskManager;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager tracker = new TaskManager();
        ArrayList<Integer> tasIdList = new ArrayList<>();
        ArrayList<Integer> subTasIdList = new ArrayList<>();
        ArrayList<Integer> epicIdList = new ArrayList<>();

        // создаем 2 отдельные задачи
        tasIdList.add(tracker.createTask(new Task("T-1", "Задача 1")));
        tasIdList.add(tracker.createTask(new Task("T-2", "Задача 2")));

        // создаем эпик с 2-мя подзадачами
        epicIdList.add(tracker.createEpic(new Epic("E-1", "Эпик 1")));

        subTasIdList.add(tracker.createSubTask(new SubTask("ST-1", "Подзадача 1", epicIdList.get(0))));
        subTasIdList.add(tracker.createSubTask(new SubTask("ST-2", "Подзадача 2", epicIdList.get(0))));

        // создаем эпик с 1-й подзадачей
        epicIdList.add(tracker.createEpic(new Epic("E-2", "Эпик 2")));
        subTasIdList.add(tracker.createSubTask(new SubTask("ST-3", "Подзадача 3", epicIdList.get(1))));

        System.out.println(("Кейс 1. Создание задач."));
        printAll(tracker);

        // Меняем статус задач на IN_PROGRESS
        for (Task task : tracker.getAllTasks()) {
            tracker.modifyTask(new Task(task.getName(), task.getDescription(), task.getId(), Status.IN_PROGRESS));
        }
        // Меняем статус нечетных подзадач на DONE, а четных - на IN_PROGRESS
        for (SubTask subTask : tracker.getAllSubTasks()) {
            if (subTask.getId() % 2 == 0) {
                tracker.modifySubTask(new SubTask(subTask.getName(), subTask.getDescription(), subTask.getId(),
                        subTask.getEpicId(), Status.IN_PROGRESS));
            } else {
                tracker.modifySubTask(new SubTask(subTask.getName(), subTask.getDescription(), subTask.getId(),
                        subTask.getEpicId(), Status.DONE));
            }
        }
        System.out.println(("Кейс 2. Изменение статусов."));
        printAll(tracker);

        // Удаляем задачу, подзадачу и эпик
        tracker.deleteTask(tracker.getAllTasks().get(0).getId());
        // удалим 2-й эпик
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
