package knemognition.heartauth.orchestrator.security;


import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DecryptJweCmd<T> {
    String jwe;
    String senderPublicKey;
    String recipientPrivateKey;
    TypeReference<T> targetType;
}