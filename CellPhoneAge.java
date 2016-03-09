package com.billbachao.cellphoneage;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CellPhoneAge extends AppCompatActivity {

    // Gingerbread release date December 6, 2010 in ms;
    private static final long sGINGER_RELEASE_DATE = 1291573800000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_phone_age);
        initView();
    }

    private void initView() {
        TextView phoneAge = (TextView) findViewById(R.id.phone_age);
        String age = getOldestAppsAge(this);
    }

    // @return oldest app's installed date.
    private static String getOldestAppsAge(Context context) {
        long appsAge = 0;
        try {
            appsAge = System.currentTimeMillis();
            PackageManager pkgManager = context.getPackageManager();
            List<ApplicationInfo> lsApp = pkgManager.getInstalledApplications(0);
            for (ApplicationInfo localApplicationInfo : lsApp) {
                String pkgName = localApplicationInfo.packageName;
                if ((localApplicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    PackageInfo info = pkgManager.getPackageInfo(pkgName, 0);
                    long firstInstallTime = info.firstInstallTime;
                    if (firstInstallTime < appsAge && firstInstallTime > sGINGER_RELEASE_DATE) {
                        appsAge = info.firstInstallTime;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException ex) {
            ex.printStackTrace();
        }
        return convertIntoDesiredDateFormat(appsAge);
    }

    public static String convertIntoDesiredDateFormat(long milliSeconds) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
