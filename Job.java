public class Job {
    int id;
    int processingTime;
    int priority; // Used in Task 2
    int arrivalTime; // Used in Task 3

    // Constructor for Task 1
    Job(int id, int processingTime) {
        this.id = id;
        this.processingTime = processingTime;
    }

    // Constructor for Task 2 and Task 3
    Job(int id, int processingTime, int value) {
        this.id = id;
        this.processingTime = processingTime;
        this.priority = value; // Could be priority or arrival time
        this.arrivalTime = value; // Could be priority or arrival time
    }
}
