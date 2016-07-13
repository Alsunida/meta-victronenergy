LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

PR = "r2"

PACKAGE_ARCH = "all"

inherit gmakevelib
inherit daemontools

RDEPENDS_${PN} = "mosquitto"

SRC_URI = " gitsm://github.com/victronenergy/dbus-mqtt.git;protocol=https;tag=v${PV}"
S = "${WORKDIR}/git"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 2000000 -s 1000000 -a 100000000 ${bindir}/dbus_mqtt.py --init-broker"
DAEMONTOOLS_DOWN = "1"