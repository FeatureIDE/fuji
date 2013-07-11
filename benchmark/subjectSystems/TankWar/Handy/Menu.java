

import java.util.Vector;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class Menu {

    protected Vector options = new Vector(2, 1);
    protected int logoXK, logoYK;
    protected int zeileAbstand;
    protected Maler maler;
    protected Image logoimage;
    private int style = 0;
    private int xStyle = 0, yStyle = 1;
    private int x_KoordinateImage, y_KoordinateImage; // center
    private boolean waehlbar = true;
    private int fontSize = 30;
    public Font font;

    public Menu(Maler maler) {
        this.maler = maler;
        this.zeileAbstand = 20;
        this.logoimage = null;
    }

    public void addLogo(Image img) {
        this.logoimage = img;
    }

    public void setLogoKoordinate(int x, int y) {
        this.logoXK = x;
        this.logoYK = y;
    }

    public void setKoordinateImage(int x, int y) {
        x_KoordinateImage = x;
        y_KoordinateImage = y;
    }

    public void setZeileAbstand(int ab) {
        this.zeileAbstand = ab;
    }

    public void setStyle(int style) {
        this.style = style;
        switch (style) {
            case 0:
                xStyle = 0;
                yStyle = 1;
                break;
            case 1:
                xStyle = 1;
                yStyle = 0;
                break;
            case 2:
                xStyle = 1;
                yStyle = 1;
                break;
            case 3:
                xStyle = 0;
                yStyle = 0;
                break;
        }
    }

    public void setWaehlbar(boolean waehlbar) {
        this.waehlbar = waehlbar;
    }

    public void add(String str, int prioritaet) {
        Vector temp = new Vector();
        if (options.size() > 0) {
            boolean insert = false;
            for (int i = 0; i < options.size(); i++) {
                if (prioritaet >= ((Option) options.elementAt(i)).prioritaet) {
                    temp.addElement(options.elementAt(i));
                } else {
                    temp.addElement(new Option(false, str, prioritaet));
                    insert = true;
                    for (int j = i; j < options.size(); j++) {
                        temp.addElement(options.elementAt(j));
                    }
                    break;
                }
            }
            if (!insert) {
                temp.addElement(new Option(false, str, prioritaet));
            }
            options.removeAllElements();
            for (int i = 0; i < temp.size(); i++) {
                options.addElement(temp.elementAt(i));
                if (i == 0) {
                    ((Option) options.elementAt(i)).status = true;
                } else {
                    ((Option) options.elementAt(i)).status = false;
                }
            }
        } else {
            options.addElement(new Option(true, str, prioritaet));
        }
    }

    public void add(String str, Image img, int prioritaet) {
        Vector temp = new Vector();
        if (options.size() > 0) {
            boolean insert = false;
            for (int i = 0; i < options.size(); i++) {
                if (prioritaet >= ((Option) options.elementAt(i)).prioritaet) {
                    temp.addElement(options.elementAt(i));
                } else {
                    temp.addElement(new Option(false, str, prioritaet));
                    insert = true;
                    for (int j = i; j < options.size(); j++) {
                        temp.addElement(options.elementAt(j));
                    }
                    break;
                }
            }
            if (!insert) {
                temp.addElement(new Option(false, str, prioritaet));
            }
            options.removeAllElements();
            for (int i = 0; i < temp.size(); i++) {
                options.addElement(temp.elementAt(i));
                if (i == 0) {
                    ((Option) options.elementAt(i)).status = true;
                } else {
                    ((Option) options.elementAt(i)).status = false;
                }
            }
        } else {
            options.addElement(new Option(true, str, prioritaet));
        }
    }

    public void add(String str, Image img, Image img2, int prioritaet) {
        Vector temp = new Vector();
        if (options.size() > 0) {
            boolean insert = false;
            for (int i = 0; i < options.size(); i++) {
                if (prioritaet >= ((Option) options.elementAt(i)).prioritaet) {
                    temp.addElement(options.elementAt(i));
                } else {
                    temp.addElement(new Option(false, str, img, img2, prioritaet));
                    insert = true;
                    for (int j = i; j < options.size(); j++) {
                        temp.addElement(options.elementAt(j));
                    }
                    break;
                }
            }
            if (!insert) {
                temp.addElement(new Option(false, str, img, img2, prioritaet));
            }
            options.removeAllElements();
            for (int i = 0; i < temp.size(); i++) {
                options.addElement(temp.elementAt(i));
                if (i == 0) {
                    ((Option) options.elementAt(i)).status = true;
                } else {
                    ((Option) options.elementAt(i)).status = false;
                }
            }
        } else {
            options.addElement(new Option(true, str, img, img2, prioritaet));
        }

    }

    public void KeyBehandeln(int key) {
        if (key == 40 || key == 39) {
            for (int i = 0; i < options.size(); i++) {
                if (((Option) options.elementAt(i)).status == true) {
                    ((Option) options.elementAt(i)).status = false;
                    if (i + 1 == options.size()) {
                        ((Option) options.elementAt(0)).status = true;
                    } else {
                        ((Option) options.elementAt(i + 1)).status = true;
                    }
                    break;
                }
            }
        }
        if (key == 38 || key == 37) {
            for (int i = 0; i < options.size(); i++) {
                if (((Option) options.elementAt(i)).status == true) {
                    ((Option) options.elementAt(i)).status = false;
                    if (i - 1 < 0) {
                        ((Option) options.elementAt(options.size() - 1)).status = true;
                    } else {
                        ((Option) options.elementAt(i - 1)).status = true;
                    }
                    break;
                }
            }
        }
        if (key == 17) {
            if (waehlbar) {
                for (int i = 0; i < options.size(); i++) {
                    if (((Option) options.elementAt(i)).status == true) {
                        maler.menuBehandeln(((Option) options.elementAt(i)).str);
                        break;
                    }
                }
            } else {
                maler.menuBehandeln(((Option) options.elementAt(0)).str);
            }

        }
    }

    public void erscheinen(Graphics gTemp) {
        int x = 0, y = 0;
        if (this.logoimage != null) {
            gTemp.drawImage(logoimage, logoXK, logoYK, Graphics.TOP | Graphics.LEFT);
        }

        gTemp.setColor(0, 255, 0);
        if (((Option) options.elementAt(0)).img == null) {

            for (int i = 0; i < options.size(); i++) {
                switch (style) {
                    case 0:
                        x = x_KoordinateImage - Font.getDefaultFont().stringWidth(((Option) options.elementAt(i)).str) / 2;
                        y = y_KoordinateImage - Font.getDefaultFont().getHeight() * options.size() + (zeileAbstand * (options.size() - 1)) / 2;
                        break;
                    case 1:
                        x = x_KoordinateImage - Font.getDefaultFont().stringWidth(((Option) options.elementAt(i)).str) * options.size() + (zeileAbstand * (options.size() - 1)) / 2;
                        y = y_KoordinateImage - Font.getDefaultFont().getHeight() / 2;
                        break;
                    case 2:
                        x = x_KoordinateImage - Font.getDefaultFont().stringWidth(((Option) options.elementAt(i)).str) * options.size() + (zeileAbstand * (options.size() - 1)) / 2;
                        y = y_KoordinateImage - Font.getDefaultFont().getHeight() * options.size() + (zeileAbstand * (options.size() - 1)) / 2;
                        break;
                    case 3:
                        x = x_KoordinateImage - Font.getDefaultFont().stringWidth(((Option) options.elementAt(i)).str) / 2;
                        y = y_KoordinateImage - Font.getDefaultFont().getHeight() / 2;
                        break;
                }

                if (((Option) options.elementAt(i)).status == true) {
                    gTemp.setColor(0, 255, 0);
                } else {
                    if (waehlbar) {
                        gTemp.setColor(255, 0, 0);
                    }
                }
                gTemp.drawString(((Option) options.elementAt(i)).str, x + i * zeileAbstand * xStyle, y + i * zeileAbstand * yStyle, Graphics.TOP | Graphics.LEFT);
            }
        } else {
            switch (style) {
                case 0:

                    x = x_KoordinateImage - ((Option) options.elementAt(0)).img.getWidth() / 2;
                    y = y_KoordinateImage - (((Option) options.elementAt(0)).img.getHeight() * options.size() + zeileAbstand * (options.size() - 1)) / 2;
                    break;
                case 1:
                    x = x_KoordinateImage - (((Option) options.elementAt(0)).img.getWidth() * options.size() + zeileAbstand * (options.size() - 1)) / 2;
                    y = y_KoordinateImage - ((Option) options.elementAt(0)).img.getHeight() / 2;
                    break;
                case 2:
                    x = x_KoordinateImage - (((Option) options.elementAt(0)).img.getWidth() * options.size() + zeileAbstand * (options.size() - 1)) / 2;
                    y = y_KoordinateImage - (((Option) options.elementAt(0)).img.getHeight() * options.size() + zeileAbstand * (options.size() - 1)) / 2;
                    break;
				case 3:
                    x = x_KoordinateImage - ((Option) options.elementAt(0)).img.getWidth() / 2;
                    y = y_KoordinateImage - ((Option) options.elementAt(0)).img.getHeight() / 2;
                    break;
            }

            for (int i = 0; i < options.size(); i++) {
                if (((Option) options.elementAt(i)).status == true) {
                    gTemp.drawImage(((Option) options.elementAt(i)).img2, x + i * (zeileAbstand + ((Option) options.elementAt(i)).img2.getWidth()) * xStyle, y + i * (zeileAbstand + ((Option) options.elementAt(i)).img2.getHeight()) * yStyle, Graphics.TOP | Graphics.LEFT);
                } else {
                    gTemp.drawImage(((Option) options.elementAt(i)).img, x + i * (zeileAbstand + ((Option) options.elementAt(i)).img2.getWidth()) * xStyle, y + i * (zeileAbstand + ((Option) options.elementAt(i)).img2.getHeight()) * yStyle, Graphics.TOP | Graphics.LEFT);
                }
            }
        }

    }
}
