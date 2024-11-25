set -e
container_name="mongo_transport"

exists_query=$(docker ps -a -q -f name="$container_name")
if [[ -z $exists_query ]]; then docker run -it -p 27017:27017 --name $container_name mongo; exit 0; fi

running_query=$(docker ps -q -f name="$container_name")
if [[ -z $running_query ]]; then docker start --attach $container_name; exit 0; fi

echo "$container_name already running. Attaching..."
docker attach $container_name
