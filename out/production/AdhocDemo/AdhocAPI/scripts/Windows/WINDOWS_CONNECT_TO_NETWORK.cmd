@ECHO OFF
REM arg1 is the PROFILE
netsh wlan connect name=%1 ssid=%1 interface=%2

REM connect name=Profile ssid=SSID interface="Wireless Network Connection"