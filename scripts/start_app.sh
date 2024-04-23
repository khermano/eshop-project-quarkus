#!/bin/bash

test_port () {
  if [ "$(lsof -i tcp:"$1")" != "" ]; then
      echo "Error! Port $1 in use!"
      exit 1
  fi
}

run_postgres () {
  docker run --rm --name dev-postgres-"$1" -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB="$1" -p "$2":5432 postgres &
}

kill_postgres () {
  docker kill dev-postgres-"$1"
}

run_service () {
  cd "$1" || exit
  mvn clean install -DskipTests
  cd ..
  java -jar "$1"/target/quarkus-app/quarkus-run.jar &
  pids[$2]=$!
}

run_consul () {
  docker run -d --rm --name consul -p 8500:8500 -p 8501:8501 consul:1.7 agent -dev -ui -client=0.0.0.0 -bind=0.0.0.0 --https-port=8501 &
}

endpoint_alive () {
  LINK=$(curl -s -o /dev/null -w "%{http_code}" localhost:"$1")
  if echo "$LINK" = "200" >/dev/null; then
    return 0
  else
    return 1
  fi
}

app_available () {
  if curl -I localhost:8500/ui/ | grep -q "200 OK" && curl localhost:8091/q/health/live | grep -q "UP" &&
      curl localhost:8092/q/health/live | grep -q "UP" && curl localhost:8093/q/health/live | grep -q "UP" &&
      curl localhost:8094/q/health/live | grep -q "UP" && curl localhost:8080/eshop-rest/q/health/live | grep -q "UP"; then
      return 0
  else
      return 1
  fi
}

test_port 8500
test_port 8501
test_port 8091
test_port 8092
test_port 8093
test_port 8094
test_port 8080
test_port 5431
test_port 5432
test_port 5433
test_port 5434

run_consul
run_postgres user-quarkus 5431
run_postgres category-quarkus 5432
run_postgres product-quarkus 5433
run_postgres order-quarkus 5434

cd ..
# here add that all below wait until user-quarkus is ready
sleep 5
run_service user-service 0
run_service category-service 1
run_service product-service 2
run_service order-service 3
run_service api-gateway-service 4

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
kill_postgres user-quarkus
kill_postgres category-quarkus
kill_postgres product-quarkus
kill_postgres order-quarkus
docker kill consul
echo
echo "Application shutdown completed."