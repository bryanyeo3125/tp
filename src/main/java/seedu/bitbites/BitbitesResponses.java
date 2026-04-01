//@@author rayminQAQ
/**
 * BitbitesResponses.java
 * <p>
 * This file stores all predefined response messages used throughout the application.
 * Centralizes message strings for consistency and easier maintenance.
 * <p>
 * Dependencies:
 * - None (contains only static string constants)
 */
package seedu.bitbites;

/**
 * BitbitesResponses provides centralized storage for all application messages.
 * Includes greetings, error messages, and feature confirmation messages.
 */
public class BitbitesResponses {
    // Greetings section
    public static String welcomeMessage = "Hello from\n"
            + "__________._____________________________.___.______________________\n"
            + "\\______   \\   \\__    ___/\\______   \\__  |   |\\__    ___/\\_   _____/\n"
            + " |    |  _/   | |    |    |    |  _//   |   |  |    |    |    __)_\n"
            + " |    |   \\   | |    |    |    |   \\\\____   |  |    |    |        \\\n"
            + " |______  /___| |____|    |______  // ______|  |____|   /_______  /\n"
            + "        \\/                       \\/ \\/                          \\/\n";
    // Errors & Exceptions Handled by the chatbot section
    public static String unknownCommand = "OOPS!!! I'm sorry, but I don't know what that means :-( ";
    public static String errorMessage = "OOPS!!! I'm sorry, but I don't know what that means :-( ";
    public static String deleteErrorMessage = "OOPS!!! Invalid index. Please provide a valid item number.";

    // Features section
    // Add Messages
    public static String addMessage = "Got it. I've added the food item!";
    public static String addFormatReminder = "Please use the correct format: " +
            "add n/NAME c/CALORIES_IN_KCAL p/PROTEIN_IN_G d/DD-MM-YYYY";
    // List Message
    public static String listMessage = "Here are all the food items in your list:";
    // List From Date Message
    public static String listFromDateMessage = "Here are the food items from ";
    // Delete Message
    public static String deleteMessage = "Got it. I've removed the food item!";
    // Edit Message
    public static String editMessage = "Got it. I've updated the food item!";
    public static String editFormatReminder = "Please use: edit INDEX [n/NAME] "
            + "[c/CALORIES] [p/PROTEIN] [d/DATE]";
    // Exit Message
    public static String exitMessage = "Bye. Hope to see you again soon!";
    // Help Message
    public static String helpMessage = "Here are the available commands:\n"
            + "  list                                    - List all food items\n"
            + "  list d/DATE                             - List food items for a specific date\n"
            + "  add n/NAME c/CALORIES p/PROTEIN d/DATE  - Add a food item\n"
            + "  delete INDEX                            - Delete a food item by index\n"
            + "  edit INDEX                              - Edit an existing food item\n"
            + "  (Optional: [n/NAME] [c/CALORIES] [p/PROTEIN] [d/DATE])\n"
            + "  preset add n/NAME c/CALORIES p/PROTEIN  - Save a new food preset\n"
            + "  preset list                             - View all saved presets\n"
            + "  preset delete INDEX                     - Delete a preset by index\n"
            + "  preset use INDEX [d/DATE]               - Log a preset (date defaults to today)\n"
            + "  goals                                   - View daily and weekly goal progress\n"
            + "  goals set dc/CAL dp/PROT wc/CAL wp/PROT - Set daily/weekly calorie and protein goals\n"
            + "  summary d/DATE                          - Show summary for a specific date\n"
            + "  summary from/DATE1 to/DATE2             - Show trend between two dates\n"
            + "  summary compare d/DATE1 d/DATE2         - Compare two days\n"
            + "  history                                 - Show all food history\n"
            + "  history /top N                          - Show top N highest calorie days\n"
            + "  history /best N                         - Show top N lowest calorie days\n"
            + "  history streak                          - Show tracking streak\n"
            + "  profile                                 - View your profile, BMI and BMR\n"
            + "  profile set n/NAME g/GENDER a/AGE w/WEIGHT h/HEIGHT - Set up your profile\n"
            + "  profile clear                           - Clear your profile\n"
            + "  tips                                    - Show tips for estimating calories and protein\n"
            + "  help                                    - Show help message\n"
            + "  exit                                    - Exit the application\n"
            + "  (Date format: DD-MM-YYYY)";
    //@@author
    //Tip Message
    public static String tipsMessage = "Here are some tips to estimate calories and protein:\n"
            + "\n"
            + "  CALORIES (kcal)\n"
            + "  ------------------------------------\n"
            + "  Rice (1 cup cooked)        ~200 kcal\n"
            + "  Noodles (1 cup cooked)     ~220 kcal\n"
            + "  Bread (1 slice)            ~80 kcal\n"
            + "  Egg (1 large)              ~70 kcal\n"
            + "  Chicken breast (100g)      ~165 kcal\n"
            + "  Chicken thigh (100g)       ~209 kcal\n"
            + "  Pork belly (100g)          ~518 kcal\n"
            + "  Salmon (100g)              ~208 kcal\n"
            + "  Tofu (100g)                ~76 kcal\n"
            + "  Milk (1 cup / 240ml)       ~150 kcal\n"
            + "  Banana (1 medium)          ~105 kcal\n"
            + "  Apple (1 medium)           ~95 kcal\n"
            + "  Potato (1 medium)          ~160 kcal\n"
            + "  Broccoli (100g)            ~35 kcal\n"
            + "\n"
            + "  PROTEIN (g)\n"
            + "  ---------------------------------------\n"
            + "  Chicken breast (100g)      ~31g protein\n"
            + "  Chicken thigh (100g)       ~26g protein\n"
            + "  Pork belly (100g)          ~9g protein\n"
            + "  Egg (1 large)              ~6g protein\n"
            + "  Salmon (100g)              ~20g protein\n"
            + "  Tuna (100g)                ~30g protein\n"
            + "  Tofu (100g)                ~8g protein\n"
            + "  Milk (1 cup / 240ml)       ~8g protein\n"
            + "  Lentils (100g cooked)      ~9g protein\n"
            + "\n"
            + "  COMMON SINGAPOREAN MEALS\n"
            + "  --------------------------------------------------\n"
            + "  Chicken rice (1 plate)     ~500 kcal, ~30g protein\n"
            + "  Char kway teow (1 plate)   ~740 kcal, ~20g protein\n"
            + "  Wonton noodles (1 bowl)    ~420 kcal, ~20g protein\n"
            + "  Nasi lemak (1 plate)       ~700 kcal, ~25g protein\n"
            + "  Roti prata (1 piece)       ~300 kcal, ~8g protein\n"
            + "  Ban mian (1 bowl)          ~450 kcal, ~25g protein\n"
            + "  Economy rice (1 plate)     ~600 kcal, ~20g protein\n"
            + "\n"
            + "  QUICK RULES OF THUMB\n"
            + "  ---------------------------------------------------------\n"
            + "  Palm-sized meat portion    ~150-200 kcal, ~25-30g protein\n"
            + "  Fist-sized carb portion    ~200-250 kcal\n"
            + "  Thumb-sized fat portion    ~100-120 kcal\n"
            + "  Daily protein target       ~0.8-1g per kg of body weight\n"
            + "  Daily calorie target       ~2000 kcal (general guideline)\n";
}
