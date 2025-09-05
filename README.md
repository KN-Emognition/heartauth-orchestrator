openssl genpkey -algorithm EC -pkeyopt ec_paramgen_curve:P-256 -out ec-private-pkcs8.pem   

openssl pkey -in ec-private-pkcs8.pem -pubout -out ec-public.pem