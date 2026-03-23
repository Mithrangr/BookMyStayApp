import java.util.*;

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }
}

// Booking Service (for demo: holds active bookings + inventory)
class BookingService {

    Map<String, Reservation> activeBookings = new HashMap<>();
    Map<String, Integer> inventory = new HashMap<>();

    public BookingService() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    // Simulate booking
    public void confirmBooking(String id, String name, String type, String roomId) {
        activeBookings.put(id, new Reservation(id, name, type, roomId));
        inventory.put(type, inventory.get(type) - 1);
    }
}

// Cancellation Service
class CancellationService {

    private BookingService bookingService;

    // Stack for rollback (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    // Track cancelled IDs to avoid duplicate cancellation
    private Set<String> cancelledReservations = new HashSet<>();

    public CancellationService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void cancelBooking(String reservationId) {

        // Validation: check existence
        if (!bookingService.activeBookings.containsKey(reservationId)) {
            System.out.println("Cancellation failed: Reservation does not exist.");
            return;
        }

        // Prevent duplicate cancellation
        if (cancelledReservations.contains(reservationId)) {
            System.out.println("Cancellation failed: Already cancelled.");
            return;
        }

        Reservation res = bookingService.activeBookings.get(reservationId);

        // Step 1: Push room ID into stack (rollback tracking)
        rollbackStack.push(res.getRoomId());

        // Step 2: Restore inventory
        String roomType = res.getRoomType();
        bookingService.inventory.put(
                roomType,
                bookingService.inventory.get(roomType) + 1
        );

        // Step 3: Remove from active bookings
        bookingService.activeBookings.remove(reservationId);

        // Step 4: Mark as cancelled
        cancelledReservations.add(reservationId);

        System.out.println("Cancellation successful for Reservation ID: " + reservationId);
    }

    // Display rollback stack
    public void showRollbackStack() {
        System.out.println("Rollback Stack (recent releases): " + rollbackStack);
    }
}

// Main class
public class CancellationDemo {
    public static void main(String[] args) {

        BookingService bookingService = new BookingService();

        // Simulate confirmed bookings
        bookingService.confirmBooking("R101", "Mithran", "Standard", "RM1");
        bookingService.confirmBooking("R102", "Arun", "Deluxe", "RM2");

        CancellationService cancelService = new CancellationService(bookingService);

        // Valid cancellation
        cancelService.cancelBooking("R101");

        // Invalid cancellation (already cancelled)
        cancelService.cancelBooking("R101");

        // Invalid cancellation (not exist)
        cancelService.cancelBooking("R999");

        // Show rollback stack
        cancelService.showRollbackStack();

        // Show inventory
        System.out.println("\nUpdated Inventory:");
        System.out.println(bookingService.inventory);
    }
}