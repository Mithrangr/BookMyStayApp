import java.util.*;

// Reservation class (basic model)
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

// Booking History (stores confirmed bookings)
class BookingHistory {

    // List preserves insertion order
    private List<Reservation> historyList;

    public BookingHistory() {
        historyList = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        historyList.add(reservation);
    }

    // Get all reservations
    public List<Reservation> getAllReservations() {
        return historyList;
    }
}

// Reporting Service (separate from storage)
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        System.out.println("Booking History:");
        for (Reservation r : reservations) {
            System.out.println(
                    "ID: " + r.getReservationId() +
                            ", Guest: " + r.getGuestName() +
                            ", Room: " + r.getRoomType()
            );
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        System.out.println("\n--- Booking Summary ---");
        System.out.println("Total Bookings: " + reservations.size());

        // Count by room type
        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomCount.put(
                    r.getRoomType(),
                    roomCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("Room Type Distribution:");
        for (String room : roomCount.keySet()) {
            System.out.println(room + " : " + roomCount.get(room));
        }
    }
}

// Main class (Driver)
public class BookingHistoryDemo {
    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirmed bookings
        history.addReservation(new Reservation("R101", "Mithran", "Deluxe"));
        history.addReservation(new Reservation("R102", "Arun", "Standard"));
        history.addReservation(new Reservation("R103", "Kiran", "Deluxe"));

        // Admin views history
        List<Reservation> bookings = history.getAllReservations();

        reportService.displayAllBookings(bookings);
        reportService.generateSummary(bookings);
    }
}