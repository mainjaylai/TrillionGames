package HitBrick;

import java.util.Random;

public class Stick {
    private int x;
    public static final int YPosition = 700;
    public static final int H = 30;
    private int length;
    public static final int MAXSENSI = 70;
    public static final int MINSENSI = 10;
    private int sensitivity ;//灵敏度
    private Random rand = new Random();//碰撞后随机减小或增大x轴速度
    private MyFrame jf;
    private Ball ball;

    public Stick(int x, int length, MyFrame jf, Ball ball) {
        this.x = x;
        this.length = length;
        this.jf = jf;
        this.ball = ball;
    }

    public int getX() {
        return x;
    }

    public int getLength() {
        return length;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

    /**
     * 判断是否击中了球
     *
     * @return
     */
    public synchronized boolean HitBall() {
        if (ball.getY() + Ball.R > YPosition) {
            if (ball.getX() >= x && ball.getX() <= x + length && !jf.isGaneOver) {//击中了球
                int tempVx = (ball.getVx() > 0 ? 1 : -1) * (rand.nextInt(15) + 1);//随机改变速度
                ball.setVy(-ball.getVy());//反向
                ball.setVx(tempVx);//随机改变速度
                ball.setY(YPosition - Ball.R);
                return true;
            }
            //没有接住，游戏结束，判断是否相反碰撞，如果是，小球改变方向
            if ((ball.getX() < x && ball.getX() + Ball.R >= x && ball.getVx() > 0 && ball.getY() <= YPosition + Stick.H)
                    || (ball.getX() > x + length && ball.getX() - Ball.R <= x + length && ball.getVx() < 0 && ball.getY() <= YPosition + Stick.H))
                ball.setVx(-ball.getVx());
            jf.isGaneOver = true;
        }
        return false;
    }
}
