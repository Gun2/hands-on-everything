FROM kalilinux/kali-rolling

RUN apt update && \
    DEBIAN_FRONTEND=noninteractive apt install -y \
    xfce4 xfce4-goodies tightvncserver dbus-x11 x11-xserver-utils sudo && \
    apt clean

RUN useradd -m -s /bin/bash kali && \
    echo "kali:kali" | chpasswd && \
    adduser kali sudo

USER kali
WORKDIR /home/kali

# ✅ 이 줄을 꼭 추가
ENV USER=kali

RUN mkdir ~/.vnc && \
    echo "kali" | vncpasswd -f > ~/.vnc/passwd && \
    chmod 600 ~/.vnc/passwd && \
    echo '#!/bin/bash\nstartxfce4 &' > ~/.vnc/xstartup && \
    chmod +x ~/.vnc/xstartup

EXPOSE 5901

CMD vncserver :1 -geometry 1280x800 -depth 24 && tail -f /dev/null