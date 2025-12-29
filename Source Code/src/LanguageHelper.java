import java.util.*;

public class LanguageHelper {
    private static Map<String, Map<String, String>> translations = new HashMap<>();
    
    static {
        loadTranslations();
    }
    
    private static void loadTranslations() {
        // English translations
        Map<String, String> en = new HashMap<>();
        en.put("welcome", "Welcome");
        en.put("logout", "Logout");
        en.put("dashboard", "Dashboard");
        en.put("farmer", "Farmer");
        en.put("consumer", "Consumer");
        en.put("farmer_dashboard", "Farmer Dashboard");
        en.put("consumer_dashboard", "Consumer Dashboard");
        en.put("manage_business", "Manage your farm business and track performance");
        en.put("shop_fresh", "Shop fresh farm products directly from farmers");
        en.put("my_products", "My Products");
        en.put("add_product", "Add Product");
        en.put("add_product_desc", "List your farm products with transparent pricing");
        en.put("product_name", "Product Name");
        en.put("category", "Category");
        en.put("description", "Description");
        en.put("price", "Price");
        en.put("quantity", "Quantity");
        en.put("unit", "Unit");
        en.put("production_cost", "Production Cost");
        en.put("transparent_pricing", "Transparent Pricing");
        en.put("profit_margin", "Profit Margin");
        en.put("total_products", "Total Products");
        en.put("total_orders", "Total Orders");
        en.put("total_revenue", "Total Revenue");
        en.put("customer_rating", "Customer Rating");
        en.put("login", "Login");
        en.put("register", "Register");
        en.put("username", "Username");
        en.put("password", "Password");
        en.put("full_name", "Full Name");
        en.put("phone", "Phone");
        en.put("address", "Address");
        en.put("select_role", "Select Role");
        
        // Tamil translations
        Map<String, String> ta = new HashMap<>();
        ta.put("welcome", "வரவேற்கிறோம்");
        ta.put("logout", "வெளியேறு");
        ta.put("dashboard", "டாஷ்போர்டு");
        ta.put("farmer", "விவசாயி");
        ta.put("consumer", "நுகர்வோர்");
        ta.put("farmer_dashboard", "விவசாயி டாஷ்போர்டு");
        ta.put("consumer_dashboard", "நுகர்வோர் டாஷ்போர்டு");
        ta.put("manage_business", "உங்கள் விவசாய வணிகத்தை நிர்வகித்து செயல்திறனை கண்காணிக்கவும்");
        ta.put("shop_fresh", "விவசாயிகளிடமிருந்து நேரடியாக புதிய விவசாய பொருட்களை வாங்கவும்");
        ta.put("my_products", "எனது பொருட்கள்");
        ta.put("add_product", "பொருள் சேர்க்க");
        ta.put("add_product_desc", "விலை விவரிப்புடன் உங்கள் விவசாய பொருட்களை பட்டியலிடுங்கள்");
        ta.put("product_name", "பொருள் பெயர்");
        ta.put("category", "வகை");
        ta.put("description", "விளக்கம்");
        ta.put("price", "விலை");
        ta.put("quantity", "அளவு");
        ta.put("unit", "அலகு");
        ta.put("production_cost", "உற்பத்தி செலவு");
        ta.put("transparent_pricing", "விலை விவரிப்பு");
        ta.put("profit_margin", "லாப விகிதம்");
        ta.put("total_products", "மொத்த பொருட்கள்");
        ta.put("total_orders", "மொத்த ஆர்டர்கள்");
        ta.put("total_revenue", "மொத்த வருவாய்");
        ta.put("customer_rating", "வாடிக்கையாளர் மதிப்பீடு");
        ta.put("login", "உள்நுழைய");
        ta.put("register", "பதிவு செய்ய");
        ta.put("username", "பயனர் பெயர்");
        ta.put("password", "கடவுச்சொல்");
        ta.put("full_name", "முழு பெயர்");
        ta.put("phone", "தொலைபேசி");
        ta.put("address", "முகவரி");
        ta.put("select_role", "பங்கை தேர்ந்தெடுக்கவும்");
        
        translations.put("en", en);
        translations.put("ta", ta);
    }
    
    public static String getTranslationsJSON(String language) {
        Map<String, String> langMap = translations.getOrDefault(language, translations.get("en"));
        StringBuilder json = new StringBuilder();
        json.append("{");
        
        int count = 0;
        for (Map.Entry<String, String> entry : langMap.entrySet()) {
            if (count++ > 0) json.append(",");
            json.append(String.format("\"%s\":\"%s\"", 
                entry.getKey(), 
                escapeJSON(entry.getValue())));
        }
        
        json.append("}");
        return json.toString();
    }
    
    private static String escapeJSON(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    public static String getTranslation(String language, String key) {
        Map<String, String> langMap = translations.get(language);
        if (langMap != null && langMap.containsKey(key)) {
            return langMap.get(key);
        }
        // Fallback to English
        Map<String, String> enMap = translations.get("en");
        return enMap.getOrDefault(key, key);
    }
}