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

  void displayArrow(boolean b, float arrowX, float arrowY) {
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
      scale(0.75);
      translate(arrowX, arrowY);
      shape(arrow);
      popMatrix();
    }
  }

  void moveArrow(boolean a, float y, float c) {
    if (a) {
      if (c == 1)
        arrowY -= y;
      else arrowY += y;
    }
  }
  void displayGameOver(boolean gameOver) {
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
  int newHighScoreLocation(int currentScore) {
    int rowID = 0;
    for (; rowID<Highscores.getRowCount(); rowID++) {
      if (currentScore > Highscores.getInt(rowID, "scores")) {
        return rowID;
      }
    }
    return rowID;
  }
  void displayHighScoreScreen(boolean newHS) {
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
