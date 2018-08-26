import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class main extends PApplet {

float x = 600;
float y = -200;
float gravity = 0;
float yVel = 0;
float xVel = 0;
float jump = -30;

float gameOverArrowX = 0;
float gameOverArrowY = 0;
float arrowYIncr = 0;
int i = 0;
int currentScore = 0;
boolean gameOver = false;
int gameOverChoice = 1;

Bird birdy;
Menu menu;
Leaderboard leaderboard;

float totalPillars = 50;
Pillar[] pillars = new Pillar[50];
float[] pillarSpeeds = {10, 7, 9, 11, 13};
float pillarSpeed = pillarSpeeds[0];
boolean pillarsAllowedToMove = false;
float pillarSpawnX = 1200;

float menuOptionCount = 1;
boolean menuTF = true;
boolean leaderboardTF = false;

Table Highscores;
boolean updateHS = false;
String name = "";
int rowID = 11;

public void setup() {
  
  imageMode(CENTER);
  frameRate(40);

  for (int i=0; i<totalPillars; i++) {
    pillars[i] = new Pillar(pillarSpawnX);
  }

  Highscores = loadTable("data/Highscores.csv", "header");
}

public void draw() {
  background(65, 169, 244);

  fill(222, 216, 149);
  rect(0, height-100, width, 100);//ground

  menu = new Menu(menuTF);
  menu.moveArrow(menuTF, arrowYIncr, menuOptionCount);
  menu.displayArrow(menuTF, menu.arrowX, menu.arrowY);

  leaderboard = new Leaderboard(leaderboardTF, Highscores);
  leaderboard.displayScore(currentScore);

  if (menuTF == false ) {

    pillars[i].drawPillar();
    pillars[i].move(pillarsAllowedToMove, pillarSpeed);

    if (pillars[i].getX() <= -105) {
      i++;
    }
    if (pillars[i].getX() == x && !gameOver)
      currentScore++;
  }

  x += xVel;
  yVel += gravity;
  y += yVel;

  birdy = new Bird(x, y);
  birdy.displayBird();

  if (gameOver == false) {
    if (y >= (height-110) || collision(birdy, pillars[i])) {
      gravity = 0;
      yVel = 0;
      y--;
      gameOver = true;
      pillarsAllowedToMove = false;
      gameOverArrowX = -165;
      gameOverArrowY = 40;
    }
  }
  menu.displayArrow(gameOver, gameOverArrowX, gameOverArrowY);
  menu.displayGameOver(gameOver);
  menu.displayHighScoreScreen(updateHS);
  menu.moveArrow(gameOver, arrowYIncr, gameOverChoice);
}

public boolean collision(Bird bird, Pillar pillar) {

  boolean output = false;

  if ( topPartCollision(bird, pillar) || bottomPartCollision(bird, pillar))
    output = true;

  return output;
}
public boolean topPartCollision(Bird bird, Pillar pillar) {
  return bird.xPosition+20 >= pillar.x && bird.xPosition <= (pillar.x + pillar.w) 
    && (bird.yPosition-20 <= (pillar.y + pillar.h));
}
public boolean bottomPartCollision(Bird bird, Pillar pillar) {
  return bird.xPosition+20 >= pillar.x && bird.xPosition <= (pillar.x + pillar.w) 
    && (bird.yPosition+20 >= (pillar.y + height + 300));
}

public void keyPressed() {
  if (gameOver == true) {

    if (updateHS == true) {
      if (keyCode == ENTER) {
        Highscores.setString(rowID, "names", name);
        Highscores.setInt(rowID, "scores", currentScore);
        saveTable(Highscores, "data/Highscores.csv");
        updateHS = false;
        name = "";
        currentScore = 0;
      } else if (keyCode == BACKSPACE && name.length()>0) {
        name = name.substring(0, name.length()-1);
      } else if((key >= 'A' && key <= 'Z') || (key >= 'a' && key <= 'z')) {
          name = name + key;
        if (name.length()>4)
          name = name.substring(0, name.length()-1);
      }
    } else if (gameOverChoice == 1) {
      if (keyCode == DOWN) {
        gameOverArrowY += 140;
        gameOverChoice ++;
      }
      if (key == ' ') {
        menuTF = true;
        gameOver = false;
        y = -200;
        currentScore = 0;
        pillars[i].x = -900;
      }
    } else if (gameOverChoice == 2) {
      if (keyCode == UP) {
        gameOverArrowY -= 140;
        gameOverChoice --;
      }
      if (key == ' ') {
        exit();
        return;
      }
    }
  } else {
    if (menuTF == false) {
      if (gameOver == false) {
        if (key == ' ') {
          yVel = jump;
          xVel = 0;
        }
      }
    } else if (menuTF == true) {
      if (key == CODED) {
        if (!leaderboardTF) {
          if (keyCode == DOWN && menuOptionCount == 1) {
            //optionChange = true;
            arrowYIncr = 100;
            menuOptionCount += 1;
          } else if (keyCode == UP && menuOptionCount == 2) {
            arrowYIncr = 0;
            menuOptionCount -= 1;
          }
        }
      }
      if (key == ' ' && menuOptionCount == 1) {
        gravity = 3;
        xVel = 0;
        menuTF = false;
        gameOver = false;
        pillarsAllowedToMove = true;
      } else if (key == ' ' && menuOptionCount == 2) {
        leaderboardTF = !leaderboardTF;
      }
    }
  }
}
class Bird {
  
  public float xPosition, yPosition;
  private PImage bird;

  Bird(float xPos, float yPos) {
    xPosition = xPos;
    yPosition = yPos;
    bird = loadImage("data/Bird.png");
  }
  
  public void displayBird(){
    image(bird, xPosition, yPosition);
  }
}
class Leaderboard {
  
  int currentScore = 0;
  
  int space = 50;
  int nameX = width/3 + 30;
  int nameY = height/3-20;
  int scoreX = nameX + 300;
  int scoreY = nameY;

  Leaderboard(boolean a, Table Highscores) {
    if (a ==  true) {
      fill(255);
      rectMode(CENTER);
      rect(width/2, height/2, 500, 700);//board
      
      fill(0);
      PFont lbFont = createFont("data/ka1.ttf", 50);
      textFont(lbFont);
      text("HIGH SCORES", width/2-215, height/2-250);
      for(int i=0; i<Highscores.getRowCount(); i++){
        textFont(lbFont, 30);
        TableRow row = Highscores.getRow(i);
        text(i+1 +".", nameX, nameY);
        text(row.getString("names"), nameX + 100, nameY);
        text(row.getInt("scores"), scoreX, scoreY);
        nameY += space;
        scoreY += space;
      }
      rectMode(CORNER);
    }
  }


  public void displayScore(int cS) {
    currentScore = cS;
    fill(0);
    PFont scoreFont = createFont("data/ka1.ttf", 85);
    textFont(scoreFont);
    text(currentScore, 100, 150);
  }
}
class Menu {

  PShape arrow;
  float arrowX = -95;
  float arrowY = -20;
  Menu(boolean a) {
    if (a == true) {
      PImage logo = loadImage("data/flappy-bird-logo.png");
      image(logo, width/2, height/2-300, 580, 150);

      fill(0);
      PFont menuFont = createFont("data/DisposableDroidBB.ttf", 55);
      textFont(menuFont);
      text("2.0", width/2+300, height/2-250);
      text("START", width/2-130, height/2-100);
      text("LEADERBOARD", width/2-130, height/2-100+70);
    }
  }

  public void displayArrow(boolean b, float arrowX, float arrowY) {
    if (b ==  true) {
      arrow = createShape();
      arrow.beginShape();
      strokeWeight(8);
      arrow.fill(255);
      arrow.vertex(width/2, height/2);
      arrow.vertex(width/2+50, height/2);    
      arrow.vertex(width/2+50, height/2-30);
      arrow.vertex(width/2+100, height/2+20);
      arrow.vertex(width/2+50, height/2+70);
      arrow.vertex(width/2+50, height/2+40);
      arrow.vertex(width/2, height/2+40);
      arrow.endShape(CLOSE);

      pushMatrix();
      scale(0.75f);
      translate(arrowX, arrowY);
      shape(arrow);
      popMatrix();
    }
  }

  public void moveArrow(boolean a, float y, float c) {
    if (a) {
      if (c == 1)
        arrowY -= y;
      else arrowY += y;
    }
  }
  public void displayGameOver(boolean gameOver) {
    if (gameOver) {
      fill(0);
      PFont font = createFont("ka1.ttf", 85);
      textFont(font, 85);
      text("GAME OVER", width/4, height/3);
      font = createFont("DisposableDroidBB.ttf", 55);
      textFont(font);
      text("RETURN TO MENU", width/4 + 120, height/3 + 100);
      text("QUIT", width/4 + 120, height/3 + 200);
      rowID = newHighScoreLocation(currentScore);
      if(rowID<10) updateHS = true;
    }
  }
  public int newHighScoreLocation(int currentScore) {
    int rowID = 0;
    for (; rowID<Highscores.getRowCount(); rowID++) {
      if (currentScore > Highscores.getInt(rowID, "scores")) {
        return rowID;
      }
    }
    return rowID;
  }
  public void displayHighScoreScreen(boolean newHS) {
    if (newHS) {
      ellipseMode(CENTER);
      PFont f1 = createFont("ka1.ttf", 50);
      PFont f2 = createFont("DisposableDroidBB.ttf", 60);
      fill(10);
      ellipse(width/2, height/2, 1000, 695);
      fill(255);
      textFont(f1);
      text("N E W   H I G H S C O R E !!!", width/3-180, height/3+10);
      textFont(f2);
      text("ENTER NAME:", width/3+40, height/3+100);
      textFont(f2, 100);
      text(name, width/3+50, height/3+200);
      ellipseMode(CORNER);
    }
  }
}
class Pillar {

  float x;
  float y;
  float w;
  float h;

  Pillar(float x1) {

    x = x1;
    y = random(-900 + 200, -200) - 200;
    w = 100;
    h = 1000;
  }

  public void move(boolean allowed, float speed) {
    if(allowed)
      x = x - speed;
  }

  public void drawPillar() {
    fill(255, 0, 0);
    rect(x, y, w, h);
    rect(x, y + height + 300, w, h);
  }
  
  public float getX(){
    return x;
  }
}
  public void settings() {  size(1200, 900); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "main" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
