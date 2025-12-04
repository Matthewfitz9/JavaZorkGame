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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class GameGUIJavaFX extends Application {

    private ZorkULGame game = new ZorkULGame();
    private Button b1, b2, b3, b4, b5, b6;
    private TextField input = new TextField();
    private TextArea output = new TextArea();
    private ComboBox<String> inventoryDrop = new ComboBox<>();
    private Parser parser = new Parser();
    private ImageView roomImage = new ImageView();
    private VBox rightSide;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        roomImage.setImage(new Image(getClass().getResource("/images/city.png").toExternalForm()));

        inventoryDrop.setEditable(true);
        inventoryDrop.setPromptText("Inventory");
        inventoryDrop.getEditor().setStyle("-fx-prompt-text-fill: white; -fx-text-fill: white; -fx-background-color:#1f2937;");
        inventoryDrop.setMaxWidth(Double.MAX_VALUE);

        output.setEditable(false);
        output.setStyle("-fx-control-inner-background:#111827; -fx-text-fill:#39FF14; -fx-font-size:14px;");
        output.setPrefHeight(200);
        VBox.setVgrow(output, Priority.ALWAYS);

        input.setPromptText("Enter command here...");
        input.setStyle("-fx-background-color:#1f2937; -fx-text-fill: white; -fx-prompt-text-fill: white;");
        input.setMaxWidth(Double.MAX_VALUE);
        input.setOnAction(e -> handleInput());  // Press ENTER to submit

        b1 = new Button("North");
        b2 = new Button("East");
        b3 = new Button("South");
        b4 = new Button("West");
        b5 = new Button("Save");
        b6 = new Button("Restart");

        b1.setOnAction(e -> {
            output.appendText(game.processCommandGUI("go north") + "\n");
            updateRoomImage();
        });

        b2.setOnAction(e -> {
            output.appendText(game.processCommandGUI("go east") + "\n");
            updateRoomImage();
        });
        b3.setOnAction(e -> {
            output.appendText(game.processCommandGUI("go south") + "\n");
            updateRoomImage();
        });
        b4.setOnAction(e -> {
            output.appendText(game.processCommandGUI("go west") + "\n");
            updateRoomImage();
        });
        b5.setOnAction(e -> output.appendText(game.processCommandGUI("save")));
        b6.setOnAction(e -> { output.appendText(game.processCommandGUI("restart") + "\n");
            restartButtons();
            updateInventoryDrop();
            updateRoomImage();
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

        b1.setMinWidth(90);
        b2.setMinWidth(90);
        b3.setMinWidth(90);
        b4.setMinWidth(90);
        b5.setMinWidth(90);
        b6.setMinWidth(90);


        HBox buttonRow = new HBox(10, b1, b2, b3, b4, b5, b6);
        HBox.setMargin(b5, new Insets(0, 0, 0, 100));
        buttonRow.setAlignment(Pos.CENTER);
        VBox.setVgrow(roomImage, Priority.ALWAYS);



        HBox mainLayout = new HBox(20);   // left and right sections
        VBox leftSide = new VBox(10, output, buttonRow, input, inventoryDrop);
        rightSide = new VBox();
        rightSide.setPadding(new Insets(10));
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setStyle("-fx-background-color: black;");
        rightSide.setMinWidth(500);
        rightSide.setMinHeight(400);
        HBox.setHgrow(rightSide, Priority.ALWAYS);
        VBox.setVgrow(rightSide, Priority.ALWAYS);
        VBox.setVgrow(roomImage, Priority.ALWAYS);


        roomImage.setPreserveRatio(true); // keeps correct proportions
        roomImage.setPreserveRatio(true);
        roomImage.fitWidthProperty().bind(rightSide.widthProperty());


        mainLayout.getChildren().addAll(leftSide, rightSide);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setStyle("-fx-background-color: #0a0f24;");


        Scene scene = new Scene(mainLayout, 1000, 500);
        stage.setTitle("Java Zork: Tokyo Drift");
        stage.setScene(scene);
        stage.show();

        output.appendText(game.printWelcome());
        updateInventoryDrop();
        updateRoomImage();

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
                    stop();
                }
            }
        };

        timer.start();

    }

    private void handleInput() {
        String command = input.getText().trim();

        if (!command.isEmpty()) {
            Command parsed = parser.parseCommand(command);
            String result = game.processCommand(parsed);     // ✓ Parser integrated
            output.appendText("> " + command + "\n" + result + "\n\n");
            updateInventoryDrop();
            updateRoomImage();
        }

        input.clear();
    }


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

            List<Pair<String, String>> qaPairs = quizRoom.getQAPairs();
            List<TextField> answerFields = new ArrayList<>();

            VBox questionBox = new VBox(5);
            for (int i = 0; i < qaPairs.size(); i++) {
                Pair<String, String> qa = qaPairs.get(i);
                Label qLabel = new Label((i + 1) + ". " + qa.getFirst());
                qLabel.setStyle("-fx-text-fill:#39FF14; -fx-font-size:14px;");
                TextField ans = new TextField();
                answerFields.add(ans);
                questionBox.getChildren().add(new VBox(qLabel, ans));
            }

            Button submit = new Button("Submit");
            Label resultLabel = new Label("");

            submit.setOnAction(e -> {
                int correct = 0;

                for (int i = 0; i < qaPairs.size(); i++) {

                    String typed = answerFields.get(i).getText().trim().toLowerCase();
                    String correctAns = qaPairs.get(i).getSecond(); // The answer

                    // Allow typed "semicolon" → ";"
                    if (correctAns.equals(typed) ||
                            (correctAns.equals(";") && typed.equals("semicolon"))) {

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

        if (game.getPlayer().getInventory().isEmpty()) {
            inventoryDrop.getItems().add("Inventory is currently empty");
            return;
        }

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

    private void updateRoomImage() {
        String desc = game.getPlayer().getCurrentRoom().getDescription().toLowerCase();
        String path = null;

        if (desc.contains("city centre")) path = "/images/city.png";
        else if (desc.contains("local pub")) path = "/images/pub.png";
        else if (desc.contains("toilet")) path = "/images/toilet.png";
        else if (desc.contains("computing admin office")) path = "/images/office.png";
        else if (desc.contains("quiz room")) path = "/images/quiz.png";
        else if (desc.contains("skyscraper")) path = "/images/skyscraper.png";

        if (path != null) {
            BackgroundImage bg = new BackgroundImage(
                    new Image(getClass().getResource(path).toExternalForm()),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(
                            BackgroundSize.AUTO, BackgroundSize.AUTO,
                            true, true, true, false)
            );

            rightSide.setBackground(new Background(bg));
        }
    }
}


