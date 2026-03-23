import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// Wrapper class to store full system state
class SystemState implements Serializable {
    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading state: " + e.getMessage());
        }

        // Return empty state if failure
        return new SystemState(new HashMap<>(), new ArrayList<>());
    }
}

// Main class
public class PersistenceDemo {
    public static void main(String[] args) {

        // STEP 1: Load previous state
        SystemState state = PersistenceService.load();

        Map<String, Integer> inventory = state.inventory;
        List<Reservation> history = state.bookingHistory;

        // If first run, initialize inventory
        if (inventory.isEmpty()) {
            inventory.put("Standard", 2);
            inventory.put("Deluxe", 1);
        }

        // Simulate booking
        Reservation r1 = new Reservation("R101", "Mithran", "Standard");
        history.add(r1);
        inventory.put("Standard", inventory.get("Standard") - 1);

        // Display current state
        System.out.println("\nCurrent Bookings:");
        for (Reservation r : history) {
            System.out.println(r);
        }

        System.out.println("\nInventory: " + inventory);

        // STEP 2: Save state before shutdown
        PersistenceService.save(new SystemState(inventory, history));
    }
}