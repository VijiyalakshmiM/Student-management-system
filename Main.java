
import java.util.Scanner;
import java.io.*;

public class Main {

    static final int MAX = 100;
    static int count = 0;

    static class Student {
        private int id, year;
        private String name, email, department;
        private double marks;

        public Student(int id, String name, String email,
                       String department, int year, double marks) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.department = department;
            this.year = year;
            this.marks = marks;
        }

        public int getId() { return id; }
        public double getMarks() { return marks; }
        public String getDepartment() { return department; }

        public void update(String name, String email,
                           String department, int year, double marks) {
            this.name = name;
            this.email = email;
            this.department = department;
            this.year = year;
            this.marks = marks;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name
                    + ", Email: " + email
                    + ", Department: " + department
                    + ", Year: " + year
                    + ", Marks: " + marks;
        }
    }

    interface Operations {
        void addStudent();
        void displayStudents();
        void searchStudent();
        void searchDepartment();
        void updateStudent();
        void deleteStudent();
        void highestMarks();
        void averageMarks();
        void saveData();
        void loadData();
    }

    static class StudentManager implements Operations {

        private final Student[] students = new Student[MAX];
        private final Scanner sc;
        private static final String FILE_NAME = "students.dat";

        StudentManager(Scanner sc) {
            this.sc = sc;
        }

        private int readInt(String message) {
            while (true) {
                System.out.print(message);
                try {
                    return Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Enter a valid integer.");
                }
            }
        }

        private double readMarks() {
            while (true) {
                System.out.print("Enter Marks (0-100): ");
                try {
                    double marks = Double.parseDouble(sc.nextLine().trim());
                    if (Double.isFinite(marks) && marks >= 0 && marks <= 100)
                        return marks;

                    System.out.println("Marks must be between 0 and 100.");
                } catch (NumberFormatException e) {
                    System.out.println("Enter valid marks.");
                }
            }
        }

        private int findStudent(int id) {
            for (int i = 0; i < count; i++) {
                if (students[i].getId() == id)
                    return i;
            }
            return -1;
        }

        @Override
        public void addStudent() {
            if (count == MAX) {
                System.out.println("Student limit reached.");
                return;
            }

            int id = readInt("Enter Student ID: ");

            if (id <= 0 || findStudent(id) != -1) {
                System.out.println("Invalid or duplicate Student ID.");
                return;
            }

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Department: ");
            String department = sc.nextLine();

            int year = readInt("Enter Year: ");

            if (year < 1 || year > 6) {
                System.out.println("Invalid year.");
                return;
            }

            double marks = readMarks();

            students[count++] = new Student(
                    id, name, email, department, year, marks);

            System.out.println("Student added successfully.");
        }

        @Override
        public void displayStudents() {
            if (count == 0) {
                System.out.println("No student records found.");
                return;
            }

            for (int i = 0; i < count; i++) {
                if (students[i] == null)
                    continue;
                System.out.println(students[i]);
            }
        }

        @Override
        public void searchStudent() {
            int id = readInt("Enter Student ID: ");
            int index = findStudent(id);

            if (index == -1)
                System.out.println("Student not found.");
            else
                System.out.println(students[index]);
        }

        @Override
        public void searchDepartment() {
            System.out.print("Enter Department: ");
            String department = sc.nextLine();
            boolean found = false;

            for (int i = 0; i < count; i++) {
                if (students[i].getDepartment()
                        .equalsIgnoreCase(department)) {
                    System.out.println(students[i]);
                    found = true;
                }
            }

            if (!found)
                System.out.println("No students found.");
        }

        @Override
        public void updateStudent() {
            int id = readInt("Enter Student ID to update: ");
            int index = findStudent(id);
            
            if (index == -1) {
                System.out.println("Student not found.");
                return;
            }
            

            System.out.print("Enter New Name: ");
            String name = sc.nextLine();

            System.out.print("Enter New Email: ");
            String email = sc.nextLine();

            System.out.print("Enter New Department: ");
            String department = sc.nextLine();

            int year = readInt("Enter New Year: ");

            if (year < 1 || year > 6) {
                System.out.println("Invalid year.");
                return;
            }

            double marks = readMarks();

            students[index].update(
                    name, email, department, year, marks);

            System.out.println("Student updated successfully.");
        }

        @Override
        public void deleteStudent() {
            int id = readInt("Enter Student ID to delete: ");
            int index = findStudent(id);

            if (index == -1) {
                System.out.println("Student not found.");
                return;
            }

            for (int i = index; i < count - 1; i++)
                students[i] = students[i + 1];

            students[--count] = null;

            System.out.println("Student deleted successfully.");
        }

        @Override
        public void highestMarks() {
            if (count == 0) {
                System.out.println("No student records found.");
                return;
            }

            Student highest = students[0];

            for (int i = 1; i < count; i++) {
                if (students[i].getMarks() > highest.getMarks())
                    highest = students[i];
            }

            System.out.println("Student with Highest Marks:");
            System.out.println(highest);
        }

        @Override
        public void averageMarks() {
            if (count == 0) {
                System.out.println("No student records found.");
                return;
            }

            double total = 0;

            for (int i = 0; i < count; i++)
                total += students[i].getMarks();

            System.out.printf("Average Marks: %.2f%n", total / count);
        }

        @Override
        public void saveData() {
            try (DataOutputStream out = new DataOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(FILE_NAME)))) {

                out.writeInt(count);

                for (int i = 0; i < count; i++) {
                    Student s = students[i];

                    out.writeInt(s.getId());
                    out.writeUTF(s.toString());
                }

                System.out.println("Data saved successfully.");

            } catch (IOException e) {
                System.out.println("Error saving data: " + e.getMessage());
            }
        }

        @Override
        public void loadData() {
            File file = new File(FILE_NAME);

            if (!file.exists()) {
                System.out.println("No saved data found.");
                return;
            }

            System.out.println(
                    "load data performed successfully");
                
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager(sc);

        while (true) {
            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Search by Department");
            System.out.println("5. Update Student");
            System.out.println("6. Delete Student");
            System.out.println("7. Highest Marks");
            System.out.println("8. Average Marks");
            System.out.println("9. Save Data");
            System.out.println("10. Load Data");
            System.out.println("11. Exit");

            System.out.print("Enter your choice: ");
            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid choice.");
                continue;
            }

            switch (choice) {
                case 1: manager.addStudent(); break;
                case 2: manager.displayStudents(); break;
                case 3: manager.searchStudent(); break;
                case 4: manager.searchDepartment(); break;
                case 5: manager.updateStudent(); break;
                case 6: manager.deleteStudent(); break;
                case 7: manager.highestMarks(); break;
                case 8: manager.averageMarks(); break;
                case 9: manager.saveData(); break;
                case 10: manager.loadData(); break;
                case 11:
                    System.out.println("Thank you!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}