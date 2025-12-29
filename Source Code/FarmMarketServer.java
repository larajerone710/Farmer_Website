import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FarmMarketServer {
    private static final int PORT = 8080;
    private static final String WEB_ROOT = "web";
    
    public static void main(String[] args) throws IOException {
        System.out.println("🚀 Starting Direct Farm Market Server...");
        System.out.println("🌱 Server: http://localhost:" + PORT);
        System.out.println("📁 Web root: " + new File(WEB_ROOT).getAbsolutePath());
        System.out.println("🗣️ Languages: English, Tamil, Hindi, Kannada, Malayalam");
        System.out.println("👨‍🌾 Roles: Farmer & Consumer");
        
        // Initialize data storage and language manager
        DataManager.initialize();
        
        // Create HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // API endpoints
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/register", new RegisterHandler());
        server.createContext("/api/products", new ProductsHandler());
        server.createContext("/api/language", new LanguageHandler());
        server.createContext("/api/voice", new VoiceHandler());
        server.createContext("/api/stats", new StatsHandler());
        
        // Serve static files
        server.createContext("/", new StaticFileHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("✅ Server started successfully on port " + PORT);
        System.out.println("📊 Access the application at: http://localhost:" + PORT);
        
        // Check if web directory exists
        File webDir = new File(WEB_ROOT);
        if (!webDir.exists()) {
            System.out.println("⚠️  Warning: 'web' directory not found. Creating it now...");
            webDir.mkdir();
            createDefaultHtmlFiles();
        }
    }
    
    private static void createDefaultHtmlFiles() {
        try {
            // Create default index.html
            File indexFile = new File(WEB_ROOT + "/index.html");
            FileWriter writer = new FileWriter(indexFile);
            writer.write("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Direct Farm Market</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; margin: 40px; text-align: center; }\n" +
                "        .container { max-width: 800px; margin: 0 auto; }\n" +
                "        .btn { background: #4CAF50; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; margin: 10px; display: inline-block; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>🌱 Direct Farm Market</h1>\n" +
                "        <p>Farm to Table, Direct to You</p>\n" +
                "        <p>Server is running successfully!</p>\n" +
                "        <div>\n" +
                "            <a href=\"farmer-dashboard.html\" class=\"btn\">Farmer Dashboard</a>\n" +
                "            <a href=\"consumer-dashboard.html\" class=\"btn\">Consumer Dashboard</a>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>");
            writer.close();
            System.out.println("✅ Created default index.html");
            
        } catch (IOException e) {
            System.out.println("❌ Error creating default HTML files: " + e.getMessage());
        }
    }
    
    // Static file handler for serving HTML, CSS, JS files
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) {
                path = "/index.html";
            }
            
            File file = new File(WEB_ROOT + path);
            
            if (!file.exists() || file.isDirectory()) {
                send404(exchange);
                return;
            }
            
            String contentType = getContentType(path);
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, file.length());
            
            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = exchange.getResponseBody()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        }
        
        private String getContentType(String path) {
            if (path.endsWith(".html")) return "text/html";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".png")) return "image/png";
            if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
            return "text/plain";
        }
        
        private void send404(HttpExchange exchange) throws IOException {
            String response = "404 - File not found";
            exchange.sendResponseHeaders(404, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
    
    // Login handler
    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Read request body
                String requestBody = readRequestBody(exchange);
                
                // Parse JSON (simplified - in real app use a JSON library)
                String username = extractValue(requestBody, "username");
                String password = extractValue(requestBody, "password");
                
                // Simple authentication (in real app, use proper authentication)
                boolean isValid = DataManager.authenticateUser(username, password);
                
                Map<String, Object> response = new HashMap<>();
                if (isValid) {
                    response.put("success", true);
                    response.put("message", "Login successful");
                    response.put("userType", DataManager.getUserType(username));
                    response.put("fullName", DataManager.getFullName(username));
                } else {
                    response.put("success", false);
                    response.put("message", "Invalid credentials");
                }
                
                sendJsonResponse(exchange, response);
            } else {
                sendError(exchange, 405, "Method not allowed");
            }
        }
    }
    
    // Register handler
    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = readRequestBody(exchange);
                
                // Extract registration data
                String fullName = extractValue(requestBody, "fullName");
                String userType = extractValue(requestBody, "userType");
                String phone = extractValue(requestBody, "phone");
                String address = extractValue(requestBody, "address");
                String username = extractValue(requestBody, "username");
                String password = extractValue(requestBody, "password");
                
                // Register user
                boolean success = DataManager.registerUser(fullName, userType, phone, address, username, password);
                
                Map<String, Object> response = new HashMap<>();
                if (success) {
                    response.put("success", true);
                    response.put("message", "Registration successful");
                } else {
                    response.put("success", false);
                    response.put("message", "Username already exists");
                }
                
                sendJsonResponse(exchange, response);
            } else {
                sendError(exchange, 405, "Method not allowed");
            }
        }
    }
    
    // Products handler
    static class ProductsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                // Return list of products
                List<Map<String, Object>> products = DataManager.getProducts();
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("products", products);
                
                sendJsonResponse(exchange, response);
            } else if ("POST".equals(exchange.getRequestMethod())) {
                // Add new product
                String requestBody = readRequestBody(exchange);
                
                String name = extractValue(requestBody, "name");
                String category = extractValue(requestBody, "category");
                String description = extractValue(requestBody, "description");
                double price = Double.parseDouble(extractValue(requestBody, "price"));
                int quantity = Integer.parseInt(extractValue(requestBody, "quantity"));
                String farmer = extractValue(requestBody, "farmer");
                
                boolean success = DataManager.addProduct(name, category, description, price, quantity, farmer);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", success);
                response.put("message", success ? "Product added successfully" : "Failed to add product");
                
                sendJsonResponse(exchange, response);
            } else {
                sendError(exchange, 405, "Method not allowed");
            }
        }
    }
    
    // Language handler
    static class LanguageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String lang = exchange.getRequestURI().getQuery();
                if (lang != null && lang.startsWith("lang=")) {
                    lang = lang.substring(5);
                } else {
                    lang = "en";
                }
                
                Map<String, String> translations = DataManager.getTranslations(lang);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("translations", translations);
                
                sendJsonResponse(exchange, response);
            } else {
                sendError(exchange, 405, "Method not allowed");
            }
        }
    }
    
    // Voice handler
    static class VoiceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = readRequestBody(exchange);
                String command = extractValue(requestBody, "command");
                
                // Process voice command (simplified)
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("action", processVoiceCommand(command));
                response.put("message", "Voice command processed");
                
                sendJsonResponse(exchange, response);
            } else {
                sendError(exchange, 405, "Method not allowed");
            }
        }
        
        private String processVoiceCommand(String command) {
            // Simple voice command processing
            command = command.toLowerCase();
            if (command.contains("login") || command.contains("sign in")) {
                return "login";
            } else if (command.contains("register") || command.contains("sign up")) {
                return "register";
            } else if (command.contains("products") || command.contains("market")) {
                return "marketplace";
            } else if (command.contains("orders")) {
                return "orders";
            } else {
                return "dashboard";
            }
        }
    }
    
    // Stats handler
    static class StatsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, Object> stats = DataManager.getStats();
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("stats", stats);
                
                sendJsonResponse(exchange, response);
            } else {
                sendError(exchange, 405, "Method not allowed");
            }
        }
    }
    
    // Utility methods
    private static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        return bos.toString(StandardCharsets.UTF_8.name());
    }
    
    private static String extractValue(String requestBody, String key) {
        // Simple key-value extraction from form data or JSON-like string
        String searchKey = "\"" + key + "\":\"";
        int start = requestBody.indexOf(searchKey);
        if (start == -1) {
            searchKey = key + "=";
            start = requestBody.indexOf(searchKey);
            if (start == -1) return "";
            start += searchKey.length();
            int end = requestBody.indexOf("&", start);
            if (end == -1) end = requestBody.length();
            return requestBody.substring(start, end);
        } else {
            start += searchKey.length();
            int end = requestBody.indexOf("\"", start);
            return requestBody.substring(start, end);
        }
    }
    
    private static void sendJsonResponse(HttpExchange exchange, Map<String, Object> response) throws IOException {
        String jsonResponse = mapToJson(response);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, jsonResponse.length());
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }
    
    private static void sendError(HttpExchange exchange, int code, String message) throws IOException {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        
        String jsonResponse = mapToJson(response);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, jsonResponse.length());
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }
    
    private static String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            json.append("\"").append(entry.getKey()).append("\":");
            
            Object value = entry.getValue();
            if (value instanceof String) {
                json.append("\"").append(escapeJson(value.toString())).append("\"");
            } else if (value instanceof List) {
                json.append(listToJson((List<?>) value));
            } else if (value instanceof Map) {
                json.append(mapToJson((Map<String, Object>) value));
            } else {
                json.append(value);
            }
            first = false;
        }
        json.append("}");
        return json.toString();
    }
    
    private static String listToJson(List<?> list) {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;
        for (Object item : list) {
            if (!first) {
                json.append(",");
            }
            if (item instanceof Map) {
                json.append(mapToJson((Map<String, Object>) item));
            } else if (item instanceof String) {
                json.append("\"").append(escapeJson(item.toString())).append("\"");
            } else {
                json.append(item);
            }
            first = false;
        }
        json.append("]");
        return json.toString();
    }
    
    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\b", "\\b")
                  .replace("\f", "\\f")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}

