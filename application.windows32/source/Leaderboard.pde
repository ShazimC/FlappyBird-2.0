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


  void displayScore(int cS) {
    currentScore = cS;
    fill(0);
    PFont scoreFont = createFont("data/ka1.ttf", 85);
    textFont(scoreFont);
    text(currentScore, 100, 150);
  }
}
