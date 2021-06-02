# TrillionGames
## 一、基本效果如下:
![show](https://user-images.githubusercontent.com/73374331/120475534-0fc93c80-c3dc-11eb-8891-477dda3cb00c.gif)
* 彩蛋视频
[彩蛋](https://user-images.githubusercontent.com/73374331/120473037-2d48d700-c3d9-11eb-903a-28b57151f407.mov)
* 效果视频
[效果](https://user-images.githubusercontent.com/73374331/120473123-45205b00-c3d9-11eb-849d-229b8012f99f.mov)
<br></br>
## 二、代码关系图如下:
![MyFrame](https://user-images.githubusercontent.com/73374331/120478008-f970b000-c3de-11eb-855b-eb8bd72badc3.png)
<br></br>
## 三、主要代码和思路如下:
#### 1.MyFrame(主要界面类)<br></br>
```Java
    public boolean isGaneOver = false;//判断游戏是否结束
    private boolean isEmpty = true;//因为砖块有延迟，所以加一个判断条件
    public boolean isStop = false;//判断是否暂停了
    public boolean isPass = false;//判断是否通关
    private int BrickCount;//初始化砖块数目
    private int Stick_length;//初始化木板的长度
    public Ball ball ;//随机初始小球的位置
    private ArrayList<Brick> bricks = new ArrayList<>();
    public Stick stick;
    private ArrayList<ExplodeDemo> explodes = new ArrayList<>();//建立爆炸类，可能同时时间有多个爆炸类，所以要用arraylist数组
    public int level = 1;//当前的关卡数
    private boolean isBonus = false;//标记是不是彩蛋

    private void init() {
        //创建砖块和小球和木棍
        create();
    }
    
    //绘图函数
     public void paint(Graphics g) {
        synchronized (MyFrame.class) {
            collision();//判断小球有无碰到砖块
            stick.HitBall();//判断小球有无碰到木板
            drawAll();//画出所有的砖块小球函数
        }
    }
    
    //碰撞函数
     public synchronized void collision() {
        for (int i = 0; i < bricks.size(); i++) {
            Brick temp = bricks.get(i);
            if (!temp.isDelete()) {//如果此时砖块还没有删除
                if (temp.isHit()) {//判断小球碰到了砖块
                    ExplodeDemo explodeDemo = new ExplodeDemo(temp, this);
                    explodeDemo.start();//新的爆炸事件
                    explodes.add(explodeDemo);
                    this.repaint();
                }
            } else {
                bricks.remove(i);//如果删除的话就移除它
                i--;
            }
        }
        if (bricks.size() == 0 && !isEmpty) isPass = true;//没有砖块了，pass就为true了
    }
    
    //创建砖块和小球和木棍
    public void create(） 
    
    //随机生成砖块
    public synchronized void CreateBricks()
    
    //构建彩蛋砖块
    public synchronized void CreateNankai()
```
#### 2.Ball(小球类)<br></br>
```Java
    private int X;//球心横坐标x
    private int Y;//球心纵坐标
    private int vx;//水平方向的速度
    private int vy;//竖直方向的速度
    public static final int R = 15;//小球坐标不变
    public static final int YMAX = 15;//小球竖直方向最大速度
    public static final int YMIN = 2;//小球竖直方向最小速度
    private MyFrame jf;


    @Override
    public void run() {
        while (!jf.isGaneOver) {//如果游戏没有结束
            try {
                if (!jf.isStop) {//判断有没有停止
                    move();//移动
                    jf.repaint();//重新绘制
                }
                Thread.sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //小球移动函数
    public synchronized void move() {
        X += vx;
        Y += vy;
        //若是碰到左边和右边,vx反向
        if (X <= R || X + R >= MyFrame.WIDTH) {
            vx = -vx;
            if (X <= R)
                X = R;
            else X = MyFrame.WIDTH - R;
        } 
        //若是碰到顶部和底部,vy反向
        else if (Y >= MyFrame.HEIGHT||Y <= 2 * R) {
            vy = -vy;
        }
    }
```
#### 3.Stick(木板类)<br></br>
```Java
    private int x;//木板左上角x左边
    public static final int YPosition = 700;//纵坐标固定
    public static final int H = 30;//高度固定
    private int length;//长度随着关卡数增加而减小
     private int sensitivity ;//灵敏度(即每按一下键盘,木板移动的距离)
    public static final int MAXSENSI = 70;/
    public static final int MINSENSI = 10;
    private Random rand = new Random();//碰撞后随机减小或增大x轴速度
    private MyFrame jf;
    private Ball ball;

    

    //判断是否击中了球
    public synchronized boolean HitBall() {
        if (ball.getY() + Ball.R > YPosition) {//小球的y距离一定到了木板的高度
           //击中了球(小球球心在木板范围内)
            if (ball.getX() >= x && ball.getX() <= x + length && !jf.isGaneOver) {
                int tempVx = (ball.getVx() > 0 ? 1 : -1) * (rand.nextInt(15) + 1);//随机改变速度
                ball.setVy(-ball.getVy());//反向
                ball.setVx(tempVx);//随机改变速度
                return true;//成功击中小球
            }
            
            //没有接住，游戏结束，判断是否相反碰撞，如果是，小球改变方向
            jf.isGaneOver = true;
        }
        return false;
    }
```
#### 4.Brick(砖块类)<br></br>
```Java
    private Ball ball;
    private int x;//砖块左上角的横坐标
    private int y;//砖块左上角的纵坐标
    public static final int WH = 30;//砖块为方块,高度和宽度相等
    private Color color;//砖块颜色,随机产生
    private boolean isDelete = false;//判断是否已经被小球碰到

    
    //判断是否碰到小球
    public synchronized boolean isHit() {
        //删除了直接返回false
        if (isDelete) {
            return false;
        }
        //撞到砖块左右两边
        else if (ball.getY() >= y && ball.getY() <= y + WH && (ball.getX() + Ball.R >= x && ball.getX() - Ball.R <= x + WH)) {
            ball.setVx(-ball.getVx());//反向
            isDelete = true;//删除了
            return true;
        } 
        //撞到砖块上下两侧
        else if (ball.getX() >= x && ball.getX() <= x + WH && (ball.getY() + Ball.R >= y && ball.getY() - Ball.R <= y + WH)) {
            ball.setVy(-ball.getVy());//反向
            isDelete = true;//删除了
            return true;
        }
        return false;
    }
```
自己写的java作业,全自创,可下载应付.但若进行商业行为,侵权必究
