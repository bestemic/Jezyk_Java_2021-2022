import java.util.*;

class Graphics implements GraphicsInterface {

    class Position2D implements Position {

        private final int col;
        private final int row;

        public Position2D(int col, int row) {
            this.col = col;
            this.row = row;
        }

        @Override
        public int getRow() {
            return row;
        }

        @Override
        public int getCol() {
            return col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(col, row);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Position2D other = (Position2D) obj;
            return col == other.col && row == other.row;
        }
    }

    CanvasInterface canvas;
    List<Position> spr;
    Set<Position> to;

    private void draw(Position position, Color color) {
        try {
            canvas.setColor(position, color);
        } catch (CanvasInterface.BorderColorException e) {
            try {
                canvas.setColor(position, e.previousColor);
            } finally {
                spr.add(position);
                to.remove(position);
                return;
            }
        } catch (CanvasInterface.CanvasBorderException e) {
            spr.add(position);
            to.remove(position);
            return;
        }

        spr.add(position);
        to.remove(position);

        Position up = new Position2D(position.getCol() + 1, position.getRow());
        Position down = new Position2D(position.getCol() - 1, position.getRow());
        Position left = new Position2D(position.getCol(), position.getRow() - 1);
        Position right = new Position2D(position.getCol(), position.getRow() + 1);

        if(!spr.contains(up)) to.add(up);
        if(!spr.contains(down)) to.add(down);
        if(!spr.contains(left)) to.add(left);
        if(!spr.contains(right)) to.add(right);
    }

    @Override
    public void setCanvas(CanvasInterface canvas) {
        this.canvas = canvas;
    }

    @Override
    public void fillWithColor(Position startingPosition, Color color) throws WrongStartingPosition, NoCanvasException {
        spr = new ArrayList<>();
        to = new HashSet<>();

        if (canvas == null) {
            throw new NoCanvasException();
        }
        if (startingPosition == null) {
            throw new WrongStartingPosition();
        }

        try {
            canvas.setColor(startingPosition, color);
        } catch (CanvasInterface.CanvasBorderException e) {
            throw new WrongStartingPosition();
        } catch (CanvasInterface.BorderColorException e) {
            try {
                canvas.setColor(startingPosition, e.previousColor);
            } finally{
                throw new WrongStartingPosition();
            }
        }

        spr.add(startingPosition);
        to.add(new Position2D(startingPosition.getCol() + 1, startingPosition.getRow()));
        to.add(new Position2D(startingPosition.getCol() - 1, startingPosition.getRow()));
        to.add(new Position2D(startingPosition.getCol(), startingPosition.getRow() - 1));
        to.add(new Position2D(startingPosition.getCol(), startingPosition.getRow() + 1));

        while (to.size() != 0) {
            Position[] position = to.toArray(new Position[to.size()]);
            draw(position[0], color);
        }
    }
}