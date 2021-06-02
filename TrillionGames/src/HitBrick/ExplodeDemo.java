package HitBrick;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

class SmallBall {
    private int x;
    private int y;
    public static final int R_RANG = 16;//半径的范围
    private int radiu;
    private int speed;//速度
    private double angle;
    public boolean isDie = false;
    private Random rand = new Random();

    public SmallBall(int x, int y) {
        this.x = x;
        this.y = y;
        radiu = rand.nextInt(R_RANG);//半径范围
        speed = rand.nextInt(11);//速度范围，后面会来越大
        angle = rand.nextInt(360) * Math.PI / 180;
    }

    public synchronized boolean move() {
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
        if (x <= 0 || x + radiu >= MyFrame.WIDTH || y <= 0 || y + radiu >= MyFrame.HEIGHT)
            isDie = true;
        speed += 3;//速度增加
        if (radiu <= 4)
            isDie = true;
        else radiu -= 1;//半径减小
        return isDie;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadiu() {
        return radiu;
    }
}

public class ExplodeDemo extends Thread {
    private Brick brick;
    public ArrayList<SmallBall> balls = new ArrayList<>();
    private MyFrame jf;
    private Random rand = new Random();
    private static final int BALL_SIZE = 20;
    private boolean isBegin = true;//判断爆炸事件是否开始

    public ExplodeDemo(Brick brick, MyFrame jf) {
        this.brick = brick;
        this.jf = jf;
        createBalls();
    }

    private synchronized void createBalls() {
        for (int i = 0; i < BALL_SIZE; i++) {
            int x = rand.nextInt(Brick.WH) + brick.getX();
            int y = rand.nextInt(Brick.WH) + brick.getY();
            balls.add(new SmallBall(x, y));
        }
    }


    @Override
    public void run() {
        while (isBegin) {
            synchronized (ExplodeDemo.class) {
                if (!jf.isStop)
                    for (int i = 0; i < balls.size(); i++) {
                        if (balls.get(i).move()) {
                            balls.remove(i);
                            i--;
                        }
                    }
            }
            try {
                jf.repaint();
                if (balls.size() == 0) {
                    break;
                }
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isBegin = false;
    }

    public boolean isBegin() {
        return isBegin;
    }


    public synchronized void drawExplode(Graphics g) {
        if (!isBegin) return;
        g.setColor(brick.getColor());
        for (int i = 0; i < balls.size(); i++) {
            SmallBall temp = balls.get(i);
            if (!temp.isDie) {
                g.fillOval(temp.getX() - temp.getRadiu(), temp.getY() - temp.getRadiu(), temp.getRadiu() * 2, temp.getRadiu() * 2);
            } else {
                balls.remove(i);
                i--;
            }
        }
    }
}
