@ECHO OFF
REM view network info
REM netsh interface ipv4 show config

REM arg1 name of interface
REM arg2 ip address
REM arg3 subnet mask
REM arg4 default gateway

netsh interface ipv4 set address name=%1 static %2 %3 %4
REM netsh interface ip set dns %1 static %2

REM netsh interface ipv4 set address name="YOUR INTERFACE NAME" static IP_ADDRESS SUBNET_MASK GATEWAY
REM netsh interface ipv4 set address name="Wi-Fi" dhcp