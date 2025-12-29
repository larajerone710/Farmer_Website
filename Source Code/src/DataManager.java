import java.io.*;
import java.util.*;
import java.security.MessageDigest;

public class DataManager {
    private static final String DATA_FILE = "farm_market_data.txt";
    private static Map<String, User> users = new HashMap<>();
    private static List<Product> products = new ArrayList<>();
    private static int nextUserId = 1;
    private static int nextProductId = 1;
    
    public static void initialize() {
        loadData();
        createSampleData();
    }
    
    // User class
    public static class User {
        public int id;
        public String username;
        public String passwordHash;
        public String fullName;
        public String role; // "farmer" or "consumer"
        public String phone;
        public String address;
        
        public User(String username, String password, String fullName, String role, String phone, String address) {
            this.id = nextUserId++;
            this.username = username;
            this.passwordHash = hashPassword(password);
            this.fullName = fullName;
            this.role = role;
            this.phone = phone;
            this.address = address;
        }
    }
    
    // Product class
    public static class Product {
        public int id;
        public int farmerId;
        public String name;
        public String category;
        public String description;
        public double price;
        public int quantity;
        public String unit;
        public double productionCost;
        
        public Product(int farmerId, String name, String category, String description, 
                      double price, int quantity, String unit, double productionCost) {
            this.id = nextProductId++;
            this.farmerId = farmerId;
            this.name = name;
            this.category = category;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
            this.unit = unit;
            this.productionCost = productionCost;
        }
    }
    
    // User operations
    public static boolean registerUser(String fullName, String username, String password, 
                                     String role, String phone, String address) {
        if (users.containsKey(username)) {
            return false;
        }
        
        User user = new User(username, password, fullName, role, phone, address);
        users.put(username, user);
        saveData();
        return true;
    }
    
    public static User validateUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.passwordHash.equals(hashPassword(password))) {
            return user;
        }
        return null;
    }
    
    public static User getUserById(int userId) {
        for (User user : users.values()) {
            if (user.id == userId) {
                return user;
            }
        }
        return null;
    }
    
    // Product operations
    public static boolean addProduct(int farmerId, String name, String category, String description,
                                   double price, int quantity, String unit, double productionCost) {
        Product product = new Product(farmerId, name, category, description, price, quantity, unit, productionCost);
        products.add(product);
        saveData();
        return true;
    }
    
    public static List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
    
    public static List<Product> getProductsByFarmer(int farmerId) {
        List<Product> farmerProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.farmerId == farmerId) {
                farmerProducts.add(product);
            }
        }
        return farmerProducts;
    }
    
    // Data persistence
    private static void loadData() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) return;
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                
                if (parts[0].equals("USER")) {
                    User user = new User("", "", "", "", "", "");
                    user.id = Integer.parseInt(parts[1]);
                    user.username = parts[2];
                    user.passwordHash = parts[3];
                    user.fullName = parts[4];
                    user.role = parts[5];
                    user.phone = parts[6];
                    user.address = parts[7];
                    users.put(user.username, user);
                    nextUserId = Math.max(nextUserId, user.id + 1);
                } else if (parts[0].equals("PRODUCT")) {
                    Product product = new Product(0, "", "", "", 0, 0, "", 0);
                    product.id = Integer.parseInt(parts[1]);
                    product.farmerId = Integer.parseInt(parts[2]);
                    product.name = parts[3];
                    product.category = parts[4];
                    product.description = parts[5];
                    product.price = Double.parseDouble(parts[6]);
                    product.quantity = Integer.parseInt(parts[7]);
                    product.unit = parts[8];
                    product.productionCost = Double.parseDouble(parts[9]);
                    products.add(product);
                    nextProductId = Math.max(nextProductId, product.id + 1);
                }
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    
    private static void saveData() {
        try {
            PrintWriter writer = new PrintWriter(DATA_FILE);
            
            for (User user : users.values()) {
                writer.println(String.format("USER|%d|%s|%s|%s|%s|%s|%s",
                    user.id, user.username, user.passwordHash, user.fullName,
                    user.role, user.phone, user.address));
            }
            
            for (Product product : products) {
                writer.println(String.format("PRODUCT|%d|%d|%s|%s|%s|%.2f|%d|%s|%.2f",
                    product.id, product.farmerId, product.name, product.category,
                    product.description, product.price, product.quantity,
                    product.unit, product.productionCost));
            }
            
            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    
    private static void createSampleData() {
        if (users.isEmpty()) {
            // Create sample farmer
            registerUser("John Farmer", "john", "password123", "farmer", 
                        "+91 9876543210", "123 Farm Street, Agricultural City");
            
            // Create sample consumer
            registerUser("Sarah Consumer", "sarah", "password123", "consumer",
                        "+91 9876543211", "456 Market Road, Consumer Town");
            
            // Create sample products
            User farmer = users.get("john");
            addProduct(farmer.id, "Organic Tomatoes", "vegetables", 
                      "Fresh organic tomatoes grown without pesticides", 3.99, 50, "kg", 1.50);
            addProduct(farmer.id, "Honeycrisp Apples", "fruits", 
                      "Sweet and crisp apples, perfect for eating fresh", 4.50, 75, "kg", 2.00);
            addProduct(farmer.id, "Free-Range Eggs", "dairy", 
                      "Farm-fresh eggs from free-range chickens", 5.99, 30, "dozen", 2.50);
        }
    }
    
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (Exception e) {
            return password;
        }
    }
}