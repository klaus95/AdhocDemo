@ECHO OFF
REM arg1 is the INTERFACE NAME
REM arg2 is the PROFILE_NAME
netsh wlan disconnect interface=%1
netsh wlan delete profile name=%2 interface=%1