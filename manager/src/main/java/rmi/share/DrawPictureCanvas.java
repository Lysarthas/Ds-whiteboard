package rmi.share;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

public class DrawPictureCanvas extends Canvas {
    private static final long serialVersionUID = 3950771061853327150L;

    private Image image = null;
	
	public void setImage(Image image) {
        this.image=image;
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}
	
	public void update(Graphics g) {
		paint(g);
    }
}
	
