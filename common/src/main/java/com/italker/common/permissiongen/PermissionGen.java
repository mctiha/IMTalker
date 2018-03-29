package com.italker.common.permissiongen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.common.R;
import com.italker.common.permissiongen.annotation.PermissionFail;
import com.italker.common.permissiongen.annotation.PermissionSuccess;
import com.italker.common.permissiongen.internal.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static com.italker.common.permissiongen.internal.Utils.getActivity;


/**
 * Created by namee on 2015. 11. 17..
 */
public class PermissionGen {
    private String[] mPermissions;
    private int mRequestCode;
    private Object object;

    private PermissionGen(Object object) {
        this.object = object;
    }

    public static PermissionGen with(Activity activity) {
        return new PermissionGen(activity);
    }

    public static PermissionGen with(Fragment fragment) {
        return new PermissionGen(fragment);
    }

    public PermissionGen permissions(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    public PermissionGen addRequestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public void request() {
        requestPermissions(object, mRequestCode, mPermissions);
    }

    public static void needPermission(Activity activity, int requestCode, String[] permissions) {
        requestPermissions(activity, requestCode, permissions);
    }

    public static void needPermission(Fragment fragment, int requestCode, String[] permissions) {
        requestPermissions(fragment, requestCode, permissions);
    }

    public static void needPermission(Activity activity, int requestCode, String permission) {
        needPermission(activity, requestCode, new String[]{permission});
    }

    public static void needPermission(Fragment fragment, int requestCode, String permission) {
        needPermission(fragment, requestCode, new String[]{permission});
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    private static void requestPermissions(Object object, int requestCode, String[] permissions) {
        if (!Utils.isOverMarshmallow()) {
            doExecuteSuccess(object, requestCode);
            return;
        }
        List<String> deniedPermissions = Utils.findDeniedPermissions(getActivity(object), permissions);

        if (deniedPermissions.size() > 0) {
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getName() + " is not supported");
            }

        } else {
            doExecuteSuccess(object, requestCode);
        }
    }


    private static void doExecuteSuccess(Object activity, int requestCode) {
        Method executeMethod = Utils.findMethodWithRequestCode(activity.getClass(), PermissionSuccess.class, requestCode);
        executeMethod(activity, executeMethod);
    }

    private static void doExecuteFail(final Object activity, final int requestCode, String[] permissions) {
        DialogInterface.OnClickListener var8 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -2:
                        Toast.makeText(getActivity(activity), getActivity(activity).getString(R.string.rc_permission_grant_needed), Toast.LENGTH_SHORT).show();
                        Method executeMethod = Utils.findMethodWithRequestCode(activity.getClass(), PermissionFail.class, requestCode);
                        executeMethod(activity, executeMethod);
                        break;
                    case -1:
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        Uri uri = Uri.fromParts("package", getActivity(activity).getPackageName(), (String) null);
                        intent.setData(uri);
                        if (activity instanceof Fragment) {
                            ((Fragment) activity).startActivityForResult(intent, requestCode > 0 ? requestCode : -1);
                        } else if (activity instanceof Activity) {
                            ((Activity) activity).startActivityForResult(intent, requestCode > 0 ? requestCode : -1);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        showPermissionAlert(getActivity(activity), getActivity(activity).getResources().getString(R.string.rc_permission_grant_needed)
                + getNotGrantedPermissionMsg(getActivity(activity), Utils.findDeniedPermissions(getActivity(activity), permissions)), var8);
    }

    private static String getNotGrantedPermissionMsg(Context context, List<String> permissions) {
        HashSet permissionsValue = new HashSet();
        Iterator result = permissions.iterator();

        while (result.hasNext()) {
            String permission = (String) result.next();
            String permissionValue = context.getString(context.getResources().getIdentifier("rc_" + permission, "string", context.getPackageName()), new Object[]{Integer.valueOf(0)});
            permissionsValue.add(permissionValue);
        }

        String result1 = "(";

        String value;
        for (Iterator permission1 = permissionsValue.iterator(); permission1.hasNext(); result1 = result1 + value + " ") {
            value = (String) permission1.next();
        }

        result1 = result1.trim() + ")";
        return result1;
    }


    @TargetApi(11)
    private static void showPermissionAlert(Context context, String content, DialogInterface.OnClickListener listener) {
        (new AlertDialog.Builder(context, 16974394))
                .setMessage(content)
                .setPositiveButton(R.string.rc_confirm, listener)
                .setNegativeButton(R.string.rc_cancel, listener)
                .setCancelable(false)
                .create()
                .show();
    }

    private static void executeMethod(Object activity, Method executeMethod) {
        if (executeMethod != null) {
            try {
                if (!executeMethod.isAccessible()) executeMethod.setAccessible(true);
                executeMethod.invoke(activity, (Object[]) null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions,
                                                  int[] grantResults) {
        requestResult(activity, requestCode, permissions, grantResults);
    }

    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions,
                                                  int[] grantResults) {
        requestResult(fragment, requestCode, permissions, grantResults);
    }

    private static void requestResult(Object obj, int requestCode, String[] permissions,
                                      int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.size() > 0) {
            doExecuteFail(obj, requestCode, permissions);
        } else {
            doExecuteSuccess(obj, requestCode);
        }
    }
}
