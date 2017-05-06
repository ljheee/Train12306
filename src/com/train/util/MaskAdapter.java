package com.train.util;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
/**
 * 查询车次loading
 * @author lijintao
 *
 */
public class MaskAdapter extends JPanel {
	private static final long serialVersionUID = -918321956883484537L;
	private JLayeredPane layeredPane;
	private MaskPanel maskPanel = new MaskPanel();
	private JComponent component;
	private boolean isMask = false;
	public MaskAdapter(final JComponent component, boolean isMask) {
		this.component = component;
		initGUI();
		installListener();
		this.setMask(isMask);
	}

	public static MaskAdapter getMaskpanel(final JComponent component, boolean isMask) {
		MaskAdapter adapter = new MaskAdapter(component, isMask);
		return adapter;
	}

	private void installListener() {
		maskPanel.addMouseListener(new MouseAdapter() {
		});
		maskPanel.addKeyListener(new KeyAdapter() {
		});
		maskPanel.addMouseMotionListener(new MouseMotionAdapter() {
		});
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				setLayerSize(MaskAdapter.this.getSize());
			}
		});
	}

	private void initGUI() {
		layeredPane = new JLayeredPane();
		layeredPane.add(component, new Integer(1));
		this.setLayout(new BorderLayout());
		this.add(layeredPane);
	}

	private void setLayerSize(Dimension2D dim) {
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		component.setBounds(0, 0, width, height);
		maskPanel.setBounds(0, 0, width, height);
	}

	public void setMask(boolean isMask) {
		this.isMask = isMask;
		if (isMask) {
			layeredPane.add(maskPanel, new Integer(2000));
			maskPanel.requestFocusInWindow();
		} else {
			layeredPane.remove(maskPanel);
		}
		layeredPane.validate();
		layeredPane.repaint();
	}

	public boolean isMask() {
		return isMask;
	}

	public MaskPanel getMask() {
		return maskPanel;
	}
	class MaskPanel extends JComponent {
		private static final long serialVersionUID = -7271327638956903617L;
		//间隔色
		public static final int STYLE_ALTERNATION = 1;
		//填充
		public static final int STYLE_FILL = 2;

		//进度
		public static final int STYLE_PROCESS = 3;
		private int blockStyle = STYLE_ALTERNATION;

		private String text = "loading";
		private Font textFond = new Font("dialog", Font.PLAIN, 12);
		private Color textColor = Color.GREEN.darker();

		private Color blockColor = Color.GREEN.darker();

		private int borderWidth = 300;
		private int blockWidth = 40;
		private Color borderColor = Color.RED;
		private int borderHeight = 20;

		// if processValue != -Double.MAX_VALUE, will draw the process.
		private double processValue = -Double.MAX_VALUE;

		private double processStep = 20;

		private double processTempValue = 0;
		private boolean processFlag = true;

		private int timeDelay = 100;

		private Color maskColor = new Color(255, 255, 255);

		private Timer timer = new Timer(timeDelay, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setProcessTempValue();
			}
		});
		private CellRendererPane cellRendererPane = new CellRendererPane();

		public MaskPanel() {
			this.addAncestorListener(new AncestorListener() {
				public void ancestorAdded(AncestorEvent event) {
					if (processValue != -Double.MIN_VALUE) {
						processTempValue = 0;
						processFlag = true;
						timer.start();
					}
				}

				public void ancestorMoved(AncestorEvent event) {
				}

				public void ancestorRemoved(AncestorEvent event) {
					if (timer.isRunning()) {
						timer.stop();
					}
				}
			});
			this.setFocusTraversalKeysEnabled(false);
		}

		public void setTimeDelay(int delay) {
			this.timeDelay = delay;
			timer.setDelay(delay);
		}

		private void setProcessTempValue() {
			Dimension size = layeredPane.getSize();
			double processActualwidth = borderWidth;
			if (borderWidth > size.width) {
				processActualwidth = size.width - 2;
			}
			double endPostion = processActualwidth - blockWidth;
			if (processFlag) {
				processTempValue += processStep;
			} else {
				processTempValue -= processStep;
			}
			if (processTempValue > endPostion || processTempValue < 0) {
				processFlag = !processFlag;
			}
			if (processTempValue > endPostion) {
				processTempValue = endPostion;
			}
			if (processTempValue < 0) {
				processTempValue = 0;
			}
			Rectangle rect = new Rectangle(0, 0, size.width, size.height);
			int x = (int) rect.getCenterX();
			int y = (int) rect.getCenterY();
			this.repaint((int) (x - processActualwidth / 2 - 1), y - 1, borderWidth + 2, borderHeight + 2);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Dimension size = layeredPane.getSize();
			double processActualwidth = borderWidth;
			if (borderWidth > size.width) {
				processActualwidth = size.width - 2;
			}
			Graphics2D g2d = (Graphics2D) g;
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
			g2d.setComposite(composite);

			//fill the whole mask panel
			g2d.setColor(maskColor);
			Rectangle rect = new Rectangle(0, 0, size.width, size.height);
			g2d.fill(rect);

			//draw the label
			g2d.setColor(borderColor);
			int x = (int) rect.getCenterX();
			int y = (int) rect.getCenterY();
			JLabel label = getLabel(text.toString(), textFond, textColor);
			Dimension textSize = label.getPreferredSize();
			paintComponent(g2d, label, x - textSize.width / 2, y - textSize.height, textSize.width, textSize.height);

			//draw the border
			Rectangle2D.Double border = new Rectangle2D.Double(x - processActualwidth / 2, y, processActualwidth, borderHeight);
			g2d.setColor(borderColor);
			g2d.draw(border);

			//draw block
			if (processValue != -Double.MAX_VALUE) {
				GradientPaint paint = new GradientPaint((float) (x), (float) y, blockColor, (float) x, (float) (y + borderHeight / 2), Color.WHITE, true);
				g2d.setPaint(paint);
				Rectangle2D.Double fill = new Rectangle2D.Double(x - border.width / 2, y, processValue / 100 * borderWidth, borderHeight);
				g2d.fill(fill);
				g2d.setColor(blockColor.darker());
				g2d.draw(fill);

				label = getLabel(processValue / 100.0 + "%", textFond, textColor);
				textSize = label.getPreferredSize();
				paintComponent(g2d, label, x - textSize.width / 2, y, textSize.width, textSize.height);
			} else {
				paintBlock(g2d, processTempValue + x - border.width / 2, y, blockWidth, borderHeight);
			}
		}

		private void paintBlock(Graphics2D g, double x, double y, double width, double height) {
			Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
			GradientPaint paint = new GradientPaint((float) (x), (float) y, blockColor, (float) x, (float) (y + height / 2), Color.WHITE, true);
			g.setPaint(paint);
			if (blockStyle == STYLE_ALTERNATION) {
				int count = 5;
				double blockW = (width / (count * 2 - 1));
				for (int i = 0; i < count; i++) {
					rect = new Rectangle2D.Double(x + i * 2 * blockW, y, blockW, height);
					g.fill(rect);
				}
			} else if (blockStyle == STYLE_FILL) {
				g.fill(rect);
			}
			rect = new Rectangle2D.Double(x, y, width, height);
			g.setColor(blockColor.darker());
			g.draw(rect);
		}

		private JLabel getLabel(String text, Font font, Color color) {
			JLabel label = new JLabel();
			label.setBorder(null);
			label.setForeground(color);
			label.setFont(font);
			label.setText(text);
			return label;
		}

		private void paintComponent(Graphics2D g, Component c, int x, int y, int w, int h) {
			g.setStroke(new BasicStroke(1));
			cellRendererPane.paintComponent(g, c, this, x, y, w, h, true);
		}

		public int getBlockStyle() {
			return blockStyle;
		}

		public void setBlockStyle(int blockStyle) {
			this.blockStyle = blockStyle;
			this.repaint();
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
			this.repaint();
		}

		public Font getTextFond() {
			return textFond;
		}

		public void setTextFond(Font textFond) {
			this.textFond = textFond;
			this.repaint();
		}

		public Color getTextColor() {
			return textColor;
		}

		public void setTextColor(Color textColor) {
			this.textColor = textColor;
			this.repaint();
		}

		public Color getBlockColor() {
			return blockColor;
		}

		public void setBlockColor(Color blockColor) {
			this.blockColor = blockColor;
			this.repaint();
		}

		public int getBorderWidth() {
			return borderWidth;
		}

		public void setBorderWidth(int borderWidth) {
			this.borderWidth = borderWidth;
			this.repaint();
		}

		public int getBlockWidth() {
			return blockWidth;
		}

		public void setBlockWidth(int blockWidth) {
			this.blockWidth = blockWidth;
			this.repaint();
		}

		public Color getBorderColor() {
			return borderColor;
		}

		public void setBorderColor(Color borderColor) {
			this.borderColor = borderColor;
			this.repaint();
		}

		public int getBorderHeight() {
			return borderHeight;
		}

		public void setBorderHeight(int borderHeight) {
			this.borderHeight = borderHeight;
			this.repaint();
		}

		public double getProcessValue() {
			return processValue;
		}

		public void setProcessValue(double processValue) {
			this.processValue = processValue;
			if (processValue == -Double.MAX_VALUE) {
				timer.start();
			} else {
				timer.stop();
			}
			this.repaint();
		}

		public double getProcessStep() {
			return processStep;
		}

		public void setProcessStep(double processStep) {
			this.processStep = processStep;
			this.repaint();
		}

		public Color getMaskColor() {
			return maskColor;
		}

		public void setMaskColor(Color maskColor) {
			this.maskColor = maskColor;
			this.repaint();
		}

		public int getTimeDelay() {
			return timeDelay;
		}
	}

}