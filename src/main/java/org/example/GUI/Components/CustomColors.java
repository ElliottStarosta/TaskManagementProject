package org.example.GUI.Components;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CustomColors {
    // Predefined colors
    public static final Color RED = Color.RED;
    public static final Color GREEN = Color.GREEN;
    public static final Color ORANGE = Color.ORANGE;
    public static final Color BLUE = Color.BLUE;
    public static final Color PINK = Color.PINK;
    public static final Color LIME = new Color(0, 255, 0);
    public static final Color CYAN = Color.CYAN;
    public static final Color GOLD = new Color(255, 215, 0);

    // Map to store custom colors
    private static Map<String, Color> customColors = new HashMap<>();

    // Method to combine colors by their names and store in the map
    public static Color combineColors(String name, String colorName1, String colorName2) {
        // Normalize color names to lowercase for comparison
        colorName1 = colorName1.trim().toLowerCase();
        colorName2 = colorName2.trim().toLowerCase();

        // Retrieve the color objects based on names
        Color color1 = getColor(colorName1);
        Color color2 = getColor(colorName2);

        if (color1 == null || color2 == null) {
            throw new IllegalArgumentException("One or both color names are invalid.");
        }

        // Combine the colors
        int red = (color1.getRed() + color2.getRed()) / 2;
        int green = (color1.getGreen() + color2.getGreen()) / 2;
        int blue = (color1.getBlue() + color2.getBlue()) / 2;

        Color combinedColor = new Color(red, green, blue);

        // Store the combined color in the custom map
        customColors.put(name.toLowerCase(), combinedColor);

        return combinedColor;
    }

    public static Color getColor(String identifier) {
        identifier = identifier.trim().toLowerCase();

        // Check for predefined color by name
        try {
            Field field = CustomColors.class.getField(identifier.toUpperCase());
            return (Color) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Handle exception if the color name is not found
        }

        // Check if the identifier is in RGB format
        try {
            String[] rgb = identifier.split(",");
            if (rgb.length == 3) {
                int red = Integer.parseInt(rgb[0].trim());
                int green = Integer.parseInt(rgb[1].trim());
                int blue = Integer.parseInt(rgb[2].trim());
                return new Color(red, green, blue);
            }
        } catch (NumberFormatException ignored) {
            // Invalid RGB input
        }

        return null; // Not found
    }

    public static String convertColorString(String colorString) {
        // Define the regex pattern to match the color format and extract RGB values
        Pattern pattern = Pattern.compile("java\\.awt\\.Color\\[r=(\\d+),g=(\\d+),b=(\\d+)\\]");
        Matcher matcher = pattern.matcher(colorString.trim());

        if (matcher.matches()) {
            // Extract the RGB values
            String red = matcher.group(1);
            String green = matcher.group(2);
            String blue = matcher.group(3);
            return red + "," + green + "," + blue; // Return as a comma-separated string
        }

        return null; // Return null if the format does not match
    }

}
