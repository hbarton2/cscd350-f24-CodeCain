package codecain.GraphicalUserInterface.Model;

import java.util.HashMap;
import java.util.Map;

import codecain.GraphicalUserInterface.Controller.Controller;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class ShortcutManager {

	private final Scene scene;
	private final Controller controller;
	private final Map<KeyCombination, Runnable> shortcuts = new HashMap<>();

	private static final KeyCode ADD_CLASS_KEY = KeyCode.N;
	private static final KeyCode DELETE_CLASS_KEY = KeyCode.DELETE;
	private static final KeyCode DELETE_CLASS_KEY_ALT = KeyCode.BACK_SPACE;
	private static final KeyCode RENAME_CLASS_KEY = KeyCode.R;

	public ShortcutManager(Scene scene, Controller controller) {
		this.scene = scene;
		this.controller = controller;
	}

	public void initializeShortcuts() {
		// Add shortcut for adding a class
		addShortcut(getPlatformSpecificShortcut(ADD_CLASS_KEY), controller::addClassBtn);

		// Add shortcut for rename a class
		addShortcut(getPlatformSpecificShortcut(RENAME_CLASS_KEY), controller::renameClassBtn);

		// Initialize delete shortcut
		initializeDeleteShortcut();

		// Dynamically listen to all shortcuts
		scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
			for (Map.Entry<KeyCombination, Runnable> entry : shortcuts.entrySet()) {
				if (entry.getKey().match(event)) {
					entry.getValue().run();
					event.consume();
					break;
				}
			}
		});
	}

	public void addShortcut(KeyCombination keyCombination, Runnable action) {
		// shortcuts.put(keyCombination, action);

		shortcuts.put(keyCombination, () -> {
			System.out.println("Shortcut triggered: " + keyCombination);
			action.run();
		});
	}

	private KeyCombination getPlatformSpecificShortcut(KeyCode keyCode) {
		String os = System.getProperty("os.name").toLowerCase();
		return os.contains("mac")
				? new KeyCodeCombination(keyCode, KeyCombination.META_DOWN) // Command + key for macOS
				: new KeyCodeCombination(keyCode, KeyCombination.CONTROL_DOWN); // Control + key for Windows and Linux
	}

	private void initializeDeleteShortcut() {
		String os = System.getProperty("os.name").toLowerCase();
		KeyCombination deleteShortcut = os.contains("mac")
				? new KeyCodeCombination(DELETE_CLASS_KEY_ALT, KeyCombination.META_DOWN) // Command + Backspace for macOS
				: new KeyCodeCombination(DELETE_CLASS_KEY, KeyCombination.CONTROL_DOWN); // Control + Delete for Windows and Linux

		addShortcut(deleteShortcut, controller::deleteClassBtn);
	}

}
