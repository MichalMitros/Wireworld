import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Board extends Canvas {

    // STAN - KOLOR - OPIS
    // 0 - SZARY     - NIC
    // 1 - NIEBISEKI - GLOWA ELEKTRONY
    // 2 - CZERWONY  - OGON ELEKTRONU
    // 3 - ZOLTY     - PRZEWODNIK

    private GraphicsContext g2D;
    static final int size = 30;
    private int[][] board;


    public Board() {
        super(600, 600);
        board = new int[size][size];
        g2D = getGraphicsContext2D();
        drawBoard();
    }

    public Board(int[][] board) {
        super(600, 600);
        this.board = board;
        g2D = getGraphicsContext2D();
        drawBoard();
    }

    public void drawBoard() {
        double s = getWidth()/size;
        g2D.setFill(Color.gray(0.5));
        g2D.fillRect(0, 0, getWidth(), getHeight());
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                if(board[i][j] == 0) {
                    g2D.setFill(Color.gray(0.2));
                } else if(board[i][j] == 3) {
                    g2D.setFill(Color.color(1, 1, 0));
                } else if(board[i][j] == 1) {
                    g2D.setFill(Color.color(0, 0, 1));
                } else if(board[i][j] == 2) {
                    g2D.setFill(Color.color(1, 0, 0));
                }

                if(board[i][j] == 0) {
                    //g2D.fillRoundRect(i*s, j*s, s-1, s-1, 15, 15);
                    g2D.fillOval(i*s + s/4, j*s + s/4, (s-1)/2, (s-1)/2);
                } else {
                    g2D.fillRect(i*s, j*s, s-1, s-1);
                }
            }
        }
    }

    public void nextGeneration() {
        int[][] newBoard = new int[size][size];
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                switch(board[i][j]) {
                    case 0:
                        newBoard[i][j] = 0;
                        break;
                    case 1:
                        newBoard[i][j] = 2;
                        break;
                    case 2:
                        newBoard[i][j] = 3;
                        break;
                    case 3:
                        int n = numberOfBlueNeighbors(i, j);
                        if(n == 1 || n == 2) {
                            newBoard[i][j] = 1;
                        } else {
                            newBoard[i][j] = 3;
                        }
                        break;
                }
            }
        }
        board = newBoard;
    }

    public void clear() {
        board = new int[size][size];
    }

    public void applyClick(int mouseX, int mouseY, boolean isDeleting) {
        int x = mouseX/((int)getWidth()/size);
        int y = mouseY/((int)getWidth()/size);

        if(x >= size) {
            x = size-1;
        }
        if(y >= size) {
            y = size-1;
        }

        if(x < 0) {
            x = 0;
        }
        if(y < 0) {
            y = 0;
        }

        if(!isDeleting) {
            if(board[x][y] == 3) {
                if(hasBlueNeumanNeighbour(x, y)) {
                    board[x][y] = 2;
                } else {
                    board[x][y] = 1;
                }
            } else {
                board[x][y] = 3;
            }
        } else {
            board[x][y] = 0;
        }
    }

    public void applyDrag(int mouseX, int mouseY, boolean isDeleting) {
        int x = mouseX/((int)getWidth()/size);
        int y = mouseY/((int)getWidth()/size);

        if(x >= size) {
            x = size-1;
        }
        if(y >= size) {
            y = size-1;
        }

        if(x < 0) {
            x = 0;
        }
        if(y < 0) {
            y = 0;
        }

        if(!isDeleting) {
            if(board[x][y] == 0) {
                board[x][y] = 3;
            }
        } else {
            board[x][y] = 0;
        }
    }

    private boolean hasBlueNeumanNeighbour(int x, int y) {
        if(board[x-1 < 0 ? size-1 : x-1][y] == 1) {
            return true;
        }
        if(board[x+1 == size ? 0 : x+1][y] == 1) {
            return true;
        }
        if(board[x][y+1 == size ? 0 : y+1] == 1) {
            return true;
        }
        if(board[x][y-1 < 0 ? size-1 : y-1] == 1) {
            return true;
        }
        return false;
    }

    private int numberOfBlueNeighbors(int x, int y) {
        int n = 0;

        // x-1:
        if(board[x-1 < 0 ? size-1 : x-1][y-1 < 0 ? size-1 : y-1] == 1) {
            n++;
        }
        if(board[x-1 < 0 ? size-1 : x-1][y] == 1) {
            n++;
        }
        if(board[x-1 < 0 ? size-1 : x-1][y+1 == size ? 0 : y+1] == 1) {
            n++;
        }
        // x:
        if(board[x][y+1 == size ? 0 : y+1] == 1) {
            n++;
        }
        if(board[x][y-1 < 0 ? size-1 : y-1] == 1) {
            n++;
        }
        // x+1:
        if(board[x+1 == size ? 0 : x+1][y-1 < 0 ? size-1 : y-1] == 1) {
            n++;
        }
        if(board[x+1 == size ? 0 : x+1][y] == 1) {
            n++;
        }
        if(board[x+1 == size ? 0 : x+1][y+1 == size ? 0 : y+1] == 1) {
            n++;
        }

        return n;
    }

    public int[][] getValuesAsArray() {
        return board;
    }

    public void applyNewBoard(int[][] board) {
        this.board = board;
    }
}
