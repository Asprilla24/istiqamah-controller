package com.team.istiqomah.istiqomahcontroller.core;

import android.app.Application;

import com.team.istiqomah.istiqomahcontroller.helper.SessionManagement;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CoreApplication extends Application {
    SessionManagement session;
    public static CoreApplication app;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        /*Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());*/

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("IstiqomahController.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        session = new SessionManagement(this);
        app = this;
    }

    public static CoreApplication getInstance() {
        return app;
    }

    public SessionManagement getSession() {
        return session;
    }

    /*public void showQuitConfirmation(Context konteks) {
        AlertDialog.Builder kotakBuilder = new AlertDialog.Builder(konteks);
        kotakBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        kotakBuilder.setTitle("Quit Confirmation");
        kotakBuilder.setMessage("Do you want to quit ?");
        kotakBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        closeApplication();
                    }
                });
        kotakBuilder.setNegativeButton("no", null);
        kotakBuilder.create().show();
    }

    public void closeApplication() {
        Process.killProcess(Process.myPid());
    }*/
}
