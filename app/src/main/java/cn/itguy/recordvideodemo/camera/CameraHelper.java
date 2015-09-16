package cn.itguy.recordvideodemo.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.os.Build;
import android.view.Surface;

import java.util.List;

/**
 * Created by Administrator on 2015/9/10.
 */
public class CameraHelper {

    private static final String TAG = "CameraHelper";

    public static int getAvailableCamerasCount() {
        return Camera.getNumberOfCameras();
    }

    /**
     * 获取默认（背部）相机id，若找不到则返回第一个相机id（0）
     * @return
     */
    public static int getDefaultCameraID() {
        int camerasCnt = getAvailableCamerasCount();
        int defaultCameraID = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i=0; i <camerasCnt; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                defaultCameraID = i;
            }
        }
        return defaultCameraID;
    }

    public static boolean isCameraFacingBack(int cameraID) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraID, cameraInfo);
        return (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    public static List<Camera.Size> getCameraSupportedVideoSizes(android.hardware.Camera camera) {
        if ((Build.VERSION.SDK_INT >= 11) && (camera != null)) {
//			return camera.getParameters().getSupportedVideoSizes();
            List<Camera.Size> sizes = camera.getParameters().getSupportedVideoSizes();
            if (sizes == null)
                return camera.getParameters().getSupportedPreviewSizes();
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    /**
     * 根据相机id获取相机对象
     * @param cameraId
     * @return
     */
    public static Camera getCameraInstance(int cameraId){
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /** Check if this device has a camera */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /**
     * 获取相机录制CIF质量视频的宽高
     * @param cameraId
     * @param camera
     * @return
     */
    public static Camera.Size getCameraPreviewSizeForVideo(int cameraId, Camera camera) {
        CamcorderProfile cameraProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        return camera.new Size(cameraProfile.videoFrameWidth, cameraProfile.videoFrameHeight);



//        Camera.Parameters parameters = camera.getParameters();
//        List<Camera.Size> supportedVideoSizeList = parameters.getSupportedVideoSizes();
//        if (supportedVideoSizeList == null) {
//            supportedVideoSizeList = parameters.getSupportedPreviewSizes();
//        }
////        for (Camera.Size size : supportedVideoSizeList) {
////        }
//        return supportedVideoSizeList.get(supportedVideoSizeList.size() - 4);



//        Camera.Size currentSize = parameters.getPreviewSize();
//        Log.d(TAG, "current camera preview size w: " + currentSize.width + "---h: " + currentSize.height);
//
//        Camera.Size willSetSize = currentSize;
//        Camera.Size tempSize = null;
//        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
//        for (Camera.Size size : sizeList) {
//            Log.d(TAG, "supported camera preview size w: " + size.width + "---h: " + size.height);
//            // 如果宽高符合4:3要求，并且宽度比之前获得的宽度大，则取当前这个
//            if (1.0f * size.width / size.height == ratio) {
//                if (tempSize == null || size.width >= tempSize.width) {
//                    tempSize = size;
//                }
//            }
//        }
//
//        if (tempSize != null)
//            willSetSize = tempSize;
//
//        return willSetSize;
    }

}
