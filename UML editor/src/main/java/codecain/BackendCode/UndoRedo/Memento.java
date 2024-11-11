package codecain.BackendCode.UndoRedo;

public class Memento {
        private String backup;
        private UndoRedoEditor editor;

        public Memento(UndoRedoEditor editor) {
            this.editor = editor;
            this.backup = editor.backup();
        }

        public void restore() {
            editor.restore(backup);
        }
}
