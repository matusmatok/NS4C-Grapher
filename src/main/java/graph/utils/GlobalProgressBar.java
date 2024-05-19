package graph.utils;

import lombok.Getter;
import lombok.Setter;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;

public class GlobalProgressBar {
    @Getter @Setter
    private static int MAX = 2277;
    @Getter
    private final static int TRACKING_DEPTH = 28;
    @Getter @Setter
    private static String TASK_NAME = "";
    @Getter
    private static boolean enabled = false;

    public static void setEnabled(final boolean value) {
        enabled = value;
    }

    private static ProgressBar progressBar;

    public static void startProgressBar() {

        if (enabled) {
            progressBar = new ProgressBarBuilder()
                    .setInitialMax(MAX)
                    .setTaskName(TASK_NAME)
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
