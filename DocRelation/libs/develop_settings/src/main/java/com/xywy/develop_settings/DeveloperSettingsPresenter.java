package com.xywy.develop_settings;

import android.support.annotation.NonNull;

public class DeveloperSettingsPresenter {

    @NonNull
    private final DeveloperSettingsModel developerSettingsModel;

    DeveloperSettingsActivity view;

    public DeveloperSettingsPresenter(@NonNull DeveloperSettingsModel developerSettingsModel) {
        this.developerSettingsModel = developerSettingsModel;
    }

    @NonNull
    private static String booleanToEnabledDisabled(boolean enabled) {
        return enabled ? "enabled" : "disabled";
    }

    public void bindView(@NonNull DeveloperSettingsActivity view) {

        view.changeBuildDate(developerSettingsModel.getBuildDate());
        view.changeBuildVersionCode(developerSettingsModel.getBuildVersionCode() + "");
        view.changeBuildVersionName(developerSettingsModel.getBuildVersionName());
        view.changeStethoState(developerSettingsModel.isStethoEnabled());
        view.changeLeakCanaryState(developerSettingsModel.isLeakCanaryEnabled());
        view.changeTinyDancerState(developerSettingsModel.isTinyDancerEnabled());
        view.changeBlockCanaryState(developerSettingsModel.isBlockCanaryEnabled());
        this.view = view;
    }

    public void changeStethoState(boolean enabled) {
        if (developerSettingsModel.isStethoEnabled() == enabled) {
            return; // no-op
        }


        boolean stethoWasEnabled = developerSettingsModel.isStethoEnabled();

        developerSettingsModel.changeStethoState(enabled);


        if (view != null) {
            view.showMessage("Stetho was " + booleanToEnabledDisabled(enabled));

            if (stethoWasEnabled) {
                view.showAppNeedsToBeRestarted();
            }
        }
    }

    public void changeLeakCanaryState(boolean enabled) {
        if (developerSettingsModel.isLeakCanaryEnabled() == enabled) {
            return; // no-op
        }

        developerSettingsModel.changeLeakCanaryState(enabled);

        if (view != null) {
            view.showMessage("LeakCanary was " + booleanToEnabledDisabled(enabled));
            view.showAppNeedsToBeRestarted(); // LeakCanary can not be enabled on demand (or it's possible?)
        }
    }

    public void changeBlockCanaryState(boolean enabled) {
        if (developerSettingsModel.isBlockCanaryEnabled() == enabled) {
            return; // no-op
        }

        developerSettingsModel.changeBlockCanaryState(enabled);

        if (view != null) {
            view.showMessage("BlockCanary was " + booleanToEnabledDisabled(enabled));
            view.showAppNeedsToBeRestarted(); // LeakCanary can not be enabled on demand (or it's possible?)
        }
    }

    public void changeTinyDancerState(boolean enabled) {
        if (developerSettingsModel.isTinyDancerEnabled() == enabled) {
            return; // no-op
        }

        developerSettingsModel.changeTinyDancerState(enabled);

        if (view != null) {
            view.showMessage("TinyDancer was " + booleanToEnabledDisabled(enabled));
        }
    }
}