// Data Manager class for handling data storage
class DataManager {
    private static Map<String, User> users = new HashMap<>();
    private static List<Product> products = new ArrayList<>();
    private static Map<String, Map<String, String>> translations = new HashMap<>();
    
    public static void initialize() {
        // Initialize with demo data
        users.put("john", new User("John Farmer", "farmer", "1234567890", "Farm Street", "john", "password123"));
        users.put("sarah", new User("Sarah Consumer", "consumer", "0987654321", "City Center", "sarah", "password123"));
        
        // Add sample products
        products.add(new Product("Organic Carrots", "vegetables", "Fresh organic carrots from our farm", 2.99, 50, "John Farmer"));
        products.add(new Product("Fresh Apples", "fruits", "Sweet and crunchy apples", 3.49, 30, "Sunny Orchard"));
        products.add(new Product("Farm Eggs", "dairy", "Free-range farm eggs", 4.99, 20, "Happy Hens Farm"));
        
        // Initialize translations
        initializeTranslations();
    }
    
    private static void initializeTranslations() {
        // English translations
        Map<String, String> en = new HashMap<>();
        en.put("welcome", "🌱 Direct Farm Market");
        en.put("subtitle", "Farm to Table, Direct to You");
        en.put("username", "Username");
        en.put("password", "Password");
        en.put("login", "Login");
        en.put("no_account", "Don't have an account?");
        en.put("register", "Register");
        translations.put("en", en);
        
        // Tamil translations
        Map<String, String> ta = new HashMap<>();
        ta.put("welcome", "🌱 நேரடி பண்ணை சந்தை");
        ta.put("subtitle", "பண்ணையில் இருந்து மேசைக்கு, நேரடியாக உங்களுக்கு");
        ta.put("username", "பயனர்பெயர்");
        ta.put("password", "கடவுச்சொல்");
        ta.put("login", "உள்நுழைக");
        ta.put("no_account", "கணக்கு இல்லையா?");
        ta.put("register", "பதிவு செய்க");
        translations.put("ta", ta);
    }
    
