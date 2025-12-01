import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;

public class GameGUIJavaFX extends Application {

    private ZorkULGame game = new ZorkULGame();
    private Button b1, b2, b3, b4, b5, b6;
    private TextField input = new TextField();
    private TextArea output = new TextArea();
    private ComboBox<String> inventoryDrop = new ComboBox<>();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        inventoryDrop.setPromptText("Inventory");
        inventoryDrop.setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white; -fx-background-color:#1f2937;");
        inventoryDrop.setMaxWidth(Double.MAX_VALUE);

        // --- Output Area ---
        output.setEditable(false);
        output.setStyle("-fx-control-inner-background:#111827; -fx-text-fill:#39FF14; -fx-font-size:14px;");
        output.setPrefHeight(200);
        VBox.setVgrow(output, Priority.ALWAYS);

        // --- Input Text Box ---
        input.setPromptText("Enter command here...");
        input.setStyle("-fx-background-color:#1f2937; -fx-text-fill:white; -fx-prompt-text-fill:gray;");
        input.setMaxWidth(Double.MAX_VALUE);
        input.setOnAction(e -> handleInput());  // Press ENTER to submit

        // --- Direction Buttons ---
        b1 = new Button("North");
        b2 = new Button("East");
        b3 = new Button("South");
        b4 = new Button("West");
        b5 = new Button("Save");
        b6 = new Button("Restart");

        b1.setOnAction(e -> output.appendText(game.processCommandGUI("go north") + "\n"));
        b2.setOnAction(e -> output.appendText(game.processCommandGUI("go east") + "\n"));
        b3.setOnAction(e -> output.appendText(game.processCommandGUI("go south") + "\n"));
        b4.setOnAction(e -> output.appendText(game.processCommandGUI("go west") + "\n"));
        b5.setOnAction(e -> output.appendText(game.processCommandGUI("save")));
        b6.setOnAction(e -> { output.appendText(game.processCommandGUI("restart") + "\n");
            restartButtons();
    });

        String buttonStyle =
                "-fx-background-color: linear-gradient(#3b82f6, #1e3a8a);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-padding: 6px 12px;";

        b1.setStyle(buttonStyle);
        b2.setStyle(buttonStyle);
        b3.setStyle(buttonStyle);
        b4.setStyle(buttonStyle);
        b5.setStyle(buttonStyle);
        b6.setStyle(buttonStyle);


        HBox buttonRow = new HBox(10, b1, b2, b3, b4, b5, b6);
        HBox.setMargin(b5, new Insets(0, 0, 0, 600));
        buttonRow.setAlignment(Pos.CENTER);

        // --- Layout ---
        VBox root = new VBox(10, output, buttonRow, input, inventoryDrop);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #0a0f24;");


        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Java Zork: Tokyo Drift");
        stage.setScene(scene);
        stage.show();

        // Show starting room
        output.appendText(game.printWelcome());
        updateInventoryDrop();

        // --- TIMER FIX ---
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                String msg = game.getTimerMessage();
                if (msg != null && !msg.isEmpty()) {
                    output.appendText(msg + "\n");
                    game.clearTimerMessage();
                }

                if (game.isGameOver()) {
                    input.setEditable(false);
                    input.setDisable(true);
                    output.appendText("\n--- GAME OVER ---\n");
                    b1.setDisable(true);
                    b2.setDisable(true);
                    b3.setDisable(true);
                    b4.setDisable(true);
                    b5.setDisable(true);
                    stop();  // << stops the animation timer
                }
            }
        };

        timer.start();   // <<<<<< THIS MAKES THE TIMER WORK
    }

    // --- Handle Enter key input ---
    private void handleInput() {
        String command = input.getText().trim();

        if (!command.isEmpty()) {
            String result = game.processCommandGUI(command);
            output.appendText("> " + command + "\n" + result + "\n\n");
            updateInventoryDrop();
        }

        input.clear();  // Clear entry field
    }

    // --- QUIZ WINDOW ---
    public static class QuizWindow {

        public static String showQuiz(QuizRoom quizRoom, Character player) {

            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Quiz Room");

            VBox layout = new VBox(10);
            layout.setStyle("-fx-background-color:#0a0f24;");
            layout.setPadding(new Insets(10));

            Label weatherLabel = new Label("Weather: " + quizRoom.getWeather());
            Label info = new Label("Answer all 5 questions:");

            weatherLabel.setStyle("-fx-text-fill:#ff00ff; -fx-font-size:16px;");
            info.setStyle("-fx-text-fill:white;");

            // QUESTIONS
            List<String> questions = quizRoom.getQuestions();
            List<TextField> answerFields = new ArrayList<>();

            VBox questionBox = new VBox(5);
            for (int i = 0; i < questions.size(); i++) {
                Label qLabel = new Label((i + 1) + ". " + questions.get(i));
                qLabel.setStyle("-fx-text-fill:#39FF14; -fx-font-size:14px;");
                TextField ans = new TextField();
                answerFields.add(ans);
                questionBox.getChildren().add(new VBox(qLabel, ans));
            }

            Button submit = new Button("Submit");
            Label resultLabel = new Label("");

            submit.setOnAction(e -> {
                int correct = 0;
                List<String> correctAnswers = quizRoom.getAnswers();

                for (int i = 0; i < answerFields.size(); i++) {
                    String typed = answerFields.get(i).getText().trim().toLowerCase();
                    String correctAns = correctAnswers.get(i);

                    if (correctAns.equals(typed) || (i == 3 && typed.equals("semicolon"))) {
                        correct++;
                    }
                }

                boolean passed = quizRoom.checkQuiz(correct, player);

                if (passed) {
                    resultLabel.setText("You passed! (" + correct + "/5)\nYou received a certificate.");
                    resultLabel.setStyle("-fx-text-fill:#39FF14; -fx-font-size:14px;");
                } else {
                    resultLabel.setText("You failed (" + correct + "/5).\nTry again!");
                    resultLabel.setStyle("-fx-text-fill:#39FF14; -fx-font-size:14px;");

                }
            });

            layout.getChildren().addAll(weatherLabel, info, questionBox, submit, resultLabel);

            Scene scene = new Scene(layout, 450, 450);
            window.setScene(scene);
            window.showAndWait();

            return "Quiz finished!";
        }
    }

    private void updateInventoryDrop() {
        inventoryDrop.getItems().clear();
        for (Item item : game.getPlayer().getInventory()) {
            inventoryDrop.getItems().add(item.getName());
        }
    }

    public void restartButtons() {
        b1.setDisable(false);
        b2.setDisable(false);
        b3.setDisable(false);
        b4.setDisable(false);
        b5.setDisable(false);
        input.setDisable(false);
        input.setEditable(true);
    }


}
