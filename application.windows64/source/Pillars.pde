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

  void move(boolean allowed, float speed) {
    if(allowed)
      x = x - speed;
  }

  void drawPillar() {
    fill(255, 0, 0);
    rect(x, y, w, h);
    rect(x, y + height + 300, w, h);
  }
  
  float getX(){
    return x;
  }
}
