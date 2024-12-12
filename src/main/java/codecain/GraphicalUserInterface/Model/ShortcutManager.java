package codecain.GraphicalUserInterface.Model;

import codecain.GraphicalUserInterface.Controller.Controller;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class ShortcutManager {

	private final Scene scene;
	private final Controller controller;

	public ShortcutManager(Scene scene, Controller controller) {
		this.scene = scene;
		this.controller = controller;
	}

	public void initializeShortcuts() {
		// Add shortcut for adding a class
		addShortcut(getPlatformSpecificShortcut(KeyCode.N), controller::addClassBtn);

		// Add shortcut for deleting a class
		addShortcut(getPlatformSpecificShortcut(KeyCode.D), controller::deleteClassBtn);
		
	}

	public void addShortcut(KeyCombination keyCombination, Runnable action) {
		scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
			if (keyCombination.match(event)) {
				action.run();
				event.consume();
			}
		});
	}

	private KeyCombination getPlatformSpecificShortcut(KeyCode keyCode) {
		String os = System.getProperty("os.name").toLowerCase();
		return os.contains("mac") 
			? new KeyCodeCombination(keyCode, KeyCombination.META_DOWN) // Command + key for macOS
			: new KeyCodeCombination(keyCode, KeyCombination.CONTROL_DOWN); // Control + key for Windows and Linux
	}
	
}
