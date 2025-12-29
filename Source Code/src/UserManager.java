import java.util.*;

public class UserManager {
    
    public static Map<String, Object> validateUser(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String passwordHash = DataStorage.hashPassword(password);
            Map<String, Object> user = DataStorage.getUserByUsername(username);
            
            if (user != null && passwordHash.equals(user.get("passwordHash"))) {
                result.put("success", true);
                result.put("userType", user.get("userType"));
                result.put("userId", ((Long) user.get("id")).intValue());
                result.put("fullName", user.get("fullName"));
            } else {
                result.put("success", false);
                result.put("message", "Invalid credentials");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Login error");
        }
        
        return result;
    }
    
    public static boolean registerUser(String fullName, String username, String password, 
                                     String userType, String phone, String address) {
        String passwordHash = DataStorage.hashPassword(password);
        return DataStorage.saveUser(fullName, username, passwordHash, userType, phone, address);
    }
    
    public static String getMarketStatsJSON() {
        try {
            List<Map<String, Object>> products = DataStorage.loadProducts();
            List<Map<String, Object>> users = DataStorage.loadUsers();
            List<Map<String, Object>> orders = DataStorage.loadOrders();
            
            int totalProducts = products.size();
            int totalFarmers = 0;
            int totalConsumers = 0;
            int totalOrders = orders.size();
            double totalRevenue = 0;
            
            for (Map<String, Object> user : users) {
                if ("farmer".equals(user.get("userType"))) {
                    totalFarmers++;
                } else {
                    totalConsumers++;
                }
            }
            
            for (Map<String, Object> order : orders) {
                totalRevenue += ((Number) order.get("totalPrice")).doubleValue();
            }
            
            return String.format(
                "{\"success\": true, \"stats\": {" +
                "\"totalProducts\": %d, \"totalFarmers\": %d, \"totalConsumers\": %d, " +
                "\"totalOrders\": %d, \"totalRevenue\": %.2f}}",
                totalProducts, totalFarmers, totalConsumers, totalOrders, totalRevenue
            );
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"Error calculating stats\"}";
        }
    }
    
    public static String getUserStatsJSON(int userId, String userType) {
        try {
            if ("farmer".equals(userType)) {
                return getFarmerStatsJSON(userId);
            } else {
                return getConsumerStatsJSON(userId);
            }
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"Error calculating user stats\"}";
        }
    }
    
    private static String getFarmerStatsJSON(int farmerId) {
        List<Map<String, Object>> products = DataStorage.getProductsByFarmer(farmerId);
        List<Map<String, Object>> orders = DataStorage.getOrdersByFarmer(farmerId);
        
        int totalProducts = products.size();
        int totalOrders = orders.size();
        double totalRevenue = 0;
        int pendingOrders = 0;
        int completedOrders = 0;
        
        for (Map<String, Object> order : orders) {
            totalRevenue += ((Number) order.get("totalPrice")).doubleValue();
            if ("pending".equals(order.get("status"))) {
                pendingOrders++;
            } else {
                completedOrders++;
            }
        }
        
        return String.format(
            "{\"success\": true, \"stats\": {" +
            "\"userType\": \"farmer\", \"totalProducts\": %d, \"totalOrders\": %d, " +
            "\"totalRevenue\": %.2f, \"pendingOrders\": %d, \"completedOrders\": %d}}",
            totalProducts, totalOrders, totalRevenue, pendingOrders, completedOrders
        );
    }
    
    private static String getConsumerStatsJSON(int consumerId) {
        List<Map<String, Object>> orders = DataStorage.getOrdersByConsumer(consumerId);
        
        int totalOrders = orders.size();
        double totalSpent = 0;
        int pendingOrders = 0;
        int completedOrders = 0;
        
        for (Map<String, Object> order : orders) {
            totalSpent += ((Number) order.get("totalPrice")).doubleValue();
            if ("pending".equals(order.get("status"))) {
                pendingOrders++;
            } else {
                completedOrders++;
            }
        }
        
        return String.format(
            "{\"success\": true, \"stats\": {" +
            "\"userType\": \"consumer\", \"totalOrders\": %d, \"totalSpent\": %.2f, " +
            "\"pendingOrders\": %d, \"completedOrders\": %d}}",
            totalOrders, totalSpent, pendingOrders, completedOrders
        );
    }
}