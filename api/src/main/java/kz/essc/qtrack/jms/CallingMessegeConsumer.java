package kz.essc.qtrack.jms;

import kz.essc.qtrack.config.ConfigBean;
import kz.essc.qtrack.core.resource.ResourceBean;

import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

@MessageDriven(mappedName = "jms/GlassFishBookQueue")
public class CallingMessegeConsumer implements MessageListener {

    private static boolean isPlaying = false;
    static ArrayList<String> files = new ArrayList<>();
    private static int index = 0;

    String relPath = ResourceBean.getSoundPath();
    String path = relPath+"/en/rachel/wav/";
    String format = ".wav";

    public void onMessage(Message message) {
//        System.out.println("consumer: onMessage");
        try {
    		if (message instanceof MapMessage) {
                String lang = message.getStringProperty("lang");
                String code = message.getStringProperty("code");
                String cabinet = message.getStringProperty("cabinet");
                int floor = message.getIntProperty("floor");

                System.out.println("code " + code + " cabinet " + cabinet + " floor " + floor + " lang " + lang);

                boolean speachEnabled = Boolean.parseBoolean(ConfigBean.get("speachEnabled"));
                ArrayList<String> list = new ArrayList<>();

                if (speachEnabled) {
    //                boolean withPrefix = true;
                        boolean withPostfix = true;
                    /* switch (code.charAt(0)) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            withPrefix = false;
                            break;
                    } */

                    switch (cabinet.charAt(cabinet.length() - 1)) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            withPostfix = false;
                            break;
                    }

                    int[] digitsCode, digitsCabinet;

    //                if (withPrefix)
    //                    digitsCode = digitize(code.substring(1));
    //                else
                    digitsCode = digitize(code);

                    if (withPostfix)
                        digitsCabinet = digitize(cabinet.substring(0, cabinet.length() - 1));
                    else
                        digitsCabinet = digitize(cabinet);
                    list = null;
                    if (lang.equals("kz") || lang.equals("kk"))
                        list = speachKz(digitsCode, digitsCabinet, withPostfix, floor);
                    else if (lang.equals("en"))
                        list = speachEn(digitsCode, digitsCabinet, withPostfix, floor);
                    else if (lang.equals("ru"))
                        list = speachRu(digitsCode, digitsCabinet, withPostfix, floor);
//              123
/*
                    ArrayList<String> list = new ArrayList<>();

                    String path = relPath+"/en/wav/";
                    list.add(path+"1.wav");
                    list.add(path+"100.wav");
                    list.add(path+"20.wav");
                    list.add(path+"3.wav");
*/
                }
                else {
                    list.add(relPath + "/notification.wav");
                }
//                System.out.println("isPlaying " + isPlaying);
                if (isPlaying) {
                    for (String file: list)
                        files.add(file);
                }
                else {
                    files = list;
                    index = 0;
                    play();
                }
            }
    	} catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private synchronized ArrayList<String> speachEn(int [] a, int [] b, boolean withLabel, int floor) {
        path = relPath+"/en/rachel/wav/";
        ArrayList<String> list = new ArrayList<>();

        list.add(relPath+"/notification.wav");

        list.add(path+"client"+format);
        for(String s: speachEnDigit(a))
            list.add(s);

        if (floor == 1)
            list.add(path+"gotoflooronecabinet"+format);
        else if (floor == 2)
            list.add(path+"gotofloortwocabinet"+format);

//        list.add(path+"cabinet"+format);

        for(String s: speachEnDigit(b))
            list.add(s);

        if (withLabel)
            list.add(path+"a"+format);

//        for (String s: list)
//            System.out.println("*** OUTPUT *** " + s);

        return list;
    }

