//package zpi.heartAuth.orchestrator.interfaces.rest.v1;
//
//import org.springframework.web.bind.annotation.RestController;
//import zpi.heartAuth.orchestrator.api.PairingApi;
//
//@RestController
//public class PairingApiImpl implements PairingApi {
//
//    private final PairingService service;
//    private final ApiMapper map;
//
//    public PairingApiImpl(PairingService service, ApiMapper map) {
//        this.service = service;
//        this.map = map;
//    }
//
//    @Override
//    public ResponseEntity<PairingInitResponse> pairExchangeInit(
//            @RequestHeader(HttpHeaders.AUTHORIZATION) String authz,
//            @RequestBody PairingInitRequest body) {
//
//        var res = service.init(
//                extractBearer(authz),
//                body.getDeviceId(),
//                body.getDisplayName(),
//                body.getPublicKeyPem(),
//                body.getFcmToken(),
//                body.getAttestation() == null ? "none" : body.getAttestation().getType(),
//                body.getAttestation() == null ? null : body.getAttestation().getVerdict()
//        );
//
//        var api = new PairingInitResponse()
//                .jti(res.jti().toString())
//                .nonce(res.nonceB64())
//                .exp(res.exp().getEpochSecond())
//                .policy(new PairingInitResponsePolicy().deviceLimitRemaining(res.deviceLimitRemaining()));
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(api);
//    }
//
//    @Override
//    public ResponseEntity<PairingConfirmResponse> pairExchangeConfirm(
//            @RequestBody PairingConfirmRequest body, HttpServletRequest req) {
//
//        var result = service.confirm(
//                UUID.fromString(body.getJti()),
//                body.getDeviceId(),
//                body.getSignature(),
//                body.getAlg().getValue(),            // enum → "ES256"
//                req.getHeader("DPoP"),
//                URI.create(req.getRequestURL().toString())
//        );
//
//        return ResponseEntity.ok(
//                new PairingConfirmResponse()
//                        .status(PairingConfirmResponse.StatusEnum.LINKED)
//                        .credential(map.toApiCredential(result.credential()))
//        );
//    }
//
//    @Override
//    public ResponseEntity<PairingStatusResponse> pairStatusJti(@PathVariable("jti") UUID jti) {
//        var s = service.status(jti);
//        return ResponseEntity.ok(
//                new PairingStatusResponse().state(PairingStatusResponse.StateEnum.fromValue(s.state()))
//                        .reason(s.reason())
//        );
//    }
//
//    private String extractBearer(String header) {
//        if (header == null || !header.startsWith("Bearer "))
//            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing bearer");
//        return header.substring("Bearer ".length());
//    }
//}