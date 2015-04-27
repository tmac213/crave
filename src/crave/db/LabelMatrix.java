package crave.db;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class LabelMatrix {
	
	private int rows_, cols_;
	
	private HashMap<Integer, JLabel[]> matrix;
	
	public LabelMatrix(int rows, int cols)
	{
		this.rows_ = rows;
		this.cols_ = cols;
		this.matrix = new HashMap<Integer, JLabel[]>(rows);
	}
	
	public LabelMatrix(int cols)
	{
		this.rows_ = 0;
		this.cols_ = cols;
		this.matrix = new HashMap<Integer, JLabel[]>(20);
	}
	
	public int getRows() { return this.rows_; }
	
	private void setRows(int rows) { this.rows_ = rows; }
	
	public int getCols() { return this.cols_; }
	
	private void setCols(int n) { this.cols_ = n; }
	
	public HashMap<Integer, JLabel[]> getMatrix() { return this.matrix; }
	
	private void setMatrix(HashMap<Integer, JLabel[]> m) { this.matrix = m; }
	
	public boolean add(int row, int col, JLabel l)
	{
		if(this.getMatrix().get(row) == null)
		{
			this.getMatrix().put(row, new JLabel[this.getCols()]);
		}
		this.getMatrix().get(row)[col] = l;
		
		return true;
	}
	
	public JPanel addToTextPane(String[] categories)
	{
		JPanel panel = new JPanel(new GridLayout(this.getRows(), this.getCols()));
		
		System.out.println("MATRIX:\n");
		this.print();
		
		GridBagConstraints bag = new GridBagConstraints();
		
		for(int i = 0; i < this.getCols(); i++)
		{	
			bag.gridx = i;
			bag.gridy = 0;
			JLabel l = new JLabel(categories[i]);
			l.setPreferredSize(l.getSize());
			panel.add(l, bag);
			
			bag.gridy = 1;
			l = new JLabel("----------------------");
			l.setPreferredSize(l.getSize());
			panel.add(l, bag);
			
			for(int j = 2; j < this.getRows() + 2; j++)
			{
				bag.gridy = j;
				panel.add(this.getMatrix().get(j - 2)[i], bag);
			}
		}
		
		return panel;
	}
	
	public void print()
	{
		for(JLabel[] p : this.matrix.values())
		{
			for(int i = 0; i < p.length; i++)
			{
				System.out.print(p[i].getText());
				if(i < p.length - 1) { System.out.print("[,] "); }
			}
			System.out.print("\n");
		}
	}

}
