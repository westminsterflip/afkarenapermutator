import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

//JOptionPane has scaling issues with windows display scaling

public class afkarenabruteforce {
    private static int ind;
    private static double max;
    private static Point windowLocation = new Point();

    public static void main(String[] args) throws InterruptedException {
        JOptionPane nameChooser = new JOptionPane("Enter names separated by commas", JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        nameChooser.setWantsInput(true);
        nameChooser.setInitialSelectionValue(null);

        final JDialog dialog = nameChooser.createDialog(null, null);
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                windowLocation.setLocation(dialog.getLocation().getX() + dialog.getWidth() / 2, dialog.getLocation().getY() + dialog.getHeight() / 2);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                windowLocation.setLocation(dialog.getLocation().getX() + dialog.getWidth() / 2, dialog.getLocation().getY() + dialog.getHeight() / 2);
            }
        });
        dialog.setVisible(true);

        String s = (String) nameChooser.getInputValue();
        if (s == null || s.equals("uninitializedValue")) //Cancel should return null but it returns "uninitializedValue"
            System.exit(0);
        String[] strings = s.split("\\s*,\\s*");
        Object[] tempArray = Arrays.stream(strings).distinct().toArray();
        strings = Arrays.copyOf(tempArray, tempArray.length, String[].class);
        if (strings.length < 5) {
            JOptionPane error = new JOptionPane("Enter five or more names", JOptionPane.ERROR_MESSAGE);
            JDialog jdialog = error.createDialog("Error");
            windowLocation.setLocation(windowLocation.getX() - jdialog.getWidth() / 2, windowLocation.getY() - jdialog.getHeight() / 2);
            jdialog.setLocation(windowLocation);
            jdialog.setVisible(true);
            System.exit(1);
        }
        BigInteger temp = factorial(strings.length).divide(factorial(strings.length - 5));
        max = temp.doubleValue();
        ArrayList<String> perms = getPerm(strings);
        showPerms(perms);
    }

    private static BigInteger factorial(int n) {
        BigInteger out = BigInteger.ONE;
        for (int p = n; p > 0; p--)
            out = out.multiply(BigInteger.valueOf(p));
        return out;
    }

    private static ArrayList<String> getPerm(String[] strings) {
        JDialog dialog = new JDialog();
        dialog.setSize(300, 75);
        JLabel progress = new JLabel("0/" + max);
        progress.setHorizontalAlignment(JLabel.CENTER);
        windowLocation.setLocation(windowLocation.getX() - dialog.getWidth() / 2, windowLocation.getY() - dialog.getHeight() / 2);

        dialog.add(BorderLayout.SOUTH, progress);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        double mult = max / Integer.MAX_VALUE; //JProgressBar only takes int as a max so make sure it falls under int max
        JProgressBar progressBar = new JProgressBar(0, (int) (max / mult));
        progressBar.setStringPainted(true);
        dialog.add(BorderLayout.CENTER, progressBar);
        dialog.setLocation(windowLocation);
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                windowLocation.setLocation(dialog.getLocation().getX() + dialog.getWidth() / 2, dialog.getLocation().getY() + dialog.getHeight() / 2);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                windowLocation.setLocation(dialog.getLocation().getX() + dialog.getWidth() / 2, dialog.getLocation().getY() + dialog.getHeight() / 2);
            }
        });
        dialog.setVisible(true);
        double prog = 0;

        String[] toadd = new String[5];
        ArrayList<String> out = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            toadd[0] = strings[i]; //add first hero
            String[] tmp1 = copyWithout(strings, i);
            for (int o = 0; o < tmp1.length; o++) {
                toadd[1] = tmp1[o]; //add second hero
                String[] tmp2 = copyWithout(tmp1, o);
                for (int p = 0; p < tmp2.length; p++) {
                    toadd[2] = tmp2[p]; //add third hero
                    String[] tmp3 = copyWithout(tmp2, p);
                    for (int s = 0; s < tmp3.length; s++) {
                        toadd[3] = tmp3[s]; //add fourth hero
                        String[] tmp4 = copyWithout(tmp3, s);
                        for (int t = 0; t < tmp4.length; t++) { //IntelliJ warns this can be a foreach, which it can't
                            toadd[4] = tmp4[t]; //add fifth hero
                            String tmp = "";
                            for (int r = 0; r < 5; r++) {
                                tmp += toadd[r]; //This also generates a warning but the loop runs 5 times so it should be fine
                                if (r != 4)
                                    tmp += ", ";
                            }
                            out.add(tmp);
                            prog++;
                            progress.setText(prog + "/" + max);
                            progressBar.setValue((int) (prog / mult));
                        }
                    }
                }
            }
        }
        dialog.dispose();
        return out;
    }

    private static String[] copyWithout(String[] strings, int ind) {
        String[] out = new String[strings.length - 1];
        int index = 0;
        for (int i = 0; i < strings.length; i++) {
            if (i != ind) {
                out[index++] = strings[i];
            }
        }
        return out;
    }

    private static void showPerms(ArrayList<String> perms) throws InterruptedException {
        ind = 0;
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle((perms.size() - ind - 1) + " options left");
        frame.setLayout(new GridLayout(2, 1, 5, 5));
        JPanel text = new JPanel();
        text.setLayout(new BorderLayout());
        text.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JLabel option = new JLabel(perms.get(0));
        option.setHorizontalAlignment(JLabel.CENTER);
        text.add(option, BorderLayout.CENTER);
        frame.add(text);
        JPanel buttons = new JPanel();
        JButton restart = new JButton("Restart");
        JButton next = new JButton("Next");
        restart.addActionListener(e -> {
            ind = 0;
            option.setText(perms.get(ind));
            frame.pack();
            frame.revalidate();
            next.setEnabled(true);
            restart.setEnabled(false);
            frame.setTitle((perms.size() - ind - 1) + " options left");
        });
        buttons.add(restart);

        next.addActionListener(e -> {
            ind++;
            option.setText(perms.get(ind));
            frame.pack();
            frame.revalidate();
            if (ind >= perms.size() - 1)
                next.setEnabled(false);
            restart.setEnabled(true);
            frame.setTitle((perms.size() - ind - 1) + " options left");
        });
        next.setPreferredSize(new Dimension(76, 26));
        buttons.add(next);

        buttons.setBorder(BorderFactory.createEmptyBorder(5, 5, 7, 5));


        frame.add(buttons);
        frame.setMinimumSize(new Dimension(320, 150));
        windowLocation.setLocation(windowLocation.getX() - frame.getWidth() / 2, windowLocation.getY() - frame.getHeight() / 2);
        frame.setLocation(windowLocation);

        frame.setResizable(false);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                frame.setMinimumSize(frame.getSize());
                windowLocation.setLocation(windowLocation.getX() - frame.getWidth() / 2, windowLocation.getY() - frame.getHeight() / 2);
                frame.setLocation(windowLocation);
                windowLocation.setLocation(windowLocation.getX() + frame.getWidth() / 2, windowLocation.getY() + frame.getHeight() / 2);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                windowLocation.setLocation(frame.getLocation().getX() + frame.getWidth() / 2, frame.getLocation().getY() + frame.getHeight() / 2);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                windowLocation.setLocation(frame.getLocation().getX() + frame.getWidth() / 2, frame.getLocation().getY() + frame.getHeight() / 2);
            }
        });
        frame.setVisible(true);
        frame.getRootPane().setDefaultButton(next);
        restart.doClick();
    }
}