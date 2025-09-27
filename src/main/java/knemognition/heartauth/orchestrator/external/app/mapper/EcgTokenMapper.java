package knemognition.heartauth.orchestrator.external.app.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import knemognition.heartauth.orchestrator.external.app.domain.EcgRefToken;
import knemognition.heartauth.orchestrator.external.app.domain.EcgTestToken;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EcgTokenMapper {

    @Mapping(source = "claimsSet", target = "refEcg", qualifiedByName = "toRefEcg")
    EcgRefToken ecgRefFromClaims(JWTClaimsSet claimsSet, @Context ObjectMapper om);

    @Mapping(source = "claimsSet", target = "testEcg", qualifiedByName = "toTestEcg")
    EcgTestToken ecgTestFromClaims(JWTClaimsSet claimsSet, @Context ObjectMapper om);

    @Named("toRefEcg")
    default List<Float> toRefEcg(JWTClaimsSet claimsSet, @Context ObjectMapper om) {
        Object raw = claimsSet.getClaim("refEcg");
        if (raw == null) return null;
        return om.convertValue(raw, new TypeReference<List<Float>>() {});
    }

    @Named("toTestEcg")
    default List<Float> toTestEcg(JWTClaimsSet claimsSet, @Context ObjectMapper om) {
        Object raw = claimsSet.getClaim("testEcg");
        if (raw == null) return null;
        return om.convertValue(raw, new TypeReference<List<Float>>() {});
    }
}