package glug.gui;

import static java.awt.Color.DARK_GRAY;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class FineCrosshairMouseCursorFactory {
	public Cursor createFineCrosshairMouseCursor() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension bestCursorSize = toolkit.getBestCursorSize(24, 24);
		BufferedImage cursorImage = new BufferedImage(bestCursorSize.width,bestCursorSize.height, BufferedImage.TYPE_INT_ARGB);
		Point hotSpot = new Point(bestCursorSize.width/2,bestCursorSize.height/2);
		Graphics2D graphics = cursorImage.createGraphics();
		graphics.translate(hotSpot.x, hotSpot.y);
		Color colour = DARK_GRAY;
		for (int point=2;point<hotSpot.x;point+=2) {
			graphics.setColor(colour);
			colour=colour.brighter();
			graphics.drawLine(point,-1,point,1);
			graphics.drawLine(-1,point,1,point);
			graphics.drawLine(-point,-1,-point,1);
			graphics.drawLine(-1,-point,1,-point);
		}
		return toolkit.createCustomCursor(cursorImage,hotSpot,"fine-crosshair");
	}
}