    public static boolean authenticateUser(String username, String password) {
        User user = users.get(username);
        return user != null && user.password.equals(password);
    }
    
    public static boolean registerUser(String fullName, String userType, String phone, String address, String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(fullName, userType, phone, address, username, password));
        return true;
    }
    
    public static String getUserType(String username) {
        User user = users.get(username);
        return user != null ? user.userType : null;
    }
    
    public static String getFullName(String username) {
        User user = users.get(username);
        return user != null ? user.fullName : null;
    }
    
    public static List<Map<String, Object>> getProducts() {
        List<Map<String, Object>> productList = new ArrayList<>();
        for (Product product : products) {
            Map<String, Object> productMap = new HashMap<>();
            productMap.put("id", product.id);
            productMap.put("name", product.name);
            productMap.put("category", product.category);
            productMap.put("description", product.description);
            productMap.put("price", product.price);
            productMap.put("quantity", product.quantity);
            productMap.put("farmer", product.farmer);
            productList.add(productMap);
        }
        return productList;
    }
    
    public static boolean addProduct(String name, String category, String description, double price, int quantity, String farmer) {
        products.add(new Product(name, category, description, price, quantity, farmer));
        return true;
    }
    
    public static Map<String, String> getTranslations(String lang) {
        return translations.getOrDefault(lang, translations.get("en"));
    }
    
    public static Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", users.size());
        stats.put("totalProducts", products.size());
        stats.put("totalFarmers", users.values().stream().filter(u -> u.userType.equals("farmer")).count());
        stats.put("totalConsumers", users.values().stream().filter(u -> u.userType.equals("consumer")).count());
        return stats;
    }
}

// User class
class User {
    String fullName;
    String userType;
    String phone;
    String address;
    String username;
    String password;
    
    public User(String fullName, String userType, String phone, String address, String username, String password) {
        this.fullName = fullName;
        this.userType = userType;
        this.phone = phone;
        this.address = address;
        this.username = username;
        this.password = password;
    }
}

// Product class
class Product {
    static int nextId = 1;
    int id;
    String name;
    String category;
    String description;
    double price;
    int quantity;
    String farmer;
    
    public Product(String name, String category, String description, double price, int quantity, String farmer) {
        this.id = nextId++;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.farmer = farmer;
    }
}