import java.util.*;

public class LanguageManager {
    private static Map<String, Map<String, String>> translations = new HashMap<>();
    
    public static void initialize() {
        loadTranslations();
    }
    
    private static void loadTranslations() {
        // English translations
        Map<String, String> en = new HashMap<>();
        
        // Common translations
        en.put("welcome", "Welcome");
        en.put("logout", "Logout");
        en.put("dashboard", "Dashboard");
        en.put("profile", "Profile");
        en.put("cancel", "Cancel");
        en.put("save", "Save");
        en.put("loading", "Loading...");
        en.put("error", "Error");
        en.put("success", "Success");
        
        // Role-based translations
        en.put("farmer", "Farmer");
        en.put("consumer", "Consumer");
        en.put("farmer_dashboard", "Farmer Dashboard");
        en.put("consumer_dashboard", "Consumer Dashboard");
        en.put("manage_business", "Manage your farm business and track performance");
        en.put("shop_fresh", "Shop fresh farm products directly from farmers");
        en.put("farm_to_table", "Farm to Table, Direct to You");
        
        // Products
        en.put("my_products", "My Products");
        en.put("manage_products", "Manage your farm products");
        en.put("add_product", "Add Product");
        en.put("add_product_desc", "List your farm products with transparent pricing");
        en.put("product_name", "Product Name");
        en.put("product_name_placeholder", "Enter product name");
        en.put("category", "Category");
        en.put("select_category", "Select Category");
        en.put("description", "Description");
        en.put("description_placeholder", "Enter product description");
        en.put("price", "Price");
        en.put("price_placeholder", "Enter price per unit");
        en.put("quantity", "Quantity");
        en.put("quantity_placeholder", "Enter available quantity");
        en.put("unit", "Unit");
        en.put("unit_placeholder", "kg, lb, piece, etc.");
        en.put("production_cost", "Production Cost");
        en.put("production_cost_placeholder", "Cost to produce per unit");
        en.put("selling_price", "Selling Price");
        en.put("profit_margin", "Profit Margin");
        en.put("transparent_pricing", "Transparent Pricing");
        
        // Categories
        en.put("vegetables", "Vegetables");
        en.put("fruits", "Fruits");
        en.put("grains", "Grains");
        en.put("dairy", "Dairy");
        en.put("poultry", "Poultry");
        en.put("other", "Other");
        
        // Orders
        en.put("my_orders", "My Orders");
        en.put("customer_orders", "Customer Orders");
        en.put("order_history", "Order History");
        en.put("manage_orders", "Manage and fulfill customer orders");
        en.put("place_order", "Place Order");
        en.put("order_details", "Order Details");
        en.put("order_date", "Order Date");
        en.put("total_amount", "Total Amount");
        en.put("status", "Status");
        en.put("pending", "Pending");
        en.put("confirmed", "Confirmed");
        en.put("shipped", "Shipped");
        en.put("delivered", "Delivered");
        en.put("cancelled", "Cancelled");
        
        // Statistics
        en.put("market_statistics", "Market Statistics");
        en.put("total_products", "Total Products");
        en.put("total_farmers", "Total Farmers");
        en.put("total_consumers", "Total Consumers");
        en.put("total_orders", "Total Orders");
        en.put("total_revenue", "Total Revenue");
        en.put("total_spent", "Total Spent");
        en.put("total_savings", "Total Savings");
        en.put("revenue", "Revenue");
        en.put("profit", "Profit");
        en.put("sales", "Sales");
        en.put("performance", "Performance");
        en.put("this_month", "This Month");
        en.put("last_month", "Last Month");
        en.put("customer_rating", "Customer Rating");
        
        // Voice Assistant
        en.put("voice_assistant", "Voice Assistant");
        en.put("click_to_speak", "Click to Speak");
        en.put("speaking", "Speaking...");
        en.put("processing", "Processing...");
        en.put("voice_help", "Voice Help");
        en.put("voice_commands", "Voice Commands");
        en.put("farmer_commands", "Farmer Commands: show products, add product, my orders");
        en.put("consumer_commands", "Consumer Commands: show vegetables, buy fruits, my cart");
        
        // Market
        en.put("marketplace", "Marketplace");
        en.put("browse_products", "Browse Products");
        en.put("fresh_from_farm", "Fresh from Farm");
        en.put("direct_from_farmer", "Direct from Farmer");
        en.put("buy_now", "Buy Now");
        en.put("add_to_cart", "Add to Cart");
        en.put("available_quantity", "Available Quantity");
        en.put("farmer_info", "Farmer Information");
        en.put("contact_farmer", "Contact Farmer");
        
        // Authentication
        en.put("login", "Login");
        en.put("register", "Register");
        en.put("username", "Username");
        en.put("password", "Password");
        en.put("full_name", "Full Name");
        en.put("phone", "Phone");
        en.put("address", "Address");
        en.put("user_type", "User Type");
        en.put("select_user_type", "Select User Type");
        en.put("login_success", "Login Successful");
        en.put("register_success", "Registration Successful");
        en.put("no_account", "Don't have an account?");
        en.put("have_account", "Already have an account?");
        
        // Additional UI elements
        en.put("recent_orders", "Recent Orders");
        en.put("latest_orders", "Latest orders from customers");
        en.put("featured_products", "Featured Products");
        en.put("fresh_today", "Fresh from farms today");
        en.put("all_categories", "All Categories");
        en.put("apply_filters", "Apply Filters");
        en.put("update_profile", "Update Profile");
        en.put("manage_profile", "Manage your account information");
        
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
        ta.put("farm_to_table", "பண்ணையில் இருந்து மேசைக்கு, நேரடியாக உங்களுக்கு");
        ta.put("my_products", "எனது பொருட்கள்");
        ta.put("add_product", "பொருள் சேர்க்க");
        ta.put("product_name", "பொருள் பெயர்");
        ta.put("category", "வகை");
        ta.put("description", "விளக்கம்");
        ta.put("price", "விலை");
        ta.put("quantity", "அளவு");
        ta.put("unit", "அலகு");
        ta.put("production_cost", "உற்பத்தி செலவு");
        ta.put("selling_price", "விற்பனை விலை");
        ta.put("profit_margin", "லாப விகிதம்");
        ta.put("transparent_pricing", "விலை விவரிப்பு");
        ta.put("voice_assistant", "குரல் உதவியாளர்");
        ta.put("click_to_speak", "பேச கிளிக் செய்யவும்");
        ta.put("marketplace", "சந்தை");
        ta.put("login", "உள்நுழைய");
        ta.put("register", "பதிவு செய்ய");
        ta.put("username", "பயனர் பெயர்");
        ta.put("password", "கடவுச்சொல்");
        ta.put("full_name", "முழு பெயர்");
        ta.put("phone", "தொலைபேசி");
        ta.put("address", "முகவரி");
        
        // Hindi translations
        Map<String, String> hi = new HashMap<>();
        hi.put("welcome", "स्वागत है");
        hi.put("logout", "लॉग आउट");
        hi.put("dashboard", "डैशबोर्ड");
        hi.put("farmer", "किसान");
        hi.put("consumer", "उपभोक्ता");
        hi.put("farmer_dashboard", "किसान डैशबोर्ड");
        hi.put("consumer_dashboard", "उपभोक्ता डैशबोर्ड");
        hi.put("manage_business", "अपने कृषि व्यवसाय का प्रबंधन करें और प्रदर्शन ट्रैक करें");
        hi.put("shop_fresh", "किसानों से सीधे ताजे कृषि उत्पाद खरीदें");
        hi.put("farm_to_table", "फार्म से टेबल तक, सीधे आपके लिए");
        hi.put("my_products", "मेरे उत्पाद");
        hi.put("add_product", "उत्पाद जोड़ें");
        hi.put("product_name", "उत्पाद का नाम");
        hi.put("category", "श्रेणी");
        hi.put("description", "विवरण");
        hi.put("price", "मूल्य");
        hi.put("quantity", "मात्रा");
        hi.put("unit", "इकाई");
        hi.put("production_cost", "उत्पादन लागत");
        hi.put("selling_price", "विक्रय मूल्य");
        hi.put("profit_margin", "लाभ मार्जिन");
        hi.put("transparent_pricing", "पारदर्शी मूल्य निर्धारण");
        hi.put("voice_assistant", "वॉइस असिस्टेंट");
        hi.put("click_to_speak", "बोलने के लिए क्लिक करें");
        hi.put("marketplace", "बाजार");
        hi.put("login", "लॉगिन");
        hi.put("register", "रजिस्टर करें");
        hi.put("username", "उपयोगकर्ता नाम");
        hi.put("password", "पासवर्ड");
        hi.put("full_name", "पूरा नाम");
        hi.put("phone", "फोन");
        hi.put("address", "पता");
        
        // Kannada translations
        Map<String, String> kn = new HashMap<>();
        kn.put("welcome", "ಸ್ವಾಗತ");
        kn.put("logout", "ಲಾಗ್ ಔಟ್");
        kn.put("dashboard", "ಡ್ಯಾಶ್ಬೋರ್ಡ್");
        kn.put("farmer", "ರೈತ");
        kn.put("consumer", "ಗ್ರಾಹಕ");
        kn.put("farmer_dashboard", "ರೈತ ಡ್ಯಾಶ್ಬೋರ್ಡ್");
        kn.put("consumer_dashboard", "ಗ್ರಾಹಕ ಡ್ಯಾಶ್ಬೋರ್ಡ್");
        kn.put("manage_business", "ನಿಮ್ಮ ಕೃಷಿ ವ್ಯವಹಾರವನ್ನು ನಿರ್ವಹಿಸಿ ಮತ್ತು ಕಾರ್ಯಕ್ಷಮತೆಯನ್ನು ಟ್ರ್ಯಾಕ್ ಮಾಡಿ");
        kn.put("shop_fresh", "ರೈತರಿಂದ ನೇರವಾಗಿ ತಾಜಾ ಕೃಷಿ ಉತ್ಪನ್ನಗಳನ್ನು ಖರೀದಿಸಿ");
        kn.put("farm_to_table", "ಫಾರ್ಮ್ನಿಂದ ಟೇಬಲ್ಗೆ, ನೇರವಾಗಿ ನಿಮಗೆ");
        kn.put("my_products", "ನನ್ನ ಉತ್ಪನ್ನಗಳು");
        kn.put("add_product", "ಉತ್ಪನ್ನ ಸೇರಿಸಿ");
        kn.put("product_name", "ಉತ್ಪನ್ನದ ಹೆಸರು");
        kn.put("category", "ವರ್ಗ");
        kn.put("description", "ವಿವರಣೆ");
        kn.put("price", "ಬೆಲೆ");
        kn.put("quantity", "ಪ್ರಮಾಣ");
        kn.put("unit", "ಘಟಕ");
        kn.put("production_cost", "ಉತ್ಪಾದನಾ ವೆಚ್ಚ");
        kn.put("selling_price", "ಮಾರಾಟ ಬೆಲೆ");
        kn.put("profit_margin", "ಲಾಭ ಮಾರ್ಜಿನ್");
        kn.put("transparent_pricing", "ಪಾರದರ್ಶಕ ಬೆಲೆ ನಿಗದಿ");
        kn.put("voice_assistant", "ವಾಯ್ಸ್ ಅಸಿಸ್ಟೆಂಟ್");
        kn.put("click_to_speak", "ಮಾತನಾಡಲು ಕ್ಲಿಕ್ ಮಾಡಿ");
        kn.put("marketplace", "ಮಾರುಕಟ್ಟೆ");
        kn.put("login", "ಲಾಗಿನ್");
        kn.put("register", "ನೋಂದಣಿ");
        kn.put("username", "ಬಳಕೆದಾರ ಹೆಸರು");
        kn.put("password", "ಗುಪ್ತಪದ");
        kn.put("full_name", "ಪೂರ್ಣ ಹೆಸರು");
        kn.put("phone", "ಫೋನ್");
        kn.put("address", "ವಿಳಾಸ");
        
        // Malayalam translations
        Map<String, String> ml = new HashMap<>();
        ml.put("welcome", "സ്വാഗതം");
        ml.put("logout", "ലോഗൗട്ട്");
        ml.put("dashboard", "ഡാഷ്ബോർഡ്");
        ml.put("farmer", "കർഷകൻ");
        ml.put("consumer", "ഉപഭോക്താവ്");
        ml.put("farmer_dashboard", "കർഷക ഡാഷ്ബോർഡ്");
        ml.put("consumer_dashboard", "ഉപഭോക്തൃ ഡാഷ്ബോർഡ്");
        ml.put("manage_business", "നിങ്ങളുടെ കൃഷി ബിസിനസ്സ് മാനേജ് ചെയ്ത് പ്രകടനം ട്രാക്ക് ചെയ്യുക");
        ml.put("shop_fresh", "കർഷകരിൽ നിന്ന് നേരിട്ട് പുതിയ കൃഷി ഉൽപ്പന്നങ്ങൾ വാങ്ങുക");
        ml.put("farm_to_table", "ഫാമിൽ നിന്ന് ടേബിളിലേക്ക്, നേരിട്ട് നിങ്ങൾക്ക്");
        ml.put("my_products", "എന്റെ ഉൽപ്പന്നങ്ങൾ");
        ml.put("add_product", "ഉൽപ്പന്നം ചേർക്കുക");
        ml.put("product_name", "ഉൽപ്പന്നത്തിന്റെ പേര്");
        ml.put("category", "വിഭാഗം");
        ml.put("description", "വിവരണം");
        ml.put("price", "വില");
        ml.put("quantity", "അളവ്");
        ml.put("unit", "യൂണിറ്റ്");
        ml.put("production_cost", "ഉത്പാദന ചെലവ്");
        ml.put("selling_price", "വിൽപ്പന വില");
        ml.put("profit_margin", "ലാഭ മാർജിൻ");
        ml.put("transparent_pricing", "വ്യക്തമായ വിലനിർണ്ണയം");
        ml.put("voice_assistant", "വോയ്സ് അസിസ്റ്റന്റ്");
        ml.put("click_to_speak", "സംസാരിക്കാൻ ക്ലിക്ക് ചെയ്യുക");
        ml.put("marketplace", "മാർക്കറ്റ്പ്ലേസ്");
        ml.put("login", "ലോഗിൻ");
        ml.put("register", "രജിസ്റ്റർ");
        ml.put("username", "ഉപയോക്തൃനാമം");
        ml.put("password", "പാസ്വേഡ്");
        ml.put("full_name", "പൂർണ്ണ നാമം");
        ml.put("phone", "ഫോൺ");
        ml.put("address", "വിലാസം");
        
        translations.put("en", en);
        translations.put("ta", ta);
        translations.put("hi", hi);
        translations.put("kn", kn);
        translations.put("ml", ml);
    }
    
    public static Map<String, String> getLanguageMap(String language) {
        return translations.getOrDefault(language, translations.get("en"));
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

    public static String getTranslationsJSON(String language) {
        Map<String, String> langMap = getLanguageMap(language);
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
}