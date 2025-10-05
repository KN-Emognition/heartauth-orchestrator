package knemognition.heartauth.orchestrator.external.app.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import knemognition.heartauth.orchestrator.shared.app.domain.EcgRefTokenClaims;
import knemognition.heartauth.orchestrator.shared.app.domain.EcgTestTokenClaims;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class EcgTokenMapper {

    @Autowired
    protected ObjectMapper objectMapper;


    @Mapping(source = "claimsSet", target = "refEcg", qualifiedByName = "toRefEcg")
    public abstract EcgRefTokenClaims ecgRefFromClaimsAndState(JWTClaimsSet claimsSet);

    @Mapping(source = "claimsSet", target = "testEcg", qualifiedByName = "toTestEcg")
    public abstract EcgTestTokenClaims ecgTestFromClaims(JWTClaimsSet claimsSet);

    @Named("toRefEcg")
    List<List<Float>> toRefEcg(JWTClaimsSet claimsSet) {
        Object raw = claimsSet.getClaim("refEcg");
        if (raw == null) return null;
        return objectMapper.convertValue(raw, new TypeReference<List<List<Float>>>() {
        });
    }

    @Named("toTestEcg")
    List<Float> toTestEcg(JWTClaimsSet claimsSet) {
        Object raw = claimsSet.getClaim("testEcg");
        if (raw == null) return null;
        return objectMapper.convertValue(raw, new TypeReference<List<Float>>() {
        });
    }
}
