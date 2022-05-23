package user;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//import Client.Painter;

public class PaintPanel extends JPanel implements MouseListener, MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int x_start;
	static int y_start;
	static int x_end;
	static int y_end;
	static Graphics2D graph;
	static String RGB = "0 0 0";
	static String text = "";
	static String tool = "Line";
	static Color color = UserWhiteBoard.color;
	static JSONObject paintData;
	static ArrayList<JSONObject> paintDataList = new ArrayList<JSONObject>();
	
	public static void setList(ArrayList<JSONObject> recorList) {
		paintDataList = recorList;
	}

	@Override
	public void paint(Graphics graph) {
		super.paint(graph);
		System.out.println("ooo: "+paintDataList);
		String jsonString = paintDataList.stream().map(Object::toString).collect(Collectors.joining("-"));
		if(!jsonString.isEmpty()) {
			String[] list = jsonString.split("-");
			for (int i = 0; i < list.length; i++) {
				JSONObject paintJson = parseResString(list[i]);
				x_start = (int) (long) paintJson.get("x_start");
				y_start = (int) (long) paintJson.get("y_start");
				x_end = (int) (long) paintJson.get("x_end");
				y_end = (int) (long) paintJson.get("y_end");
				RGB = (String) paintJson.get("RGB");
				String[] RGBList = RGB.split(" ");
				graph.setColor(new Color(Integer.parseInt(RGBList[0]), Integer.parseInt(RGBList[1]),
						Integer.parseInt(RGBList[2])));
				if (paintJson.get("tool").equals("Line")) {
					graph.drawLine(x_start, y_start, x_end, y_end);
//						panel.repaint();
				} else if (paintJson.get("tool").equals("Circle")) {
					int diameter = Math.min(Math.abs(x_start - x_end), Math.abs(y_start - y_end));
					graph.drawOval(Math.min(x_start, x_end), Math.min(y_start, y_end), diameter, diameter);
				} else if (paintJson.get("tool").equals("Rect")) {
					graph.drawRect(Math.min(x_start, x_end), Math.min(y_start, y_end), Math.abs(x_start - x_end),
							Math.abs(y_start - y_end));
				} else if (paintJson.get("tool").equals("Text")) {
					text = (String) paintJson.get("Text");
//					System.out.println(text);
					graph.drawString(text, x_end, y_end);
				}
			}
		}
//		draw(paintDataList);
//		addMouseListener(this);
//		this.addMouseListener(this);
//		this.addMouseMotionListener(this);
//		System.out.println("ggggn: " + graph);
	}

	public PaintPanel() {
//		System.out.println("soaos");
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	private static JSONObject createJSON() {
		JSONObject jsonData = new JSONObject();

//		jsonData.put("userName", userName);
		jsonData.put("tool", tool);
		jsonData.put("type", "draw");
		jsonData.put("RGB", RGB);
		jsonData.put("x_start", x_start);
		jsonData.put("y_start", y_start);
		jsonData.put("x_end", x_end);
		jsonData.put("y_end", y_end);
		jsonData.put("text", text);
		return jsonData;
	}

	private static JSONObject parseResString(String res) {
		JSONObject resJSON = null;
		try {
			JSONParser parser = new JSONParser();
			resJSON = (JSONObject) parser.parse(res);
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("Exception: " + e);
		}
		return resJSON;
	}

//	public static void draw(String paintDataList) {
//		System.out.println("dddd");
//		String[] list = paintDataList.split("-");
//		for (int i = 0; i < list.length; i++) {
//			JSONObject paintJson = parseResString(list[i]);
//			x_start = (int) (long) paintJson.get("x_start");
//			y_start = (int) (long) paintJson.get("y_start");
//			x_end = (int) (long) paintJson.get("x_end");
//			y_end = (int) (long) paintJson.get("y_end");
//			RGB = (String) paintJson.get("RGB");
//			String[] RGBList = RGB.split(" ");
//			graph.setColor(new Color(Integer.parseInt(RGBList[0]), Integer.parseInt(RGBList[1]),
//					Integer.parseInt(RGBList[2])));
//			if (paintJson.get("tool").equals("Line")) {
//				graph.setStroke(new BasicStroke(1));
//				graph.drawLine(x_start, y_start, x_end, y_end);
////					panel.repaint();
//			} else if (paintJson.get("tool").equals("Circle")) {
//				int diameter = Math.min(Math.abs(x_start - x_end), Math.abs(y_start - y_end));
//				graph.drawOval(Math.min(x_start, x_end), Math.min(y_start, y_end), diameter, diameter);
//			} else if (paintJson.get("tool").equals("Rect")) {
//				graph.drawRect(Math.min(x_start, x_end), Math.min(y_start, y_end), Math.abs(x_start - x_end),
//						Math.abs(y_start - y_end));
//			} else if (paintJson.get("tool").equals("Text")) {
//				text = (String) paintJson.get("text");
////				System.out.println(text);
//				graph.drawString(text, x_end, y_end);
//			}
//		}
//	}
	
	public void draw(String dataList) {
	
		String[] list = dataList.split("-");
		for (int i = 0; i < list.length; i++) {
			JSONObject paintJson = parseResString(list[i]);
			x_start = (int) (long) paintJson.get("x_start");
			y_start = (int) (long) paintJson.get("y_start");
			x_end = (int) (long) paintJson.get("x_end");
			y_end = (int) (long) paintJson.get("y_end");
			RGB = (String) paintJson.get("RGB");
			paintData = createJSON();
			System.out.println("draw: "+paintData);
			paintDataList.add(paintJson);	
			System.out.println("drawList: "+paintDataList);
//			String[] RGBList = RGB.split(" ");
//			graph.setColor(new Color(Integer.parseInt(RGBList[0]), Integer.parseInt(RGBList[1]),
//					Integer.parseInt(RGBList[2])));
//			if (paintJson.get("tool").equals("Line")) {
//				graph.setStroke(new BasicStroke(1));
//				graph.drawLine(x_start, y_start, x_end, y_end);
////					panel.repaint();
//			} else if (paintJson.get("tool").equals("Circle")) {
//				int diameter = Math.min(Math.abs(x_start - x_end), Math.abs(y_start - y_end));
//				graph.drawOval(Math.min(x_start, x_end), Math.min(y_start, y_end), diameter, diameter);
//			} else if (paintJson.get("tool").equals("Rect")) {
//				graph.drawRect(Math.min(x_start, x_end), Math.min(y_start, y_end), Math.abs(x_start - x_end),
//						Math.abs(y_start - y_end));
//			} else if (paintJson.get("tool").equals("Text")) {
//				text = (String) paintJson.get("text");
////				System.out.println(text);
//				graph.drawString(text, x_end, y_end);
//			}
		}
//		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
//		System.out.println("mmmmm");
		x_start = e.getX();
		y_start = e.getY();
//		graph.setColor(color);
//		if (!graph.getColor().equals(color)) {
//			graph.setColor(color);
//		}
//		graph.setColor(color);
//		System.out.println(x_start);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

		System.out.println("tool:" + tool);
		x_end = e.getX();
		y_end = e.getY();
		RGB = color.getRed() + " " + color.getGreen() + " " + color.getBlue();
		switch (tool) {
		case "Line":
//			graph.drawLine(x_start, y_start, x_end, y_end);
			paintData = createJSON();
			paintDataList.add(paintData);
//			System.out.println(paintDataList);
			Client.fetchData(paintDataList);
//			try {
//						
//			}catch{
//						
//			}
			break;
		case "Circle":
			int diameter = Math.min(Math.abs(x_start - x_end), Math.abs(y_start - y_end));
//			graph.drawOval(Math.min(x_start, x_end), Math.min(y_start, y_end), diameter, diameter);
			paintData = createJSON();
			paintDataList.add(paintData);
			Client.fetchData(paintDataList);
			break;
		case "Text":
			text = JOptionPane.showInputDialog("Input text");
			if (text != null) {
//				graph.drawString(text, x_end, y_end);
				paintData = createJSON();
				paintDataList.add(paintData);
				Client.fetchData(paintDataList);
			}
			break;
		case "Rect":
//			graph.drawRect(Math.min(x_start, x_end), Math.min(y_start, y_end), Math.abs(x_start - x_end), Math.abs(y_start - y_end));
			paintData = createJSON();
			paintDataList.add(paintData);
			Client.fetchData(paintDataList);
			break;
		}
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	public static void convertData() {
		
	}
}
