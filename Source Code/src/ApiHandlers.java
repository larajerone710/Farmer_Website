import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.util.*;

abstract class BaseHandler {
    String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
    
    Map<String, String> parseFormData(String formData) {
        Map<String, String> params = new HashMap<>();
        if (formData == null || formData.isEmpty()) return params;
        
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                try {
                    String key = java.net.URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                    params.put(key, value);
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return params;
    }
    
    void sendResponse(HttpExchange exchange, int status, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(status, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
    
    String getQueryParam(HttpExchange exchange, String paramName) {
        String query = exchange.getRequestURI().getQuery();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2 && paramName.equals(keyValue[0])) {
                    try {
                        return java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                    } catch (Exception e) {
                        return keyValue[1];
                    }
                }
            }
        }
        return null;
    }
    
    String escapeJSON(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t")
                   .replace("/", "\\/");
    }
}

class LoginHandler extends BaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 200, "{}");
            return;
        }
        
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                String requestBody = readRequestBody(exchange);
                Map<String, String> params = parseFormData(requestBody);
                
                String username = params.get("username");
                String password = params.get("password");
                
                DataManager.User user = DataManager.validateUser(username, password);
                
                if (user != null) {
                    String response = String.format(
                        "{\"success\":true,\"userType\":\"%s\",\"userId\":%d,\"fullName\":\"%s\",\"message\":\"Login successful\"}",
                        user.role, user.id, user.fullName
                    );
                    sendResponse(exchange, 200, response);
                } else {
                    String response = "{\"success\":false,\"message\":\"Invalid username or password\"}";
                    sendResponse(exchange, 401, response);
                }
            } catch (Exception e) {
                String response = "{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}";
                sendResponse(exchange, 500, response);
            }
        } else {
            sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }
}

class RegisterHandler extends BaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 200, "{}");
            return;
        }
        
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                String requestBody = readRequestBody(exchange);
                Map<String, String> params = parseFormData(requestBody);
                
                boolean success = DataManager.registerUser(
                    params.get("fullName"),
                    params.get("username"),
                    params.get("password"),
                    params.get("role"),
                    params.get("phone"),
                    params.get("address")
                );
                
                String response = success ? 
                    "{\"success\":true,\"message\":\"Registration successful\"}" :
                    "{\"success\":false,\"message\":\"Username already exists\"}";
                
                sendResponse(exchange, success ? 200 : 400, response);
            } catch (Exception e) {
                String response = "{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}";
                sendResponse(exchange, 500, response);
            }
        } else {
            sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }
}

class ProductsHandler extends BaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 200, "{}");
            return;
        }
        
        if ("GET".equals(exchange.getRequestMethod())) {
            try {
                String farmerIdParam = getQueryParam(exchange, "farmerId");
                String response;
                
                if (farmerIdParam != null) {
                    List<DataManager.Product> products = DataManager.getProductsByFarmer(Integer.parseInt(farmerIdParam));
                    response = formatProductsResponse(products);
                } else {
                    List<DataManager.Product> products = DataManager.getAllProducts();
                    response = formatProductsResponse(products);
                }
                
                sendResponse(exchange, 200, response);
            } catch (Exception e) {
                String response = "{\"success\":false,\"message\":\"Error loading products\"}";
                sendResponse(exchange, 500, response);
            }
        } else if ("POST".equals(exchange.getRequestMethod())) {
            try {
                String requestBody = readRequestBody(exchange);
                Map<String, String> params = parseFormData(requestBody);
                
                boolean success = DataManager.addProduct(
                    Integer.parseInt(params.get("farmerId")),
                    params.get("name"),
                    params.get("category"),
                    params.get("description"),
                    Double.parseDouble(params.get("price")),
                    Integer.parseInt(params.get("quantity")),
                    params.get("unit"),
                    Double.parseDouble(params.get("productionCost"))
                );
                
                String response = success ? 
                    "{\"success\":true,\"message\":\"Product added successfully\"}" :
                    "{\"success\":false,\"message\":\"Failed to add product\"}";
                
                sendResponse(exchange, success ? 200 : 400, response);
            } catch (Exception e) {
                String response = "{\"success\":false,\"message\":\"Error: " + e.getMessage() + "\"}";
                sendResponse(exchange, 500, response);
            }
        } else {
            sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }
    
    private String formatProductsResponse(List<DataManager.Product> products) {
        StringBuilder json = new StringBuilder();
        json.append("{\"success\":true,\"products\":[");
        
        for (int i = 0; i < products.size(); i++) {
            DataManager.Product p = products.get(i);
            if (i > 0) json.append(",");
            
            double profitMargin = p.price - p.productionCost;
            DataManager.User farmer = DataManager.getUserById(p.farmerId);
            String farmerName = farmer != null ? farmer.fullName : "Unknown Farmer";
            
            json.append(String.format(
                "{\"id\":%d,\"name\":\"%s\",\"category\":\"%s\",\"description\":\"%s\"," +
                "\"price\":%.2f,\"quantity\":%d,\"unit\":\"%s\",\"productionCost\":%.2f," +
                "\"profitMargin\":%.2f,\"farmerName\":\"%s\",\"farmerId\":%d}",
                p.id, escapeJSON(p.name), escapeJSON(p.category), escapeJSON(p.description),
                p.price, p.quantity, escapeJSON(p.unit), p.productionCost,
                profitMargin, escapeJSON(farmerName), p.farmerId
            ));
        }
        
        json.append("]}");
        return json.toString();
    }
}

class LanguageHandler extends BaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 200, "{}");
            return;
        }
        
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                String requestBody = readRequestBody(exchange);
                Map<String, String> params = parseFormData(requestBody);
                
                String language = params.get("language");
                String translations = LanguageManager.getTranslationsJSON(language);
                
                sendResponse(exchange, 200, "{\"success\":true,\"translations\":" + translations + "}");
            } catch (Exception e) {
                String response = "{\"success\":false,\"message\":\"Language error\"}";
                sendResponse(exchange, 500, response);
            }
        } else {
            sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }
}

class VoiceHandler extends BaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 200, "{}");
            return;
        }
        
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                String requestBody = readRequestBody(exchange);
                Map<String, String> params = parseFormData(requestBody);
                
                String command = params.get("command");
                String language = params.get("language");
                if (language == null) language = "en";
                
                String response = VoiceAssistant.processVoiceCommand(command, language);
                sendResponse(exchange, 200, response);
            } catch (Exception e) {
                String response = "{\"success\":false,\"message\":\"Error processing voice command: " + e.getMessage() + "\"}";
                sendResponse(exchange, 500, response);
            }
        } else {
            sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }
}

class StatsHandler extends BaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 200, "{}");
            return;
        }
        
        if ("GET".equals(exchange.getRequestMethod())) {
            try {
                String userId = getQueryParam(exchange, "userId");
                String userType = getQueryParam(exchange, "userType");
                
                // For demo purposes - return sample stats
                String response;
                if ("farmer".equals(userType)) {
                    response = "{\"success\":true,\"stats\":{\"totalProducts\":8,\"totalOrders\":42,\"totalRevenue\":2450.00,\"rating\":4.8}}";
                } else {
                    response = "{\"success\":true,\"stats\":{\"totalOrders\":12,\"totalSpent\":325.50,\"savings\":45.75}}";
                }
                
                sendResponse(exchange, 200, response);
            } catch (Exception e) {
                String response = "{\"success\":false,\"message\":\"Error loading stats: " + e.getMessage() + "\"}";
                sendResponse(exchange, 500, response);
            }
        } else {
            sendResponse(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }
}