    private synchronized ArrayList<String> speachEnDigit(int [] digits) {
        ArrayList<String> list = new ArrayList<>();

        int l = digits.length;
        if (l == 2) {
            int first = digits[0]*10;
            int second = digits[1];
            int n = first + second;

            if (n < 20) {
                list.add(path+n+format);
            }
            else {
                list.add(path+first+format);
                if (second != 0)
                    list.add(path+second+format);
            }
        }
        else if (l == 3) {
            int first = digits[0]*100;
            int second = digits[1]*10;
            int third = digits[2];
            int n = first + second + third;

            if (n < 20) {
                list.add(path+n+format);
            }
            else if (n < 100){
                list.add(path+second+format);
                if (third != 0)
                    list.add(path+third+format);
            }
            else if (n < 1000) {
                list.add(path+digits[0]+format);
                list.add(path+100+format);

                int d = n%100;

                if (d == 0) {}
                else if (d < 20) {
                    list.add(path+d+format);
                }
                else {
                    list.add(path+second+format);

                    if (third != 0)
                        list.add(path+third+format);
                }
            }
        }
        /*else if (l == 3) {
            int first = digits[0]*100;
            int second = digits[1]*10;
            int third = digits[2];
            int n = first + second + third;

            if (n < 20) {
                list.add(path+n+format);
            }
            else if (n < 100){
                list.add(path+second+format);
                if (third != 0)
                    list.add(path+third+format);
            }
            else {
                list.add(path+digits[0]+format);
                list.add(path+100+format);

                int d = n%100;

                if (d == 0) {}
                else if (d < 20) {
                    list.add(path+d+format);
                }
                else {
                    list.add(path+second+format);

                    if (third != 0)
                        list.add(path+third+format);
                }
            }
        }*/
        else if (l == 4) {
            int first = digits[0]*1000;
            int second = digits[1]*100;
            int third = digits[2]*10;
            int forth = digits[3];
            int n = first + second + third + forth;

            if (n < 20) {
                list.add(path+n+format);
            }
            else if (n < 100){
                list.add(path+third+format);
                if (forth != 0)
                    list.add(path+forth+format);
            }
            else if (n < 1000) {
                list.add(path+digits[1]+format);
                list.add(path+100+format);

                int d = n%100;

                if (d == 0) {}
                else if (d < 20) {
                    list.add(path+d+format);
                }
                else {
                    list.add(path+third+format);

                    if (forth != 0)
                        list.add(path+forth+format);
                }
            }
            else {
                list.add(path+digits[0]+format);
                list.add(path+1000+format);

                if (digits[1] > 0) {
                    list.add(path+digits[1]+format);
                    list.add(path+100+format);
                }

                int d = n%100;

                if (d == 0) {}
                else if (d < 20) {
                    list.add(path+d+format);
                }
                else {
                    list.add(path+third+format);

                    if (forth != 0)
                        list.add(path+forth+format);
                }
            }
        }
        return list;
    }

    private ArrayList<String> speachKz(int [] a, int [] b, boolean withLabel, int floor) {
        path = relPath+"/kz/fatima/louder/wav/";
        ArrayList<String> list = new ArrayList<>();

        list.add(relPath+"/notification.wav");

        list.add(path+"client"+format);
        for(String s: speachKzDigit(a))
            list.add(s);

//        list.add(path+"n"+format);
//        list.add(path+"gotofloorone"+format);

        if (floor == 1)
            list.add(path+"gotofloorone"+format);
        else if (floor == 2)
            list.add(path+"gotofloortwo"+format);


        for(String s: speachKzDigit(b))
            list.add(s);

        if (withLabel)
            list.add(path+"a"+format);

        list.add(path+"cabinet"+format);

        return list;
    }

