package org.hogel.android.narouviewer.app;

import android.app.Application;
import org.hogel.android.narouviewer.app.module.NarouViewerModule;
import roboguice.RoboGuice;

public class NarouViewerApplication extends Application {
    @Override
    public void onCreate() {
        RoboGuice.setBaseApplicationInjector(
                this,
                RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(this),
                new NarouViewerModule()
        );
    }
}
