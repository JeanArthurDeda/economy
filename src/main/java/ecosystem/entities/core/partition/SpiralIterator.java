package ecosystem.entities.core.partition;

import core.MathExt;

public class SpiralIterator {
    public static abstract class Receiver {
        public void onStart (int startX, int startY) {}
        public boolean onStartSquare (int x, int y, int startX, int startY) {return true;}
        public boolean onValidValue (int x, int y, int startX, int startY) {return true;}
    }

    public boolean iterate (int x, int y, int width, int height, Receiver receiver){
        int totalNumSamples = width * height;
        int numSamples = 0;
        int spiralOffset = 1;
        boolean valid;
        int cx = x;
        int cy = y;
        receiver.onStart(cx, cy);

        if (!receiver.onValidValue(cx, cy, x, y))
            return false;
        numSamples ++;

        while (true) {
            if (!receiver.onStartSquare(x - spiralOffset, y - spiralOffset, x, y))
                return false;

            // ===================
            // up left to up right
            // ===================
            int x1 = x - spiralOffset;
            int x2 = x + spiralOffset;
            cy = y - spiralOffset;
            valid = cy >= 0 && cy < height;
            if (valid) {
                x1 = MathExt.clamp(x1, 0, width - 1);
                x2 = MathExt.clamp(x2, 0, width - 1);
                for (cx = x1; cx <= x2; ++cx) {
                    if (!receiver.onValidValue(cx, cy, x, y))
                        return false;
                    numSamples++;
                    if (numSamples == totalNumSamples)
                        return true;
                }
            }

            // ======================
            // up right to down right
            // ======================
            int y1 = y - spiralOffset + 1;
            int y2 = y + spiralOffset;
            cx = x + spiralOffset;
            valid = cx >= 0 && cx < width;
            if (valid) {
                y1 = MathExt.clamp(y1, 0, height - 1);
                y2 = MathExt.clamp(y2, 0, height - 1);
                for (cy = y1; cy <= y2; ++cy) {
                    if (!receiver.onValidValue(cx, cy, x, y))
                        return false;
                    numSamples++;
                    if (numSamples == totalNumSamples)
                        return true;
                }
            }

            // =======================
            // down right to down left
            // =======================
            x1 = x + spiralOffset - 1;
            x2 = x - spiralOffset;
            cy = y + spiralOffset;
            valid = cy >= 0 && cy < height;
            if (valid) {
                x1 = MathExt.clamp(x1, 0, width - 1);
                x2 = MathExt.clamp(x2, 0, width - 1);
                for (cx = x1; cx >= x2; --cx) {
                    if (!receiver.onValidValue(cx, cy, x, y))
                        return false;
                    numSamples++;
                    if (numSamples == totalNumSamples)
                        return true;
                }
            }

            // ====================
            // down left to up left
            // ====================
            y1 = y + spiralOffset - 1;
            y2 = y - spiralOffset + 1;
            cx = x - spiralOffset;
            valid = cx >= 0 && cx < width;
            if (valid) {
                y1 = MathExt.clamp(y1, 0, height - 1);
                y2 = MathExt.clamp(y2, 0, height - 1);
                for (cy = y1; cy >= y2; --cy) {
                    if (!receiver.onValidValue(cx, cy, x, y))
                        return false;
                    numSamples++;
                    if (numSamples == totalNumSamples)
                        return true;
                }
            }
            spiralOffset ++;
        }
    }

}
