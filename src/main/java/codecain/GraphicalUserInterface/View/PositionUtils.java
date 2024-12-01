package codecain.GraphicalUserInterface.View;

import codecain.BackendCode.Model.UMLClassInfo;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class PositionUtils {
	public static void calculateAndSetPosition(ClassNode classNode, UMLClassInfo classInfo, Pane container) {
		double x = 0, y = 0;
		double padding = 20;
		double nodeWidth;
		if (classNode.getPrefWidth() > 0) {
			nodeWidth = classNode.getPrefWidth();
		} else {
			nodeWidth = 200;
		}

		double nodeHeight;
		if (classNode.getPrefHeight() > 0) {
			nodeHeight = classNode.getPrefHeight();
		} else {
			nodeHeight = 300;
		}

		double containerWidth;
		if (container.getWidth() > 0) {
			containerWidth = container.getWidth();
		} else {
			containerWidth = 800;
		}

		boolean positionFound = false;
		while (!positionFound) {
			positionFound = true;
			for (Node node : container.getChildren()) {
				if (node instanceof ClassNode) {
					double otherX = node.getLayoutX();
					double otherY = node.getLayoutY();
					if (Math.abs(x - otherX) < nodeWidth + padding && Math.abs(y - otherY) < nodeHeight + padding) {
						positionFound = false;
						x += nodeWidth + padding;
						if (x + nodeWidth > containerWidth) {
							x = 0;
							y += nodeHeight + padding;
						}
						break;
					}
				}
			}
		}

		classNode.setLayoutX(x);
		classNode.setLayoutY(y);
		classInfo.setX((int) x);
		classInfo.setY((int) y);
	}
}
