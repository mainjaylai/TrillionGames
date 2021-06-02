package HitBrick;

import java.awt.*;

public class Brick {
    private Ball ball;
    private int x;
    private int y;
    public static final int WH = 30;
    private Color color;
    private boolean isDelete = false;//判断是否消失了

    public Brick(Ball ball, int x, int y, Color color) {
        this.ball = ball;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public boolean isDelete() {
        return isDelete;
    }


    /**
     * 判断是否碰到小球
     *
     * @return
     */
    public synchronized boolean isHit() {
        if (isDelete) {
            return false;
        } else if (ball.getY() >= y && ball.getY() <= y + WH && (ball.getX() + Ball.R >= x && ball.getX() - Ball.R <= x + WH)) {//撞到左右两边
            ball.setVx(-ball.getVx());//反向
            isDelete = true;//删除了
            return true;
        } else if (ball.getX() >= x && ball.getX() <= x + WH && (ball.getY() + Ball.R >= y && ball.getY() - Ball.R <= y + WH)) {//撞到上下两侧了
            ball.setVy(-ball.getVy());//反向
            isDelete = true;//删除了
            return true;
        }
        return false;
    }
}
