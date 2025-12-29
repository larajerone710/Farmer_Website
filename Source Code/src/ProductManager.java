import java.util.*;

public class ProductManager {
    
    public static boolean addProduct(int farmerId, String name, String category, String description,
                                   double price, int quantity, String unit, double productionCost, String imagePath) {
        return DataStorage.saveProduct(farmerId, name, category, description, price, quantity, unit, productionCost, imagePath);
    }
    
    public static String getAllProductsJSON() {
        try {
            List<Map<String, Object>> products = DataStorage.loadProducts();
            StringBuilder json = new StringBuilder();
            json.append("{\"success\": true, \"products\": [");
            
            for (int i = 0; i < products.size(); i++) {
                Map<String, Object> p = products.get(i);
                if (i > 0) json.append(",");
                
                double price = ((Number) p.get("price")).doubleValue();
                double productionCost = ((Number) p.get("productionCost")).doubleValue();
                double profitMargin = price - productionCost;
                
                json.append(String.format(
                    "{\"id\": %d, \"name\": \"%s\", \"category\": \"%s\", \"description\": \"%s\", " +
                    "\"price\": %.2f, \"quantity\": %d, \"unit\": \"%s\", \"productionCost\": %.2f, " +
                    "\"farmerName\": \"%s\", \"farmerId\": %d, \"profitMargin\": %.2f, \"imagePath\": \"%s\"}",
                    ((Long) p.get("id")).intValue(),
                    escapeJSON((String) p.get("name")),
                    escapeJSON((String) p.get("category")),
                    escapeJSON((String) p.get("description")),
                    price,
                    ((Long) p.get("quantity")).intValue(),
                    escapeJSON((String) p.get("unit")),
                    productionCost,
                    escapeJSON((String) p.get("farmerName")),
                    ((Long) p.get("farmerId")).intValue(),
                    profitMargin,
                    escapeJSON((String) p.getOrDefault("imagePath", ""))
                ));
            }
            
            json.append("]}");
            return json.toString();
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"Error loading products\"}";
        }
    }
    
    public static String getFarmerProductsJSON(int farmerId) {
        try {
            List<Map<String, Object>> products = DataStorage.getProductsByFarmer(farmerId);
            StringBuilder json = new StringBuilder();
            json.append("{\"success\": true, \"products\": [");
            
            for (int i = 0; i < products.size(); i++) {
                Map<String, Object> p = products.get(i);
                if (i > 0) json.append(",");
                
                double price = ((Number) p.get("price")).doubleValue();
                double productionCost = ((Number) p.get("productionCost")).doubleValue();
                double profitMargin = price - productionCost;
                
                json.append(String.format(
                    "{\"id\": %d, \"name\": \"%s\", \"category\": \"%s\", \"description\": \"%s\", " +
                    "\"price\": %.2f, \"quantity\": %d, \"unit\": \"%s\", \"productionCost\": %.2f, " +
                    "\"profitMargin\": %.2f, \"imagePath\": \"%s\", \"createdAt\": \"%s\"}",
                    ((Long) p.get("id")).intValue(),
                    escapeJSON((String) p.get("name")),
                    escapeJSON((String) p.get("category")),
                    escapeJSON((String) p.get("description")),
                    price,
                    ((Long) p.get("quantity")).intValue(),
                    escapeJSON((String) p.get("unit")),
                    productionCost,
                    profitMargin,
                    escapeJSON((String) p.getOrDefault("imagePath", "")),
                    escapeJSON((String) p.get("createdAt"))
                ));
            }
            
            json.append("]}");
            return json.toString();
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"Error loading farmer products\"}";
        }
    }
    
    private static String escapeJSON(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}