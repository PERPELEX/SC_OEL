package application;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskManager {
    private static final String FILE_NAME = "tasks.dat";
    private List<Task> tasks;

    public TaskManager() {
        tasks = loadTasks();
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    public void updateTask(Task task, String title, String description, LocalDate dueDate, Task.Priority priority) {
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setPriority(priority);
        saveTasks();
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
        saveTasks();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void sortByPriority() {
        tasks.sort(Comparator.comparing(Task::getPriority));
    }

    public void sortByDueDate() {
        tasks.sort(Comparator.comparing(Task::getDueDate));
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Task> loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}