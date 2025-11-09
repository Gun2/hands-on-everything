#!/bin/bash

# docker를 통해 next app 제어를 위한 스크립트

CONTAINER_NAME="next-app"
IMAGE_NAME="my-next-app"
TAG=1.0
PORT=3000

case "$1" in
  build)
    echo "Building Docker image..."
    docker build -t ${IMAGE_NAME}:${TAG} .
    ;;
  export)
    echo "Exporting Docker image to tar file..."
    docker save -o ${IMAGE_NAME}-${TAG}.tar ${IMAGE_NAME}:${TAG}
    echo "Saved to ${IMAGE_NAME}.tar"
    ;;
  import)
    if [ -z "$2" ]; then
      echo "Usage: $0 import <tar-file>"
      exit 1
    fi
    TAR_FILE="$2"
    if [ ! -f "$TAR_FILE" ]; then
      echo "File not found: $TAR_FILE"
      exit 1
    fi
    echo "Importing Docker image from $TAR_FILE..."
    docker load -i "$TAR_FILE"
    ;;
  start)
    echo "Starting Next.js container..."
    docker run -d --name $CONTAINER_NAME -p $PORT:3000 --env-file .env $IMAGE_NAME
    docker run -d -p 3000:3000 --name $CONTAINER_NAME -v $(pwd)/logs:/app/logs --env-file .env ${IMAGE_NAME}:${TAG}
    ;;
  stop)
    echo "Stopping container..."
    docker stop $CONTAINER_NAME && docker rm $CONTAINER_NAME
    ;;
  restart)
    echo "Restarting container..."
    $0 stop
    $0 start
    ;;
  status)
    echo "Container status:"
    docker ps -a --filter "name=$CONTAINER_NAME"
    ;;
  logs)
    echo "Tailing logs..."
    docker logs -f $CONTAINER_NAME
    ;;
  *)
    echo "Usage: $0 {build|export|import|start|stop|restart|status|logs}"
    exit 1
    ;;
esac