    private ArrayList<String> speachKzDigit(int [] digits) {
        ArrayList<String> list = new ArrayList<>();

        int l = digits.length;
        if (l == 2) {
            int first = digits[0]*10;
            int second = digits[1];
            int n = first + second;

            if (n < 10) {
                list.add(path+n+format);
            }
            else {
                list.add(path+first+format);
                if (second != 0)
                    list.add(path+second+format);
            }
        }
        else if (l == 3) {
            int first = digits[0]*100;
            int second = digits[1]*10;
            int third = digits[2];
            int n = first + second + third;

            if (n < 10) {
                list.add(path+n+format);
            }
            else if (n < 100){
                list.add(path+second+format);
                if (third != 0)
                    list.add(path+third+format);
            }
            else if (n < 1000) {
                if (digits[0] > 1)
                    list.add(path+digits[0]+format);
                list.add(path+100+format);

                int d = n%100;
                if (d < 10 && d != 0) {
                    list.add(path+d+format);
                }
                else {
                    if (second != 0)
                        list.add(path+second+format);

                    if (third != 0)
                        list.add(path+third+format);
                }
            }
//            int first = digits[0]*100;
//            int second = digits[1]*10;
//            int third = digits[2];
//            int n = first + second + third;
//
//            if (n < 10) {
//                list.add(path+n+format);
//            }
//            else if (n < 100){
//                list.add(path+second+format);
//                if (third != 0)
//                    list.add(path+third+format);
//            }
//            else {
//                if (digits[0] > 1)
//                    list.add(path+digits[0]+format);
//                list.add(path+100+format);
//
//                int d = n%100;
//                if (d < 10 && d != 0) {
//                    list.add(path+d+format);
//                }
//                else {
//                    if (second != 0)
//                        list.add(path+second+format);
//
//                    if (third != 0)
//                        list.add(path+third+format);
//                }
//            }
        }
        else if (l == 4) {
            int first = digits[0]*1000;
            int second = digits[1]*100;
            int third = digits[2]*10;
            int forth = digits[3];
            int n = first + second + third + forth;

            if (n < 10) {
                list.add(path+n+format);
            }
            else if (n < 100){
                list.add(path+third+format);
                if (forth != 0)
                    list.add(path+forth+format);
            }
            else if (n < 1000) {
                if (digits[1] > 1)
                    list.add(path+digits[1]+format);
                list.add(path+100+format);

                int d = n%100;
                if (d < 10 && d != 0) {
                    list.add(path+d+format);
                }
                else {
                    if (third != 0)
                        list.add(path+third+format);

                    if (forth != 0)
                        list.add(path+forth+format);
                }
            }
            else {
                if (digits[0] > 1)
                    list.add(path+digits[0]+format);
                list.add(path+1000+format);

                if (second != 0) {
                    if (digits[1] > 1)
                        list.add(path+digits[1]+format);
                    list.add(path+100+format);
                }

                int d = n%100;
                if (d==0) {} else if (d < 10) {
                    list.add(path+d+format);
                }
                else {
                    list.add(path+third+format);

                    if (forth != 0)
                        list.add(path+forth+format);
                }
            }
        }
        return list;
    }

    private synchronized ArrayList<String> speachRu(int [] a, int [] b, boolean withLabel, int floor) {
        path = relPath+"/ru/wav/";
        ArrayList<String> list = new ArrayList<>();

        list.add(relPath+"/notification.wav");

        list.add(path+"client"+format);
        for(String s: speachRuDigit(a))
            list.add(s);


//        list.add(path+"gotoflooronecabinet"+format);
        if (floor == 1)
            list.add(path+"gotoflooronecabinet"+format);
        else if (floor == 2)
            list.add(path+"gotofloortwocabinet"+format);

//        list.add(path+"cabinet"+format);

        for(String s: speachRuDigit(b))
            list.add(s);

        if (withLabel)
            list.add(path+"a"+format);

//        for (String s: list)
//            System.out.println("*** OUTPUT *** " + s);

        return list;
    }

