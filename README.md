# Orchestrator Service for Heartauth Project

## Generating EC Keys for JWT Signing

To generate a new EC private key and corresponding public key for JWT signing:

1. **Generate a new EC private key in PKCS#8 format:**

   ```sh
   openssl genpkey -algorithm EC -pkeyopt ec_paramgen_curve:P-256 -out ec-private-pkcs8.pem
   ```

2. **Extract the public key:**

   ```sh
   openssl pkey -in ec-private-pkcs8.pem -pubout -out ec-public.pem
   ```
