import java.io.*;
import java.util.*;

public class TaskScheduler {

    public static void main(String[] args) {
        List<Job> task1Jobs = readJobsFromFile("src/task1-input.txt");
        if (task1Jobs != null) {
            task1(task1Jobs);
        }

        List<Job> task2Jobs = readJobsFromFile("src/task2-input.txt");
        if (task2Jobs != null) {
            task2(task2Jobs);
        }

        List<Job> task3Jobs = readJobsFromFile("src/task3-input.txt");
        if (task3Jobs != null) {
            task3(task3Jobs);
        }
    }

    // Method to read jobs from a specified input file
    private static List<Job> readJobsFromFile(String filename) {
        List<Job> jobs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 2) {
                    // Task 1: Job ID and Processing Time
                    int id = Integer.parseInt(parts[0]);
                    int processingTime = Integer.parseInt(parts[1]);
                    jobs.add(new Job(id, processingTime));
                } else if (parts.length == 3) {
                    // Task 2 or Task 3: Job ID, Processing Time, and either Priority or Arrival Time
                    int id = Integer.parseInt(parts[0]);
                    int processingTime = Integer.parseInt(parts[1]);
                    int thirdValue = Integer.parseInt(parts[2]);
                    jobs.add(new Job(id, processingTime, thirdValue));
                } else {
                    System.out.println("Invalid job format in " + filename);
                    return null;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading from file: " + filename);
            e.printStackTrace();
            return null;
        }
        return jobs;
    }

    // Task 1: SPT scheduling using a min-heap
    public static void task1(List<Job> jobs) {
        PriorityQueue<Job> minHeap = new PriorityQueue<>(Comparator.comparingInt(j -> j.processingTime));

        minHeap.addAll(jobs); // Add all jobs to the min-heap

        int currentTime = 0;
        int totalCompletionTime = 0;
        List<Integer> executionOrder = new ArrayList<>();

        while (!minHeap.isEmpty()) {
            Job job = minHeap.poll(); // Get job with the shortest processing time
            currentTime += job.processingTime;
            totalCompletionTime += currentTime;
            executionOrder.add(job.id);
        }

        double averageCompletionTime = (double) totalCompletionTime / jobs.size();
        writeOutput(1, executionOrder, averageCompletionTime);
    }

    // Task 2: SPT scheduling with priority levels using a min-heap
    public static void task2(List<Job> jobs) {
        PriorityQueue<Job> minHeap = new PriorityQueue<>(new Comparator<Job>() {
            public int compare(Job j1, Job j2) {
                if (j1.priority != j2.priority) {
                    return Integer.compare(j1.priority, j2.priority); // Sort by priority
                } else {
                    return Integer.compare(j1.processingTime, j2.processingTime); // Sort by processing time
                }
            }
        });

        minHeap.addAll(jobs); // Add all jobs to the min-heap

        int currentTime = 0;
        int totalCompletionTime = 0;
        List<Integer> executionOrder = new ArrayList<>();

        while (!minHeap.isEmpty()) {
            Job job = minHeap.poll(); // Get job with the highest priority (and shortest time in case of a tie)
            currentTime += job.processingTime;
            totalCompletionTime += currentTime;
            executionOrder.add(job.id);
        }

        double averageCompletionTime = (double) totalCompletionTime / jobs.size();
        writeOutput(2, executionOrder, averageCompletionTime);
    }

    // Task 3: SPT scheduling with dynamic job arrival using a min-heap
    public static void task3(List<Job> jobs) {
        PriorityQueue<Job> minHeap = new PriorityQueue<>(Comparator.comparingInt(j -> j.processingTime));

        int currentTime = 0;
        int totalCompletionTime = 0;
        int jobIndex = 0;
        List<Integer> executionOrder = new ArrayList<>();

        // First, sort jobs based on arrival time
        Collections.sort(jobs, Comparator.comparingInt(j -> j.arrivalTime));

        while (jobIndex < jobs.size() || !minHeap.isEmpty()) {
            // Add jobs that have arrived by the current time
            while (jobIndex < jobs.size() && jobs.get(jobIndex).arrivalTime <= currentTime) {
                minHeap.add(jobs.get(jobIndex));
                jobIndex++;
            }

            if (!minHeap.isEmpty()) {
                Job job = minHeap.poll(); // Get the job with the shortest processing time
                currentTime += job.processingTime;
                totalCompletionTime += currentTime;
                executionOrder.add(job.id);
            } else {
                if (jobIndex < jobs.size()) {
                    currentTime = jobs.get(jobIndex).arrivalTime;
                }
            }
        }

        double averageCompletionTime = (double) totalCompletionTime / jobs.size();
        writeOutput(3, executionOrder, averageCompletionTime);
    }

    // Method to write the output to the console
    private static void writeOutput(int taskNumber, List<Integer> executionOrder, double averageCompletionTime) {
        System.out.println("Task " + taskNumber + " Output:");
        System.out.println("Execution order: " + executionOrder);
        System.out.println("Average completion time: " + averageCompletionTime);
    }
}
