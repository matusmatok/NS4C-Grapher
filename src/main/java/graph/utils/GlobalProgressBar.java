package graph.utils;

import lombok.Getter;
import lombok.Setter;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;

public class GlobalProgressBar {
    @Getter
    private final static int NO_LADDERS_AT_TD = 2277;
    @Getter
    private final static int TRACKING_DEPTH = 28;
    @Getter @Setter
    private static boolean enabled = false;

    public static void setEnabled(final boolean value) {
        enabled = value;
    }

    private static ProgressBar progressBar;

    public static void startProgressBar() {
        if (enabled) {
            progressBar = new ProgressBarBuilder()
                    .setInitialMax(NO_LADDERS_AT_TD)
                    .setTaskName("GENERATION ")
                    .build();
        }
    }

    public static void step() {
        if (enabled) {
            progressBar.step();
        }
    }

    public static void terminate() {
        if (enabled) {
            progressBar.close();
        }
    }
}
