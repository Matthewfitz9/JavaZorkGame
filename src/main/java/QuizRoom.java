

import java.util.ArrayList;
import java.util.List;

public class QuizRoom extends Room {

    private WeatherSystem weather;
    private boolean passed = false;
    private List<Pair<String, String>> qaPairs = new ArrayList<>();

    public QuizRoom(String description) {
        super(description);
        this.weather = new WeatherSystem();

        boolean foggy = weather.getCurrentWeather().equalsIgnoreCase("foggy");

        if (foggy) {
            qaPairs.add(new Pair<>("Wht keyword is used in Jva to create a new objct?", "new"));
            qaPairs.add(new Pair<>("In Jav, what is te name of the mehod tht sarts evry program?", "main"));
            qaPairs.add(new Pair<>("Wat keyword lets one clas use the methods and varbles of a parnt class?", "extends"));
            qaPairs.add(new Pair<>("What symol do you ue to end a statement in ava?", ";"));
            qaPairs.add(new Pair<>("Wha Jva type is use to sore whole numbers?", "int"));

        } else {
            qaPairs.add(new Pair<>("What keyword is used in Java to create a new object?", "new"));
            qaPairs.add(new Pair<>("In Java, what is the name of the method that starts every program?", "main"));
            qaPairs.add(new Pair<>("What keyword lets one class use the methods of a parent class?", "extends"));
            qaPairs.add(new Pair<>("What symbol do you use to end a statement in Java?", ";"));
            qaPairs.add(new Pair<>("What Java type is used to store whole numbers?", "int"));
        }
    }

    public String getWeather() {
        return weather.getCurrentWeather();
    }


    public List<Pair<String, String>> getQAPairs() {
        return qaPairs;
    }

    public boolean checkQuiz(int correct, Character player) {
        if (correct >= 3) {
            passed = true;
            player.addItem(new Item("Certificate", "Certificate from quiz room"));
            return true;
        }
        return false;
    }
}

