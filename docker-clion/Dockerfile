FROM ubuntu:latest

RUN apt-get update

RUN apt-get install -y build-essential


RUN DEBIAN_FRONTEND='noninteractive'  apt-get install -y cmake

RUN apt-get install -y gdb

RUN apt-get install -y openssh-server
RUN mkdir /var/run/sshd
RUN sed -ri 's/^#PermitRootLogin\s+.*/PermitRootLogin yes/' /etc/ssh/sshd_config
RUN sed -ri 's/UsePAM yes/#UsePAM yes/g' /etc/ssh/sshd_config

RUN apt-get install -y rsync
RUN sed -ri 's/RSYNC_ENABLE=false/RSYNC_ENABLE=true/g' /etc/default/rsync
COPY rsync.conf /etc

RUN echo 'root:000000' |chpasswd

RUN mkdir /root/sync

COPY entrypoint.sh /sbin
RUN chmod +x /sbin/entrypoint.sh
ENTRYPOINT [ "/sbin/entrypoint.sh" ]
