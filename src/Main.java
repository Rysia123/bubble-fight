import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    static final int SIZE = 800;
    static AnchorPane root = new AnchorPane();
    int points  = 10;

    Rectangle generator  = new Rectangle(SIZE/2, SIZE -100, 50,50);

    List <Circle> list = new ArrayList<>();
    List<Circle> enemies = new ArrayList<>();
    int enemySpawnRate = 100;
    int level = 1;
    int cooldown = 10;
    int score = 0;
    int enemyScore= 0;
    boolean direction = true;

    Text scoreText = new Text("0");
    Text enemyScoreText = new Text("0");
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        generator.setFill(Color.PAPAYAWHIP);
        scoreText.setFill(Color.VIOLET);
        scoreText.setY(SIZE-100);
        scoreText.setX(SIZE -100);
        scoreText.setOpacity(40);

        enemyScoreText.setFill(Color.DEEPSKYBLUE);
        enemyScoreText.setX(100);
        enemyScoreText.setY(100);
        enemyScoreText.setOpacity(40);


        root.getChildren().addAll(generator,scoreText,enemyScoreText);
        Scene scene = new Scene(root,SIZE,SIZE);
        scene.setFill(Color.PALETURQUOISE);

        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.LEFT){
                direction = true;
            }
            if(event.getCode() == KeyCode.RIGHT){
                direction = false;
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000/60),event -> {
            //spawn
            cooldown--;
            if(cooldown == 0){
                cooldown = 10;
                Circle bullet = new Circle(generator.getX() + generator.getWidth()/2, generator.getY(), 20, Color.LIGHTPINK);
                root.getChildren().add(bullet);
                list.add(bullet);
            }
            //projectile movement
            for (int i = 0; i < list.size(); i++) {
                Circle b = list.get(i);
                b.setCenterY(b.getCenterY() - 5);
                int deltaX = (int) (Math.random()*10 -5);
                b.setCenterX(b.getCenterX()+deltaX);
                if(b.getCenterY() < -b.getRadius()){
                    list.remove(b);
                    root.getChildren().remove(b);
                }
                for (int j = 0; j < enemies.size(); j++) {
                    Circle target = enemies.get(j);
                    if(b.intersects(target.getBoundsInLocal())){
                        list.remove(b);
                        root.getChildren().remove(b);
                        enemies.remove(target);
                        root.getChildren().remove(target);
                        score++;
                        scoreText.setText(String.valueOf(score));
                    }
                }
            }

            //moving generator
            if(direction && generator.getX()>0){
                generator.setX(generator.getX() -5);
            }
            else if(!direction && generator.getX() + 50 < SIZE){
                generator.setX(generator.getX() +5);
            }
            // spawning enemy
            enemySpawnRate--;
            if(enemySpawnRate == 0){
                enemySpawnRate = 100;
                for (int i = 0; i < level; i++) {
                    Circle enemy = new Circle(Math.random() * SIZE, 0, 20, Color.LAVENDERBLUSH);
                    enemies.add(enemy);
                    root.getChildren().add(enemy);
                }
                level++;
            }
            for (int i = 0; i < enemies.size(); i++) {
                Circle e = enemies.get(i);
                e.setCenterY(e.getCenterY() + 5);
                int deltaX = (int) (Math.random()*10 -5);
                e.setCenterX(e.getCenterX()+deltaX);
                if(e.getCenterY() >= SIZE){
                    enemies.remove(e);
                    root.getChildren().remove(e);
                    enemyScore++;
                    enemyScoreText.setText(String.valueOf(enemyScore));
                }

            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}