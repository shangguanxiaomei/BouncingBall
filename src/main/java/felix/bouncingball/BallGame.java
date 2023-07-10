package felix.bouncingball;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BallGame extends Application {
    private static final double BALL_RADIUS = 40;
    private static final double BALL_SPEED = 20;
    private static final int TOTAL_TIME = 300;

    private double ballX = BALL_RADIUS;
    private double ballY = BALL_RADIUS;
    private double ballSpeedX = BALL_SPEED;
    private double ballSpeedY = BALL_SPEED;

    private Label timerLabel;
    private Timeline timer, timer2;
    private int secondsElapsed;
    private int currentTime = 0;


    @Override
    public void start(Stage primaryStage) {
        // 创建小球
        Circle ball = new Circle(BALL_RADIUS, Color.CYAN);
        ball.relocate(ballX, ballY);

        Pane root = new Pane(ball);
        root.setStyle("-fx-background-color: black;"); // 设置背景色为黑色

        // 创建时间标签
        timerLabel = new Label();
        timerLabel.setTextFill(Color.WHITE);
        timerLabel.setStyle("-fx-font-size: 18px;");
        StackPane.setAlignment(timerLabel, Pos.TOP_RIGHT);
        StackPane.setMargin(timerLabel, new javafx.geometry.Insets(10));

        StackPane stackPane = new StackPane(root, timerLabel);
        Scene scene = new Scene(stackPane);
        primaryStage.setScene(scene);
//        primaryStage.initStyle(StageStyle.UNDECORATED);  // 隐藏窗口边框
        primaryStage.setFullScreen(true);  // 设置全屏模式

        // 获取屏幕尺寸
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // 更新小球位置
        AnimationTimer ballTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // 更新小球的位置
                ballX += ballSpeedX;
                ballY += ballSpeedY;

                // 检测碰撞边界
                if (ballX <= BALL_RADIUS || ballX >= screenWidth - BALL_RADIUS) {
                    ballSpeedX *= -1;
                }
                if (ballY <= BALL_RADIUS || ballY >= screenHeight - BALL_RADIUS) {
                    ballSpeedY *= -1;
                }

                // 更新小球位置
                ball.relocate(ballX, ballY);
            }
        };

        // 创建计时器
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsElapsed++;
            if (secondsElapsed == 3) {
                timerLabel.setText("");
                timer.stop();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);

        timer2 = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            currentTime++;
        }));
        timer2.setCycleCount(Timeline.INDEFINITE);
        timer2.play();

        // 监听空格键按下事件
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                secondsElapsed = 0;
                timerLabel.setText("TimeLeft: " + (TOTAL_TIME - currentTime) + "s");
                timer.playFromStart();
            }
        });

        ballTimer.start();
        primaryStage.show();

        // 在N秒后关闭程序
        Duration duration = Duration.seconds(TOTAL_TIME);
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> {
            primaryStage.close();
        }));
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}