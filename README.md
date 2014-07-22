RecordActivity
==============

The following activity tests the MediaRecorder class by recording a 5 second video.

The code in RecordActivity.java was tested on the following devices:

- Nexus 4 running Android 4.4.4 (working)
- Google Glass XE 18.3          (working)
- Google Glass XE 19.1          (error)

RecorderActivity logs the following error as of XE 19.1:

      W/CameraBase﹕ mediaserver's remote binder Camera object died
      W/CameraBase﹕ Camera service died!
      E/Camera﹕ Error 100

