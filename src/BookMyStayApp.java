import java.util.*;

// Class representing an Add-On Service
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

// Manager class to handle mapping between Reservation and Services
class AddOnServiceManager {

    // Map: ReservationID -> List of Services
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Add service to a reservation
    public void addServiceToReservation(String reservationId, AddOnService service) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).add(service);
    }

    // Get all services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServices.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of services
    public double calculateTotalServiceCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = getServices(reservationId);

        for (AddOnService service : services) {
            total += service.getCost();
        }
        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No services added.");
            return;
        }

        System.out.println("Services for Reservation " + reservationId + ":");
        for (AddOnService service : services) {
            System.out.println("- " + service.getServiceName() + " : ₹" + service.getCost());
        }
    }
}

// Main class (Driver)
public class AddOnDemo {
    public static void main(String[] args) {

        AddOnServiceManager manager = new AddOnServiceManager();

        // Sample services
        AddOnService wifi = new AddOnService("WiFi", 100);
        AddOnService breakfast = new AddOnService("Breakfast", 250);
        AddOnService laundry = new AddOnService("Laundry", 150);

        String reservationId = "R101";

        // Guest selects services
        manager.addServiceToReservation(reservationId, wifi);
        manager.addServiceToReservation(reservationId, breakfast);
        manager.addServiceToReservation(reservationId, laundry);

        // Display services
        manager.displayServices(reservationId);

        // Calculate total cost
        double totalCost = manager.calculateTotalServiceCost(reservationId);
        System.out.println("Total Add-On Cost: ₹" + totalCost);
    }
}