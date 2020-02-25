DESCRIPTION = "Modbus device handler"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit ve_package
inherit daemontools
inherit python-compile

RDEPENDS_${PN} = "\
    bash \
    python \
    python-dbus \
    python-pygobject \
    python-pymodbus \
"

SRC_URI = " \
    gitsm://github.com/victronenergy/${BPN}.git;protocol=ssh;user=git;branch=master \
    file://start-tty.sh \
"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "${bindir}/${PN}.py"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}"

DAEMONTOOLS_SERVICE_DIR_TTY = "${vedir}/${PN}.tty/service"
DAEMONTOOLS_RUN_TTY = "${bindir}/start-tty.sh TTY"
DAEMONTOOLS_LOG_DIR_TTY = "${DAEMONTOOLS_LOG_DIR}.TTY"

do_install () {
    oe_runmake DESTDIR=${D} install

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/start-tty.sh ${D}${bindir}

    SERVICE="${D}${DAEMONTOOLS_SERVICE_DIR_TTY}"

    install -d ${SERVICE}
    echo "#!/bin/sh" > ${SERVICE}/run
    echo "exec 2>&1" >> ${SERVICE}/run
    echo "exec ${DAEMONTOOLS_RUN_TTY}" >> ${SERVICE}/run
    chmod 755 ${SERVICE}/run

    install -d ${SERVICE}/log
    echo "#!/bin/sh" > ${SERVICE}/log/run
    echo "exec 2>&1" >> ${SERVICE}/log/run
    echo "exec multilog t s25000 n4 ${DAEMONTOOLS_LOG_DIR_TTY}" >> ${SERVICE}/log/run
    chmod 755 ${SERVICE}/log/run
}

FILES_${PN} += "${DAEMONTOOLS_SERVICE_DIR_TTY}"
