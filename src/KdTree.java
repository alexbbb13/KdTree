import edu.princeton.cs.introcs.*;
import edu.princeton.cs.algs4.*;

public class KdTree {
 /*
  * public KdTree() // construct an empty KdTree() 
  * public boolean isEmpty() // is the set empty? 
  * public int size() // number of points in the set 
  * public void insert(Point2D p) // add the point p to the set (if it is not
  * already in the set) 
  * public boolean contains(Point2D p) // does the set contain the point p? 
  * public void draw() // draw all of the points to standard draw 
  * public Iterable<Point2D> range(RectHV rect) // all points in the set that are inside the rectangle public 
  * Point2D nearest(Point2D p) // a nearest neighbor in the set to p; null if set is empty
  */
 private static class Node {
  private Point2D p; // the point
  private RectHV rect; // the axis-aligned rectangle corresponding to this
        // node
  private Node lb; // the left/bottom subtree
  private Node rt; // the right/top subtree
  
  
  public Node(Point2D newp, double xMin,double yMin,double xMax,double yMax) {
	   p = newp;
	   rect =  new RectHV(xMin, yMin,xMax,yMax);
	  }
 }

 private Node root = null;
 private int size = 0;
 //private RectHV rootHV = new RectHV(0D, 0D, 1D, 1D);
 
 public KdTree() {
  root = null;
 }

 public boolean isEmpty() {
  if (root == null || root.p == null)
   return true;
  if (size == 0)
   return true;
  return false;
 }

 public int size() {
  return size;
 }

 public void insert(Point2D p) {
  if (p == null)
   throw new NullPointerException();
  boolean comparex = true;
  //RectHV rootHV = new RectHV(0D, 0D, 1D, 1D);
  this.size++;
  //root = insert(root, p, comparex, rootHV);
  root = insert(root, p, comparex, 0D, 0D, 1D, 1D);
 }

  
 private Node insert(Node x, Point2D p, boolean comparex, double xMin,double yMin,double xMax,double yMax) {
	  if (x == null) {
	   //this.size++;
	   //return new Node(p, rect);
	   return new Node(p, xMin, yMin,xMax,yMax);
	  }
	  
	  double val1=p.x();
	  double val2 = x.p.x();
	  double val3=p.y();
	  double val4 = x.p.y();
	  
	  if (val1==val2 && val3==val4){
		  size--;
		  return x;
	  }
	  
	 	  // p divides rectHV rect by two
	  if (comparex) {
		  
		  if (val1 <= val2) {
			  x.lb = insert(x.lb, p, !comparex, xMin, yMin,val2,yMax);
		  } else{
			  x.rt = insert(x.rt, p, !comparex, val2,yMin,xMax,yMax);
		  }
	  } else {
		  if (val3 <= val4) {
			x.lb = insert(x.lb, p, !comparex, xMin, yMin,xMax,val4);
		  } else{
			  x.rt = insert(x.rt, p, !comparex, xMin, val4, xMax,yMax);
		  }
	  }	  
	   return x;
	 }
	  
 
 
 public boolean contains(Point2D key) {
  return get(key) != null;
 }

 public void draw() {
  // draw canvas border
  StdDraw.setPenColor(StdDraw.BLACK);
  StdDraw.line(0, 0, 1, 0);
  StdDraw.line(1, 0, 1, 1);
  StdDraw.line(1, 1, 0, 1);
  StdDraw.line(0, 1, 0, 0);
  boolean comparex = true;
  draw(root, comparex);
 }

 public Iterable<Point2D> range(RectHV rect) {
  final Stack<Point2D> stack = new Stack<Point2D>();
  range(root, rect, stack);
  return stack;
 }

 private void range(Node x, RectHV thatrect, Stack<Point2D> stack) {
  if (x == null)
   return;
  if (!x.rect.intersects(thatrect))
   return;
  if (thatrect.contains(x.p))
   stack.push(x.p);
  range(x.lb, thatrect, stack);
  range(x.rt, thatrect, stack);
 }

 private Point2D nearest;
 private double nearestDistance=2000;
 
 public Point2D nearest(Point2D p) {
  boolean comparex = true;
  if(root==null) return null;
  if(root.p==null) return null;
  nearest = root.p;
  nearestDistance = p.distanceSquaredTo(root.p);
  
  nearest(root, p, comparex);
  return nearest;
 }
 
 private void nearest(Node node, Point2D key,  boolean comparex) {
	 if(node==null) return;
	 double thisRectDistance = node.rect.distanceSquaredTo(key);
	 if(thisRectDistance>nearestDistance) return;
	 if(node.p.distanceSquaredTo(key)<nearestDistance){
		 nearest=node.p;
		 nearestDistance=nearest.distanceSquaredTo(key);
	 }
	 double val1;
	 double val2;
		if (comparex) {
			val1 = key.x();
			val2 = node.p.x();
		} else {
			val1 = key.y();
			val2 = node.p.y();
		}
	if(val1<val2){
		nearest(node.lb,key,!comparex);
		nearest(node.rt,key,!comparex);
	}else{
		nearest(node.rt,key,!comparex);
		nearest(node.lb,key,!comparex);
	}
 }
 
  private void draw(Node node, boolean comparex) {
  if (node == null) {
   return;
  }

  StdDraw.setPenColor(StdDraw.BLACK);
  node.p.draw();

  double x = node.p.x();
  double y = node.p.y();

  if (comparex) {
   StdDraw.setPenColor(StdDraw.RED);

   StdDraw.line(x, node.rect.ymin(), x, node.rect.ymax());

  } else {
   StdDraw.setPenColor(StdDraw.BLUE);

   StdDraw.line(node.rect.xmin(), y, node.rect.xmax(), y);
  }

  draw(node.lb, !comparex);
  draw(node.rt, !comparex);
 }

 // return value associated with the given key, or null if no such key exists
 private Node get(Point2D key) {
  boolean comparex = true;
  return get(root, key, comparex);
 }

 private Node get(Node x, Point2D key, boolean comparex) {
  if (x == null)
   return null;
  double xpX=x.p.x();
  double xpY=x.p.y();
  double keyX=key.x();
  double keyY=key.y();
  if(xpX==keyX && xpY==keyY) return x;
  
  double val1;
  double val2;
  if (comparex) {
   val1 = keyX;
   val2 = xpX;
  } else {
   val1 = keyY;
   val2 = xpY;
  }
  if (val1 <= val2)
   return get(x.lb, key, !comparex);
  else {
   return get(x.rt, key, !comparex);
  }
  
 }

 
}
