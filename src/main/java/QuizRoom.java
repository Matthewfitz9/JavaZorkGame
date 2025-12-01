

import java.util.ArrayList;
import java.util.List;

public class QuizRoom extends Room {

    private WeatherSystem weather;
    private boolean passed = false;
    private int timeLeft = 90;

    public QuizRoom(String description) {
        super(description);
        this.weather = new WeatherSystem();
    }

    public boolean hasPassedQuiz() {
        return passed;
    }

    public String getWeather() {
        return weather.getCurrentWeather();
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    // ---------------------------
    // RETURN QUESTIONS AS STRINGS
    // ---------------------------
    public List<String> getQuestions() {

        List<String> q = new ArrayList<>();
        boolean foggy = weather.getCurrentWeather().equalsIgnoreCase("foggy");

        if (foggy) {
            q.add("Wht keyword is used in Jva to create a new objct?");
            q.add("In Jav, what is te name of the mehod tht sarts evry program?");
            q.add("Wat keyword lets one clas use the methods and varbles of a parnt class?");
            q.add("What symol do you ue to end a statement in ava?");
            q.add("Wha Jva type is use to sore whole numbers?");
        } else {
            q.add("What keyword is used in Java to create a new object?");
            q.add("In Java, what is the name of the method that starts every program?");
            q.add("What keyword lets one class use the methods of a parent class?");
            q.add("What symbol do you use to end a statement in Java?");
            q.add("What Java type is used to store whole numbers?");
        }

        return q;
    }

    // ------------------------------
    // THE CORRECT ANSWERS (IN ORDER)
    // ------------------------------
    public List<String> getAnswers() {
        List<String> a = new ArrayList<>();
        a.add("new");
        a.add("main");
        a.add("extends");
        a.add(";");    // semicolon also allowed
        a.add("int");
        return a;
    }

    // ----------------------------------------
    // GUI sends number of correct answers here
    // ----------------------------------------
    public boolean checkQuiz(int correct, Character player) {
        if (correct >= 3) {
            passed = true;
            player.addItem(new Item("Certificate", "Certificate from quiz room"));
            return true;
        }
        return false;
    }
}

