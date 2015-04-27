package crave.db;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.SwingConstants;

public class OutputTuple extends JButton {

	private List<String> labels_;
	
	public OutputTuple(int numLabels)
	{
		this.labels_ = new ArrayList<String>(numLabels);
		int xDimension = 0;
	}
	
	public List<String> getLabels() { return this.labels_; }
	
	private void setLabels(List<String> l) { this.labels_ = l; }
	
	public boolean addLabel(String s)
	{
		this.getLabels().add(s);
		return true;
	}
	
	public boolean finalizeTuple(boolean clickable)
	{
		StringBuilder b = new StringBuilder();
		int xDimension = 0, yDimension = 25;
		for(String s : this.getLabels())
		{
			b.append(s);
			xDimension += s.length();
		}
		
		super.setHorizontalAlignment(SwingConstants.LEFT);
		super.setBackground(Color.WHITE);
		super.setBorderPainted(false);
		super.setText(b.toString());
		super.setPreferredSize(new Dimension(700, yDimension));
		super.setMaximumSize(new Dimension(700, yDimension));
		super.setMinimumSize(new Dimension(700, yDimension));
		super.setEnabled(clickable);
		return true;
	}
	
}
