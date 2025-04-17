FROM kalilinux/kali-rolling

# 필수 패키지 설치
# Install noVNC and websockify
# VNC 프로토콜(TCP 5901)을 → 브라우저(WebSocket, HTTP/HTTPS)로 바꿔주는 중간 서버가 필요
# [브라우저] ⇄ [noVNC (HTML5)] ⇄ [websockify] ⇄ [VNC 서버 (:1)] ⇄ [Xfce GUI]
RUN apt update && \
    DEBIAN_FRONTEND=noninteractive apt install -y \
    xfce4 xfce4-goodies tightvncserver dbus-x11 x11-xserver-utils sudo \
    git python3 python3-websockify novnc && \
    ln -s /usr/share/novnc/vnc.html /usr/share/novnc/index.html && \
    apt clean

# 사용자 생성 및 권한 부여
RUN useradd -m -s /bin/bash kali && \
    echo "kali:kali" | chpasswd && \
    adduser kali sudo

# 환경 설정
USER kali
ENV USER=kali
ENV HOME=/home/kali
WORKDIR /home/kali

# VNC 비밀번호 설정 및 기본 Xfce 시작 스크립트
RUN mkdir -p /home/kali/.vnc && \
    echo "kali" | vncpasswd -f > /home/kali/.vnc/passwd && \
    chmod 600 /home/kali/.vnc/passwd && \
    echo -e '#!/bin/bash\nxrdb $HOME/.Xresources\nstartxfce4 &' > /home/kali/.vnc/xstartup && \
    chmod +x /home/kali/.vnc/xstartup

# JSON 형식 CMD: Entrypoint 스크립트를 쓰거나 exec 형식으로 직접 지정
CMD ["sh", "-c", "vncserver :1 -geometry 1280x800 -depth 24 && websockify --web=/usr/share/novnc/ 6080 localhost:5901"]
