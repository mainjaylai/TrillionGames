package HitBrick;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class StopFrame extends JFrame implements KeyListener {
    private JPanel jp = new JPanel();
    public static final int WID = 600;
    public static final int HEI = 400;
    private JSlider sensity = new JSlider();//设置灵敏度进度条
    private JSlider BallSpeed = new JSlider();//小球竖直速度
    private JLabel label = new JLabel();
    private JLabel speedLabel = new JLabel();
    private MyFrame jf;

    public StopFrame(MyFrame jf, int defalutValue, int speedDefault) {
        super();
        this.jf = jf;
        init(defalutValue, speedDefault);
    }

    private void init(int defalutValue, int speedDefault) {
        //调整布局
        this.requestFocus();
        this.setTitle("设置");
        this.setSize(WID, HEI);
        this.setLocationRelativeTo(null);//居中

        //调整标签位置
        label.setBounds(20, 100, 150, 50);
        label.setFont(new Font(null, Font.ROMAN_BASELINE, 15));
        label.setText("木板灵敏度 ： ");
        speedLabel.setBounds(20, 200, 150, 50);
        speedLabel.setFont(new Font(null, Font.ROMAN_BASELINE, 15));
        speedLabel.setText("小球下落速度 ： ");

        //加滑动条
        //创建灵敏度
        sensity.setBounds(350, 100, 200, 30);
        sensity.setMinimum(Stick.MINSENSI);
        sensity.setMaximum(Stick.MAXSENSI);
        sensity.setValue(defalutValue);
        sensity.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                jf.stick.setSensitivity(sensity.getValue());
                //System.out.println("sensity:" + sensity.getValue());
                StopFrame.this.requestFocus();
            }
        });


        //小球下落速度
        BallSpeed.setBounds(350, 200, 200, 30);
        BallSpeed.setMinimum(Ball.YMIN);
        BallSpeed.setMaximum(Ball.YMAX);
        BallSpeed.setValue(speedDefault);
        BallSpeed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int tempVy = (jf.ball.getVy() > 0 ? 1 : -1) * BallSpeed.getValue();
                jf.ball.setVy(tempVy);
                //  System.out.println("speed:" + BallSpeed.getValue());
                StopFrame.this.requestFocus();
            }
        });
        this.add(sensity);
        this.add(BallSpeed);
        this.add(label);
        this.add(speedLabel);
        this.setVisible(false);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        addKeyListener(this);
    }

    public void Set(int a, int b) {
        sensity.setValue(a);
        BallSpeed.setValue(b);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jf.isStop = false;
            this.setVisible(false);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
