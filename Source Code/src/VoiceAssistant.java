import java.util.*;
import java.util.regex.*;

public class VoiceAssistant {
    
    public static String processVoiceCommand(String command, String language) {
        if (command == null || command.trim().isEmpty()) {
            return "{\"success\": false, \"message\": \"No command received\"}";
        }
        
        command = command.toLowerCase().trim();
        String response = "";
        
        try {
            switch (language) {
                case "en":
                    response = processEnglishCommand(command);
                    break;
                case "ta":
                    response = processTamilCommand(command);
                    break;
                case "hi":
                    response = processHindiCommand(command);
                    break;
                case "kn":
                    response = processKannadaCommand(command);
                    break;
                case "ml":
                    response = processMalayalamCommand(command);
                    break;
                default:
                    response = processEnglishCommand(command);
            }
            
            return "{\"success\": true, \"response\": \"" + escapeJSON(response) + "\"}";
            
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"Error processing voice command\"}";
        }
    }
    
    private static String processEnglishCommand(String command) {
        // Farmer-specific commands
        if (command.contains("show") && command.contains("product")) {
            return "Showing all your farm products.";
        }
        else if (command.contains("add") && command.contains("product")) {
            return "Opening product addition form. Please fill in the details.";
        }
        else if (command.contains("my") && command.contains("order")) {
            return "Showing your customer orders.";
        }
        else if (command.contains("dashboard") || command.contains("home")) {
            return "Navigating to your farmer dashboard.";
        }
        
        // Consumer-specific commands
        else if (command.contains("market") || command.contains("buy") || command.contains("shop")) {
            return "Taking you to the marketplace to browse fresh farm products.";
        }
        else if (command.contains("vegetables") || command.contains("fruits") || command.contains("dairy")) {
            return "Showing " + command + " in the marketplace.";
        }
        else if (command.contains("cart") || command.contains("purchase")) {
            return "Showing your shopping cart and purchase options.";
        }
        
        // General commands
        else if (command.contains("help")) {
            return "I can help you navigate. Farmers can say: show products, add product, my orders. Consumers can say: show vegetables, buy fruits, my cart.";
        }
        else if (command.contains("hello") || command.contains("hi")) {
            return "Hello! How can I assist you today?";
        }
        else if (command.contains("thank")) {
            return "You're welcome! Is there anything else I can help with?";
        }
        else {
            return "I'm not sure I understand. You can ask for help or try specific commands for your role.";
        }
    }
    
    private static String processTamilCommand(String command) {
        // Farmer-specific commands in Tamil
        if (command.contains("காட்டு") && command.contains("பொருள்")) {
            return "உங்கள் விவசாய பொருட்களை காட்டுகிறது.";
        }
        else if (command.contains("சேர்") && command.contains("பொருள்")) {
            return "பொருள் சேர்க்கும் படிவம் திறக்கிறது. விவரங்களை நிரப்பவும்.";
        }
        else if (command.contains("எனது") && command.contains("ஆர்டர்")) {
            return "உங்கள் வாடிக்கையாளர் ஆர்டர்களை காட்டுகிறது.";
        }
        else if (command.contains("டாஷ்போர்டு") || command.contains("முகப்பு")) {
            return "உங்கள் விவசாயி டாஷ்போர்டுக்கு செல்கிறது.";
        }
        
        // Consumer-specific commands in Tamil
        else if (command.contains("சந்தை") || command.contains("வாங்க") || command.contains("கடை")) {
            return "புதிய விவசாய பொருட்களை பார்க்க சந்தைக்கு அழைத்துச் செல்கிறது.";
        }
        else if (command.contains("காய்கறி") || command.contains("பழம்") || command.contains("பால்")) {
            return command + " சந்தையில் காட்டப்படுகிறது.";
        }
        else if (command.contains("கார்ட்") || command.contains("வாங்குதல்")) {
            return "உங்கள் ஷாப்பிங் கார்ட் மற்றும் வாங்கும் விருப்பங்களை காட்டுகிறது.";
        }
        
        // General commands in Tamil
        else if (command.contains("உதவி")) {
            return "விவசாயிகள் கூறலாம்: பொருட்களை காட்டு, பொருள் சேர், எனது ஆர்டர்கள். நுகர்வோர் கூறலாம்: காய்கறிகள் காட்டு, பழங்கள் வாங்கு, எனது கார்ட்.";
        }
        else if (command.contains("வணக்கம்") || command.contains("ஹலோ")) {
            return "வணக்கம்! இன்று நான் உங்களுக்கு எவ்வாறு உதவ முடியும்?";
        }
        else if (command.contains("நன்றி")) {
            return "வரவேற்கிறேன்! வேறு ஏதாவது உதவி தேவையா?";
        }
        else {
            return "எனக்கு புரியவில்லை. உதவி கேட்கலாம் அல்லது உங்கள் பங்கிற்கு குறிப்பிட்ட கட்டளைகளை முயற்சிக்கலாம்.";
        }
    }
    
