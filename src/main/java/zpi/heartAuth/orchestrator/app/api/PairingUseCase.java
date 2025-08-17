//package zpi.heartAuth.orchestrator.app.api;
//
//
//import zpi.heartAuth.orchestrator.app.models.DeviceCredential;
//
//import java.net.URI;
//import java.time.Instant;
//import java.util.UUID;
//
//public interface PairingUseCase {
//    record InitResult(UUID jti, String nonceB64, Instant exp, Integer deviceLimitRemaining) {
//    }
//
//    record StatusResult(String state, String reason) {
//    }
//
//    record ConfirmResult(DeviceCredential credential) {
//    }
//
//    InitResult init(String pairingBearer,
//                    String deviceId, String displayName, String publicKeyPem,
//                    String fcmToken, String attType, String attVerdict);
//
//    ConfirmResult confirm(UUID jti, String deviceId, String signatureB64Url,
//                          String alg, String dpopHeader, URI httpUri);
//
//    StatusResult status(UUID jti);
//}