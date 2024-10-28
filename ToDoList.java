import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.sound.sampled.*;
import java.util.Timer;
import java.util.TimerTask;

public class ToDoList extends JFrame {
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskInput;
    private JTextField descriptionInput;
    private JTextField reminderInput;
    private JButton addButton;
    private JButton deleteButton;
    private JButton setReminderButton;
    private JButton changePasswordButton;
    private final File passwordFile = new File("password.dat");
    private final File taskFile = new File("tasks.dat");
    private ArrayList<String> savedTasks = new ArrayList<>();
    private String password;

    public ToDoList() {
        if (!authenticate()) {
            JOptionPane.showMessageDialog(null, "Access Denied.");
            System.exit(0);
        }

        // Frame setup
        setTitle("Enhanced To-Do List App with Descriptions");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load tasks from file
        loadTasks();

        // Initialize components
        taskListModel = new DefaultListModel<>();
        for (String task : savedTasks) {
            taskListModel.addElement(task);
        }

        taskList = new JList<>(taskListModel);
        taskInput = new JTextField(15);
        descriptionInput = new JTextField(25);
        reminderInput = new JTextField(5);
        addButton = new JButton("Add Task");
        deleteButton = new JButton("Delete Task");
        setReminderButton = new JButton("Set Reminder");
        changePasswordButton = new JButton("Change Password");

        // Layout setup
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(taskList), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(taskInput);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionInput);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        JPanel reminderPanel = new JPanel();
        reminderPanel.add(new JLabel("Reminder (secs):"));
        reminderPanel.add(reminderInput);
        reminderPanel.add(setReminderButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(reminderPanel, BorderLayout.SOUTH);
        
        JPanel passwordPanel = new JPanel();
        passwordPanel.add(changePasswordButton);
        panel.add(passwordPanel, BorderLayout.EAST);

        add(panel);

        // Action listeners
        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteTask());
        setReminderButton.addActionListener(e -> setReminder());
        changePasswordButton.addActionListener(e -> changePassword());

        setVisible(true);
    }

    private boolean authenticate() {
        String defaultPin = JOptionPane.showInputDialog("Enter the default PIN (1234):");
        if (!"1234".equals(defaultPin)) {
            return false;
        }

        String newPassword = JOptionPane.showInputDialog("Set your new password:");
        String confirmPassword = JOptionPane.showInputDialog("Confirm your new password:");

        if (newPassword != null && newPassword.equals(confirmPassword)) {
            password = newPassword;
            savePassword();
            JOptionPane.showMessageDialog(null, "Password set successfully!");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Passwords do not match. Access Denied.");
            return false;
        }
    }

    private void changePassword() {
        String currentPassword = JOptionPane.showInputDialog("Enter current password:");
        if (currentPassword.equals(password)) {
            String newPassword = JOptionPane.showInputDialog("Set your new password:");
            String confirmPassword = JOptionPane.showInputDialog("Confirm your new password:");

            if (newPassword != null && newPassword.equals(confirmPassword)) {
                password = newPassword;
                savePassword();
                JOptionPane.showMessageDialog(null, "Password changed successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Passwords do not match. Try again.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Current password is incorrect.");
        }
    }

    private void savePassword() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(passwordFile))) {
            writer.write(password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPassword() {
        try (BufferedReader reader = new BufferedReader(new FileReader(passwordFile))) {
            password = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTask() {
        String task = taskInput.getText().trim();
        String description = descriptionInput.getText().trim();

        if (!task.isEmpty() && description.length() <= 100) {
            String fullTask = task + " - " + description;
            taskListModel.addElement(fullTask);
            taskInput.setText("");
            descriptionInput.setText("");
            saveTasks();
        } else if (description.length() > 100) {
            JOptionPane.showMessageDialog(null, "Description cannot exceed 100 characters!");
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a task and a description!");
        }
    }

    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            taskListModel.remove(selectedIndex);
            saveTasks();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task to delete!");
        }
    }

    private void setReminder() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String reminderTimeText = reminderInput.getText().trim();
            try {
                int reminderTime = Integer.parseInt(reminderTimeText) * 1000;
                String task = taskListModel.get(selectedIndex);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        JOptionPane.showMessageDialog(null, "Reminder for task: " + task);
                        playSound();
                    }
                }, reminderTime);
                reminderInput.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid reminder time. Enter seconds.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a task to set a reminder.");
        }
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(taskFile))) {
            ArrayList<String> tasks = new ArrayList<>();
            for (int i = 0; i < taskListModel.size(); i++) {
                tasks.add(taskListModel.getElementAt(i));
            }
            oos.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        if (taskFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(taskFile))) {
                savedTasks = (ArrayList<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void playSound() {
        try {
            File soundFile = new File("alert.wav"); // Add an alert.wav file in the same directory
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoList());
    }
}
