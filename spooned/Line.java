import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.*;
/* Note: For this class the changes you need to do relate to color handling. */
public class Line {
    private Point startPoint;

    private Point endPoint;

    private Color color;

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.setColor(color);
        g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
    }

    public Line(Color color, Point start) {
        startPoint = start;
        this.color = color;
    }

    public void setEnd(Point end) {
        endPoint = end;
    }

    public Point getStart() {
        return startPoint;
    }

    public Point getEnd() {
        return endPoint;
    }
}