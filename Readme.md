LTSEHome

This project tends to read trade orders from a csv file and generate two outputs including the valid orders and invalid orders based on certain filters.

Usage

The project is based on maven.
To run the application, please use the following command:
>mvn -q clean install exec:java -Dexec.mainClass="com.ltse.Main" -Dexec.args="-m rolling -o /tmp"

Please note there are two arguments we passed in here:
-m: this is for the window filter mode, two options are rolling and fixed
-o: this is for output director, if not provided, the file will be generated in the root on classpath

After the run, there will be 4 files generated:
accepted.csv: broker and sequence id of valid orders
rejected.csv: broker and sequence if of invalid orders
accepted.pb:  valid orders in format of proto buffer
rejected.csv: invalid orders in format of proto buffer

Testing

1. unit test: two unit test files OrderTest and FilterTest covered most of the critical logic for order processing and filters.
2. integration test: by running the above maven command, we can verify the total count of valid order and invalid orders match original count of orders.

Proto Buffer

This is an efficient message format. I think it's good for this scenario:
1. It's binary so has more security than other text based format, such as csv, json or even FIX.
2. It can be transferred through grpc protocal which provides better performance than http.
3. The size of the file is smaller.
Here is google's official website: https://developers.google.com/protocol-buffers

TODOs

1. Didn't use firms.txt but it seems a good candidate for filter
2. After writing the orders in proto buffer, how to transfer and read
