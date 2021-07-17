#!/bin/bash

/usr/bin/rsync --daemon --config=/etc/rsync.conf
/usr/sbin/sshd -D
