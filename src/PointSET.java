import edu.princeton.cs.introcs.*;
import edu.princeton.cs.algs4.*;

public class PointSET {
	/*
	 * public PointSET() // construct an empty set of points public boolean
	 * isEmpty() // is the set empty? public int size() // number of points in
	 * the set public void insert(Point2D p) // add the point p to the set (if
	 * it is not already in the set) public boolean contains(Point2D p) // does
	 * the set contain the point p? public void draw() // draw all of the points
	 * to standard draw public Iterable<Point2D> range(RectHV rect) // all
	 * points in the set that are inside the rectangle public Point2D
	 * nearest(Point2D p) // a nearest neighbor in the set to p; null if set is
	 * empty
	 */

	private SET<Point2D> set;

	public PointSET() {
		set = new SET<Point2D>();
	}

	public boolean isEmpty() {
		return set.size() == 0;
	}

	public int size() {
		return set.size();
	}

	public void insert(Point2D p) {
		set.add(p);
	}

	public boolean contains(Point2D p) {
		return set.contains(p);
	}

	public void draw() {
		for (Point2D p : set)
			p.draw();
	}

	public Iterable<Point2D> range(RectHV rect) {
		Stack<Point2D> stack = new Stack<Point2D>();
		for (Point2D p : set) {
			if (rect.contains(p))
				stack.push(p);
		}
		return stack;
	}

	public Point2D nearest(Point2D p) {
		if (this.size() == 0)
			return null;
		Point2D nearest = null;
		double mindist = 0;
		for (Point2D ipoint : set) {
			if (nearest == null) {
				nearest = ipoint;
				mindist = p.distanceSquaredTo(ipoint);
			}
			if (p.distanceSquaredTo(ipoint) < mindist) {
				nearest = ipoint;
				mindist = p.distanceSquaredTo(ipoint);
			}
		}
		return nearest;
	}

}
