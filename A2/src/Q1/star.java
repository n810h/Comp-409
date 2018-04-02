package Q1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

public class star {
	int c;
	int m;
	static int n = 6;
	public static int width = 1920;
	public static int height = 1080;
	static Node polygon = new Node(-1.0f,5.0f);
	static AtomicInteger count;
	public static Node[] nodes = new Node[6];
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Command args missing");
			return;
		}
		int m = Integer.parseInt(args[0]);
		int c = Integer.parseInt(args[1]);
		
		if(m > n || c < 0) {
			System.out.println("Invalid parameters.");
			return;
		}
		count = new AtomicInteger(c);
		polygon.elem = 0;
		createPolygon();
		createArray();
		//Start threads
		Thread[] threads = new Thread[m];
		for(int i = 0; i < m; i++) {
			threads[i] = new Thread(new Modifier(count,nodes));
		}
		
		try {
			for(int i = 0; i < m; i++) {
				threads[i].start();
			}
			for(int i = 0; i < m; i++) {
				threads[i].join();
			}
//			threads[0].join();
//			threads[1].join();
//			threads[2].join();
//			threads[3].join();
//			threads[4].join();
//			threads[5].join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		draw();
	}
	
	public static void createPolygon() {
		Node curr = polygon;
		
		curr.addNode(new Node(1f,2f));
		curr = curr.next;
		curr.addNode(new Node(5f,0f));
		curr = curr.next;
		curr.addNode(new Node(1f,-2f));
		curr = curr.next;
		curr.addNode(new Node(-4f,-4f));
		curr = curr.next;
		curr.addNode(new Node(-3f,-1f));
		curr = curr.next;
		curr.next = polygon;
		polygon.prev = curr;
	}
	
	/**
	 * Draws output image.
	 * */
	public static void draw() {
		BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = img.createGraphics();
		
		//Set background color to white
		for(int i = 0; i< width; i++) {
			for(int j =0; j < height; j++) {
				img.setRGB(i,j, 0xffffffff);
			}
		}
		
		//Get coordinates to draw
		Iterator<Float> xs = Node.getXs().iterator();
		Iterator<Float> ys = Node.getYs().iterator();
		int prevx = Math.round(xs.next());
		int prevy = Math.round(ys.next());
		int cx = 0;
		int cy = 0;
		float dx = maxX();
		float dy = maxY();
		float yscale = ((float)height/2)/dy;
		float xscale = ((float)width/2)/dy;
		if(xscale < 0.001)
			xscale = 10;
		if(yscale < 0.001)
			yscale = 10;
		g2D.setColor(Color.BLACK);
		while(xs.hasNext() && ys.hasNext()) {
			cx = Math.round(xs.next());
			cy = Math.round(ys.next());
			System.out.println("("+prevx+","+prevy+")->("+cx+","+cy+")" );
			g2D.drawLine(Math.round(prevx * xscale)+width/2,Math.round(prevy*yscale)+height/2, 
						 Math.round(cx*xscale)+width/2, Math.round(cy*yscale)+height/2);
			prevx = cx;
			prevy = cy;
		}
		//Basic scaling
		g2D.drawLine(Math.round(cx*xscale)+width/2,Math.round(cy*yscale)+height/2, 
					 Math.round(Math.round(polygon.x)*xscale)+width/2, Math.round(Math.round(polygon.y)*yscale)+height/2);
		System.out.println("("+cx+","+cy+")->("+Math.round(polygon.x)+","+Math.round(polygon.y)+")" );
		
		//Output file
		File outputfile = new File("outputimage.png");
        try {
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Picks random node and modifies it.
	 * */
	public static void modify() {
		Random rn = new Random();
		int v = rn.nextInt(6);
		float r1 = rn.nextFloat();
		float r2 = rn.nextFloat();
		float rx = randomX(nodes[v], r1, r2);
		float ry = randomY(nodes[v], r1, r2);
		nodes[v].x = rx;
		nodes[v].y = ry;
	}
	
	/**
	 * Just makes it easier to pick a random node.
	 * */
	public static void createArray() {
		int i = 0;
		Node curr = star.polygon.next;
		star.nodes[i++] = star.polygon; 
		while(curr != star.polygon) {
			star.nodes[i++] = curr;
			curr = curr.next;
		}
	}
	
	/**
	 * Get random x coord in triangle.
	 * */
	public static float randomX(Node rnode, float r1, float r2) {
		return (1 - sqrt(r1)) * rnode.prev.x + (sqrt(r1) * (1 - r2)) * rnode.x + (sqrt(r1) * r2) * rnode.next.x;
	}
	
	/**
	 * Get random y coord in triangle
	 * */
	public static float randomY(Node rnode, float r1, float r2) {
		return (1 - sqrt(r1)) * rnode.prev.y + (sqrt(r1) * (1 - r2)) * rnode.y + (sqrt(r1) * r2) * rnode.next.y;
	}
	
	public static float sqrt(float x) {
		return (float) Math.sqrt(x);
	}
	
	public static float maxX() {
		float dx = 0;
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 6;j++) {
				dx = Math.max(nodes[i].x - nodes[j].x,dx);
			}
		}
		return dx;
	}
	
	public static float maxY() {
		float dy = 0;
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 6;j++) {
				dy = Math.max(nodes[i].y - nodes[j].y,dy);
			}
		}
		return dy;
	}

}


/*
P(x) = (1 - sqrt(r1)) * A(x) + (sqrt(r1) * (1 - r2)) * B(x) + (sqrt(r1) * r2) * C(x)
P(y) = (1 - sqrt(r1)) * A(y) + (sqrt(r1) * (1 - r2)) * B(y) + (sqrt(r1) * r2) * C(y)
*/
