import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class BoardSaver {

    public static void saveToFile(Stage parentStage, Board board) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        File file = fileChooser.showSaveDialog(parentStage);
        if(file != null) {
            int[][] data = board.getValuesAsArray();
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
            } catch (FileNotFoundException e) {
                return;
            } catch (UnsupportedEncodingException e) {
                return;
            }

            for(int i=0; i<Board.size; i++) {
                for(int j=0; j<Board.size; j++) {
                    writer.printf("%d\n", data[i][j]);
                }
            }

            writer.close();
        }
    }
}