    private synchronized ArrayList<String> speachRuDigit(int [] digits) {
        ArrayList<String> list = new ArrayList<>();

        int l = digits.length;
        if (l == 2) {
            int first = digits[0]*10;
            int second = digits[1];
            int n = first + second;

            if (n < 20) {
                list.add(path+n+format);
            }
            else {
                list.add(path+first+format);
                if (second != 0)
                    list.add(path+second+format);
            }
        }
        else if (l == 3) {
            int first = digits[0]*100;
            int second = digits[1]*10;
            int third = digits[2];
            int n = first + second + third;

            if (n < 20) {
                list.add(path+n+format);
            }
            else if (n < 100) {
                list.add(path+second+format);
                if (third != 0)
                    list.add(path+third+format);
            }
            else if (n < 1000) {
                list.add(path+first+format);

                int d = n%100;

                if (d == 0) {}
                else if (d < 20) {
                    list.add(path+d+format);
                }
                else {
                    list.add(path+second+format);

                    if (third != 0)
                        list.add(path+third+format);
                }
            }
        }
        /*else if (l == 3) {
            int first = digits[0]*100;
            int second = digits[1]*10;
            int third = digits[2];
            int n = first + second + third;

            if (n < 20) {
                list.add(path+n+format);
            }
            else if (n < 100){
                list.add(path+second+format);
                if (third != 0)
                    list.add(path+third+format);
            }
            else {
                list.add(path+first+format);

                int d = n%100;

                if (d == 0) {}
                else if (d < 20) {
                    list.add(path+d+format);
                }
                else {
                    list.add(path+second+format);

                    if (third != 0)
                        list.add(path+third+format);
                }
            }
        }*/
        else if (l == 4) {
            int first = digits[0]*1000;
            int second = digits[1]*100;
            int third = digits[2]*10;
            int forth = digits[3];
            int n = first + second + third + forth;

            if (n < 20) {
                list.add(path+n+format);
            }
            else if (n < 100) {
                list.add(path+third+format);
                if (forth != 0)
                    list.add(path+forth+format);
            }
            else if (n < 1000) {
                list.add(path+second+format);

                int d = n%100;

                if (d == 0) {}
                else if (d < 20) {
                    list.add(path+d+format);
                }
                else {
                    list.add(path+third+format);

                    if (forth != 0)
                        list.add(path+forth+format);
                }
            }
            else {
                if (digits[0] == 1)
                    list.add(path + "1000one" + format);
                else if (digits[0] <= 4) {
                    list.add(path + digits[0] + format);
                    list.add(path + "1000twothreefour" + format);
                }
                else if (digits[0] > 4) {
                    list.add(path + digits[0] + format);
                    list.add(path + 1000 + format);
                }

                if (second != 0)
                    list.add(path+second+format);

                int d = n%100;

                if (d == 0) {}
                else if (d < 20) {
                    list.add(path+d+format);
                }
                else {
                    list.add(path+third+format);

                    if (forth != 0)
                        list.add(path+forth+format);
                }
            }
        }
        return list;
    }

    public synchronized void play() {
        isPlaying = true;
//        System.out.println("*** SOUND play index = " + index + " file " + files.get(index) +  " !!!");
        try {
            File soundFile = new File(files.get(index));
            AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);

            // load the sound into memory (a Clip)
            DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(sound);

            clip.addLineListener(new LineListener() {
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        event.getLine().close();
                    }
                }
            });

            // play the sound clip
            clip.start();

            while (!clip.isRunning())
                Thread.sleep(10);
            while (clip.isRunning())
                Thread.sleep(10);

            clip.drain();
            clip.close();
            sound.close();
//            System.out.println("*** SOUND stop !!!");

            if (index == (files.size() - 1)) {
                isPlaying = false;
                index = 0;
//                System.out.println("*** finally SOUND stop !!!");
//                System.out.println("*** index = " + index);
            } else {
                index++;
                play();
            }
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int[] digitize(String code) {
        int number = Integer.parseInt(code);
        int n = code.length();
        int [] digits = new int[n];

        if (n == 2) {
            digits[0] = number/10;
            digits[1] = number%10;
        }
        else if (n == 3) {
            digits[0] = number/100;
            digits[1] = (number%100)/10;
            digits[2] = number%10;
        }
        else if (n == 4) {
            digits[0] = number/1000;
            digits[1] = (number%1000)/100;
            digits[2] = (number%100)/10;
            digits[3] = number%10;
        }

        return digits;
    }
}