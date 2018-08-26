class Bird {
  
  public float xPosition, yPosition;
  private PImage bird;

  Bird(float xPos, float yPos) {
    xPosition = xPos;
    yPosition = yPos;
    bird = loadImage("data/Bird.png");
  }
  
  void displayBird(){
    image(bird, xPosition, yPosition);
  }
}
