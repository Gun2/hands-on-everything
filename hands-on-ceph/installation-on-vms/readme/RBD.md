# Ceph RBD (RADOS Block Device) 구성
Ceph RBD(Block Storage)를 만들고 OS에 디바이스로 붙인 뒤 디렉터리로 마운트하는 과정 정리

## RBD Pool 생성
```shell
# pool 생성
ceph osd pool create rbdpool 128

# 확인
ceph osd pool ls

```
> 128 = PG 수 (운영에선 OSD 수 따라 조정)

## RBD 초기화
```shell
rbd pool init rbdpool
```

## 10G RBD 생성
```shell
rbd create rbdpool/mydisk --size 10G

# 확인
rbd ls rbdpool
rbd info rbdpool/mydisk
```

## RBD를 OS Block Device로 연결 (map)
```shell
# 로컬 커널에서
modprobe rbd
# 확인
lsmod | grep rbd

# cephamd shell에서
rbd map rbdpool/mydisk
# >>> 결과 예
/dev/rbd0

# 확인
lsmod | grep rbd

```

## 파일시스템 생성(ext4)
```shell
mkfs.ext4 /dev/rbd0
```

## 마운트
> RBD는 부팅 시 Ceph가 먼저 살아 있어야하기에  테스트 환경에서는 수동 마운트로 진행 (fstab ❌)

```shell
# 마운트 디렉터리 생성
mkdir -p /mnt/rbd

# 마운트
mount /dev/rbd0 /mnt/rbd
# 확인
df -h | grep rbd
# >>> 출력 예
# /dev/rbd0       9.8G   24K  9.3G   1% /mnt/rbd
```
