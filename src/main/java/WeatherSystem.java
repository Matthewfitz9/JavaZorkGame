import java.util.Random;

public class WeatherSystem {
    private String currentWeather;
    private String[] possibleWeathers = {"sunny", "foggy"};

    public WeatherSystem() {
        generateWeather();
    }

    public void generateWeather() {
        Random random = new Random();
        int index = random.nextInt(possibleWeathers.length);
        currentWeather = possibleWeathers[index];
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public String getWeatherDescription() {
        switch (currentWeather) {
            case "sunny":
                return "The sun is shining brightly above.";
            case "foggy":
                return "A dense fog makes it hard to see far ahead.";
            default:
                return "The weather is calm and unremarkable.";
        }
    }
}
