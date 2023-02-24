package com.p;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalProgressBarUI;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    static  JLabel label;
    static JProgressBar bar;
    static Clip clip;
    public static void main(String[] args){
        AudioInputStream audio;

        try {
            File file = new File("src/Demo.wav");
            audio = AudioSystem.getAudioInputStream(file);
             clip = AudioSystem.getClip();
            clip.open(audio);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        myFrame frame = new myFrame();


        Border border = BorderFactory.createLineBorder(Color.black,1,true);

        JButton play = new JButton("play");
        JButton pause = new JButton("Pause");
        JButton stop = new JButton("Stop");
        JButton ff = new JButton("FF");
        JButton bb = new JButton("BB");
         label  = new JLabel("--/--");
         label.setFont(new Font("",Font.BOLD,42));

        JPanel panel = new JPanel(new BorderLayout());

        panel.setPreferredSize(new Dimension(0,10));
        ff.setBorder(border);
        bb.setBorder(border);

        stop.setBorder(border);

        ff.addActionListener(e->
                clip.setMicrosecondPosition(clip.getMicrosecondPosition()+5000000)
        );
        bb.addActionListener(e->
                clip.setMicrosecondPosition(clip.getMicrosecondPosition()-5000000)
        );
       bar = new JProgressBar(0,clip.getFrameLength());
        stop.addActionListener(e->{
            clip.stop();
            clip.setMicrosecondPosition(0);
        });
        bar.setUI(new MetalProgressBarUI());
        play.setBorder(border);
        play.addActionListener(e->{
            clip.start();
            Thread thread1 = new Thread(new no());
            Thread thread2 = new Thread(new time());
            thread1.start();
            thread2.start();


        });
        pause.setBorder(border);
        pause.addActionListener(e->{
            clip.stop();
            bar.setValue(clip.getFramePosition());
        });
        JPanel control = new JPanel(new FlowLayout());
        bar.setStringPainted(true);
        panel.add(label,BorderLayout.CENTER);
        panel.add(bar,BorderLayout.SOUTH);
        frame.add(panel,BorderLayout.CENTER);

        control.add(pause);
        control.add(play);
        control.add(stop);
        control.add(ff);
        control.add(bb);

        frame.add(control,BorderLayout.SOUTH);
         frame.setVisible(true);


    }

}
class no implements Runnable {

    @Override
    public void run() {
            while (Main.clip.getFrameLength() > Main.clip.getFramePosition()) {
                Main.bar.setValue(Main.clip.getFramePosition());

            }
            Main.bar.setString("Stopped");


    }
}

class time implements Runnable{

    @Override
    public void run() {
        try{
            while (Main.clip.getFrameLength() > Main.clip.getFramePosition()) {
                if(Main.clip.getMicrosecondPosition()/1000000<10) {
                    Main.label.setText("00:0" + (Main.clip.getMicrosecondPosition() / 1000000));
                }
                else if(Main.clip.getMicrosecondPosition()/1000000<60) {
                    Main.label.setText("00:" + (Main.clip.getMicrosecondPosition() / 1000000));
                }
               else if (Main.clip.getMicrosecondPosition()/1000000>=60) {
                    if((Main.clip.getMicrosecondPosition()/1000000)-60<10) {
                        Main.label.setText("01:0" + ((Main.clip.getMicrosecondPosition() / 1000000)-60));
                    }else {
                        Main.label.setText("01:" + ((Main.clip.getMicrosecondPosition() / 1000000) - 60));
                    }
                }
            }

        }catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}

