FROM kalilinux/kali-rolling

# 필수 패키지 설치
# Install noVNC and websockify
# autocutsel은 tightvncserver에 copy & paste 지원
# VNC 프로토콜(TCP 5901)을 → 브라우저(WebSocket, HTTP/HTTPS)로 바꿔주는 중간 서버가 필요
# [브라우저] ⇄ [noVNC (HTML5)] ⇄ [websockify] ⇄ [VNC 서버 (:1)] ⇄ [Xfce GUI]
RUN apt update && \
    DEBIAN_FRONTEND=noninteractive apt install -y \
    xfce4 xfce4-goodies tightvncserver dbus-x11 x11-xserver-utils sudo \
    git python3 python3-websockify novnc openssl autocutsel && \
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

# SSL 인증서 생성
RUN mkdir -p /home/kali/certs && \
    openssl req -x509 -nodes -days 365 \
    -newkey rsa:2048 \
    -keyout /home/kali/certs/key.pem \
    -out /home/kali/certs/cert.pem \
    -subj "/C=US/ST=Nowhere/L=Nowhere/O=Development/CN=localhost"clipboardPasteFrom

# VNC 비밀번호 설정 및 기본 Xfce 시작 스크립트
RUN mkdir -p /home/kali/.vnc && \
    echo "kali" | vncpasswd -f > /home/kali/.vnc/passwd && \
    chmod 600 /home/kali/.vnc/passwd && \
    # 아래의 autocutsel -fork는 autocutsel를 활성화
    echo -e '#!/bin/bash\nxrdb $HOME/.Xresources\nautocutsel -fork\nstartxfce4 &' > /home/kali/.vnc/xstartup && \
    chmod +x /home/kali/.vnc/xstartup

# JSON 형식 CMD: Entrypoint 스크립트를 쓰거나 exec 형식으로 직접 지정
CMD ["sh", "-c", "vncserver :1 -geometry 1280x800 -depth 24 && websockify --web=/usr/share/novnc/ --cert=/home/kali/certs/cert.pem --key=/home/kali/certs/key.pem 6080 localhost:5901"]