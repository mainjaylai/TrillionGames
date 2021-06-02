package HitBrick;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BeginFrame extends JFrame implements ActionListener {
    public static final int WID = 480;
    public static final int HEI = 640;
    private JPanel jp = new JPanel();
    private ImageIcon back = new ImageIcon("image/begin.jpeg"); //加载背景图片
    private JLabel background = new JLabel();
    private JButton Bonus = new JButton("彩蛋");
    private JButton normal = new JButton("普通");

    public BeginFrame() {
        init();
    }

    private void init() {
        //调整布局
        this.setSize(WID, HEI);
        this.setLocationRelativeTo(null);//居中

        jp.setBounds(0, 0, WID, HEI);
        jp.setVisible(true);

        Bonus.setBounds(200, 150, 80, 50);
        Bonus.setFont(new Font("宋体", Font.PLAIN, 30));

        normal.setBounds(200, 350, 80, 50);
        normal.setFont(new Font("宋体", Font.PLAIN, 30));


        //加背景图片
        background.setBounds(0, 0, WID, HEI);
        background.setIcon(back);

        jp.add(normal);
        jp.add(Bonus);
        jp.add(background);

        this.setVisible(true);
        this.setLayout(null);
        this.add(jp, BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //添加监听事件
        Bonus.addActionListener(this);
        normal.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Bonus) {
            new MyFrame(1, 20, 8, true);
        } else if (e.getSource() == normal) {
            new MyFrame(1, 20, 8, false);
        }
        this.dispose();
    }
}