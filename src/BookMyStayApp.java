import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Validator class (Fail-Fast Design)
class BookingValidator {

    private static final Set<String> VALID_ROOM_TYPES =
            new HashSet<>(Arrays.asList("Standard", "Deluxe", "Suite"));

    public static void validate(String guestName, String roomType, int availableRooms)
            throws InvalidBookingException {

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        // Validate availability
        if (availableRooms <= 0) {
            throw new InvalidBookingException("No rooms available for booking.");
        }
    }
}

// Booking Service (uses validation)
class BookingService {

    private Map<String, Integer> roomInventory;

    public BookingService() {
        roomInventory = new HashMap<>();
        roomInventory.put("Standard", 2);
        roomInventory.put("Deluxe", 1);
        roomInventory.put("Suite", 0); // No rooms available
    }

    public void bookRoom(String reservationId, String guestName, String roomType) {
        try {
            int available = roomInventory.getOrDefault(roomType, 0);

            // Validate before booking
            BookingValidator.validate(guestName, roomType, available);

            // Reduce inventory
            roomInventory.put(roomType, available - 1);

            // Success
            System.out.println("Booking successful for " + guestName +
                    " | Room: " + roomType +
                    " | ID: " + reservationId);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking failed: " + e.getMessage());
        }
    }

    // Display inventory
    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String room : roomInventory.keySet()) {
            System.out.println(room + " : " + roomInventory.get(room));
        }
    }
}

// Main class
public class BookingValidationDemo {
    public static void main(String[] args) {

        BookingService service = new BookingService();

        // Valid booking
        service.bookRoom("R101", "Mithran", "Standard");

        // Invalid room type
        service.bookRoom("R102", "Arun", "Luxury");

        // No availability
        service.bookRoom("R103", "Kiran", "Suite");

        // Empty name
        service.bookRoom("R104", "", "Deluxe");

        // Check inventory
        service.displayInventory();
    }
}