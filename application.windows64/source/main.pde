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

void setup() {
  size(1200, 900);
  imageMode(CENTER);
  frameRate(40);

  for (int i=0; i<totalPillars; i++) {
    pillars[i] = new Pillar(pillarSpawnX);
  }

  Highscores = loadTable("data/Highscores.csv", "header");
}

void draw() {
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

boolean collision(Bird bird, Pillar pillar) {

  boolean output = false;

  if ( topPartCollision(bird, pillar) || bottomPartCollision(bird, pillar))
    output = true;

  return output;
}
boolean topPartCollision(Bird bird, Pillar pillar) {
  return bird.xPosition+20 >= pillar.x && bird.xPosition <= (pillar.x + pillar.w) 
    && (bird.yPosition-20 <= (pillar.y + pillar.h));
}
boolean bottomPartCollision(Bird bird, Pillar pillar) {
  return bird.xPosition+20 >= pillar.x && bird.xPosition <= (pillar.x + pillar.w) 
    && (bird.yPosition+20 >= (pillar.y + height + 300));
}

void keyPressed() {
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
