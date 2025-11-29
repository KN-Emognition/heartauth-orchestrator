# Orchestrator Service for Heartauth Project

The **Orchestrator Service** is a core backend component of the Heartauth system, responsible for challenge management, device pairing, JWT signing, Kafka communication, and tenant administration.

This service relies heavily on configurable environment variables to control TTL values, database access, messaging, key locations, and more.

---

## 🔧 Environment Variables

Below is the full list of environment variables used by the Orchestrator service.

| Variable | Description | Example |
|---------|-------------|---------|
| `CHALLENGE_DEFAULT_TTL` | Default TTL (in seconds) for generated authentication challenges. | `180` |
| `CHALLENGE_MAX_TTL` | Maximum allowed TTL for a challenge. | `600` |
| `CHALLENGE_MIN_TTL` | Minimum allowed TTL for a challenge. | `60` |
| `CHALLENGE_NONCE_LENGTH` | Length of generated challenge nonces. | `32` |
| `DB_HOST` | Hostname of the PostgreSQL database. | `heartauth-postgres-db` |
| `DB_NAME` | Name of the PostgreSQL database. | `orchestrator` |
| `DB_PORT` | Port for PostgreSQL. | `5432` |
| `FIREBASE_CREDENTIALS_LOCATION` | Path to Firebase service account JSON for sending FCM notifications. | `file:/orchestrator-data/firebaseServiceAccount.json` |
| `JWT_KID` | Unique Key ID embedded in JWT headers. | `orchestrator-key` |
| `JWT_PRIVATE_KEY_LOCATION` | File path to EC private key used for signing JWTs. | `file:/orchestrator-data/privateKey.pem` |
| `JWT_PUBLIC_KEY_LOCATION` | File path to EC public key used for token verification. | `file:/orchestrator-data/publicKey.pem` |
| `MODEL_API_TOPICS_COMBINED` | Kafka topic for combined ECG authentication model results. | `heartauth.model-api.ecg-auth-combined` |
| `MODEL_API_TOPICS_REQUEST` | Kafka topic for sending ECG model requests. | `heartauth.model-api.ecg-auth-request` |
| `MODEL_API_TOPICS_RESPONSE` | Kafka topic for receiving ECG model responses. | `heartauth.model-api.ecg-auth-response` |
| `PAIRING_AUDIENCE` | Audience identifier for pairing JWTs. | `hauth.tenant` |
| `PAIRING_DEFAULT_TTL` | Default TTL for device pairing tokens. | `180` |
| `PAIRING_ISSUER` | Issuer claim for pairing JWTs. | `heartauth.orchestrator` |
| `PAIRING_MAX_TTL` | Maximum allowed TTL for pairing tokens. | `600` |
| `PAIRING_MIN_TTL` | Minimum allowed TTL for pairing tokens. | `60` |
| `REDIS_HOST` | Hostname of the Redis instance. | `heartauth-redis` |
| `REDIS_PORT` | Port for Redis. | `6379` |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Kafka bootstrap server address(es). | `heartauth-kafka:9092` |
| `SPRING_PROFILES_ACTIVE` | Active Spring Boot profile set. | `tenant,admin,fcm` |
| `ADMIN_API_KEY` | Admin API key (loaded from Kubernetes secret). | *(secret)* |
| `DB_PASSWORD` | Database password (loaded from Kubernetes secret). | *(secret)* |
| `DB_USER` | Database username (loaded from Kubernetes secret). | *(secret)* |

---

## 🔐 Generating EC Keys for JWT Signing

Use the following commands to generate a valid EC private/public key pair.

### 1. Generate EC private key (PKCS#8 format)

```sh
openssl genpkey -algorithm EC -pkeyopt ec_paramgen_curve:P-256 -out ec-private-pkcs8.pem