    private static String processHindiCommand(String command) {
        // Farmer-specific commands in Hindi
        if ((command.contains("दिखाएं") || command.contains("दिखाओ")) && command.contains("उत्पाद")) {
            return "आपके सभी कृषि उत्पाद दिखा रहा हूं।";
        }
        else if (command.contains("जोड़ें") && command.contains("उत्पाद")) {
            return "उत्पाद जोड़ने का फॉर्म खोल रहा हूं। कृपया विवरण भरें।";
        }
        else if (command.contains("मेरे") && command.contains("आर्डर")) {
            return "आपके ग्राहक आर्डर दिखा रहा हूं।";
        }
        else if (command.contains("डैशबोर्ड") || command.contains("होम")) {
            return "आपके किसान डैशबोर्ड पर नेविगेट कर रहा हूं।";
        }
        
        // Consumer-specific commands in Hindi
        else if (command.contains("बाजार") || command.contains("खरीद") || command.contains("दुकान")) {
            return "आपको ताजे कृषि उत्पादों को ब्राउज़ करने के लिए बाजार में ले जा रहा हूं।";
        }
        else if (command.contains("सब्जी") || command.contains("फल") || command.contains("डेयरी")) {
            return "बाजार में " + command + " दिखा रहा हूं।";
        }
        else if (command.contains("कार्ट") || command.contains("खरीदारी")) {
            return "आपकी शॉपिंग कार्ट और खरीदारी विकल्प दिखा रहा हूं।";
        }
        
        // General commands in Hindi
        else if (command.contains("मदद")) {
            return "किसान कह सकते हैं: उत्पाद दिखाएं, उत्पाद जोड़ें, मेरे आर्डर। उपभोक्ता कह सकते हैं: सब्जियां दिखाएं, फल खरीदें, मेरी कार्ट।";
        }
        else if (command.contains("नमस्ते") || command.contains("हैलो")) {
            return "नमस्ते! आज मैं आपकी कैसे सहायता कर सकता हूं?";
        }
        else if (command.contains("धन्यवाद") || command.contains("शुक्रिया")) {
            return "आपका स्वागत है! क्या मैं और कुछ मदद कर सकता हूं?";
        }
        else {
            return "मुझे समझ नहीं आया। आप मदद मांग सकते हैं या अपनी भूमिका के लिए विशिष्ट आदेश आजमा सकते हैं।";
        }
    }
    
    private static String processKannadaCommand(String command) {
        // Farmer-specific commands in Kannada
        if (command.contains("ತೋರಿಸು") && command.contains("ಉತ್ಪನ್ನ")) {
            return "ನಿಮ್ಮ ಎಲ್ಲಾ ಕೃಷಿ ಉತ್ಪನ್ನಗಳನ್ನು ತೋರಿಸುತ್ತಿದೆ.";
        }
        else if (command.contains("ಸೇರಿಸು") && command.contains("ಉತ್ಪನ್ನ")) {
            return "ಉತ್ಪನ್ನ ಸೇರಿಸುವ ಫಾರ್ಮ್ ತೆರೆಯುತ್ತಿದೆ. ದಯವಿಟ್ಟು ವಿವರಗಳನ್ನು ನಮೂದಿಸಿ.";
        }
        else if (command.contains("ನನ್ನ") && command.contains("ಆರ್ಡರ್")) {
            return "ನಿಮ್ಮ ಗ್ರಾಹಕ ಆರ್ಡರ್ಗಳನ್ನು ತೋರಿಸುತ್ತಿದೆ.";
        }
        else if (command.contains("ಡ್ಯಾಶ್ಬೋರ್ಡ್") || command.contains("ಮುಖಪುಟ")) {
            return "ನಿಮ್ಮ ರೈತ ಡ್ಯಾಶ್ಬೋರ್ಡ್ಗೆ ನ್ಯಾವಿಗೇಟ್ ಮಾಡುತ್ತಿದೆ.";
        }
        
        // Consumer-specific commands in Kannada
        else if (command.contains("ಮಾರುಕಟ್ಟೆ") || command.contains("ಖರೀದಿ") || command.contains("ಅಂಗಡಿ")) {
            return "ತಾಜಾ ಕೃಷಿ ಉತ್ಪನ್ನಗಳನ್ನು ಬ್ರೌಸ್ ಮಾಡಲು ನಿಮ್ಮನ್ನು ಮಾರುಕಟ್ಟೆಗೆ ಕರೆದುಕೊಂಡು ಹೋಗುತ್ತಿದೆ.";
        }
        else if (command.contains("ತರಕಾರಿ") || command.contains("ಹಣ್ಣು") || command.contains("ಹಾಲು")) {
            return "ಮಾರುಕಟ್ಟೆಯಲ್ಲಿ " + command + " ತೋರಿಸುತ್ತಿದೆ.";
        }
        else if (command.contains("ಕಾರ್ಟ್") || command.contains("ಖರೀದಿಸು")) {
            return "ನಿಮ್ಮ ಶಾಪಿಂಗ್ ಕಾರ್ಟ್ ಮತ್ತು ಖರೀದಿ ಆಯ್ಕೆಗಳನ್ನು ತೋರಿಸುತ್ತಿದೆ.";
        }
        
        // General commands in Kannada
        else if (command.contains("ಸಹಾಯ")) {
            return "ರೈತರು ಹೇಳಬಹುದು: ಉತ್ಪನ್ನಗಳನ್ನು ತೋರಿಸು, ಉತ್ಪನ್ನ ಸೇರಿಸು, ನನ್ನ ಆರ್ಡರ್ಗಳು. ಗ್ರಾಹಕರು ಹೇಳಬಹುದು: ತರಕಾರಿಗಳನ್ನು ತೋರಿಸು, ಹಣ್ಣುಗಳನ್ನು ಖರೀದಿಸು, ನನ್ನ ಕಾರ್ಟ್.";
        }
        else if (command.contains("ನಮಸ್ತೆ") || command.contains("ಹಲೋ")) {
            return "ನಮಸ್ತೆ! ಇಂದು ನಾನು ನಿಮಗೆ ಹೇಗೆ ಸಹಾಯ ಮಾಡಬಹುದು?";
        }
        else if (command.contains("ಧನ್ಯವಾದ")) {
            return "ಸ್ವಾಗತ! ನಾನು ಇನ್ನೇನಾದರೂ ಸಹಾಯ ಮಾಡಬಹುದೇ?";
        }
        else {
            return "ನನಗೆ ಅರ್ಥವಾಗುತ್ತಿಲ್ಲ. ನೀವು ಸಹಾಯ ಕೇಳಬಹುದು ಅಥವಾ ನಿಮ್ಮ ಪಾತ್ರಕ್ಕೆ ನಿರ್ದಿಷ್ಟ ಆದೇಶಗಳನ್ನು ಪ್ರಯತ್ನಿಸಬಹುದು.";
        }
    }
    
