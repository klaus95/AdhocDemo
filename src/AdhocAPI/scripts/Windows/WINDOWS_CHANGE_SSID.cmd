@ECHO OFF
REM arg1 is the new SSID
netsh wlan stop hostednetwork
netsh wlan set hostednetwork mode=allow ssid=%1
netsh wlan start hostednetwork

REM to reset password as well, take in 2nd parameter
REM and add key=%2 to line 4