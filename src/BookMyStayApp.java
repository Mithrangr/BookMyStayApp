import java.util.*;

// Booking Request
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Shared Booking System
class BookingSystem {

    // Shared inventory
    private Map<String, Integer> inventory = new HashMap<>();

    // Shared queue
    private Queue<BookingRequest> requestQueue = new LinkedList<>();

    public BookingSystem() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    // Add request (Producer)
    public synchronized void addRequest(BookingRequest request) {
        requestQueue.add(request);
        System.out.println("Request added: " + request.guestName);
    }

    // Process request (Consumer)
    public synchronized void processRequest() {

        if (requestQueue.isEmpty()) {
            return;
        }

        BookingRequest request = requestQueue.poll();

        String roomType = request.roomType;
        int available = inventory.getOrDefault(roomType, 0);

        // Critical Section
        if (available > 0) {
            inventory.put(roomType, available - 1);

            System.out.println(Thread.currentThread().getName() +
                    " booked " + roomType +
                    " for " + request.guestName);
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " failed booking for " + request.guestName +
                    " (No rooms available)");
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory: " + inventory);
    }
}

// Thread class
class BookingProcessor extends Thread {

    private BookingSystem system;

    public BookingProcessor(BookingSystem system, String name) {
        super(name);
        this.system = system;
    }

    public void run() {
        // Each thread tries to process multiple requests
        for (int i = 0; i < 3; i++) {
            system.processRequest();
            try {
                Thread.sleep(100); // simulate delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Main class
public class ConcurrentBookingDemo {
    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        // Add requests (simulating multiple guests)
        system.addRequest(new BookingRequest("Mithran", "Standard"));
        system.addRequest(new BookingRequest("Arun", "Standard"));
        system.addRequest(new BookingRequest("Kiran", "Standard"));
        system.addRequest(new BookingRequest("Ravi", "Deluxe"));

        // Create multiple threads
        BookingProcessor t1 = new BookingProcessor(system, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(system, "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final inventory
        system.displayInventory();
    }
}