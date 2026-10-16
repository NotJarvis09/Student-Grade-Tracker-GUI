import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

class Student {
    private final String name;
    private final double grade;

    public Student(String name, double grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public double getGrade() {
        return grade;
    }

    public String getLetterGrade() {
        if (grade >= 90.0) return "A";
        if (grade >= 80.0) return "B";
        if (grade >= 70.0) return "C";
        if (grade >= 60.0) return "D";
        return "F";
    }
}

public class StudentGradeTrackerGUI extends JFrame {

    private final ArrayList<Student> studentList = new ArrayList<>();
    
    // UI Components
    private JTextField txtName;
    private JTextField txtGrade;
    private DefaultTableModel tableModel;
    private JTable studentTable;
    
    // Summary Labels
    private JLabel lblTotalStudents;
    private JLabel lblAverage;
    private JLabel lblHighest;
    private JLabel lblLowest;

    public StudentGradeTrackerGUI() {
        setTitle("Student Grade Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(650, 450));
        setLocationRelativeTo(null); // Center on screen

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. Header Title
        JLabel lblTitle = new JLabel("Student Grade Tracker", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // 2. Input Form (Top Panel under title)
        JPanel formContainer = new JPanel(new BorderLayout(10, 10));
        formContainer.add(createInputForm(), BorderLayout.NORTH);

        // 3. Center Table
        String[] columnNames = {"ID", "Student Name", "Grade (%)", "Letter Grade"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable directly in table
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(24);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Center-align ID, Grade, and Letter Grade columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        studentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        studentTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        studentTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        formContainer.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(formContainer, BorderLayout.CENTER);

        // 4. Bottom Statistics & Action Panel
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createInputForm() {
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Student Record"));

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtName = new JTextField(14);

        JLabel lblGrade = new JLabel("Grade (0 - 100):");
        lblGrade.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtGrade = new JTextField(6);

        JButton btnAdd = new JButton("Add Student");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAdd.setBackground(new Color(25, 135, 84));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(this::handleAddStudent);

        formPanel.add(lblName);
        formPanel.add(txtName);
        formPanel.add(lblGrade);
        formPanel.add(txtGrade);
        formPanel.add(btnAdd);

        return formPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomContainer = new JPanel(new BorderLayout(10, 10));

        // Statistics Card Panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 6));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Summary Statistics"));

        lblTotalStudents = new JLabel("Total Students: 0");
        lblAverage = new JLabel("Class Average: N/A");
        lblHighest = new JLabel("Highest Score: N/A");
        lblLowest = new JLabel("Lowest Score: N/A");

        Font statsFont = new Font("Segoe UI", Font.PLAIN, 13);
        lblTotalStudents.setFont(statsFont);
        lblAverage.setFont(statsFont);
        lblHighest.setFont(statsFont);
        lblLowest.setFont(statsFont);

        statsPanel.add(lblTotalStudents);
        statsPanel.add(lblHighest);
        statsPanel.add(lblAverage);
        statsPanel.add(lblLowest);

        // Button Controls
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(this::handleDeleteSelected);

        JButton btnClearAll = new JButton("Clear All");
        btnClearAll.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnClearAll.setFocusPainted(false);
        btnClearAll.addActionListener(e -> handleClearAll());

        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClearAll);

        bottomContainer.add(statsPanel, BorderLayout.CENTER);
        bottomContainer.add(buttonPanel, BorderLayout.SOUTH);

        return bottomContainer;
    }

    private void handleAddStudent(ActionEvent e) {
        String name = txtName.getText().trim();
        String gradeStr = txtGrade.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid student name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtName.requestFocus();
            return;
        }

        double grade;
        try {
            grade = Double.parseDouble(gradeStr);
            if (grade < 0.0 || grade > 100.0) {
                JOptionPane.showMessageDialog(this, "Grade must be between 0.0 and 100.0.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                txtGrade.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a numeric grade value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtGrade.requestFocus();
            return;
        }

        // Add to model & list
        Student newStudent = new Student(name, grade);
        studentList.add(newStudent);

        tableModel.addRow(new Object[]{
                studentList.size(),
                newStudent.getName(),
                String.format("%.2f", newStudent.getGrade()),
                newStudent.getLetterGrade()
        });

        // Clear fields
        txtName.setText("");
        txtGrade.setText("");
        txtName.requestFocus();

        updateStatistics();
    }

    private void handleDeleteSelected(ActionEvent e) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table to delete.", "Selection Required", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        studentList.remove(selectedRow);
        tableModel.removeRow(selectedRow);

        // Re-number ID column
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(i + 1, i, 0);
        }

        updateStatistics();
    }

    private void handleClearAll() {
        if (studentList.isEmpty()) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete all student records?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            studentList.clear();
            tableModel.setRowCount(0);
            updateStatistics();
        }
    }

    private void updateStatistics() {
        if (studentList.isEmpty()) {
            lblTotalStudents.setText("Total Students: 0");
            lblAverage.setText("Class Average: N/A");
            lblHighest.setText("Highest Score: N/A");
            lblLowest.setText("Lowest Score: N/A");
            return;
        }

        double sum = 0.0;
        Student highest = studentList.get(0);
        Student lowest = studentList.get(0);

        for (Student s : studentList) {
            double g = s.getGrade();
            sum += g;

            if (g > highest.getGrade()) highest = s;
            if (g < lowest.getGrade()) lowest = s;
        }

        double avg = sum / studentList.size();

        lblTotalStudents.setText("Total Students: " + studentList.size());
        lblAverage.setText(String.format("Class Average: %.2f", avg));
        lblHighest.setText(String.format("Highest Score: %.2f (%s)", highest.getGrade(), highest.getName()));
        lblLowest.setText(String.format("Lowest Score: %.2f (%s)", lowest.getGrade(), lowest.getName()));
    }

    public static void main(String[] args) {
        // Set system look and feel for a modern native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            StudentGradeTrackerGUI app = new StudentGradeTrackerGUI();
            app.setVisible(true);
        });
    }
}