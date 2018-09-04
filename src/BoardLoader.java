import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class BoardLoader {

    public static int[][] loadFromFile(Stage parentStage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        File file = fileChooser.showOpenDialog(parentStage);

        int[][] board = new int[Board.size][Board.size];

        if(file != null) {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()));

            for(int i=0; i<Board.size; i++) {
                for(int j=0; j<Board.size; j++) {
                    board[i][j] = Integer.parseInt(bufferedReader.readLine());
                }
            }
        }

        return board;
    }
}
