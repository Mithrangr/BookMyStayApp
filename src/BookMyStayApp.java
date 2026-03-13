import java.util.*;

/* Abstract Room */
abstract class Room {
    protected String roomType;
    protected int beds;
    protected int size;
    protected double price;

    public Room(String roomType, int beds, int size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }
}

/* Concrete Room Types */
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200, 100);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350, 180);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600, 350);
    }
}

/* Inventory Service */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decreaseAvailability(String roomType) {
        int current = inventory.get(roomType);
        inventory.put(roomType, current - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

/* Reservation */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/* Booking Request Queue */
class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

/* Booking Service */
class BookingService {

    private RoomInventory inventory;

    private Set<String> allocatedRoomIds = new HashSet<>();

    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();

    private int roomCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBookingQueue(BookingRequestQueue requestQueue) {

        while (!requestQueue.isEmpty()) {

            Reservation reservation = requestQueue.getNextRequest();
            String roomType = reservation.getRoomType();

            if (inventory.getAvailability(roomType) > 0) {

                String roomId = generateRoomId(roomType);

                allocatedRoomIds.add(roomId);

                roomAllocations.putIfAbsent(roomType, new HashSet<>());
                roomAllocations.get(roomType).add(roomId);

                inventory.decreaseAvailability(roomType);

                System.out.println("Reservation Confirmed:");
                System.out.println("Guest: " + reservation.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println();
            } else {
                System.out.println("Reservation Failed for " + reservation.getGuestName() +
                        " (No rooms available for " + roomType + ")");
            }
        }
    }

    private String generateRoomId(String roomType) {

        String prefix = roomType.replace(" ", "").substring(0, 2).toUpperCase();
        String roomId = prefix + roomCounter++;

        while (allocatedRoomIds.contains(roomId)) {
            roomId = prefix + roomCounter++;
        }

        return roomId;
    }

    public void displayAllocatedRooms() {

        System.out.println("\nAllocated Rooms:");

        for (Map.Entry<String, Set<String>> entry : roomAllocations.entrySet()) {

            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

/* Application Entry */
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        BookingRequestQueue requestQueue = new BookingRequestQueue();

        requestQueue.addRequest(new Reservation("Arun", "Single Room"));
        requestQueue.addRequest(new Reservation("Priya", "Double Room"));
        requestQueue.addRequest(new Reservation("Rahul", "Suite Room"));
        requestQueue.addRequest(new Reservation("Kiran", "Single Room"));

        BookingService bookingService = new BookingService(inventory);

        bookingService.processBookingQueue(requestQueue);

        bookingService.displayAllocatedRooms();

        inventory.displayInventory();
    }
}