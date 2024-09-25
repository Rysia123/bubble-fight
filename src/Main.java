import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
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
    Text score  = new Text("10");
    Rectangle generator  = new Rectangle(SIZE/2, SIZE -100, 50,50);

    List <Circle> list = new ArrayList<>();
    List<Circle> enemies = new ArrayList<>();
    int enemySpawnRate = 100;
    int level = 1;
    int cooldown = 10;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        generator.setFill(Color.PAPAYAWHIP);
        root.getChildren().addAll(generator,score);
        Scene scene = new Scene(root,SIZE,SIZE);
        scene.setFill(Color.PALETURQUOISE);
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

            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}