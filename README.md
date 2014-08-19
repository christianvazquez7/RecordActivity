RecordActivity
==============

This app demonstrates a bug in the MediaRecorder class of Glass XE19.1, as well
as a workaround for it. h/t to [mark gamache](http://stackoverflow.com/users/488160/mark-gamache)
for posting [this](http://stackoverflow.com/a/25328046/1819499).

The following activity tests the MediaRecorder class by recording a 5 second video.

Try it from the command line:

      adb shell am start -a android.intent.action.MAIN -n  com.example.mediarecorderexample.app/com.example.mediarecorderexample.app.RecordActivity
      
The code in RecordActivity.java was tested on the following devices:

- Nexus 4 running Android 4.4.4 (working)
- Google Glass XE 18.3          (working)
- Google Glass XE 19.1          (error fixed with commit 3492baf408bf6a5c31ce2269ba355540955d069d)

Without the workaround, RecorderActivity logs the following error as of XE 19.1:

      W/CameraBase﹕ mediaserver's remote binder Camera object died
      W/CameraBase﹕ Camera service died!
      E/Camera﹕ Error 100
