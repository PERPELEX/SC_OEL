package application;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import java.time.LocalDate;

public class TaskUI {
    private TaskManager taskManager;
    private VBox leftLayout;
    private VBox rightLayout;
    private ObservableList<Task> taskObservableList;

    // Fields for task editing
    private TextField titleInput;
    private TextField descriptionInput;
    private DatePicker dueDatePicker;
    private ComboBox<Task.Priority> priorityComboBox;

    public TaskUI(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.taskObservableList = FXCollections.observableArrayList(taskManager.getTasks());
        initializeUI();
    }

    public HBox getMainLayout() {
        HBox mainLayout = new HBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-padding: 20; -fx-background-color: #f7f7f7;");
        mainLayout.getChildren().addAll(leftLayout, rightLayout);
        return mainLayout;
    }

    private void initializeUI() {
        // Left Layout: Adding and Updating Tasks
        leftLayout = new VBox(20);
        leftLayout.setAlignment(Pos.CENTER_LEFT);
        leftLayout.setStyle("-fx-background-color: #e8f5e9; -fx-padding: 20; -fx-border-color: #4CAF50; -fx-border-width: 2px;");

        Label titleLabel = new Label("Todo List");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: #4CAF50; -fx-font-weight: bold;");

        titleInput = new TextField();
        titleInput.setPromptText("Task Title");

        descriptionInput = new TextField();
        descriptionInput.setPromptText("Task Description");

        dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Due Date");

        priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll(Task.Priority.values());
        priorityComboBox.setValue(Task.Priority.MEDIUM);

        Button addButton = new Button("Add Task");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addButton.setOnAction(e -> {
            String title = titleInput.getText().trim();
            String description = descriptionInput.getText().trim();
            LocalDate dueDate = dueDatePicker.getValue();
            Task.Priority priority = priorityComboBox.getValue();

            if (!title.isEmpty() && dueDate != null) {
                Task newTask = new Task(title, description, dueDate, priority);
                taskManager.addTask(newTask);
                taskObservableList.add(newTask);
                clearInputs();
            }
        });

        leftLayout.getChildren().addAll(titleLabel, titleInput, descriptionInput, dueDatePicker, priorityComboBox, addButton);

        // Right Layout: Displaying and Managing Tasks
        rightLayout = new VBox(20);
        rightLayout.setAlignment(Pos.TOP_CENTER);
        rightLayout.setStyle("-fx-background-color: #ffffff; -fx-padding: 20; -fx-border-color: #9e9e9e; -fx-border-width: 2px;");

        ComboBox<String> sortComboBox = new ComboBox<>();
        sortComboBox.getItems().addAll("Normal Order", "Priority Order", "Due Date Order");
        sortComboBox.setValue("Normal Order");

        ListView<Task> taskListView = new ListView<>(taskObservableList);
        taskListView.setCellFactory(TextFieldListCell.forListView(new StringConverter<>() {
            @Override
            public String toString(Task task) {
                return task.getTitle() + " (" + task.getPriority() + ")"; // Excludes `[ ]` and descriptions
            }

            @Override
            public Task fromString(String string) {
                return null;
            }
        }));

        sortComboBox.setOnAction(e -> {
            String selectedOption = sortComboBox.getValue();
            if ("Priority Order".equals(selectedOption)) {
                taskManager.sortByPriority();
            } else if ("Due Date Order".equals(selectedOption)) {
                taskManager.sortByDueDate();
            }
            taskObservableList.setAll(taskManager.getTasks());
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                taskManager.deleteTask(selectedTask);
                taskObservableList.remove(selectedTask);
            }
        });

        Button updateButton = new Button("Update");
        updateButton.setStyle("-fx-background-color: #ffa726; -fx-text-fill: white;");
        updateButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                titleInput.setText(selectedTask.getTitle());
                descriptionInput.setText(selectedTask.getDescription());
                dueDatePicker.setValue(selectedTask.getDueDate());
                priorityComboBox.setValue(selectedTask.getPriority());

                taskManager.deleteTask(selectedTask);
                taskObservableList.remove(selectedTask);
            }
        });

        Button readButton = new Button("View");
        readButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white;");
        readButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Task Details");
                alert.setHeaderText(selectedTask.getTitle());
                alert.setContentText("Description: " + selectedTask.getDescription() + "\n"
                        + "Due Date: " + selectedTask.getDueDate() + "\n"
                        + "Priority: " + selectedTask.getPriority() + "\n"
                        + "Completed: " + (selectedTask.isCompleted() ? "Yes" : "No"));
                alert.showAndWait();
            }
        });

        // Place buttons in a row
        HBox buttonRow = new HBox(10, deleteButton, updateButton, readButton);
        buttonRow.setAlignment(Pos.CENTER);

        rightLayout.getChildren().addAll(sortComboBox, taskListView, buttonRow);
    }

    private void clearInputs() {
        titleInput.clear();
        descriptionInput.clear();
        dueDatePicker.setValue(null);
        priorityComboBox.setValue(Task.Priority.MEDIUM);
    }
}
