package com.terpomo.wavy.ui.util;

import java.awt.*;

public class AbsoluteLayout extends FlowLayout {

	private static final long serialVersionUID = 2006851486527705179L;
	
	@Override
	public void layoutContainer(Container target) {
		int nmembers = target.getComponentCount();
		for (int i = 0 ; i < nmembers ; i++) {
            Component m = target.getComponent(i);
            if (m.isVisible()) {
                Dimension d = m.getPreferredSize();
                m.setSize(d.width, d.height);
            }
		}
	}

}
