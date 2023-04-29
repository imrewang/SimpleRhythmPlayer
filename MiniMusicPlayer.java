package chap12;

import javax.sound.midi.*;//为MIDI（乐器数字接口）数据的I / O，排序和合成提供接口和类。
// 乐器数字interface(MIDI)标准定义了电子音乐设备(例如电子键盘乐器和个人计算机)的通信协议。
import javax.swing.*;
import java.awt.*;

// this one plays random music with it, but only because there is a listener.

public class MiniMusicPlayer {

    static JFrame f = new JFrame("My First Music Video");//使用指定的标题创建一个新的，最初不可见的Frame 。
    static MyDrawPanel ml;

    public static void main(String[] args) {
        MiniMusicPlayer mini = new MiniMusicPlayer();
        mini.go();
    }


    public void setUpGui() {
        ml = new MyDrawPanel();
        f.setContentPane(ml);//设置contentPane(内容面板)属性。 构造函数调用此方法。
        f.setBounds(30, 30, 300, 300);//移动并调整此组件的大小。 左上角的新位置由x和y指定，新大小由width和height指定。
        f.setVisible(true);//显示或隐藏此Window具体取决于参数b的值。

    }


    public void go() {
        setUpGui();

        try {

            // make (and open) a sequencer, make a sequence and track制作（并打开）一个音序器，制作一个音序器并进行跟踪

            Sequencer sequencer = MidiSystem.getSequencer();//播放MIDI sequence的硬件或软件设备称为音序器 。
            //MidiSystem类提供对已安装的MIDI系统资源的访问，包括合成器，音序器和MIDI输入和输出端口等设备。
            //getSequencer获得连接到默认设备的默认 Sequencer 。
            sequencer.open();//打开设备，指示它现在应该获取它所需的任何系统资源并开始运行。

            sequencer.addControllerEventListener(ml, new int[]{127});
            //注册控制器事件侦听器，以便在sequencer处理所请求类型的控件更改事件时接收通知。
            Sequence seq = new Sequence(Sequence.PPQ, 4);//Sequence是包含可由Sequencer对象播放的音乐信息（通常是整首歌曲或乐曲）的数据结构。
            //PPQ基于速度的定时类型，其分辨率以每四分音符的脉冲（滴答）表示。
            Track track = seq.createTrack();//MIDI轨道是MIDI事件（带时间戳的MIDI数据）的独立流，可以与标准MIDI文件中的其他轨道一起存储。
            //createTrack作为此序列的一部分，创建一个新的，最初为空的轨道。

            // now make two midi events (containing a midi message)现在制作两个 MIDI 事件（包含 MIDI 消息）

            int r = 0;
            for (int i = 0; i < 60; i += 4) {

                r = (int) ((Math.random() * 50) + 1);

                track.add(makeEvent(144, 1, r, 100, i));//向赛道添加新赛事。

                track.add(makeEvent(176, 1, 127, 0, i));

                track.add(makeEvent(128, 1, r, 100, i + 2));
            } // end loop

            // add the events to the track将事件添加到轨道
            // add the sequence to the sequencer, set timing, and start将序列添加到音序器，设置时间，然后开始

            sequencer.setSequence(seq);//设置顺控程序运行的当前顺序。

            sequencer.start();//开始播放当前加载的序列中的MIDI数据。
            sequencer.setTempoInBPM(120);//以每分钟节拍数设置速度。
        } catch (Exception ex) {
            ex.printStackTrace();//提供对 printStackTrace()打印的堆栈跟踪信息的编程访问。
        }
    } // close go


    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();//A ShortMessage包含一条MIDI消息，其状态字节后最多有两个数据字节。
            a.setMessage(comd, chan, one, two);//设置最多需要两个数据字节的通道消息的短消息参数。
            /*参数
            command - 此消息表示的MIDI命令
            channel - 与消息关联的通道
            data1 - 第一个数据字节
            data2 - 第二个数据字节*/
            event = new MidiEvent(a, tick);//MIDI事件包含MIDI信息和以刻度表示的相应时间戳，可以表示存储在MIDI文件或Sequence对象中的MIDI事件信息。
            /*
            参数
            message - 事件中包含的MIDI消息
            tick - 事件的时间戳，以MIDI刻度表示
            * */
        } catch (Exception e) {
            System.out.println("Exception");
        }
        return event;
    }


    class MyDrawPanel extends JPanel implements ControllerEventListener {//我的绘图面板

        // only if we got an event do we want to paint
        boolean msg = false;

        public void controlChange(ShortMessage event) {
            msg = true;
            repaint();
        }

        public void paintComponent(Graphics g) {//Graphics类是所有图形上下文的抽象基类，允许应用程序绘制到在各种设备上实现的组件以及屏幕外图像。
            if (msg) {

                int r = (int) (Math.random() * 250);
                int gr = (int) (Math.random() * 250);
                int b = (int) (Math.random() * 250);

                g.setColor(new Color(r, gr, b));//获取此图形上下文的当前颜色。

                int ht = (int) ((Math.random() * 120) + 10);
                int width = (int) ((Math.random() * 120) + 10);

                int x = (int) ((Math.random() * 40) + 10);
                int y = (int) ((Math.random() * 40) + 10);

                g.fillRect(x, y, width, ht);//填充指定的矩形。
                msg = false;

            } // close if
        } // close method
    }  // close inner class

} // close class
