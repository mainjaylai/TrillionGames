package HitBrick;


import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class EndFrame extends JFrame {
    private JPanel jp = new JPanel();
    public static final int WID = 500;
    public static final int HEI = 500;
    private ImageIcon fail = new ImageIcon("image/fail.gif");
    private ImageIcon pass = new ImageIcon("image/pass.gif");
    private JButton back = new JButton();
    private MyFrame jf;
    private boolean flag;
    private int level = 1;

    public EndFrame(MyFrame jf, boolean flag) {
        this.flag = flag;
        this.jf = jf;
        init();
    }

    private void init() {
        //调整布局
        this.setSize(WID, HEI);
        this.setLocationRelativeTo(null);//居中
        jp.setVisible(true);
        jp.setBounds(0, 0, WID, HEI);
        this.setUndecorated(true);
        this.add(jp, BorderLayout.CENTER);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //加背景图片
        back.setBounds(0, 0, WID, HEI);
        if (flag)
            back.setIcon(pass);
        else back.setIcon(fail);
        jp.add(back);

        //如果过关了
        if (flag) {
            new Timer().schedule(new TimerTask() {

                //run方法就是具体需要定时执行的任务
                @Override
                public void run() {
                    int result = JOptionPane.showConfirmDialog(//存储结果
                            jp,
                            "过关成功结束，继续",
                            "提示",
                            JOptionPane.YES_NO_CANCEL_OPTION
                    );
                    if (result == 0) {
                        level++;
                        jf = new MyFrame(level, jf.stick.getSensitivity(), Math.abs(jf.ball.getVy()), false);
                    } else jf.dispose();
                    EndFrame.this.dispose();
                }
            }, 3000);
        }
        //没有过关
        else {
            new Timer().schedule(new TimerTask() {

                //run方法就是具体需要定时执行的任务
                @Override
                public void run() {
                    int result = JOptionPane.showConfirmDialog(
                            jp,
                            "游戏失败，是否重新开始",
                            "失败",
                            JOptionPane.YES_NO_CANCEL_OPTION
                    );
                    if (result == 0) {
                        jf = new MyFrame(1, jf.stick.getSensitivity(), Math.abs(jf.ball.getVy()), false);
                    } else jf.dispose();
                    EndFrame.this.dispose();
                }
            }, 3000);
        }
    }
}


