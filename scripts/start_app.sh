#!/bin/bash

test_port () {
  if [ "$(lsof -i tcp:"$1")" != "" ]; then
      echo "Error! Port $1 in use!"
      exit 1
  fi
}

run_service () {
  cd "$1" || exit
  mvn clean install -DskipTests
  cd ..
  java -jar "$1"/target/quarkus-app/quarkus-run.jar &
  pids[$2]=$!
}

run_docker () {
  docker run --rm --name consul -p 8500:8500 -p 8501:8501 consul:1.7 agent -dev -ui -client=0.0.0.0 -bind=0.0.0.0 --https-port=8501 &
  pids[$1]=$!
}

app_available () {
  if curl -I localhost:8500/ui/ | grep -q "200 OK" && curl -I localhost:8085/users/ | grep -q "200 OK" &&
    curl -I localhost:8082/categories/ | grep -q "200 OK" && curl -I localhost:8083/products/ | grep -q "200 OK" &&
    curl -I localhost:8084/orders?status=ALL | grep -q "200 OK" && curl -I localhost:8080/eshop-rest/ | grep -q "200 OK"; then
      return 0
  else
      return 1
  fi
}

test_port 8500
test_port 8501
test_port 8085
test_port 8082
test_port 8083
test_port 8084
test_port 8080

cd ..
run_docker 0
run_service user-service 1
run_service category-service 2
run_service product-service 3
run_service order-service 4
run_service api-gateway-service 5

while true; do
  if app_available; then
    echo
    echo "All services available."
    break
  else
    echo "One or more services are not available yet. Retrying..."
    sleep 1
  fi
done

echo "You can now access the application endpoints at http://localhost:8080/eshop-rest/."
read -rsn1 -p "For quit press any button and wait until application shutdown is completed."
echo
pkill -P $$
for pid in ${pids[*]}
do
  wait $pid
done
echo
echo "Application shutdown completed."