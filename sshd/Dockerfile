FROM alpine:3.19.0
MAINTAINER fredrik

RUN apk add --no-cache \
  bash \
  openssh \
  openssh-sftp-server \
  gettext \
  linux-pam \
  mosh \
  git \
  curl

RUN addgroup logit \
  && adduser -D -G logit -s /bin/bash -h /home/logit logit \
  && sed -i 's|logit:!:|logit:*:|g' /etc/shadow # Updating the /etc/shadow file to allow password less login for user logit

COPY logitssh.pub /home/logit/.ssh/authorized_keys

RUN chown -R logit:users /home/logit/ # Update the ownership of /home/logit directory

RUN ssh-keygen -A \
    && mkdir /var/run/sshd \
    && echo "PasswordAuthentication no" >> /etc/ssh/sshd_config \
    && echo "AllowAgentForwarding yes" >> /etc/ssh/sshd_config \
    && echo "AllowGroups logit" >> /etc/ssh/sshd_config \
    && echo "AllowTcpForwarding yes" >> /etc/ssh/sshd_config \
    && echo "X11Forwarding yes" >> /etc/ssh/sshd_config \
    && echo "PermitRootLogin no" \
    && echo "PermitTunnel yes" >> /etc/ssh/sshd_config \
    && echo "PubkeyAuthentication yes" >> /etc/ssh/sshd_config

EXPOSE 22 2220

ENTRYPOINT ["/usr/sbin/sshd","-D","-e","-p","2220"]

CMD []

# Build command with the tag for this image
# docker build -t flux_sshd:v1 .

# Load the Docker image into kind
# kind load docker-image flux_sshd:v1