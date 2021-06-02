package HitBrick;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class MyFrame extends JFrame {

    private JPanel jp = new JPanel();
    private Random rand = new Random();
    public static final int WIDTH = 800;
    public static final int HEIGHT = 1000;
    public boolean isGaneOver = false;
    private boolean isEmpty = true;//因为砖块有延迟，所以加一个判断条件
    public boolean isStop = false;//判断是否暂停了
    public boolean isPass = false;//判断是否通关
    private int BrickCount;//初始化砖块数目
    private int Stick_length;//初始化木板的长度
    public Ball ball = new Ball(300, 650, 8, 8, this);//随机初始小球的位置
    private ArrayList<Brick> bricks = new ArrayList<>();
    public Stick stick = new Stick(300, 300, this, ball);
    private ArrayList<ExplodeDemo> explodes = new ArrayList<>();
    public int level = 1;
    private MusicPlay musicPlay = new MusicPlay();
    private StopFrame stopFrame = new StopFrame(this, stick.getSensitivity(), ball.getVy());
    private Image back = Toolkit.getDefaultToolkit().getImage("image/background.jpeg"); //加载背景图片
    private Image ballimage = Toolkit.getDefaultToolkit().getImage("image/ball.png");
    private boolean isBonus = false;//标记是不是彩蛋

    public MyFrame(int level, int sensity, int speed, boolean flag) {//光卡数越多，长度越短，砖块越多
        super();
        this.level = level;
        this.isBonus = flag;
        stick.setSensitivity(sensity);//记录上次条的数据
        init();
    }

    private void init() {
        //调整布局
        this.setTitle("打砖块————第" + level + "关");
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);//居中

        this.add(jp, BorderLayout.CENTER);
        this.setVisible(true);


        //创建砖块和小球和木棍
        create();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT && !isStop) {
                    if (stick.getX() + stick.getSensitivity() + stick.getLength() < WIDTH)
                        stick.setX(stick.getX() + stick.getSensitivity());
                    else stick.setX(WIDTH - stick.getLength());
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT && !isStop) {
                    if (stick.getX() - stick.getSensitivity() > 0)
                        stick.setX(stick.getX() - stick.getSensitivity());
                    else stick.setX(0);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    isStop = true;
                    stopFrame.Set(stick.getSensitivity(), Math.abs(ball.getVy()));
                    stopFrame.setVisible(true);
                }
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

    }

    public void paint(Graphics g) {
        synchronized (MyFrame.class) {
            collision();
            if (stick.HitBall())
                musicPlay.PlayStick();
            drawAll();
        }
    }

    public synchronized void drawAll() {
        Image image = createImage(WIDTH, HEIGHT);
        Graphics g = image.getGraphics();

        //画背景
        g.drawImage(back, 0, 0, WIDTH, HEIGHT, null);

        //画球
        g.drawImage(ballimage, ball.getX() - Ball.R, ball.getY() - Ball.R, 2 * Ball.R, 2 * Ball.R, null);

        //画砖块
        for (int i = 0; i < bricks.size(); i++) {
            Brick temp = bricks.get(i);
            if (!temp.isDelete()) {
                g.setColor(bricks.get(i).getColor());
                g.fillRect(temp.getX(), temp.getY(), Brick.WH, Brick.WH);
            } else {
                bricks.remove(i);
                i--;
            }
        }

        //画爆炸粒子
        for (int i = 0; i < explodes.size(); i++) {
            ExplodeDemo ex = explodes.get(i);
            if (ex.isBegin()) {
                ex.drawExplode(g);
            } else {
                explodes.remove(i);
                i--;
            }
        }

        //画木板
        g.setColor(Color.yellow);
        g.fillRect(stick.getX(), Stick.YPosition, stick.getLength(), Stick.H);
        this.getGraphics().drawImage(image, 0, 0, null);
    }

    public synchronized void collision() {//碰撞函数
        for (int i = 0; i < bricks.size(); i++) {
            Brick temp = bricks.get(i);
            if (!temp.isDelete()) {
                if (temp.isHit()) {
                    musicPlay.PlayBrick();
                    ExplodeDemo explodeDemo = new ExplodeDemo(temp, this);
                    explodeDemo.start();
                    explodes.add(explodeDemo);
                    this.repaint();
                }
            } else {
                bricks.remove(i);
                i--;
            }
        }
        if (bricks.size() == 0 && !isEmpty) isPass = true;
    }

    public void create() {
        initBall();
        if (isBonus)
            CreateNankai();
        else CreateBricks();
        ball.start();
    }

    /**
     * 初始化小球和砖块
     */
    public synchronized void initBall() {
        BrickCount = 10 + level * 10;//初始化砖块数目
        Stick_length = 400 - level * 20;//初始化木板的长度

        stick.setLength(Stick_length);
        stick.setX((WIDTH - Stick_length) / 2);
        ball.setX(rand.nextInt(Stick_length) + stick.getX());//随机初始小球的位置
        ball.setY(100);
    }

    /**
     * 初始化砖块
     */
    public synchronized void CreateBricks() {
        int ycounts = (Stick.YPosition - 200) / Brick.WH;
        int xcounts = MyFrame.WIDTH / Brick.WH;
        Set<ArrayList<Integer>> set = new HashSet<>();//判断该位置是否重复
        for (int i = 0; i < BrickCount; ) {
            int position_x = rand.nextInt(xcounts);
            int position_y = rand.nextInt(ycounts);
            if (!set.contains(new ArrayList<>(Arrays.asList(position_x, position_y)))) {
                Brick brick = new Brick(ball, position_x * Brick.WH, position_y * Brick.WH + 20, new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
                bricks.add(brick);
                set.add(new ArrayList<>(Arrays.asList(position_x, position_y)));
                i++;
            }
        }
        isEmpty = false;
    }

    //彩蛋
    public synchronized void CreateNankai() {
        Color purple = new Color(128, 0, 128);
        int shux = 200;
        //上上竖
        int outerh = 3;
        for (int i = 0; i < outerh; i++) {
            Brick brick = new Brick(ball, shux, 50 + i * Brick.WH, purple);
            bricks.add(brick);
        }

        //小横线
        int x1 = 8;
        for (int i = 0; i < x1; i++) {
            Brick brick = new Brick(ball, 100 + i * Brick.WH, 50 + outerh * Brick.WH, purple);
            bricks.add(brick);
        }

        //上中竖
        int inerh = 3;
        for (int i = 0; i < outerh; i++) {
            Brick brick = new Brick(ball, shux, 50 + (outerh + 1 + i) * Brick.WH, purple);
            bricks.add(brick);
        }

        //中横
        int x2 = 12;
        for (int i = 0; i < x2; i++) {
            Brick brick = new Brick(ball, 50 + i * Brick.WH, 50 + (outerh + 1 + inerh) * Brick.WH, purple);
            bricks.add(brick);
        }

        //两大竖
        int xx2 = 9;
        for (int i = 0; i < xx2; i++) {
            Brick brick = new Brick(ball, 50, 50 + (outerh + 2 + inerh + i) * Brick.WH, purple);
            bricks.add(brick);
            brick = new Brick(ball, 50 + (x2 - 1) * Brick.WH, 50 + (outerh + 2 + inerh + i) * Brick.WH, purple);
            bricks.add(brick);
        }

        //两点
        int yy = 2;
        int yposition = 300;
        for (int i = 0; i < yy; i++) {
            Brick brick = new Brick(ball, shux - 50, yposition + i * Brick.WH, purple);
            bricks.add(brick);
            brick = new Brick(ball, shux + 50, yposition + i * Brick.WH, purple);
            bricks.add(brick);
        }

        int x3 = 6;
        //两小横线
        int xiaohengu = 370;
        int downh1 = 2;
        int xposition = 125;
        for (int i = 0; i < x3; i++) {
            Brick brick = new Brick(ball, xposition + i * Brick.WH, xiaohengu, purple);
            bricks.add(brick);
            brick = new Brick(ball, xposition + i * Brick.WH, xiaohengu + (downh1 + 1) * Brick.WH, purple);
            bricks.add(brick);
        }

        //小横线中间一竖
        for (int i = 0; i < downh1; i++) {
            Brick brick = new Brick(ball, shux, xiaohengu + (i + 1) * Brick.WH, purple);
            bricks.add(brick);
        }

        //下横
        int downh2 = 3;
        for (int i = 0; i < downh2; i++) {
            Brick brick = new Brick(ball, shux, xiaohengu + (downh1 + 2 + i) * Brick.WH, purple);
            bricks.add(brick);
        }

        //开
        //上横
        int kxpostioon = 600;
        int kx = 6;
        for (int i = 0; i < kx; i++) {
            Brick brick = new Brick(ball, kxpostioon + (i - kx / 2) * Brick.WH, 50 + outerh * Brick.WH, purple);
            bricks.add(brick);
        }

        //下横
        int kx2 = 10;
        for (int i = 0; i < kx2; i++) {
            Brick brick = new Brick(ball, kxpostioon + (i - kx2 / 2) * Brick.WH, yposition, purple);
            bricks.add(brick);
        }

        //左竖
        int ky1 = 10;
        for (int i = 0; i < ky1; i++) {
            Brick brick = new Brick(ball, kxpostioon - 50, 50 + (outerh + 1 + i) * Brick.WH, purple);
            bricks.add(brick);
        }

        //右竖
        int ky2 = 14;
        for (int i = 0; i < ky2; i++) {
            Brick brick = new Brick(ball, kxpostioon + 50, 50 + (outerh + 1 + i) * Brick.WH, purple);
            bricks.add(brick);
        }

        isEmpty = false;
    }

}