    private static String processMalayalamCommand(String command) {
        // Farmer-specific commands in Malayalam
        if (command.contains("കാണിക്കുക") && command.contains("ഉൽപ്പന്നം")) {
            return "നിങ്ങളുടെ എല്ലാ കൃഷി ഉൽപ്പന്നങ്ങളും കാണിക്കുന്നു.";
        }
        else if (command.contains("ചേർക്കുക") && command.contains("ഉൽപ്പന്നം")) {
            return "ഉൽപ്പന്നം ചേർക്കുന്ന ഫോം തുറക്കുന്നു. വിശദാംശങ്ങൾ പൂരിപ്പിക്കുക.";
        }
        else if (command.contains("എന്റെ") && command.contains("ഓർഡർ")) {
            return "നിങ്ങളുടെ ഉപഭോക്തൃ ഓർഡറുകൾ കാണിക്കുന്നു.";
        }
        else if (command.contains("ഡാഷ്ബോർഡ്") || command.contains("ഹോം")) {
            return "നിങ്ങളുടെ കർഷക ഡാഷ്ബോർഡിലേക്ക് നാവിഗേറ്റ് ചെയ്യുന്നു.";
        }
        
        // Consumer-specific commands in Malayalam
        else if (command.contains("മാർക്കറ്റ്") || command.contains("വാങ്ങുക") || command.contains("ഷോപ്പ്")) {
            return "പുതിയ കൃഷി ഉൽപ്പന്നങ്ങൾ ബ്രൗസ് ചെയ്യാൻ നിങ്ങളെ മാർക്കറ്റിലേക്ക് കൊണ്ടുപോകുന്നു.";
        }
        else if (command.contains("പച്ചക്കറി") || command.contains("പഴം") || command.contains("പാൽ")) {
            return "മാർക്കറ്റിൽ " + command + " കാണിക്കുന്നു.";
        }
        else if (command.contains("കാർട്ട്") || command.contains("വാങ്ങൽ")) {
            return "നിങ്ങളുടെ ഷോപ്പിംഗ് കാർട്ടും വാങ്ങൽ ഓപ്ഷനുകളും കാണിക്കുന്നു.";
        }
        
        // General commands in Malayalam
        else if (command.contains("സഹായം")) {
            return "കർഷകർക്ക് പറയാം: ഉൽപ്പന്നങ്ങൾ കാണിക്കുക, ഉൽപ്പന്നം ചേർക്കുക, എന്റെ ഓർഡറുകൾ. ഉപഭോക്താക്കൾക്ക് പറയാം: പച്ചക്കറികൾ കാണിക്കുക, പഴങ്ങൾ വാങ്ങുക, എന്റെ കാർട്ട്.";
        }
        else if (command.contains("നമസ്കാരം") || command.contains("ഹലോ")) {
            return "നമസ്കാരം! ഇന്ന് ഞാൻ നിങ്ങളെ എങ്ങനെ സഹായിക്കാം?";
        }
        else if (command.contains("നന്ദി")) {
            return "സ്വാഗതം! എനിക്ക് വേറെ എന്തെങ്കിലും സഹായിക്കാനാകുമോ?";
        }
        else {
            return "എനിക്ക് മനസ്സിലാകുന്നില്ല. നിങ്ങൾക്ക് സഹായം ആവശ്യപ്പെടാം അല്ലെങ്കിൽ നിങ്ങളുടെ റോളിനായി നിർദ്ദിഷ്ട കമാൻഡുകൾ പരീക്ഷിക്കാം.";
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