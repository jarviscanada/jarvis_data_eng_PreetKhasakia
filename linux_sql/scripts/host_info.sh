#Setup and validate arguments
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#Incorrect number of args
if [ "$#" -ne  5 ]; then
	echo "Incorrect number of arguments"
	exit 1
fi

        
    hostname=$(hostname -f)
    lscpu_out=$(lscpu)

    #Retrieve hardware specification variables
    cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
    cpu_architecture=$(echo "$lscpu_out"  | egrep "Architecture:" | awk '{print $2}' | xargs)
    cpu_model=$(echo "$lscpu_out"  | egrep "Model:" | awk '{print $2}' | xargs)
    cpu_mhz=$(echo "$lscpu_out"  | egrep "CPU MHz:" | awk '{print $3 }' | xargs)
    l2_cache=$(echo "$lscpu_out"  | egrep "L2 cache:" | awk '{print $3}' | xargs)
    #l2_cache1=$(echo "$lscpu_out"  | egrep "L2 cache" | awk '{print $3 }' | xargs)
    #l2_cache=${l2_cache//[^[:digit:].-]/}
    total_mem=$(vmstat -s | egrep "total memory"| awk '{print $1}' | xargs)
    #total_mem=$(grep MemTotal /proc/meminfo | awk '{print $2}' | xargs)    
    #timestamp=$(date +%F" "%T | awk '{print $1" "$2}'| xargs)

    #Current time in `2019-11-26 14:40:19` UTC format
    timestamp=$(vmstat -t | awk '{print $18" "$19}' | xargs)

    #Inserts server usage data into host_usage table
    insert_stmt="INSERT INTO host_info(timestamp, total_mem, l2_cache, cpu_mhz, cpu_model, cpu_architecture, cpu_number, hostname) VALUES('$timestamp', '$total_mem', '$l2_cache', '$cpu_mhz', '$cpu_model', '$cpu_architecture', '$cpu_number', '$hostname');"

    export PGPASSWORD=$psql_password 
   
    psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
    exit $?
