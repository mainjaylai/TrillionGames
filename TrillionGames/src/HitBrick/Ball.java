package HitBrick;

public class Ball extends Thread {
    private int X;
    private int Y;
    private int vx;//水平方向的速度
    private int vy;//竖直方向的速度
    public static final int R = 15;
    public static final int YMAX = 15;
    public static final int YMIN = 2;
    private MyFrame jf;

    public Ball(int x, int y, int vx, int vy, MyFrame jf) {
        X = x;
        Y = y;
        this.vx = vx;
        this.vy = vy;
        this.jf = jf;
    }


    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getVx() {
        return vx;
    }

    public int getVy() {
        return vy;
    }


    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    @Override
    public void run() {
        boolean flag = false;
        while (!jf.isGaneOver || (jf.isGaneOver && Y <= MyFrame.HEIGHT)) {
            try {
                if (!jf.isStop) {
                    move();
                    jf.repaint();
                    if (jf.isPass) {
                        flag = true;
                        break;
                    }
                }
                Thread.sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("run is out");
        //等待其余线程进行完
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jf.setVisible(false);
        new EndFrame(jf, flag);
    }

    public synchronized void move() {
        X += vx;
        Y += vy;
        if (X <= R || X + R >= MyFrame.WIDTH) {
            vx = -vx;
            if (X <= R)
                X = R;
            else X = MyFrame.WIDTH - R;
        } else if (Y >= MyFrame.HEIGHT) {
            jf.isGaneOver = true;
        } else if (Y <= 2 * R) {
            vy = -vy;
            Y = 2 * R;
        }
    }
}
